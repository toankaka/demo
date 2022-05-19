package com.abs.demo.job;

import com.abs.demo.client.nas.NasService;
import com.abs.demo.client.nas.dto.NasRes;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleJob {
  private final NasService nasService;
  private static NasRes nasRes;

  @PostConstruct
  public void getStocks(){
    log.info("Start get stocks");
    nasRes = nasService.getStockList();
    log.info("End get stocks");
  }
}
