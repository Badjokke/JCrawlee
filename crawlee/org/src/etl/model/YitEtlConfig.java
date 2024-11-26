package org.src.etl.model;

import org.src.etl.model.yit.YitEndpointConfiguration;

public record YitEtlConfig(String url, YitEndpointConfiguration restEndpointConfig) {

}
