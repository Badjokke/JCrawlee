package org.src.etl.model;

import java.util.Map;

public record RestEtlConfig(String url, RestEndpointConfig restEndpointConfig, Map<String, String> projection) {

}
