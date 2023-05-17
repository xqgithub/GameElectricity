package com.sn.gameelectricity.data.model.bean;

import java.util.List;

public class GoodsRightListResponse {

    private int totalGoodsNum;
    private List<GoodsVOListDTO> goodsVOList;
    private int total;

    public int getTotalGoodsNum() {
        return totalGoodsNum;
    }

    public void setTotalGoodsNum(int totalGoodsNum) {
        this.totalGoodsNum = totalGoodsNum;
    }

    public List<GoodsVOListDTO> getGoodsVOList() {
        return goodsVOList;
    }

    public void setGoodsVOList(List<GoodsVOListDTO> goodsVOList) {
        this.goodsVOList = goodsVOList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class GoodsVOListDTO {
        private Object id;
        private int userId;
        private int goodsId;
        private String goodsIcon;
        private Object goodsName;
        private Object defaultPrice;
        private Object discountPrice;
        private Object recoverScore;
        private Object goodsUrl;
        private Object storyUrl;
        private int number;
        private Object postage;
        private Object supplierAddress;
        private Object supplierPhone;
        private Object lastExchangeTime;

        public GoodsVOListDTO() {
        }

        public GoodsVOListDTO(int goodsId, String goodsIcon, int number) {
            this.goodsId = goodsId;
            this.goodsIcon = goodsIcon;
            this.number = number;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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

        public Object getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(Object goodsName) {
            this.goodsName = goodsName;
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

        public Object getRecoverScore() {
            return recoverScore;
        }

        public void setRecoverScore(Object recoverScore) {
            this.recoverScore = recoverScore;
        }

        public Object getGoodsUrl() {
            return goodsUrl;
        }

        public void setGoodsUrl(Object goodsUrl) {
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

        public Object getPostage() {
            return postage;
        }

        public void setPostage(Object postage) {
            this.postage = postage;
        }

        public Object getSupplierAddress() {
            return supplierAddress;
        }

        public void setSupplierAddress(Object supplierAddress) {
            this.supplierAddress = supplierAddress;
        }

        public Object getSupplierPhone() {
            return supplierPhone;
        }

        public void setSupplierPhone(Object supplierPhone) {
            this.supplierPhone = supplierPhone;
        }

        public Object getLastExchangeTime() {
            return lastExchangeTime;
        }

        public void setLastExchangeTime(Object lastExchangeTime) {
            this.lastExchangeTime = lastExchangeTime;
        }
    }
}


