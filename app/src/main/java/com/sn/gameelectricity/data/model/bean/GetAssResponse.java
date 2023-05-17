package com.sn.gameelectricity.data.model.bean;

public class GetAssResponse {
    private String avatarUrl;
    private String nickName;

    public GetAssResponse(String avatarUrl, String nickName) {
        this.avatarUrl = avatarUrl;
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
