package com.abs.demo.job;

import com.abs.demo.client.nas.NasService;
import com.abs.demo.client.nas.dto.NasRes;
import com.abs.demo.client.nas.dto.Row;
import com.abs.demo.client.quandl.QuandlService;
import com.abs.demo.client.quandl.dto.QuandlRes;
import com.abs.demo.dto.response.StockInfoDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
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
  private final QuandlService quandlService;
  private Set<String> lstSymbol;
  private List<StockInfoDto> closePrice;
  private Map<String, List<StockInfoDto>> listStock;
  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  private static final DecimalFormat df = new DecimalFormat("0.00");

  @Scheduled(cron = "${cron.start-time}")
  public void getStockList() throws IOException, ClassNotFoundException {
    //job cuoi ngay, lay close price, avg
    getStocks();
  }

  @PostConstruct
  public void loadStocks() throws IOException, ClassNotFoundException {
    listStock = new HashMap<>();
    log.info("Start load stocks");
    File folder = new File("data");
    for (File file : folder.listFiles()) {
      File f = new File(file.getAbsolutePath());
      if (!f.isFile() || !f.canRead()) {
        continue;
      }
      FileInputStream infile = new FileInputStream(f);
      ObjectInputStream inobj = new ObjectInputStream(infile);
      List<StockInfoDto> stockInfoDtos = (List<StockInfoDto>) inobj.readObject();
      inobj.close();
      infile.close();
      String[] strings = file.getName().split("\\.");
      listStock.put(strings[0], stockInfoDtos);
    }
    log.info("End load stocks");
  }

  public void getStocks() throws IOException, ClassNotFoundException {
    log.info("Start get stocks");
    NasRes nasRes = nasService.getStockList();
    lstSymbol = nasRes.getData().getTable().getRows().stream().map(Row::getSymbol).collect(Collectors.toSet());
    for (String s : lstSymbol) {
      QuandlRes stockInfo;
      try {
        stockInfo = quandlService.getStockInfo(s);
      } catch (Exception e) {
        log.info("Error call stock info symbol: {} with error: {}", s, e.getMessage());
        continue;
      }
      average(stockInfo);
    }
    loadStocks();
    log.info("End get stocks");
  }


  @Async
  public void average(QuandlRes stockInfo) throws IOException {
    log.info("Start average");
    //init
    int size = stockInfo.getDataset().getData().size();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    String symbol = stockInfo.getDataset().getDataset_code();

    for (int i = 0; i < size; i++) {
      String closeDate = stockInfo.getDataset().getData().get(i).get(0).toString();
      String closePrice = stockInfo.getDataset().getData().get(i).get(4).toString();
      OptionalDouble average = OptionalDouble.empty();
      //khong du 200 ngay thi khong tinh average
      if (i + 201 < size) {
        average = stockInfo.getDataset().getData().subList(i, i + 201).stream().mapToDouble(x -> (double) x.get(4)).average();
      }
      String avg = "";
      if (average != null && average.isPresent()) {
        avg = df.format(average.getAsDouble());
      }
      stockInfoDtos.add(StockInfoDto.builder().closeDate(stringToDate(closeDate, YYYY_MM_DD)).ma200(avg)
          .closePrice(closePrice).build());
    }

    //luu file
    String tenFile = String.format("data/%s.dat", symbol);
    FileOutputStream fos = new FileOutputStream(tenFile);
    ObjectOutputStream outobj = new
        ObjectOutputStream(fos);
    outobj.writeObject(stockInfoDtos);
    outobj.close();
    fos.close();
    log.info("End average {}", symbol);
  }

  public static Date stringToDate(String strDate, String format) {
    DateFormat df = new SimpleDateFormat(format);
    try {
      return df.parse(strDate);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
}
