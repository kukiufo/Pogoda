package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YChannel {

    @SerializedName("location")
    private YLocation location;

    @SerializedName("units")
    private YUnits units;

    @SerializedName("wind")
    private YWind wind;

    @SerializedName("atmosphere")
    private YAtmosphere atmosphere;

    @SerializedName("astronomy")
    private YAstronomy astronomy;

    @SerializedName("item")
    private YItem item;

    public YLocation getLocation() {
        return location;
    }
    public void setLocation(YLocation location) {
        this.location = location;
    }

    public YUnits getUnits() {
        return units;
    }
    public void setUnits(YUnits units) {
        this.units = units;
    }
    public YWind getWind() {
        return wind;
    }
    public void setWind(YWind wind) {
        this.wind = wind;
    }

    public YAtmosphere getAtmosphere() {
        return atmosphere;
    }
    public void setAtmosphere(YAtmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public YAstronomy getAstronomy() {
        return astronomy;
    }
    public void setAstronomy(YAstronomy astronomy) {
        this.astronomy = astronomy;
    }

    public YItem getItem() {
        return item;
    }
    public void setItem(YItem item) {
        this.item = item;
    }
}