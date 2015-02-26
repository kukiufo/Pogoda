package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YError {

    @SerializedName("code")
    private String description;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}