package com.saic.uicds.clients.em.georssadapter.data;

import javax.swing.JLabel;

public interface ConstantData {

    public static final String FeedType_Atom = "atom_1.0";

    public static final String TreeRootNodeName = "GeoRSS Feeds";

    public static final String ContextFile = "georssadapter-context.xml";
    public static final String Earthquake_Feed = "http://earthquake.usgs.gov/earthquakes/shakemap/rss.xml";
    public static final String DC_Juvenile_Feed = "http://data.octo.dc.gov/feeds/cjis_juvenile/cjis_juvenile_current.xml";
    public static final String DC_Crime_Feed = "http://data.octo.dc.gov/feeds/crime_incidents/crime_incidents_current.xml";

    public static final long oneSecondInMillisecond = 1000L;
    public static final long oneMinuteInMillisecond = 60000L;
    public static final long oneDayInMillisecond = 86400000L;

    public static final String Title_GeoRSSInjector = "GeoRSS Injector";
    public static final String Title_CreateFeed = "Create Atom/RSS Feed";
    public static final String Title_DeleteFeed = "Delete Atom/RSS Feed";

    public static final String S_File = "File";
    public static final String S_New = "New";

    public static final String S_Incident = "Incident";
    public static final String S_Alert = "Alert";
    public static final String S_Alert_Optional_Data = "CAP Alert";
    public static final String S_Ok = "Ok";
    public static final String S_Cancel = "Cancel";

    public static final String Title_AlertConfig = "Alert Configuration";
    public static final String Title_AlertODConfig = "Alert Optional Data Configuration";
    public static final String Title_FeedConfig = "GeoRSS Feed Configuration";
    public static final String Title_FeedMangement = "GeoRSS Feed Management";

    public static final String S_Url = "RSS/Atom Feed Url: ";
    public static final String S_Type = "Entry Type: ";
    public static final String S_PollingInterval = "Polling Interval (in minute): ";

    //for filtering and event type 
    public static final String S_Filtering = "Filter Text: ";
    public static final String S_EventType = "Event Type (no space): ";
    
    public static final String S_ModifyFeed = "Re-Configure";
    public static final String S_DeleteFeed = "Delete";

    public static final String S_Event = "Event";
    public static final String S_Description = "Description";
    public static final String S_Headline = "Headline";
    public static final String S_Contact = "Contact";
    public static final String S_Address = "Address";
    public static final String S_Status = "Status";
    public static final String S_Scope = "Scope";
    public static final String S_Category = "Category";
    public static final String S_Urgency = "Urgency";
    public static final String S_Severity = "Severity";
    public static final String S_Certainty = "Certainty";
    
    //for alert optional data  ================================
    public static final String S_Identifier = "Identifier";
    public static final String S_Sender = "Sender";
    public static final String S_SentDateTime = "Sent Date Time";
    public static final String S_MessageType = "Message Type";
    public static final String S_Source = "Source";
    public static final String S_Restriction = "Restriction ";
 //   public static final String S_Code = "Code ";
    public static final String S_Note = "Note ";
    public static final String S_Reference = "Reference ";
   // public static final String S_ResponseType = "Response Type ";
    //public static final String S_EventCode = "Event Code: ";
    
    public static final String S_Language = "Language";
    public static final String S_Effective = "Effective";
    public static final String S_Onset = "Onset";
    public static final String S_Expires = "Expires";
  //  public static final String S_Sendername = "Sender Name";
    public static final String S_Instruction = "Instruction";
    public static final String S_Web = "Web";
  //  public static final String S_Parameter = "Parameter";
    public static final String S_Resource = "Resource";
    public static final String S_Area = "Area";
    public static final String S_CircleLatLong = "Circle"; //Lat/Long ";
    public static final String S_Polygon = "Polygon ";
  //  public static final String S_Geocode = "Geocode ";
       
    //===================end of alert optional data ======================

    public static final long Default_PollingInterval = 5;
    public static final long Default_Expiration = 1;

    public static final String Type_Plain_Text = "plain/text";

    public static final JLabel EmptyLabel = new JLabel("");
}
