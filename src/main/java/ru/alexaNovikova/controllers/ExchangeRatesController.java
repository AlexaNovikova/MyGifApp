package ru.alexaNovikova.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.alexaNovikova.services.ExchangeRatesService;
import ru.alexaNovikova.services.GiphyService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rate")
@RequiredArgsConstructor
public class ExchangeRatesController {

    private final ExchangeRatesService exchangeRatesService;
    private final GiphyService giphyService;

    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.zero}")
    private String zeroTag;
    @Value("${giphy.error}")
    private String errorTag;

    @GetMapping("/getCodes")
    public List<String> getCharCodes() {
        return exchangeRatesService.getCharCodes();
    }

    @GetMapping("/compare/{charCode}")
    public int compareRates(@PathVariable String charCode) {
        return exchangeRatesService.compareRateForCurrencyCode(charCode);
    }

    @GetMapping("/getGif/{code}")
    public ResponseEntity<Map> getGif(@PathVariable String code) {
        ResponseEntity<Map> result = null;
        int gifKey = -5;
        String gifTag = this.errorTag;
        if (code != null) {
            gifKey = exchangeRatesService.compareRateForCurrencyCode(code);
        }
        switch (gifKey) {
            case 1:
                gifTag = this.richTag;
                break;
            case -1:
                gifTag = this.brokeTag;
                break;
            case 0:
                gifTag = this.zeroTag;
                break;
        }
        result = giphyService.getGif(gifTag);
        return result;
    }
}
