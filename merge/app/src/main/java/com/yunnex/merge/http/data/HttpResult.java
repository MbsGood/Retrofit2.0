package com.yunnex.merge.http.data;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/23 11:30
 */
public class HttpResult<T> {
    private int resultCode;
    private String reason;
    private T result;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public T getData() {
        return result;
    }

    public void setData(T data) {
        this.result = data;
    }
}
