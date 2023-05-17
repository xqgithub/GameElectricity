package com.sn.gameelectricity.data.model.bean;

import java.util.List;

public class AwardPoolResponse {

    private LegendDTO legend;
    private EpicDTO epic;
    private RareDTO rare;
    private ValuableDTO valuable;
    private WishShoppingDTO wishShopping;
    private int refreshNeedGoldCoinNum;

    public LegendDTO getLegend() {
        return legend;
    }

    public void setLegend(LegendDTO legend) {
        this.legend = legend;
    }

    public EpicDTO getEpic() {
        return epic;
    }

    public void setEpic(EpicDTO epic) {
        this.epic = epic;
    }

    public RareDTO getRare() {
        return rare;
    }

    public void setRare(RareDTO rare) {
        this.rare = rare;
    }

    public ValuableDTO getValuable() {
        return valuable;
    }

    public void setValuable(ValuableDTO valuable) {
        this.valuable = valuable;
    }

    public WishShoppingDTO getWishShopping() {
        return wishShopping;
    }

    public void setWishShopping(WishShoppingDTO wishShopping) {
        this.wishShopping = wishShopping;
    }

    public int getRefreshNeedGoldCoinNum() {
        return refreshNeedGoldCoinNum;
    }

    public void setRefreshNeedGoldCoinNum(int refreshNeedGoldCoinNum) {
        this.refreshNeedGoldCoinNum = refreshNeedGoldCoinNum;
    }

    public static class LegendDTO {
        private List<AwardListDTO> awardList;
        private double ratio;

        public List<AwardListDTO> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<AwardListDTO> awardList) {
            this.awardList = awardList;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        public static class AwardListDTO {
            private int awardId;
            private int awardType;
            private String goodsName;
            private Object icon;
            private Object defaultPrice;
            private Object discountPrice;

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
    }

    public static class EpicDTO {
        private List<AwardListDTO> awardList;
        private double ratio;

        public List<AwardListDTO> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<AwardListDTO> awardList) {
            this.awardList = awardList;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        public static class AwardListDTO {
            private int awardId;
            private int awardType;
            private String goodsName;
            private String icon;
            private Object defaultPrice;
            private Object discountPrice;

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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
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
    }

    public static class RareDTO {
        private List<AwardListDTO> awardList;
        private double ratio;

        public List<AwardListDTO> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<AwardListDTO> awardList) {
            this.awardList = awardList;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        public static class AwardListDTO {
            private int awardId;
            private int awardType;
            private String goodsName;
            private String icon;
            private Object defaultPrice;
            private Object discountPrice;

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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
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
    }

    public static class ValuableDTO {
        private List<AwardListDTO> awardList;
        private double ratio;

        public List<AwardListDTO> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<AwardListDTO> awardList) {
            this.awardList = awardList;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        public static class AwardListDTO {
            private int awardId;
            private int awardType;
            private String goodsName;
            private Object icon;
            private Object defaultPrice;
            private Object discountPrice;

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
    }

    public static class WishShoppingDTO {
        private List<AwardListDTO> awardList;
        private double ratio;

        public List<AwardListDTO> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<AwardListDTO> awardList) {
            this.awardList = awardList;
        }

        public double getRatio() {
            return ratio;
        }

        public void setRatio(double ratio) {
            this.ratio = ratio;
        }

        public static class AwardListDTO {
            private int awardId;
            private int awardType;
            private String goodsName;
            private String icon;
            private Object defaultPrice;
            private Object discountPrice;

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

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
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
    }
}
