package com.sn.gameelectricity.data.model.bean;

import java.util.List;

public class GetAssistResponse {

    private int userId;
    private int goodsId;
    private String goodsName;
    private String goodsIcon;
    private Object patternId;
    private Object groupMember;
    private int groupMemberNum;
    private int successMemberNum;
    private String ownerName;
    private String ownerIcon;
    private String groupCode;
    private String startTime;
    private String endTime;
    private int status;
    private String requestUri;
    private Object inviteCode;
    private boolean joinFlag;
    private List<MemberInfoDTO> memberInfo;

    public int getSuccessMemberNum() {
        return successMemberNum;
    }

    public void setSuccessMemberNum(int successMemberNum) {
        this.successMemberNum = successMemberNum;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public Object getPatternId() {
        return patternId;
    }

    public void setPatternId(Object patternId) {
        this.patternId = patternId;
    }

    public Object getGroupMember() {
        return groupMember;
    }

    public void setGroupMember(Object groupMember) {
        this.groupMember = groupMember;
    }

    public int getGroupMemberNum() {
        return groupMemberNum;
    }

    public void setGroupMemberNum(int groupMemberNum) {
        this.groupMemberNum = groupMemberNum;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerIcon() {
        return ownerIcon;
    }

    public void setOwnerIcon(String ownerIcon) {
        this.ownerIcon = ownerIcon;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public Object getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(Object inviteCode) {
        this.inviteCode = inviteCode;
    }

    public boolean isJoinFlag() {
        return joinFlag;
    }

    public void setJoinFlag(boolean joinFlag) {
        this.joinFlag = joinFlag;
    }

    public List<MemberInfoDTO> getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(List<MemberInfoDTO> memberInfo) {
        this.memberInfo = memberInfo;
    }

    public static class MemberInfoDTO {
        private int accountStatus;
        private String avatarUrl;
        private boolean boolGuide;
        private String email;
        private int gender;
        private int goldCoin;
        private int guideStageId;
        private String idCard;
        private String mobile;
        private int money;
        private String nickName;
        private String realName;
        private int score;
        private String token;
        private String userCode;
        private int userId;

        public int getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(int accountStatus) {
            this.accountStatus = accountStatus;
        }

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

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
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

        public String getUserCode() {
            return userCode;
        }

        public void setUserCode(String userCode) {
            this.userCode = userCode;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
