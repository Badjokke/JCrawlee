package org.src.etl;

import com.google.gson.Gson;
import org.src.common.io.IOManager;
import org.src.common.model.export.CsvExportDocument;
import org.src.common.model.export.ExportDocument;
import org.src.etl.http.rest.RestClient;
import org.src.etl.mapper.ProjectionMapper;
import org.src.etl.model.RestEtlConfig;
import org.src.etl.model.yit.BuildingEntity;
import org.src.etl.model.yit.YitRequest;
import org.src.etl.model.yit.YitResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private void saveExportFile(ExportDocument exportDocument) {
        log.info(String.format("%s.%s", exportDocument.getFilename(), exportDocument.getFileExtension()));
        FutureTask<Boolean> task = new FutureTask<>(() -> IOManager.writeFile(String.format("%s.%s", exportDocument.getFilename(), exportDocument.getFileExtension()), exportDocument.getContent(), this.getRestEtlConfig().outputFileDirectory()));
        task.run();
    }

    private ExportDocument createExportDocument(BuildingEntity entity) {
        Map<String, Object> fields = entity.Fields();
        String[] content = new String[fields.keySet().size()];
        int i = 0;
        for (String key : fields.keySet())
            content[i++] = fields.get(key).toString();

        return new CsvExportDocument(this.getRestEtlConfig().outputFilePrefix() + Math.random(), fields.keySet(), new ArrayList<>() {{
            add(content);
        }});
    }

    private List<ExportDocument> createExportFile(List<BuildingEntity> entities) {
        return entities.stream().map(this::createExportDocument).collect(Collectors.toList());
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
                createExportFile(transform.apply(response.Hits())).stream().forEach(this::saveExportFile);
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
