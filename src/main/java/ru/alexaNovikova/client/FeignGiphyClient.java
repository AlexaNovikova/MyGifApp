package ru.alexaNovikova.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@org.springframework.cloud.openfeign.FeignClient(name = "giphyClient", url = "${giphy.url.general}")
public interface FeignGiphyClient {

    @GetMapping("/random")
    ResponseEntity<Map> getRandomGifByTag(
            @RequestParam("api_key") String apiKey,
            @RequestParam("tag") String tag
    );

}