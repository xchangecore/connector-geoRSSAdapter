package com.saic.uicds.clients.em.georssadapter.data;

import gov.niem.niem.niemCore.x20.IncidentType;

import java.util.Calendar;


import java.util.Date;
import java.util.GregorianCalendar;
import org.apache.xmlbeans.XmlObject;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Area;

public class AlertODEntry
    extends Entry {

	
    private AlertDocument alert;

    public static AlertDocument createDefaultAlertOD() {

        AlertDocument alert = x1.oasisNamesTcEmergencyCap1.AlertDocument.Factory.newInstance();
        alert.addNewAlert();
        alert.getAlert().setStatus(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Status.ACTUAL);
        alert.getAlert().setMsgType(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.MsgType.ALERT);
        alert.getAlert().setScope(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Scope.PUBLIC);
        
        //added note (optional data) ///////////
        alert.getAlert().setNote(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Factory.newInstance().getNote());
        alert.getAlert().setReferences(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Factory.newInstance().getReferences());
        alert.getAlert().setSource(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Factory.newInstance().getSource());
        alert.getAlert().setRestriction(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Factory.newInstance().getRestriction());
        alert.getAlert().setAddresses(x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Factory.newInstance().getAddresses());
        
        //////////////////////////
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
        
        ////////////////////optional data parts //////////////////////////
        //not work for those two items, no idea why.
     //   alert.getAlert().getInfoArray(0).getParameterArray(0).setValueName(
       // 		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getParameterArray(0).getValueName());
        
  //      alert.getAlert().getInfoArray(0).setResponseTypeArray(
    //    		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getResponseTypeArray());
        
        alert.getAlert().getInfoArray(0).setLanguage(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getLanguage());
        
        alert.getAlert().getInfoArray(0).setContact(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getContact());
        
        alert.getAlert().getInfoArray(0).setWeb(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getWeb());
        
        alert.getAlert().getInfoArray(0).setOnset(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getOnset());
        
        alert.getAlert().getInfoArray(0).setSenderName(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getSenderName());
        
        alert.getAlert().getInfoArray(0).setInstruction(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getInstruction());
       
        alert.getAlert().getInfoArray(0).setEffective(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getEffective());
        
        alert.getAlert().getInfoArray(0).setExpires(
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Factory.newInstance().getExpires());       
                 
        //optional data, resource related
        alert.getAlert().getInfoArray(0).addNewResource();
        alert.getAlert().getInfoArray(0).getResourceArray(0).setResourceDesc(
        		""+x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Resource.EQUAL);
                       
   //     alert.getAlert().getInfoArray(0).getResourceArray(0).setUri(
     //   		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Resource.Factory.newInstance().getUri());
                  
        //optional data, area related
        alert.getAlert().getInfoArray(0).addNewArea();
        alert.getAlert().getInfoArray(0).getAreaArray(0).setAreaDesc(
        		""+x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Area.EQUAL);
               
        alert.getAlert().getInfoArray(0).getAreaArray(0).addNewCircle();
        alert.getAlert().getInfoArray(0).getAreaArray(0).setCircleArray( 0,
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Area.Factory.newInstance().getCircleArray().toString());
        
        alert.getAlert().getInfoArray(0).getAreaArray(0).addNewPolygon();
        alert.getAlert().getInfoArray(0).getAreaArray(0).setPolygonArray(0,
        		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Area.Factory.newInstance().getPolygonArray().toString());
        
     //   alert.getAlert().getInfoArray(0).getAreaArray(0).addNewGeocode();
     //   alert.getAlert().getInfoArray(0).getAreaArray(0).setGeocodeArray(
     //   		x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Area.Factory.newInstance().getGeocodeArray());
        
          return alert;
    }

    public AlertODEntry() {

        alert = createDefaultAlertOD();
    }

    public AlertODEntry(AlertDocument alert, String id, String sender, String author,
        String headline, String category, String description) {

        this.alert = (AlertDocument) alert.copy();
        init(id, sender, author, headline, category, description);
    }

    public AlertODEntry(String id, String sender, String event, String headline, String category, String description) {

        this();
        init(id, sender, event, headline, category, description);
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

    private String getID(String id)
    {
       int index = id.indexOf('=');
       int len = id.length();
          
       String newID = id.substring(index+1, len);
       
       return newID;
    }
    
    private void init(String id, String sender, String author, String headline, String category, String description) {

    	//1003 fli test
    	//alert = createDefaultAlertOD();
    	
    //	String newID = getID(id);
    	super.setId(id);
    	 
    	String inStr= this.getId(); 
    	
    	if (id != null) {
   //        super.setId(newID);
            alert.getAlert().setIdentifier(id);
        }
    	
    	inStr= this.getId(); 
    	
    	/*
        if (id != null) {
            super.setId(id);
            alert.getAlert().setIdentifier(id);
        }
       */
               
        if (sender != null)
            alert.getAlert().setSender(sender);
        if (author != null)
            alert.getAlert().getInfoArray(0).setEvent(author);
        if (headline != null)
            alert.getAlert().getInfoArray(0).setHeadline(headline);
        if (description != null)
            alert.getAlert().getInfoArray(0).setDescription(description);
        if (category != null)
            alert.getAlert().getInfoArray(0).setCategoryArray(0, getCatgory(category));
    }

    private x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.Enum getCatgory(String value)
    {
 	   x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.Enum x=null;
 	   x=x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.Enum.forString(value);	 
 	   return x;
    }
    
    
    public void setTimestamp(long timestamp) {

        super.setTimestamp(timestamp);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(timestamp));
        alert.getAlert().setSent(calendar);
    }
}
