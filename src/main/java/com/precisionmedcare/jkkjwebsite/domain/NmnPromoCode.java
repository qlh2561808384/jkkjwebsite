package com.precisionmedcare.jkkjwebsite.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="NmnPromo_Code对象", description="商品优惠码")
public class NmnPromoCode extends Model<NmnPromoCode> {
  private static final long serialVersionUID = 1L;

  @ApiModelProperty(value = "文章主键id")
  @TableId(value = "id", type = IdType.AUTO)
  private Integer id;

  @ApiModelProperty(value = "优惠码")
  private String promoCode;

  @ApiModelProperty(value = "优惠码使用次数")
  private Integer usageCount;

  @ApiModelProperty(value = "优惠金额  单位：美金")
  private Double discountedPrice;

  @ApiModelProperty(value = "优惠码是否可用：0可用，1不可用")
  private Integer state;

}
