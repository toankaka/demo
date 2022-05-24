package com.abs.demo.feature;

import com.abs.demo.client.quandl.dto.QuandlRes;
import com.abs.demo.dto.response.StockInfoDto;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StockService {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  private static final DecimalFormat df = new DecimalFormat("0.00");

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
