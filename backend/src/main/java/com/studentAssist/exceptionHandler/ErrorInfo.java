package com.studentAssist.exceptionHandler;

public class ErrorInfo {

    public final String response;
    public final String message;


    public String getResponse() {
        return response;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @param response
     * @param code
     */
    public ErrorInfo(String response, String code) {
        super();
        this.response = response;
        this.message = code;
    }

}
