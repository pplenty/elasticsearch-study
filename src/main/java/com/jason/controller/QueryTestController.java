package com.jason.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by kohyusik on 23/10/2019.
 */
@Slf4j
@RequestMapping("/query")
@RestController
public class QueryTestController {

    @Value("${elasticsearch.hosts}")
    private String host;

    @Value("${elasticsearch.port}")
    private int port;

    @GetMapping("/")
    public String index() throws IOException, URISyntaxException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getRequest = new HttpGet();
        URI uri = new URI("http", null, host, port, "/classes/_search", "q=\"{\n" +
                "  \"query\": {\n" +
                "    \"term\": {\n" +
                "      \"professor\": {\n" +
                "        \"value\": \"kys\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  , \"_source\": []\n" +
                "}\n\"", null);
        getRequest.setURI(uri);
        HttpResponse response = client.execute(getRequest);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String body = handler.handleResponse(response);

        log.debug("{}", uri.toString());
        log.debug("{}", body);

        return "index page";
    }
}
