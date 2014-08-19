package com.saic.uicds.clients.em.georssadapter.data;

import java.util.Calendar;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.xmlbeans.XmlObject;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Area;

public class AlertEntry
    extends Entry {

    private AlertDocument alert;

    public static AlertDocument createDefaultAlert() {

        AlertDocument alert = x1.oasisNamesTcEmergencyCap1.AlertDocument.Factory.newInstance();
        alert.addNewAlert();
        alert.getAlert().setStatus(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Status.ACTUAL);
        alert.getAlert().setMsgType(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.MsgType.ALERT);
        alert.getAlert().setScope(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Scope.PUBLIC);
        alert.getAlert().addNewInfo();
        alert.getAlert().getInfoArray(0).addCategory(
            x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.CBRNE);
        alert.getAlert().getInfoArray(0).setEvent("event");
        alert.getAlert().getInfoArray(0).setUrgency(
            x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Urgency.EXPECTED);
        alert.getAlert().getInfoArray(0).setSeverity(
            x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Severity.EXTREME);
        alert.getAlert().getInfoArray(0).setCertainty(
            x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Certainty.OBSERVED);

        return alert;
    }

    public AlertEntry() {

        alert = createDefaultAlert();
    }

    public AlertEntry(AlertDocument alert, String id, String sender, String author,
        String headline, String description) {

        this.alert = (AlertDocument) alert.copy();
        init(id, sender, author, headline, description);
    }

    public AlertEntry(String id, String sender, String event, String headline, String description) {

        this();
        init(id, sender, event, headline, description);
    }

    @Override
    public void addPoint(double latitude, double longitude) {

        Area[] areas = alert.getAlert().getInfoArray(0).getAreaArray();
        if (areas.length > 0) {
            String[] circles = areas[0].getCircleArray();
            if (circles.length > 0) {
                String[] parts = circles[0].split(" ");
                String[] points = parts[0].split(",");
                if (Double.parseDouble(points[0]) == latitude
                    && Double.parseDouble(points[1]) == longitude)
                    return;
            }
        }
        Area area = Area.Factory.newInstance();
        area.addCircle(new String((new StringBuilder()).append(latitude).append(",").append(
            longitude).append(" 0.0").toString()));
        alert.getAlert().getInfoArray(0).addNewArea().set(area);
    }

    @Override
    public void addPolygon(Object polygon) {

        // TODO Auto-generated method stub

    }

    public XmlObject getContent() {

        return alert;
    }

    private void init(String id, String sender, String author, String headline, String description) {

        if (id != null) {
            super.setId(id);
            alert.getAlert().setIdentifier(id);
        }
        if (sender != null)
            alert.getAlert().setSender(sender);
        if (author != null)
            alert.getAlert().getInfoArray(0).setEvent(author);
        if (headline != null)
            alert.getAlert().getInfoArray(0).setHeadline(headline);
        if (description != null)
            alert.getAlert().getInfoArray(0).setDescription(description);
        
        
    }

    public void setTimestamp(long timestamp) {

        super.setTimestamp(timestamp);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(timestamp));
        alert.getAlert().setSent(calendar);
    }
}
