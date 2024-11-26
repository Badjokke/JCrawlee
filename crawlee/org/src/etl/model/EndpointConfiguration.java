package org.src.etl.model;

public interface EndpointConfiguration {
    enum HttpMethod{
        POST, GET
    }
    HttpMethod getHttpMethod();
    String toJson();
}
