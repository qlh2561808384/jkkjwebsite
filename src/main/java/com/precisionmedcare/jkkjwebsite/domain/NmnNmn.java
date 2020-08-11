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
  private long viewNum;
  private long price;
  private String createTime;
  private long online;
  private double score;
  private String coverImg;
  private long status;
  private long detailedDrawing;
  private double quarterlyDiscount;
  private double annualDiscount;

}
