package ru.alexaNovikova.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alexaNovikova.models.ExchangeRates;
import ru.alexaNovikova.client.FeignOpenExchangeRatesClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("alexaNovikova.ru")
public class ExchangeRatesServiceTest {

    @Value("${openexchangerates.base}")
    private String base;
    @Autowired
    private ExchangeRatesService exchangeRatesService;
    @MockBean
    private FeignOpenExchangeRatesClient openExchangeRatesClient;

    private ExchangeRates currentRates;
    private ExchangeRates prevRates;


    @Before
    public void init() {
        int time = 1709459222;
        this.currentRates = new ExchangeRates();
        this.currentRates.setTimestamp(time);
        this.currentRates.setBase("BASE");
        Map<String, Double> currentRatesMap = new HashMap<>();
        currentRatesMap.put("value1", 0.2);
        currentRatesMap.put("value2", 0.3);
        currentRatesMap.put("value3", 1.0);
        this.currentRates.setRates(currentRatesMap);

        time = 1709372822;
        this.prevRates = new ExchangeRates();
        this.prevRates.setTimestamp(time);
        this.prevRates.setBase("BASE");
        Map<String, Double> prevRatesMap = new HashMap<>();
        prevRatesMap.put("value1", 0.2);
        prevRatesMap.put("value2", 1.0);
        prevRatesMap.put("value3", 0.5);
        this.prevRates.setRates(prevRatesMap);

    }

    @Test
    public void whenPositiveChanges() {
        Mockito.when(openExchangeRatesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(openExchangeRatesClient.getHistoricalRates(anyString(), anyString(), anyString()))
                .thenReturn(this.prevRates);
        int result = exchangeRatesService.compareRateForCurrencyCode("value3");
        assertEquals(1, result);
    }


    @Test
    public void whenZeroChanges() {
        Mockito.when(openExchangeRatesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(openExchangeRatesClient.getHistoricalRates(anyString(), anyString(), anyString()))
                .thenReturn(this.prevRates);
        int result = exchangeRatesService.compareRateForCurrencyCode("value1");
        assertEquals(0, result);
    }

    @Test
    public void whenNegativeChanges() {
        Mockito.when(openExchangeRatesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(openExchangeRatesClient.getHistoricalRates(anyString(), anyString(), anyString()))
                .thenReturn(this.prevRates);
        int result = exchangeRatesService.compareRateForCurrencyCode("value2");
        assertEquals(-1, result);
    }

    @Test
    public void whenInputIsNull() {
        Mockito.when(openExchangeRatesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(openExchangeRatesClient.getHistoricalRates(anyString(), anyString(), anyString()))
                .thenReturn(this.prevRates);
        int result = exchangeRatesService.compareRateForCurrencyCode(null);
        assertEquals(-5, result);
    }


    @Test
    public void whenGetList() {
        Mockito.when(openExchangeRatesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(openExchangeRatesClient.getHistoricalRates(anyString(), anyString(), anyString()))
                .thenReturn(this.prevRates);
        exchangeRatesService.refreshRates();
        List<String> result = exchangeRatesService.getCharCodes();
        assertThat(result, containsInAnyOrder("value1", "value2", "value3"));
    }

    @Test
    public void whenCurrentIsNull() {
        this.currentRates = null;
        Mockito.when(openExchangeRatesClient.getLatestRates(anyString(), anyString()))
                .thenReturn(this.currentRates);
        Mockito.when(openExchangeRatesClient.getHistoricalRates(anyString(), anyString(), anyString()))
                .thenReturn(this.prevRates);
        exchangeRatesService.refreshRates();
        int result = exchangeRatesService.compareRateForCurrencyCode("value1");
        assertEquals(-5, result);
    }
}