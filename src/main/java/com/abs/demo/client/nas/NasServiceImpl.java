package com.abs.demo.client.nas;

import com.abs.demo.client.nas.dto.NasRes;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Data
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class NasServiceImpl implements NasService{

  private final RestTemplate restTemplate;

  @Value("${services.nas.base-uri}")
  private String getStocksUrl;

  @Override
  public NasRes getStockList() {
//    Map<String, Object> uriMaps = new HashMap<>();
//    uriMaps.put("tableonly", true);
//    uriMaps.put("limit", 10);
//    uriMaps.put("exchange", "NASDAQ");
    UriComponentsBuilder urlBuilderGet = UriComponentsBuilder
        .fromHttpUrl(getStocksUrl);
    String url = urlBuilderGet.toUriString();
    HttpEntity<?> entity = new HttpEntity<>(buildHeaders());
    ResponseEntity<NasRes> responseGet = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        NasRes.class);
    if (responseGet.getStatusCode() != HttpStatus.OK) {
      log.error("Call Nas error, return response: {} {}", responseGet.getStatusCode(), responseGet.getBody());
    }
    return responseGet.getBody();
  }

  private HttpHeaders buildHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON_UTF8));
    return headers;
  }
}
