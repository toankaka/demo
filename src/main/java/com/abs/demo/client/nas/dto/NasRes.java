package com.abs.demo.client.nas.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class NasRes {
  public static class Data{
    public Object filters;
    public Table table;
    public int totalrecords;
    public String asof;
  }

  public static class Headers{
    public String symbol;
    public String name;
    public String lastsale;
    public String netchange;
    public String pctchange;
    public String marketCap;
  }

  public class Root{
    public Data data;
    public Object message;
    public Status status;
  }

  public static class Row{
    public String symbol;
    public String name;
    public String lastsale;
    public String netchange;
    public String pctchange;
    public String marketCap;
    public String url;
  }

  public static class Status{
    public int rCode;
    public Object bCodeMessage;
    public Object developerMessage;
  }

  public static class Table{
    public Headers headers;
    public List<Row> rows;
  }


}
