package com.abs.demo.feature.stock;

import com.abs.demo.dto.response.ClosePriceRes;
import com.abs.demo.dto.response.ClosePriceRes.DateClose;
import com.abs.demo.dto.response.ClosePriceRes.Prices;
import com.abs.demo.dto.response.Dma200;
import com.abs.demo.dto.response.Dma200Res;
import com.abs.demo.dto.response.StockInfoDto;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockUsecaseImpl implements StockUsecase {

  public static final String YYYY_MM_DD = "yyyy-MM-dd";

  @Override
  public ClosePriceRes getClosePrice(String symbol, String startDate, String endDate) {
    Date stDate = stringToDate(startDate, YYYY_MM_DD);
    Date enDate = stringToDate(endDate, YYYY_MM_DD);
    if (stDate.after(enDate)) {
      log.error("Start Date must be less than End Date");
      return null;
    }

    //kiem tra symbol co ton tai khong
    File f = checkFileExist(symbol);

    //doc file
    List<StockInfoDto> stockInfoDtos = readFile(f);

    int size = stockInfoDtos.size();
    List<DateClose> dateCloses = new ArrayList<>();
    for (int i = size; i > 0; i--) {
      StockInfoDto data = stockInfoDtos.get(i - 1);

      if (stDate.equals(data.getCloseDate()) || stDate.before(data.getCloseDate())) {
        if (enDate.before(data.getCloseDate())) {
          break;
        }
        dateCloses.add(DateClose.builder().date(dateToString(data.getCloseDate(), YYYY_MM_DD))
            .closePrice(data.getClosePrice()).build());
      }
    }
    return ClosePriceRes.builder().prices(Prices.builder().ticker(symbol).dateClose(dateCloses).build()).build();
  }

  @Override
  public Dma200Res get200dma(String symbol, String startDate) {
    Date stDate = stringToDate(startDate, YYYY_MM_DD);
    //kiem tra symbol co ton tai khong
    File f = checkFileExist(symbol);

    //doc file
    List<StockInfoDto> stockInfoDtos = readFile(f);

    int size = stockInfoDtos.size();
    for (int i = size; i > 0; i--) {
      StockInfoDto data = stockInfoDtos.get(i - 1);
      if (stDate.equals(data.getCloseDate())) {
        return Dma200Res.builder().Dma200(Dma200.builder().avg(data.getMa200()).ticker(symbol).build()).build();
      }
    }
    return null;
  }

  @Override
  public List<Dma200Res> get200dma1000(List<String> symbol, String startDate) {
    return null;
  }

  private File checkFileExist(String symbol) {
    //kiem tra symbol co ton tai khong
    String fileName = String.format("%s.dat", symbol);
    File f = new File(fileName);
    if (!f.isFile() || !f.canRead()) {
      log.error("File not found");
      //throw o day
    }
    return f;
  }

  private List<StockInfoDto> readFile(File f) {
    List<StockInfoDto> stockInfoDtos = null;
    try {
      FileInputStream infile = new FileInputStream(f);
      ObjectInputStream inobj = new ObjectInputStream(infile);
      stockInfoDtos = (List<StockInfoDto>) inobj.readObject();
      inobj.close();
      infile.close();
    } catch (Exception e) {
//      throw o day
    }
    return stockInfoDtos;
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
