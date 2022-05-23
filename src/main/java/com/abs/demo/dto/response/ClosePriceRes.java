package com.abs.demo.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class ClosePriceRes {

  @JsonProperty("Prices")
  public Prices prices;

  @Builder
  @EqualsAndHashCode
  @ToString
  public static class DateClose {

    public String date;
    public String closePrice;
  }

  @Builder
  @EqualsAndHashCode
  @ToString
  public static class Prices {

    @JsonProperty("Ticker")
    public String ticker;
    @JsonProperty("DateClose")
    public List<DateClose> dateClose;
  }
}
