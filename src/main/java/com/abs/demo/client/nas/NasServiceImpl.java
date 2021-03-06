package com.abs.demo.client.nas;

import com.abs.demo.client.nas.dto.NasRes;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Data
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class NasServiceImpl implements NasService {

  private final RestTemplate restTemplate;

  @Override
  public NasRes getStockList() {
    HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders());
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://api.nasdaq.com")
        .path("/api/screener/stocks").queryParam("tableonly", true).queryParam("limit", 6000)
        .queryParam("exchange", "NASDAQ");

    try {
      ResponseEntity<NasRes> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity,
          NasRes.class);
      return response.getBody();
    } catch (HttpClientErrorException e) {
      System.out.println("Call API " + builder.toUriString() + " FAIL. " + e.getResponseBodyAsString());
    }
    return null;
  }

  protected static HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("User-Agent", "PostmanRuntime/7.28.4");
    return headers;
  }

}
