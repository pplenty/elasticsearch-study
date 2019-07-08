package com.jason.controller;

import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : yusik
 * @date : 2019-06-22
 */
@RestController
public class IndexController {

    final
    RestClient restClient;

    @Autowired
    public IndexController(RestClient restClient) {
        this.restClient = restClient;
    }

    @GetMapping("/")
    public String index() {
        System.out.println(restClient.getNodes());
        return "index page";
    }
}
