package com.precisionmedcare.jkkjwebsite.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmnUser implements Serializable {

  private long id;
  private String email;
  private String password;
  private String nickname;
  private long status;

}
