package com.jason.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by kohyusik on 23/10/2019.
 */
@Slf4j
@Component
public class EsClient {
    @Value("${elasticsearch.hosts}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    public String query(String index, String query) throws IOException, URISyntaxException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet();
        URI uri = new URI("http", null, host, port, "/" + index + "/_search", "q=\"" + query + "\"", null);
        getRequest.setURI(uri);
        HttpResponse response = client.execute(getRequest);
        ResponseHandler<String> handler = new BasicResponseHandler();
        return handler.handleResponse(response);
    }
}
