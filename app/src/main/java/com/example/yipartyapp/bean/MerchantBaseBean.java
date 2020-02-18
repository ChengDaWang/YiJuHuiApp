package com.example.yipartyapp.bean;

import java.util.List;

/**
 * 商家展示  外层结构bean
 */
public class MerchantBaseBean {
    private List<MerchantBean> Data;

    public List<MerchantBean> getData() {
        return Data;
    }

    public void setData(List<MerchantBean> data) {
        Data = data;
    }
}
