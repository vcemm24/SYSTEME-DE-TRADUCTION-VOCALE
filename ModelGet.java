package com.spokeapi;

import com.google.gson.annotations.SerializedName;

public class ModelGet {
    @SerializedName("reponse")
    private String reponse;

    public String getReponse() {
        return reponse;
    }
}
