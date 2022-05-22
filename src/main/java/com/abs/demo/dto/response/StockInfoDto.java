package com.abs.demo.dto.response;

import java.io.Serializable;
import java.util.Date;
import lombok.Builder;

@Builder
public class StockInfoDto implements Serializable {

  private static final long serialVersionUID = -6875765108816414972L;
  private Date closeDate;

  private String closePrice;

  private String ma200;

  public Date getCloseDate() {
    return closeDate;
  }

  public String getClosePrice() {
    return closePrice;
  }

  public String getMa200() {
    return ma200;
  }
}
