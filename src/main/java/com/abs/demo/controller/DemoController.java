package com.abs.demo.controller;

import com.abs.demo.dto.response.ClosePriceRes;
import com.abs.demo.dto.response.Dma200Res;
import com.abs.demo.feature.stock.StockUsecase;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(path = "/api/v2")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class DemoController {

  private final StockUsecase stockUsecase;

  @GetMapping("/{symbol}/closePrice")
  public ResponseEntity<ClosePriceRes> getClosePrice(@PathVariable(value = "symbol") String symbol,
      @Valid @NotBlank(message = "startDate not blank") @RequestParam(value = "startDate") String startDate,
      @Valid @RequestParam(value = "endDate") String endDate) {
    ClosePriceRes closePriceRes = stockUsecase.getClosePrice(symbol, startDate, endDate);
    return ResponseEntity.ok(closePriceRes);
  }

  @GetMapping("/{symbol}/200dma")
  public ResponseEntity<Dma200Res> get200dma(@PathVariable(value = "symbol") String symbol,
      @Valid @NotBlank(message = "startDate not blank") @RequestParam(value = "startDate") String startDate) {
    Dma200Res dma200Res = stockUsecase.get200dma(symbol, startDate);
    return ResponseEntity.ok(dma200Res);
  }

  @GetMapping("/{symbol}/200dma1000")
  public ResponseEntity<List<Dma200Res>> get200dma1000(@PathVariable(value = "symbol") List<String> symbol,
      @Valid @NotBlank(message = "startDate not blank") @RequestParam(value = "startDate") String startDate) {
    List<Dma200Res> dma200Res = stockUsecase.get200dma1000(symbol, startDate);
    return ResponseEntity.ok(dma200Res);
  }
}
