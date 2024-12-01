package crawlee.org.src.etl;

import com.google.gson.Gson;
import crawlee.org.src.common.io.IOManager;
import crawlee.org.src.common.model.export.CsvExportDocument;
import crawlee.org.src.common.model.export.ExportDocument;
import crawlee.org.src.etl.http.rest.RestClient;
import crawlee.org.src.etl.mapper.ProjectionMapper;
import crawlee.org.src.etl.model.RestEtlConfig;
import crawlee.org.src.etl.model.yit.BuildingEntity;
import crawlee.org.src.etl.model.yit.YitRequest;
import crawlee.org.src.etl.model.yit.YitResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class YitRestEtl extends AbstractRestEtl {
    private static final Logger log = Logger.getLogger(YitRestEtl.class.getCanonicalName());
    private final Gson gson;
    private final ConcurrentLinkedQueue<YitResponse> queue;

    public YitRestEtl(RestEtlConfig restEtlConfig) {
        super(restEtlConfig);
        gson = new Gson();
        queue = new ConcurrentLinkedQueue<>();
    }

    private YitResponse inputStreamToYitResponse(InputStream stream) throws IOException {
        String response;
        try {
            response = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
            stream.close();
        } catch (IOException e) {
            log.warning("Failed to read from stream!");
            throw e;
        }
        return gson.fromJson(response, YitResponse.class);
    }

    private YitResponse extract() {
        try {
            HttpResponse<InputStream> httpResponse = RestClient.doPost(getRestEtlConfig().url(), new Gson().toJson(getRestEtlConfig().restEndpointConfig().body()));
            return inputStreamToYitResponse(httpResponse.body());
        } catch (URISyntaxException exception) {
            log.warning("malformed uri " + exception.getMessage());
            throw new RuntimeException(exception);
        } catch (IOException | InterruptedException e) {
            log.warning("connection exception " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private List<BuildingEntity> transformWithProjection(List<BuildingEntity> buildings) {
        return buildings.stream().map(buildingEntity -> ProjectionMapper.map(buildingEntity, this.getRestEtlConfig().projection())).collect(Collectors.toList());
    }

    private List<BuildingEntity> transformWithStrictProjection(List<BuildingEntity> buildings) {
        return buildings.stream().map(buildingEntity -> ProjectionMapper.mapStrictProjection(buildingEntity, this.getRestEtlConfig().projection())).collect(Collectors.toList());
    }

    private void saveExportDocument(ExportDocument exportDocument) {
        log.info(String.format("%s.%s", exportDocument.getFilename(), exportDocument.getFileExtension()));
        FutureTask<Boolean> task = new FutureTask<>(() -> IOManager.writeFile(String.format("%s.%s", exportDocument.getFilename(), exportDocument.getFileExtension()), exportDocument.getContent(), this.getRestEtlConfig().outputFileDirectory()));
        task.run();
    }

    private ExportDocument createExportDocument(List<BuildingEntity> entities) {
        Set<String> columns = entities.get(0).Fields().keySet();
        List<String[]> contentWrapper = new ArrayList<>();
        for (BuildingEntity entity : entities) {
            Map<String, Object> fields = entity.Fields();
            String[] content = new String[columns.size()];
            int i = 0;
            for (String key : columns) {
                Object value = fields.get(key);
                if (value == null) {
                    log.finest(String.format("Null value for key: %s", key));
                    continue;
                }
                content[i++] = value.toString();
            }
            contentWrapper.add(content);
        }
        return new CsvExportDocument(this.getRestEtlConfig().outputFilePrefix() + Math.random(), columns, contentWrapper);
    }


    private Runnable extractYitData() {
        return () -> {
            int pagesConsumed = 0;
            YitRequest body = this.getRestEtlConfig().restEndpointConfig().body();
            while (true) {
                YitResponse response = extract();
                log.info(String.format("Consumed page: %d", pagesConsumed));
                queue.add(response);
                pagesConsumed++;
                if (!response.IsMoreAvailable()) {
                    queue.add(new YitResponse(0, false, null));
                    log.info(String.format("No more available data for given filter. Consumed %d pages.", pagesConsumed));
                    return;
                }
                body.setStartPage(pagesConsumed);
            }
        };
    }

    private void waitTillItemIsAvailable() {
        while (queue.isEmpty()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException exception) {
                log.warning("Thread interrupted while waiting!");
                throw new RuntimeException(exception);
            }
        }
    }

    private Runnable consumeYitData() {
        return () -> {
            boolean strict = this.getRestEtlConfig().useOnlyProjection();
            Function<List<BuildingEntity>, List<BuildingEntity>> transform = strict ? this::transformWithStrictProjection : this::transformWithProjection;
            while (true) {
                waitTillItemIsAvailable();
                YitResponse response = queue.poll();
                if (response.Hits() == null) {
                    return;
                }
                saveExportDocument(createExportDocument(transform.apply(response.Hits())));
            }
        };
    }

    @Override
    public void extractAndSaveTransformedData() {
        final Thread producer = new Thread(extractYitData());
        final Thread consumer = new Thread(consumeYitData());
        producer.start();
        consumer.start();
        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException interruptedException) {
            log.severe("Interrupt occurred while waiting for threads to finish.");
            throw new RuntimeException(interruptedException);
        }
        log.info("Yit ETL finished");
    }
}
