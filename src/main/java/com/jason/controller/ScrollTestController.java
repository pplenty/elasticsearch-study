package com.jason.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jason.config.EsClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * Created by kohyusik on 2020/02/28.
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/scroll")
public class ScrollTestController {

    private final EsClient esClient;

    @GetMapping("/plan")
    public int plan() throws IOException, URISyntaxException {
/*        SearchResponse scrollResp = client.prepareSearch(test)
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setQuery(qb)
                .setSize(100).get(); //max of 100 hits will be returned for each scroll
//Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                //Handle the hit...
            }

            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        } while (scrollResp.getHits().getHits().length != 0);
        // Zero hits mark the end of the scroll and the while loop.*/


        ObjectMapper objectMapper = new ObjectMapper();
        String response = esClient.scrollQuery("plan_registrant_deal", 10000);

        Map<String, Object> map = objectMapper.readValue(response, Map.class);
        String scrollId = (String) map.get("_scroll_id");
        List<Object> result = new ArrayList<>();
        List resp = Optional.ofNullable(map.get("hits"))
                .map(o -> ((Map) o).get("hits"))
                .map(hits -> ((List) hits))
                .orElse(Collections.emptyList());

        do {

            for (Object o : resp) {
                result.add(o);
            }
            String scrollResp = esClient.scroll("1m", scrollId);
            map = objectMapper.readValue(scrollResp, Map.class);
            resp = Optional.ofNullable(map.get("hits"))
                    .map(o -> ((Map) o).get("hits"))
                    .map(hits -> ((List) hits))
                    .orElse(Collections.emptyList());
        } while (!resp.isEmpty());

        log.debug("{}", result.get(0));

        return result.size();
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(4);
        List<String> results = new ArrayList<>();

        IntStream.range(0, 5)
                .boxed()
                .forEach(i -> es.execute(() -> {

                    HttpClient client = HttpClientBuilder.create().build();
                    HttpGet getRequest = new HttpGet();
                    URI uri = null;
                    try {
                        uri = new URI("http", null, "127.0.0.1", 9999, "/scroll/plan", "q=" + i, null);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    getRequest.setURI(uri);
                    HttpResponse response = null;
                    try {
                        response = client.execute(getRequest);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    try {
                        results.add(handler.handleResponse(response));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }));

        Thread.sleep(30000);
        System.out.println(results);
    }
}
