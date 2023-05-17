package com.sn.gameelectricity.data.model.bean;

public class GoodsRightDetailResponse {

    private int id;
    private Object userId;
    private int goodsId;
    private String goodsIcon;
    private String goodsName;
    private double defaultPrice;
    private double discountPrice;
    private int recoverScore;
    private String goodsUrl;
    private Object storyUrl;
    private int number;
    private double postage;
    private String supplierAddress;
    private String supplierPhone;
    private Object lastExchangeTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public int getRecoverScore() {
        return recoverScore;
    }

    public void setRecoverScore(int recoverScore) {
        this.recoverScore = recoverScore;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public Object getStoryUrl() {
        return storyUrl;
    }

    public void setStoryUrl(Object storyUrl) {
        this.storyUrl = storyUrl;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPostage() {
        return postage;
    }

    public void setPostage(double postage) {
        this.postage = postage;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }

    public Object getLastExchangeTime() {
        return lastExchangeTime;
    }

    public void setLastExchangeTime(Object lastExchangeTime) {
        this.lastExchangeTime = lastExchangeTime;
    }
}
