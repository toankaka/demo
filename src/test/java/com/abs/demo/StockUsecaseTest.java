package com.abs.demo;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.abs.demo.dto.response.ClosePriceRes;
import com.abs.demo.dto.response.ClosePriceRes.DateClose;
import com.abs.demo.dto.response.ClosePriceRes.Prices;
import com.abs.demo.dto.response.Dma200;
import com.abs.demo.dto.response.Dma200Res;
import com.abs.demo.dto.response.StockInfoDto;
import com.abs.demo.exception.model.BadParamsException;
import com.abs.demo.exception.model.NotFoundException;
import com.abs.demo.feature.stock.StockUsecaseImpl;
import com.abs.demo.job.ScheduleJob;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class StockUsecaseTest {

  @InjectMocks
  StockUsecaseImpl stockUsecase;

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  @Mock
  ScheduleJob scheduleJob;

  @Test
  public void getClosePrice_Success() {

    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-06";
    String endDate = "2017-02-07";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    ClosePriceRes closePriceResExpected = new ClosePriceRes();
    List<DateClose> dateCloses = new ArrayList<>();
    dateCloses.add(DateClose.builder().closePrice("17.05").date("2017-02-06").build());
    dateCloses.add(DateClose.builder().closePrice("17.34").date("2017-02-07").build());
    closePriceResExpected.setPrices(Prices.builder().ticker(symbol).dateClose(dateCloses).build());

    // Actual
    ClosePriceRes closePriceActual = stockUsecase.getClosePrice(symbol, startDate, endDate);
    assertEquals(closePriceResExpected, closePriceActual);
  }

  @Test(expected = NotFoundException.class)
  public void getClosePrice_symbol_not_found() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-06";
    String endDate = "2017-02-07";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("").build());
    listStock.put("SVCO", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.getClosePrice(symbol, startDate, endDate);
  }

  @Test(expected = NotFoundException.class)
  public void getClosePrice_start_date_not_found() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-06";
    String endDate = "2017-02-07";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-08", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.getClosePrice(symbol, startDate, endDate);
  }

  @Test(expected = NotFoundException.class)
  public void getClosePrice_start_date_no_data() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-06";
    String endDate = "2017-02-07";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-08", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-05", YYYY_MM_DD)).ma200("").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.getClosePrice(symbol, startDate, endDate);
  }

  @Test(expected = NotFoundException.class)
  public void getClosePrice_end_date_not_found() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-07";
    String endDate = "2017-02-09";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-08", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.getClosePrice(symbol, startDate, endDate);
  }

  @Test(expected = NotFoundException.class)
  public void getClosePrice_end_date_no_data() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-07";
    String endDate = "2017-02-09";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-10", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-08", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-04", YYYY_MM_DD)).ma200("").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.getClosePrice(symbol, startDate, endDate);
  }

  @Test(expected = BadParamsException.class)
  public void getClosePrice_start_date_greater_than_end_date() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-09";
    String endDate = "2017-02-07";

    // Expected
    stockUsecase.getClosePrice(symbol, startDate, endDate);
  }

  @Test
  public void get200dma_Success() {

    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-06";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.33").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.34").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    Dma200Res dma200ResExpected = new Dma200Res();
    dma200ResExpected.setDma200(Dma200.builder().ticker("TWIN").avg("17.34").build());

    // Actual
    Dma200Res dma200Actual = stockUsecase.get200dma(symbol, startDate);
    assertEquals(dma200ResExpected, dma200Actual);
  }

  @Test(expected = NotFoundException.class)
  public void get200dma_symbol_not_found() {
    //Input
    String symbol = "ABC";
    String startDate = "2017-02-06";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("").build());
    listStock.put("SVCO", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.get200dma(symbol, startDate);
  }

  @Test(expected = NotFoundException.class)
  public void get200dma_start_date_not_found() {
    //Input
    String symbol = "TWIN";
    String startDate = "2017-02-06";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoDtos = new ArrayList<>();
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-08", YYYY_MM_DD)).ma200("").build());
    stockInfoDtos.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("").build());
    listStock.put("TWIN", stockInfoDtos);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.get200dma(symbol, startDate);
  }

  @Test
  public void get200dma1000_Success() {

    //Input
    List<String> symbol = Arrays.asList("TWIN", "GE", "CVCO");
    String startDate = "2017-02-06";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoTWIN = new ArrayList<>();
    stockInfoTWIN.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.33").build());
    stockInfoTWIN.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.34").build());
    List<StockInfoDto> stockInfoGE = new ArrayList<>();
    stockInfoGE.add(StockInfoDto.builder().closePrice("15.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.31").build());
    stockInfoGE.add(StockInfoDto.builder().closePrice("15.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.32").build());
    List<StockInfoDto> stockInfoCVCO = new ArrayList<>();
    stockInfoCVCO.add(StockInfoDto.builder().closePrice("14.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.35").build());
    stockInfoCVCO.add(StockInfoDto.builder().closePrice("14.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.36").build());
    listStock.put("TWIN", stockInfoTWIN);
    listStock.put("GE", stockInfoGE);
    listStock.put("CVCO", stockInfoCVCO);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    List<Dma200Res> dma200ResExpected = new ArrayList<>();
    dma200ResExpected.add(Dma200Res.builder().Dma200(Dma200.builder().ticker("TWIN").avg("17.34").build()).build());
    dma200ResExpected.add(Dma200Res.builder().Dma200(Dma200.builder().ticker("GE").avg("17.32").build()).build());
    dma200ResExpected.add(Dma200Res.builder().Dma200(Dma200.builder().ticker("CVCO").avg("17.36").build()).build());

    // Actual
    List<Dma200Res> dma200Actual = stockUsecase.get200dma1000(symbol, startDate);
    assertEquals(dma200ResExpected, dma200Actual);
  }

  @Test(expected = NotFoundException.class)
  public void get200dma1000_symbol_not_found() {
    //Input
    List<String> symbol = Arrays.asList("TWIN", "GE", "ABC");
    String startDate = "2017-02-06";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoTWIN = new ArrayList<>();
    stockInfoTWIN.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.33").build());
    stockInfoTWIN.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.34").build());
    List<StockInfoDto> stockInfoGE = new ArrayList<>();
    stockInfoGE.add(StockInfoDto.builder().closePrice("15.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.31").build());
    stockInfoGE.add(StockInfoDto.builder().closePrice("15.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.32").build());
    List<StockInfoDto> stockInfoCVCO = new ArrayList<>();
    stockInfoCVCO.add(StockInfoDto.builder().closePrice("14.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.35").build());
    stockInfoCVCO.add(StockInfoDto.builder().closePrice("14.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.36").build());
    listStock.put("TWIN", stockInfoTWIN);
    listStock.put("GE", stockInfoGE);
    listStock.put("CVCO", stockInfoCVCO);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.get200dma1000(symbol, startDate);
  }

  @Test(expected = NotFoundException.class)
  public void get200dma1000_start_date_not_found() {
    //Input
    List<String> symbol = Arrays.asList("TWIN", "GE", "CVCO");
    String startDate = "2017-02-06";

    // mock
    Map<String, List<StockInfoDto>> listStock = new HashMap<>();
    List<StockInfoDto> stockInfoTWIN = new ArrayList<>();
    stockInfoTWIN.add(StockInfoDto.builder().closePrice("17.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.33").build());
    stockInfoTWIN.add(StockInfoDto.builder().closePrice("17.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.34").build());
    List<StockInfoDto> stockInfoGE = new ArrayList<>();
    stockInfoGE.add(StockInfoDto.builder().closePrice("15.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.31").build());
    stockInfoGE.add(StockInfoDto.builder().closePrice("15.05").closeDate(stringToDate("2017-02-06", YYYY_MM_DD)).ma200("17.32").build());
    List<StockInfoDto> stockInfoCVCO = new ArrayList<>();
    stockInfoCVCO.add(StockInfoDto.builder().closePrice("14.34").closeDate(stringToDate("2017-02-07", YYYY_MM_DD)).ma200("17.35").build());
    stockInfoCVCO.add(StockInfoDto.builder().closePrice("14.05").closeDate(stringToDate("2017-02-05", YYYY_MM_DD)).ma200("17.36").build());
    listStock.put("TWIN", stockInfoTWIN);
    listStock.put("GE", stockInfoGE);
    listStock.put("CVCO", stockInfoCVCO);
    when(scheduleJob.getListStock()).thenReturn(listStock);

    // Expected
    stockUsecase.get200dma1000(symbol, startDate);
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
