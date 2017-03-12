package org.sitenv.xdrmessagesender.dto;

/**
 * Created by Brian on 2/23/2017.
 */
public class XdrMessageMetaDataProperties {
    private String from = "admin@sitenv.org";
    private String to = "admin@sitenv.org";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
