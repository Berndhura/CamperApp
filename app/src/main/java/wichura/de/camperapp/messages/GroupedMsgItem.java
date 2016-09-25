package wichura.de.camperapp.messages;

import com.google.gson.annotations.Expose;

/**
 * Created by ich on 20.06.2016.
 * CamperApp
 */
public class GroupedMsgItem {

    @Expose
    private String message;
    @Expose
    private String name;
    @Expose
    private String url;
    @Expose
    private long date;
    @Expose
    private String idFrom;
    @Expose
    private String idTo;
    @Expose
    private String adId;

    public String getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(String idFrom) {
        this.idFrom=idFrom;
    }

    public String getIdTo() {
        return idTo;
    }

    public void setIdTo(String idTo) {
        this.idTo=idTo;
    }


    public String getUrl() { return url; }

    public String getMessage() {
        return message;
    }

    public String getName() { return name;}

    public void setName(String name) {
        this.name = name;
    }

    public long getDate() {
        return this.date;
    }

    public String getAdId() { return this.adId; }
}
