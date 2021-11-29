package ru.alexaNovikova.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alexaNovikova.client.FeignOpenExchangeRatesClient;
import ru.alexaNovikova.models.ExchangeRates;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Service
public class ExchangeRatesService {

    private ExchangeRates prevRates;
    private ExchangeRates currentRates;

    private final FeignOpenExchangeRatesClient openExchangeRatesClient;
    private final SimpleDateFormat dateFormat;
    private final SimpleDateFormat timeFormat;
    @Value("${openexchangerates.app.id}")
    private String appId;
    @Value("${openexchangerates.base}")
    private String base;

    @Autowired
    public ExchangeRatesService(
            FeignOpenExchangeRatesClient openExchangeRatesClient
    ) {
        this.openExchangeRatesClient = openExchangeRatesClient;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.timeFormat = new SimpleDateFormat("yyyy-MM-dd HH");
    }

    public List<String> getCharCodes() {
        return new ArrayList<>(this.currentRates.getRates().keySet());
    }

    @PostConstruct
    public void init(){
        refreshRates();
    }

    public int compareRateForCurrencyCode(String charCode) {
        refreshRates();
        return (charCode!= null && this.currentRates!=null&&this.prevRates!=null)
                ?  currentRates.getRates().get(charCode).compareTo(prevRates.getRates().get(charCode))
                : -5;
    }

    public void refreshRates() {
        long currentTime = System.currentTimeMillis();
        this.refreshCurrentRates(currentTime);
        this.refreshPrevRates(currentTime);
    }


    private void refreshCurrentRates(long time) {
        if (
                this.currentRates == null ||
                        !timeFormat.format(Long.valueOf(this.currentRates.getTimestamp()) * 1000)
                                .equals(timeFormat.format(time))
        ) {
            this.currentRates = openExchangeRatesClient.getLatestRates(this.appId, base);
        }
    }

    private void refreshPrevRates(long time) {
        Calendar prevCalendar = Calendar.getInstance();
        prevCalendar.setTimeInMillis(time);
        String currentDate = dateFormat.format(prevCalendar.getTime());
        prevCalendar.add(Calendar.DAY_OF_YEAR, -1);
        String newPrevDate = dateFormat.format(prevCalendar.getTime());
        if (
                this.prevRates == null
                        || (
                        !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(newPrevDate)
                                && !dateFormat.format(Long.valueOf(this.prevRates.getTimestamp()) * 1000)
                                .equals(currentDate)
                )
        ) {
            this.prevRates = openExchangeRatesClient.getHistoricalRates(newPrevDate, appId, base);
        }
    }

}
