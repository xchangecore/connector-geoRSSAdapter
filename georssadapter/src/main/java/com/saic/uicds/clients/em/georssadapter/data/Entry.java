package com.saic.uicds.clients.em.georssadapter.data;

import java.util.Date;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

import com.saic.precis.x2009.x06.base.IdentificationType;

public abstract class Entry {

    private long timestamp;
    private String id;
    private String igId;
    // private String wpId;
    private IdentificationType identification;

    public abstract void addPoint(double latitude, double longitude);

    public abstract void addPolygon(Object polygon);

    public abstract XmlObject getContent();

    public String getContentString() {

        XmlObject content = getContent();
        if (content != null)
            return content.xmlText(new XmlOptions().setSavePrettyPrint());
        else
            return null;
    }

    public String getId() {

        return id;
    }

    public IdentificationType getIdentification() {

        return identification;
    }

    public String getIgId() {

        return igId;
    }

    public long getTimestamp() {

        return timestamp;
    }

    public String getWpId() {

        if (identification != null) {
            return identification.getIdentifier().getStringValue();
        } else {
            return null;
        }
    }

    public void setId(String id) {

        this.id = id;
    }

    public void setIdentification(IdentificationType identification) {

        this.identification = identification;
    }

    public void setIgId(String igId) {

        this.igId = igId;
    }

    public void setTimestamp(long timestamp) {

        this.timestamp = timestamp;
    }

    public String toString() {

        StringBuffer sb = new StringBuffer("UicdsEntry: Id:[ " + id + " ]\n");
        sb.append("Last Updated:[ " + new Date(timestamp).toString() + " ]\n");
        if (igId != null)
            sb.append(", IgId:[ " + igId + " ]\n");
        if (getWpId() != null)
            sb.append(", WPId:[ " + getWpId() + " ]\n");
        if (identification != null)
            sb.append("Identification:\n"
                + identification.xmlText(new XmlOptions().setSavePrettyPrint()));
        sb.append("\n");
        return sb.toString();
    }
}
