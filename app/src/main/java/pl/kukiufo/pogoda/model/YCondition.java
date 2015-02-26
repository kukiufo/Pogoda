package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YCondition {

    @SerializedName("code")
    private String code;

    @SerializedName("temp")
    private String temp;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public String getTemp() {
        return temp;
    }
    public void setTemp(String temp) {
        this.temp = temp;
    }
}