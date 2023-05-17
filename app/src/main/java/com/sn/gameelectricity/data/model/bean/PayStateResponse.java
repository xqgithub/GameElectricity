package com.sn.gameelectricity.data.model.bean;

import java.io.Serializable;

/**
 * 支付结果bean
 */

public class PayStateResponse implements Serializable {

    private String respMsg;
    private String respCode;

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

}
