package com.precisionmedcare.jkkjwebsite.vo;

import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;

public class NmnNmnOrderVo extends NmnNmnOrder {

    private String generalTitle;

    private Double totalAmount;

    private String payType;

    private Integer usageCount;

    private Integer promoCodeId;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getGeneralTitle() {
        return generalTitle;
    }

    public void setGeneralTitle(String generalTitle) {
        this.generalTitle = generalTitle;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public Integer getPromoCodeId() {
        return promoCodeId;
    }

    public void setPromoCodeId(Integer promoCodeId) {
        this.promoCodeId = promoCodeId;
    }

}
