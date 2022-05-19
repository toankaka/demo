package com.abs.demo.client.quandl;

import com.abs.demo.client.quandl.dto.QuandlRes;

public interface QuandlService {

  QuandlRes getStockInfo(String symbol);
}
