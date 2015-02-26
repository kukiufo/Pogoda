package pl.kukiufo.pogoda.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kukiufo on 26.02.15.
 */
public class YResults {

    @SerializedName("channel")
    private YChannel channel;

    public YChannel getChannel() {
        return channel;
    }
    public void setChannel(YChannel channel) {
        this.channel = channel;
    }
}