package com.abs.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Builder;

@Builder
public class ClosePriceRes {

  @JsonProperty("Prices")
  public Prices prices;

  @Builder
  public static class DateClose {

    public String date;
    public String closePrice;
  }

  @Builder
  public static class Prices {

    @JsonProperty("Ticker")
    public String ticker;
    @JsonProperty("DateClose")
    public List<DateClose> dateClose;
  }
}
