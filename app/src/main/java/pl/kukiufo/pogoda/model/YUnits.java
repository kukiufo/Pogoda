package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by kukiufo on 26.02.15.
 */

@Parcel
public class YUnits {

    @SerializedName("distance")
    private String distance;

    @SerializedName("pressure")
    private String  pressure;

    @SerializedName("speed")
    private String speed;

    @SerializedName("temperature")
    private String temperature;

    public String getDistance() {
        return distance;
    }
    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPressure() {
        return pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTemperature() {
        return temperature;
    }
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}