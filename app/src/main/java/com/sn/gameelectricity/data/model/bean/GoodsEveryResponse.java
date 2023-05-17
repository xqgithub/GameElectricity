package com.sn.gameelectricity.data.model.bean;

public class GoodsEveryResponse {

    private int id;
    private int goodsId;
    private int totalNum;
    private int usedNum;
    private String validlyTimeStart;
    private String validlyTimeEnd;
    private String goodsName;
    private String icon;
    private String services;
    private String mark;
    private double defaultPrice;
    private double discountPrice;
    private int type;
    private int rewardType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getUsedNum() {
        return usedNum;
    }

    public void setUsedNum(int usedNum) {
        this.usedNum = usedNum;
    }

    public String getValidlyTimeStart() {
        return validlyTimeStart;
    }

    public void setValidlyTimeStart(String validlyTimeStart) {
        this.validlyTimeStart = validlyTimeStart;
    }

    public String getValidlyTimeEnd() {
        return validlyTimeEnd;
    }

    public void setValidlyTimeEnd(String validlyTimeEnd) {
        this.validlyTimeEnd = validlyTimeEnd;
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

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public double getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRewardType() {
        return rewardType;
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
    }
}
