package com.abs.demo.feature.stock;

import com.abs.demo.dto.response.ClosePriceRes;
import java.util.Date;

public interface StockUsecase {
  ClosePriceRes getClosePrice(String symbol, String startDate, String endDate);
}
