package com.precisionmedcare.jkkjwebsite.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmnNmnOrder implements Serializable {

  private long id;
  private String outTradeNo;
  private long state;
  private String createTime;
  private String notifyTime;
  private long totalFee;
  private long nmnId;
  private String nmnTitle;
  private String nmnImg;
  private long userId;
  private String ip;
  private long del;
  private long status;
  private long paymentTypes;
  private String phone;
  private String email;
  private String idcard;
  private String address;
  private String receiverName;
}
