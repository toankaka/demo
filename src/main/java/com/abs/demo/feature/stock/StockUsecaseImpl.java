package com.abs.demo.feature.stock;

import com.abs.demo.dto.response.ClosePriceRes;
import com.abs.demo.dto.response.ClosePriceRes.DateClose;
import com.abs.demo.dto.response.ClosePriceRes.Prices;
import com.abs.demo.dto.response.Dma200;
import com.abs.demo.dto.response.Dma200Res;
import com.abs.demo.dto.response.StockInfoDto;
import com.abs.demo.exception.model.BadParamsException;
import com.abs.demo.exception.model.NotFoundException;
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


  private final ScheduleJob scheduleJob;
  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  @Override
  public ClosePriceRes getClosePrice(String symbol, String startDate, String endDate) {
    Date stDate = stringToDate(startDate, YYYY_MM_DD);
    Date enDate = stringToDate(endDate, YYYY_MM_DD);
    if (stDate.after(enDate)) {
      log.error("Start Date must be less than End Date");
      throw new BadParamsException("Start Date must be less than End Date");
    }

    Map<String, List<StockInfoDto>> listStock = scheduleJob.getListStock();
    if (listStock.get(symbol) == null) {
      log.info("Symbol {} not found", symbol);
      String mes = String.format("Symbol %s not found", symbol);
      throw new NotFoundException(mes);
    }
    List<StockInfoDto> stockInfoDtos = listStock.get(symbol);
    int size = stockInfoDtos.size();

    Date stInDate = stockInfoDtos.get(size - 1).getCloseDate();
    Date enInDate = stockInfoDtos.get(0).getCloseDate();

    if (stDate.before(stInDate)) {
      String mes = String.format("Symbol %s not found Start Date %s", symbol, startDate);
      throw new NotFoundException(mes);
    }

    if (stDate.after(enInDate)) {
      String mes = String.format("Symbol %s not found Start Date %s", symbol, startDate);
      throw new NotFoundException(mes);
    }

    if (enDate.after(enInDate)) {
      String mes = String.format("Symbol %s not found End Date %s", symbol, endDate);
      throw new NotFoundException(mes);
    }

    List<DateClose> dateCloses = new ArrayList<>();
    boolean foundSD = false;
    boolean foundED = false;
    for (int i = size; i > 0; i--) {
      StockInfoDto data = stockInfoDtos.get(i - 1);
      if (stDate.equals(data.getCloseDate())) {
        foundSD = true;
      }

      if (enDate.equals(data.getCloseDate())) {
        foundED = true;
      }
      if (stDate.equals(data.getCloseDate()) || stDate.before(data.getCloseDate())) {
        if (enDate.before(data.getCloseDate())) {
          break;
        }
        dateCloses.add(DateClose.builder().date(dateToString(data.getCloseDate(), YYYY_MM_DD))
            .closePrice(data.getClosePrice()).build());
      }
    }
    if (!foundSD || !foundED) {
      throw new NotFoundException("Start Date or End Date not found");
    }
    return ClosePriceRes.builder().prices(Prices.builder().ticker(symbol).dateClose(dateCloses).build()).build();
  }

  @Override
  public Dma200Res get200dma(String symbol, String startDate) {
    Date stDate = stringToDate(startDate, YYYY_MM_DD);
    Map<String, List<StockInfoDto>> listStock = scheduleJob.getListStock();
    if (listStock.get(symbol) == null) {
      log.info("Symbol {} not found", symbol);
      String mes = String.format("Symbol %s not found", symbol);
      throw new NotFoundException(mes);
    }

    List<StockInfoDto> stockInfoDtos = listStock.get(symbol);
    int size = stockInfoDtos.size();
    for (int i = size; i > 0; i--) {
      StockInfoDto data = stockInfoDtos.get(i - 1);
      if (stDate.equals(data.getCloseDate())) {
        return Dma200Res.builder().Dma200(Dma200.builder().avg(data.getMa200()).ticker(symbol).build()).build();
      }
    }
    String msg = String.format("Start date not found, date of first data: %s", dateToString(stockInfoDtos.get(size - 1).getCloseDate(), YYYY_MM_DD));
    throw new NotFoundException(msg);
  }

  @Override
  public List<Dma200Res> get200dma1000(List<String> symbol, String startDate) {
    Map<String, List<StockInfoDto>> listStock = scheduleJob.getListStock();
    List<Dma200Res> dma200Res = new ArrayList<>();
    Date stDate = stringToDate(startDate, YYYY_MM_DD);
    for (String s : symbol) {
      if (listStock.get(s) == null || listStock.get(s).size() == 0) {
        log.info("Symbol {} not found", s);
        String mes = String.format("Symbol %s not found", s);
        throw new NotFoundException(mes);
      }
      int size = listStock.get(s).size();
      boolean foundDate = false;
      for (int i = 0; i < size; i++) {
        StockInfoDto data = listStock.get(s).get(i);
        if (stDate.equals(data.getCloseDate())) {
          dma200Res.add(Dma200Res.builder().Dma200(Dma200.builder().avg(data.getMa200()).ticker(s).build()).build());
          foundDate = true;
          break;
        }
      }
      if (!foundDate) {
        String mes = String.format("Symbol %s not found date %s, date of first data: %s", s, startDate,
            dateToString(listStock.get(s).get(size - 1).getCloseDate(), YYYY_MM_DD));
        log.info(mes);
        throw new NotFoundException(mes);
      }
    }
    return dma200Res;
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

  public static String dateToString(Date date, String strFormat) {
    if (date != null) {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
      return simpleDateFormat.format(date);
    } else {
      return null;
    }
  }
}
