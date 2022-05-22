package com.abs.demo.feature.entity;

import java.util.Date;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.SequenceGenerator;
//import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
//@Entity
//@Table(name = "STOCK_CLOSE_PRICE")
public class StockEntity {

//  @Id
//  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WL_STOCK_CLOSE_PRICE_SEQ")
//  @SequenceGenerator(sequenceName = "WL_STOCK_CLOSE_PRICE_SEQ", allocationSize = 1, name = "WL_STOCK_CLOSE_PRICE_SEQ")
//  @Column(name = "ID")
//  private Long id;

//  @Column(name = "SYMBOL")
//  private String symbol;

//  @Column(name = "CLOSE_DATE")
  private Date closeDate;

//  @Column(name = "CLOSE_PRICE")
  private String closePrice;

//  @Column(name = "MA200")
  private String ma200;
}
