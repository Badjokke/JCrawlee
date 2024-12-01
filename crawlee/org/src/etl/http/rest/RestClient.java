package org.src.etl.http.rest;


import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.logging.Logger;


public final class RestClient {
    private static final Logger log = Logger.getLogger(RestClient.class.getCanonicalName());

    private static HttpClient createHttpClient() {
        return HttpClient.newBuilder().build();
    }

    private static HttpRequest createPlainPostRequest(String uri, String body) throws URISyntaxException {
        assert uri != null : "Uri is null";
        assert body != null : "Body is null";
        return HttpRequest
                .newBuilder(new URI(uri))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(body)).build();
    }

    public static HttpResponse<InputStream> doPost(String uri, String body) throws URISyntaxException, IOException, InterruptedException {
        HttpRequest request = createPlainPostRequest(uri, body);
        log.info(String.format("Sending POST request to: %s with body: %s", request.uri().toString(), body));
        return createHttpClient().send(createPlainPostRequest(uri, body), HttpResponse.BodyHandlers.ofInputStream());
    }


}
