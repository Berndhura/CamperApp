package de.wichura.lks.models;

/**
 * Created by Bernd Wichura on 11.06.2017.
 * Luftkraftsport
 */

public class EbayAd {

    private String Title;
    private String location;
    private String thumbNailUrl;
    private String url;

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public void setThumbNailUrl(String thumbNailUrl) {
        this.thumbNailUrl = thumbNailUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
