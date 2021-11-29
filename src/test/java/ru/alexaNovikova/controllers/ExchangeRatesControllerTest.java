package ru.alexaNovikova.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.alexaNovikova.services.ExchangeRatesService;
import ru.alexaNovikova.services.GiphyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(ExchangeRatesController.class)
public class ExchangeRatesControllerTest {

    @Value("${giphy.rich}")
    private String richTag;
    @Value("${giphy.broke}")
    private String brokeTag;
    @Value("${giphy.zero}")
    private String zeroTag;
    @Value("${giphy.error}")
    private String errorTag;

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRatesService exchangeRatesService;
    @MockBean
    private GiphyService gifService;

    @Test
    public void testListOfCharCodes() throws Exception {
        List<String> responseList = new ArrayList<>();
        responseList.add("TEST");
        Mockito.when(exchangeRatesService.getCharCodes())
                .thenReturn(responseList);
        mockMvc.perform(get("/rate/getCodes")
                .content(mapper.writeValueAsString(responseList))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").value("TEST"));
    }

    @Test
    public void testNullList() throws Exception {
        Mockito.when(exchangeRatesService.getCharCodes())
                .thenReturn(null);
        mockMvc.perform(get("/rate/getCodes")
                .content(mapper.writeValueAsString(null))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    public void whenReturnRichGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("richTag", this.richTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.compareRateForCurrencyCode(anyString()))
                .thenReturn(1);
        Mockito.when(gifService.getGif(this.richTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/rate/getGif/TESTCODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.richTag").value(this.richTag));
    }

    @Test
    public void whenReturnBrokeGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("brokeTag", this.brokeTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.compareRateForCurrencyCode(anyString()))
                .thenReturn(-1);
        Mockito.when(gifService.getGif(this.brokeTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/rate/getGif/TESTCODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.brokeTag").value(this.brokeTag));
    }

    @Test
    public void whenReturnZeroGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("zeroTag", this.zeroTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.compareRateForCurrencyCode(anyString()))
                .thenReturn(0);
        Mockito.when(gifService.getGif(this.zeroTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/rate/getGif/TESTCODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.zeroTag").value(this.zeroTag));
    }

    @Test
    public void whenReturnErrorGif() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("errorTag", this.errorTag);
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(map, HttpStatus.OK);
        Mockito.when(exchangeRatesService.compareRateForCurrencyCode(anyString()))
                .thenReturn(-5);
        Mockito.when(gifService.getGif(this.errorTag))
                .thenReturn(responseEntity);
        mockMvc.perform(get("/rate/getGif/TESTCODE")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errorTag").value(this.errorTag));
    }

}