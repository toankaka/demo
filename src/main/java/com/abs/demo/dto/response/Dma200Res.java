package com.abs.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public class Dma200Res {

  @JsonProperty("200dma")
  public Dma200 Dma200;
}
