package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YForecast {

    @SerializedName("date")
    private String date;

    @SerializedName("high")
    private String high;

    @SerializedName("low")
    private String low;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getHigh() {
        return high;
    }
    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }
    public void setLow(String low) {
        this.low = low;
    }
}