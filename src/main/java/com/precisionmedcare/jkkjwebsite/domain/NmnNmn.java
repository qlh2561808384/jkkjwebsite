package com.precisionmedcare.jkkjwebsite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmnNmn implements Serializable {

  private long id;
  private String title;
  private String summary;
  private Long viewNum;
  private double price;
  private String createTime;
  private Long online;
  private Double score;
  private String coverImg;
  private Long status;
  private String detailedDrawing;
  private double quarterlyDiscount;
  private double annualDiscount;
  private String titleCn;
  private String summaryCn;
  private long amountOfGoods;
}
