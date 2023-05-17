package me.hgj.jetpackmvvm.network;

/**
 * 解析实体基类
 * Created by seaky on 2017/4/21.
 */

public class BaseEntity<T> {
    private int code;
    private String msg;
    private T data;


    public boolean isSuccess() {
        return getCode() == 0;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
