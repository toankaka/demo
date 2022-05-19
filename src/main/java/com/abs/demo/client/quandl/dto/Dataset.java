package com.abs.demo.client.quandl.dto;

import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class Dataset {
  public int id;
  public String dataset_code;
  public String database_code;
  public String name;
  public String description;
  public Date refreshed_at;
  public String newest_available_date;
  public String oldest_available_date;
  public List<String> column_names;
  public String frequency;
  public String type;
  public boolean premium;
  public Object limit;
  public Object transform;
  public Object column_index;
  public String start_date;
  public String end_date;
  public List<List<Object>> data;
  public Object collapse;
  public Object order;
  public int database_id;
}
