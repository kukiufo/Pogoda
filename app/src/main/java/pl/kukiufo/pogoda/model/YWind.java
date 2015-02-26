package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YWind {

    @SerializedName("direction")
    private String direction;

    @SerializedName("speed")
    private String speed;

    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }
}