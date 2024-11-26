package org.src.etl.model.yit;

import com.google.gson.Gson;
import org.src.etl.model.EndpointConfiguration;

public class YitEndpointConfiguration implements EndpointConfiguration {

    private String method;
    private YitRequestBody requestBody;
    @Override
    public HttpMethod getHttpMethod() {
        return null;
    }

    @Override
    public String toJson() {
        return new Gson().toJson(this);
    }

}
