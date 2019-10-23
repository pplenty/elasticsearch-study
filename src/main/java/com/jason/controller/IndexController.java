package com.jason.controller;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @author : yusik
 * @date : 2019-06-22
 */
@Slf4j
@RestController
public class IndexController {

    private final RestClient restClient;
    private final RestHighLevelClient restHighLevelClient;

    public IndexController(RestClient restClient, RestHighLevelClient restHighLevelClient) {
        this.restClient = restClient;
        this.restHighLevelClient = restHighLevelClient;
    }

    @GetMapping("/")
    public String index() throws IOException {

        log.info("{}", restClient.getNodes());
        log.info("{}", restHighLevelClient.indices().getAlias(new GetAliasesRequest("*"), RequestOptions.DEFAULT).getAliases());

        return "index page";
    }
}
