package com.abs.demo.feature.stock;

import com.abs.demo.client.nas.dto.Row;
import com.abs.demo.client.quandl.QuandlService;
import com.abs.demo.client.quandl.dto.QuandlRes;
import com.abs.demo.dto.response.ClosePriceRes;
import com.abs.demo.dto.response.ClosePriceRes.DateClose;
import com.abs.demo.dto.response.ClosePriceRes.Prices;
import com.abs.demo.job.ScheduleJob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockUsecaseImpl implements StockUsecase {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  private final ScheduleJob scheduleJob;
  private final QuandlService quandlService;

  @Override
  public ClosePriceRes getClosePrice(String symbol, String startDate, String endDate) {
    Map<String, List<Row>> resMap = scheduleJob.getResMap();
    List<Row> rows = resMap.get(symbol);
    if (rows == null || rows.size() == 0) {
      log.error("not found");
    }
    QuandlRes stockInfo = quandlService.getStockInfo(symbol);
    Date sDate = stringToDate(stockInfo.getDataset().getStart_date(), YYYY_MM_DD);
    Date eDate = stringToDate(stockInfo.getDataset().getEnd_date(), YYYY_MM_DD);
    Date stDate = stringToDate(startDate, YYYY_MM_DD);
    Date enDate = stringToDate(endDate, YYYY_MM_DD);
    if (stDate.after(eDate)) {
      log.error("Start Date must be less than End Date");
    }
    if (stDate.before(sDate)) {
      log.error("No data from StartDate ");
    }
    if (enDate.after(eDate)) {
      log.error("No data from EndDate ");
    }
    int size = stockInfo.getDataset().getData().size();
    List<DateClose> dateCloses = new ArrayList<>();
    for (int i = size; i > 0; i--) {
      Date dataDate = stringToDate(stockInfo.getDataset().getData().get(i - 1).get(0).toString(), YYYY_MM_DD);
      if (stDate.equals(dataDate) || stDate.before(dataDate)) {
        if (enDate.before(dataDate)) {
          break;
        }
        dateCloses.add(DateClose.builder().date(stockInfo.getDataset().getData().get(i - 1).get(0).toString())
            .closePrice(stockInfo.getDataset().getData().get(i - 1).get(11).toString()).build());
      }
    }
    return ClosePriceRes.builder().prices(Prices.builder().ticker(symbol).dateClose(dateCloses).build()).build();
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
