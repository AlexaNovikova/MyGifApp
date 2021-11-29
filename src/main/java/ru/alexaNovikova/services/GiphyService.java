package ru.alexaNovikova.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.alexaNovikova.client.FeignGiphyClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GiphyService {

    private final FeignGiphyClient giphyClient;
    @Value("${giphy.api.key}")
    private String apiKey;


    public ResponseEntity<Map> getGif(String tag) {
        ResponseEntity<Map> result = giphyClient.getRandomGifByTag(this.apiKey, tag);
        result.getBody().put("compareResult", tag);
        return result;
    }
}
