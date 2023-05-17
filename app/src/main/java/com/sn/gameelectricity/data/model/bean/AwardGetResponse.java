package com.sn.gameelectricity.data.model.bean;

public class AwardGetResponse {

    private int id;
    private String goodsName;
    private String icon;
    private int recoverCoinNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getRecoverCoinNum() {
        return recoverCoinNum;
    }

    public void setRecoverCoinNum(int recoverCoinNum) {
        this.recoverCoinNum = recoverCoinNum;
    }
}
