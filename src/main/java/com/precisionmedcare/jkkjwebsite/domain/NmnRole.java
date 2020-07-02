package com.precisionmedcare.jkkjwebsite.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmnRole implements Serializable {

  private long id;
  private String rolename;
  private long status;
  private String description;

}
