package com.abs.demo.feature.stock;

import com.abs.demo.dto.response.ClosePriceRes;
import com.abs.demo.dto.response.Dma200Res;
import java.util.List;

public interface StockUsecase {

  ClosePriceRes getClosePrice(String symbol, String startDate, String endDate);

  Dma200Res get200dma(String symbol, String startDate);

  List<Dma200Res> get200dma1000(List<String> symbol, String startDate);
}
