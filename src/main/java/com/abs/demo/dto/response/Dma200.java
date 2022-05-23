package com.abs.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Builder
@Data
@ToString
@EqualsAndHashCode
public class Dma200 {

  @JsonProperty("Ticker")
  public String ticker;
  @JsonProperty("Avg")
  public String avg;
}
