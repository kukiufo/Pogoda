package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YItem {

    @SerializedName("condition")
    private YCondition condition;

    @SerializedName("forecast")
    private List<YForecast> forecast;

    public YCondition getCondition() {
        return condition;
    }
    public void setCondition(YCondition condition) {
        this.condition = condition;
    }

    public List<YForecast> getForecast() {
        return forecast;
    }
    public void setForecast(List<YForecast> forecast) {
        this.forecast = forecast;
    }
}