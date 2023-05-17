package com.sn.gameelectricity.data.model.bean;

public class CheckinResponse {

    private int checkinActivityId;
    private int rewardNum;
    private int rewardType;

    public int getCheckinActivityId() {
        return checkinActivityId;
    }

    public void setCheckinActivityId(int checkinActivityId) {
        this.checkinActivityId = checkinActivityId;
    }

    public int getRewardNum() {
        return rewardNum;
    }

    public void setRewardNum(int rewardNum) {
        this.rewardNum = rewardNum;
    }

    public int getRewardType() {
        return rewardType;
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
    }
}
