package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YResponse {

    @SerializedName("query")
    private YQuery query;

    @SerializedName("error")
    private YError error;

    public YQuery getQuery() {
        return query;
    }
    public void setQuery(YQuery query) {
        this.query = query;
    }

    public YError getError() {
        return error;
    }
    public void setError(YError error) {
        this.error = error;
    }
}