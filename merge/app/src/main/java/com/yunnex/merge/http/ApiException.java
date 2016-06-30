package com.yunnex.merge.http;

/**
 * author ChenCHaoXue
 * Created by supercard on 2016/6/23 11:33
 */
public class ApiException extends RuntimeException {

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    public static final int USER_NOT_EXIST = 100;
    public static final int SESSION_EFFECT = 101;
    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code){
        String message = "";
        switch (code) {
            case USER_NOT_EXIST:
                message = "该用户不存在";
                break;
            case SESSION_EFFECT:
                message = "session失效，请重新登录";
                break;
            default:
                message = "未知错误";

        }
        return message;
    }
}
