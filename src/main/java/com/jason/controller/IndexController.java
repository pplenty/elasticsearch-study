package com.jason.controller;

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

@RestController
public class IndexController {

    final RestClient restClient;
    final RestHighLevelClient restHighLevlClient;

    public IndexController(RestClient restClient, RestHighLevelClient restHighLevlClient) {
        this.restClient = restClient;
        this.restHighLevlClient = restHighLevlClient;
    }

    @GetMapping("/")
    public String index() throws IOException {
        System.out.println(restClient.getNodes());
        boolean result = true;

        System.out.println(restHighLevlClient.indices().getAlias(new GetAliasesRequest("*"), RequestOptions.DEFAULT).getAliases());

        if (result) {
//            throw new ArithmeticException("test");
        }
        return "index page";
    }
}
