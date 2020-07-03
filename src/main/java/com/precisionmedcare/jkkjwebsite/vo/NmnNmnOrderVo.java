package com.precisionmedcare.jkkjwebsite.vo;

import com.precisionmedcare.jkkjwebsite.domain.NmnNmnOrder;

public class NmnNmnOrderVo extends NmnNmnOrder {

    private String generalTitle;

    private String totalAmount;

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }


    public String getGeneralTitle() {
        return generalTitle;
    }

    public void setGeneralTitle(String generalTitle) {
        this.generalTitle = generalTitle;
    }

}
