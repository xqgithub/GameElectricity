package com.sn.gameelectricity.data.model.bean;

public class GetByEnabledResponse {

    private int id;
    private int payType;
    private int payNum;
    private int exchangeGoodsId;
    private String exchangeGoodsName;
    private String exchangeGoodsIcon;
    private int limitNum;
    private int totalNum;
    private int usedNum;
    private int status;
    private String validlyTimeStart;
    private String validlyTimeEnd;
    private int sysUserId;
    private Object sysUserName;
    private String createTime;
    private String updateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getPayNum() {
        return payNum;
    }

    public void setPayNum(int payNum) {
        this.payNum = payNum;
    }

    public int getExchangeGoodsId() {
        return exchangeGoodsId;
    }

    public void setExchangeGoodsId(int exchangeGoodsId) {
        this.exchangeGoodsId = exchangeGoodsId;
    }

    public String getExchangeGoodsName() {
        return exchangeGoodsName;
    }

    public void setExchangeGoodsName(String exchangeGoodsName) {
        this.exchangeGoodsName = exchangeGoodsName;
    }

    public String getExchangeGoodsIcon() {
        return exchangeGoodsIcon;
    }

    public void setExchangeGoodsIcon(String exchangeGoodsIcon) {
        this.exchangeGoodsIcon = exchangeGoodsIcon;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public int getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(int sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Object getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(Object sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
