package crawlee.org.src.etl.model;

import java.util.Map;

public record RestEtlConfig(String url, RestEndpointConfig restEndpointConfig, Map<String, String> projection,
                            String outputFilePrefix, String outputFileDirectory,Boolean useOnlyProjection) {

}
