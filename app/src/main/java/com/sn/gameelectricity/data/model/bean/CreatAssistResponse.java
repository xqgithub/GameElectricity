package com.sn.gameelectricity.data.model.bean;

import java.util.List;

public class CreatAssistResponse {

    private String endTime;
    private int goodsId;
    private String groupCode;
    private String groupMember;
    private int groupMemberNum;
    private int id;
    private List<MemberInfoDTO> memberInfo;
    private int patternId;
    private String requestUri;
    private String startTime;
    private int status;
    private int userId;

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(int goodsId) {
        this.goodsId = goodsId;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(String groupMember) {
        this.groupMember = groupMember;
    }

    public int getGroupMemberNum() {
        return groupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        this.groupMemberNum = groupMemberNum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<MemberInfoDTO> getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(List<MemberInfoDTO> memberInfo) {
        this.memberInfo = memberInfo;
    }

    public int getPatternId() {
        return patternId;
    }

    public void setPatternId(int patternId) {
        this.patternId = patternId;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public static class MemberInfoDTO {
        private String avatarUrl;
        private boolean boolGuide;
        private String email;
        private int gender;
        private int goldCoin;
        private int guideStageId;
        private String idCard;
        private String mobile;
        private String nickName;
        private String realName;
        private int score;
        private String token;
        private int userId;

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        public boolean isBoolGuide() {
            return boolGuide;
        }

        public void setBoolGuide(boolean boolGuide) {
            this.boolGuide = boolGuide;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public int getGoldCoin() {
            return goldCoin;
        }

        public void setGoldCoin(int goldCoin) {
            this.goldCoin = goldCoin;
        }

        public int getGuideStageId() {
            return guideStageId;
        }

        public void setGuideStageId(int guideStageId) {
            this.guideStageId = guideStageId;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
