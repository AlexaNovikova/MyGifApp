package ru.alexaNovikova.client;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.alexaNovikova.models.ExchangeRates;

@org.springframework.cloud.openfeign.FeignClient(name = "OERClient", url = "${openexchangerates.url.general}")
public interface FeignOpenExchangeRatesClient {

    @GetMapping("/latest.json")
    ExchangeRates getLatestRates(
            @RequestParam("app_id") String appId,
            @RequestParam("base") String base
    );

    @GetMapping("/historical/{date}.json")
    ExchangeRates getHistoricalRates(
            @PathVariable String date,
            @RequestParam("app_id") String appId,
            @RequestParam("base") String base
    );
}
