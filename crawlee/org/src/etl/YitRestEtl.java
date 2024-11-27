package org.src.etl;

import com.google.gson.Gson;
import org.src.common.model.export.CsvExportDocument;
import org.src.common.model.export.ExportDocument;
import org.src.etl.http.rest.RestClient;
import org.src.etl.model.RestEtlConfig;
import org.src.etl.model.yit.BuildingEntity;
import org.src.etl.model.yit.YitResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

public class YitRestEtl extends AbstractRestEtl {
    private static final Logger log = Logger.getLogger(YitRestEtl.class.getCanonicalName());
    private final Gson gson;

    public YitRestEtl(RestEtlConfig restEtlConfig) {
        super(restEtlConfig);
        gson = new Gson();
    }

    private YitResponse inputStreamToYitResponse(InputStream stream) throws IOException {
        String response = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        stream.close();
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

    private Object[] transformWithProjection(List<BuildingEntity> buildings){
        return null;
    }

    private ExportDocument load(){
        return null;
    }
    @Override
    public ExportDocument extractTransformLoad() {
        YitResponse response = extract();
        return null;
    }
}
