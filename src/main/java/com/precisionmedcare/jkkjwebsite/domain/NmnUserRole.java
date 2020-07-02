package com.precisionmedcare.jkkjwebsite.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NmnUserRole implements Serializable {

  private long id;
  private long userid;
  private long roleid;

}
