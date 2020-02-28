package com.jason.controller;

import com.jason.config.EsClient;
import lombok.RequiredArgsConstructor;
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
import java.util.concurrent.CompletableFuture;

/**
 * Created by kohyusik on 23/10/2019.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/query")
public class QueryTestController {

    private final EsClient esClient;

    @GetMapping("/")
    public String index() throws IOException, URISyntaxException {

        String response = esClient.query("classes", "{\n" +
                "  \"query\": {\n" +
                "    \"term\": {\n" +
                "      \"professor\": {\n" +
                "        \"value\": \"kys\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "  , \"_source\": []\n" +
                "}");
        log.debug("{}", response);

        return "index page";
    }

    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {

            }

            System.out.println(1);
            return 1;
        });
    }
}
