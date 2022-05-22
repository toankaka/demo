package com.abs.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class Dma200 {

  @JsonProperty("Ticker")
  public String ticker;
  @JsonProperty("Avg")
  public String avg;
}
