package com.abs.demo.client.quandl;

import com.abs.demo.client.nas.dto.NasRes;
import com.abs.demo.client.quandl.dto.Dataset;
import com.abs.demo.client.quandl.dto.QuandlRes;
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
public class QuandlServiceImpl implements QuandlService {

  private final RestTemplate restTemplate;

  @Override
  public QuandlRes getStockInfo(String symbol) {
    HttpEntity<?> requestEntity = new HttpEntity<>(getHeaders());
    String format = String.format("/api/v3/datasets/WIKI/%s.json", symbol);
    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://www.quandl.com")
        .path(format);

    try {
      ResponseEntity<QuandlRes> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, requestEntity,
          QuandlRes.class);
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
