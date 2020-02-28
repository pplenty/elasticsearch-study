package com.jason.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : yusik
 * @date : 2019-06-22
 */
@Configuration
public class ElasticsearchConfig {

    @Value("#{'${elasticsearch.hosts}'.split(',')}")
    private List<String> hosts;
    
    @Value("${elasticsearch.port}")
    private int port;

    @Bean
    public EsClient getEsClient() {
        return new EsClient();
    }

    @Bean
    public RestClient restClient() {

        List<HttpHost> hostList = new ArrayList<>();
        for(String host : hosts) {
            hostList.add(new HttpHost(host, port, "http"));
        }

        return RestClient.builder(
                hostList.toArray(new HttpHost[0])).build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {

        List<HttpHost> hostList = new ArrayList<>();
        for(String host : hosts) {
            hostList.add(new HttpHost(host, port, "http"));
        }

        return new RestHighLevelClient(RestClient.builder(
                hostList.toArray(new HttpHost[0])));
    }
}