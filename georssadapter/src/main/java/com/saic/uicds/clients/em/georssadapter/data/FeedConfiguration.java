package com.saic.uicds.clients.em.georssadapter.data;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;

import com.saic.uicds.clients.em.georssadapter.FeedParser;

public class FeedConfiguration
    implements ConstantData {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String url = null;
    private boolean isIncident = false;
    private boolean isAlertOD = false;
    private String alert = null;
    private FeedParser parser = null;
    private String filter = null;
    private String eventType = null;

    // the default polling interval is set to 5 minutes
    private long pollingInterval = Default_PollingInterval;

    private String category;
    
    public FeedConfiguration() {

    }

    public boolean createFeedParser() {

        if (url != null) {
            parser = new FeedParser(url);
            return true;
        }
        return false;
    }

    public AlertDocument getAlert() {

        if (alert == null)
            return null;

        AlertDocument alertDocument = null;
        try {
            alertDocument = AlertDocument.Factory.parse(alert);
        } catch (XmlException e) {
            logger.error("Cannot parse the alert document:\n" + alert + "\nError Message: "
                + e.getMessage());
        }

        return alertDocument;
    }

    public FeedParser getParser() {

        return parser;
    }

    public long getPollingInterval() {

        return pollingInterval;
    }

    public String getUrl() {

        return url;
    }

    public String[] getXPaths() {

        if (parser != null) {
            return parser.getXPaths();
        }
        return null;
    }

    public boolean isIncident() {

        return isIncident;
    }

    public void setAlert(AlertDocument alert) {

        this.alert = alert.xmlText();
    }

    public void setIncident(boolean isIncident) {

        this.isIncident = isIncident;
    }

    public void setPollingInterval(long pollingInterval) {

        this.pollingInterval = pollingInterval;
    }

    public void setUrl(String url) {

        this.url = url;
    }

    public String toString() {
    	String wholeString =new String();
    	wholeString ="Url: " + url;
    	String type="";
    	String help2="";
    	
    	if(isIncident)
    	{
    		type="Incident";
    		help2="";
    		
    	}
    	else if(isAlertOD)
    	{
    		type="AlertOptionalData";
    		help2 ="";
    	}
    	else
    	{
    		type ="Alert";
    		help2=getAlert().xmlText(new XmlOptions().setSavePrettyPrint());
    	}
    		
    	wholeString +="\nType: " + type + "\n"
                + "\nPolling Interval: " + pollingInterval + "\n" + help2;
            //    + (isIncident ? "" : getAlert().xmlText(new XmlOptions().setSavePrettyPrint()));
    	
    	return wholeString;
/*
        return new String("Url: " + url + "\nType: " + (isIncident ? "Incident" : "Alert") + "\n"
            + "\nPolling Interval: " + pollingInterval + "\n"
            + (isIncident ? "" : getAlert().xmlText(new XmlOptions().setSavePrettyPrint())));
            */
    	
    }

	public boolean isAlertOD() {
		return isAlertOD;
	}

	public void setAlertOD(boolean isAlertOD) {
		this.isAlertOD = isAlertOD;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {

        this.filter = filter;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {

        this.eventType = eventType.replaceAll(" ", "_");
	}
}
