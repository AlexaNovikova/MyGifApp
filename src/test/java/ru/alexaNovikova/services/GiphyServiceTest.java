package ru.alexaNovikova.services;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alexaNovikova.client.FeignGiphyClient;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan("alexaNovikova.ru")
public class GiphyServiceTest {

    @Autowired
    private GiphyService gifService;
    @MockBean
    private FeignGiphyClient gifClient;

    @Test
    public void whenPositiveChanges() {
        ResponseEntity<Map> testEntity = new ResponseEntity<>(new HashMap(), HttpStatus.OK);
        Mockito.when(gifClient.getRandomGifByTag(anyString(), anyString()))
                .thenReturn(testEntity);
        ResponseEntity<Map> result = gifService.getGif("controlResult");
        assertEquals("controlResult", result.getBody().get("compareResult"));
    }
}