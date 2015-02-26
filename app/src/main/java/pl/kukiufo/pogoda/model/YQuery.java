package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YQuery {

    @SerializedName("results")
    private YResults results;

    public YResults getResults() {
        return results;
    }
    public void setResults(YResults results) {
        this.results = results;
    }
}