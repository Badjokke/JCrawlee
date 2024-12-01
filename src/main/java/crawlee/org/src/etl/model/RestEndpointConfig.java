package crawlee.org.src.etl.model;


import crawlee.org.src.etl.model.yit.YitRequest;

public record RestEndpointConfig(String method, YitRequest body) {
}
