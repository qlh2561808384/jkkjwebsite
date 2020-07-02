package com.precisionmedcare.jkkjwebsite.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetails implements Serializable {

  private long id;
  private long userId;
  private String phone;
  private String email;
  private String idcard;

}
