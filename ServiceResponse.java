package com.spokeapi;

public class ServiceResponse {
    private int success;
    private String message;
    public ServiceResponse(){

    }
    public ServiceResponse(int success,String message){
        super();
        this.success=success;
        this.message=message;

    }

    public int getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
