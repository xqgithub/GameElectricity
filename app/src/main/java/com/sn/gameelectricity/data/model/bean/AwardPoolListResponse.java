package com.sn.gameelectricity.data.model.bean;

public class AwardPoolListResponse {
    private int awardId;
    private int awardType;
    private String goodsName;
    private Object icon;
    private Object defaultPrice;
    private Object discountPrice;
    private int poolType;

    public AwardPoolListResponse() {
    }

    public AwardPoolListResponse(String goodsName, Object icon) {
        this.goodsName = goodsName;
        this.icon = icon;
    }

    public AwardPoolListResponse(int awardId, int awardType, String goodsName, Object icon, Object defaultPrice, Object discountPrice, int poolType) {
        this.awardId = awardId;
        this.awardType = awardType;
        this.goodsName = goodsName;
        this.icon = icon;
        this.defaultPrice = defaultPrice;
        this.discountPrice = discountPrice;
        this.poolType = poolType;
    }

    public int getPoolType() {
        return poolType;
    }

    public void setPoolType(int poolType) {
        this.poolType = poolType;
    }

    public int getAwardId() {
        return awardId;
    }

    public void setAwardId(int awardId) {
        this.awardId = awardId;
    }

    public int getAwardType() {
        return awardType;
    }

    public void setAwardType(int awardType) {
        this.awardType = awardType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Object getIcon() {
        return icon;
    }

    public void setIcon(Object icon) {
        this.icon = icon;
    }

    public Object getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(Object defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Object getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Object discountPrice) {
        this.discountPrice = discountPrice;
    }
}
