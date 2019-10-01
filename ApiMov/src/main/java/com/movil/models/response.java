
package com.movil.models;


public class response {
    private boolean success;
    private String errorMsg;
    private String data;
    private final int status;

    public response(boolean success, String errorMsg, String data, int status) {
        this.success = success;
        this.errorMsg = errorMsg;
        this.data = data;
        this.status = status;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    
}
