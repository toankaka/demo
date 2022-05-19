package com.abs.demo.job;

import com.abs.demo.client.nas.NasService;
import com.abs.demo.client.nas.dto.NasRes;
import com.abs.demo.client.nas.dto.Row;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Data
@Slf4j
@EnableScheduling
@RequiredArgsConstructor
public class ScheduleJob {
  private final NasService nasService;
  private Map<String, List<Row>> resMap;

  @Scheduled(cron = "${cron.start-time}")
  public void getStockList(){
    getStocks();
  }

  @PostConstruct
  public void getStocks() {
    log.info("Start get stocks");
    NasRes nasRes = nasService.getStockList();
    resMap = nasRes.getData().getTable().getRows().stream().collect(Collectors.groupingBy(Row::getSymbol));
    log.info("End get stocks");
  }
}
