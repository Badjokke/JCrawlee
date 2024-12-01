package org.src.etl.model;

import org.src.etl.model.yit.YitRequest;


public record RestEndpointConfig(String method, YitRequest body) {
}
