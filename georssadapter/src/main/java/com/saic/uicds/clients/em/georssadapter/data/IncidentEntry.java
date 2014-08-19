package com.saic.uicds.clients.em.georssadapter.data;

import gov.niem.niem.niemCore.x20.ActivityDateDocument;
import gov.niem.niem.niemCore.x20.AreaType;
import gov.niem.niem.niemCore.x20.IncidentType;
import gov.niem.niem.niemCore.x20.TextType;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.namespace.QName;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uicds.incident.IncidentDocument;

import com.saic.uicds.clients.em.georssadapter.AirQualityObject;
import com.saic.uicds.clients.util.Common;

public class IncidentEntry extends Entry {

    private static Logger logger = LoggerFactory.getLogger(IncidentEntry.class);
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    private final IncidentDocument incident;

    //air quality data list
    ArrayList<AirQualityObject> aqList = new ArrayList<AirQualityObject>();

    private String airQualityIndexString;

    public IncidentEntry(String id, String sender, String headline, String author,
                         String description) {

        if (id == null)
            if (headline != null)
                id = headline;
            else
                logger.error("Link and Title cannot be null");

        String inStr = getId();

        //load the air quality data into a list for air quality each case
        BuildAirQualityDataList();

        if (description == null)
            description = "First level incident for alert";

        incident = org.uicds.incident.IncidentDocument.Factory.newInstance();
        IncidentType theIncident = incident.addNewIncident();
        super.setId(id);

        inStr = getId();

        if (id != null)
            theIncident.addNewIncidentEvent().addNewActivityIdentification()
                .addNewIdentificationID().setStringValue(id);
        // theIncident.addNewActivityIdentification().addNewIdentificationID().setStringValue(id);

        inStr = getId();

        if (headline != null)
            theIncident.addNewActivityName().setStringValue(headline);
        if (author != null)
            theIncident.addNewActivityCategoryText().setStringValue(author);
        else
            theIncident.addNewActivityCategoryText().setStringValue("CBRNE");
        if (description != null)
            theIncident.addNewActivityDescriptionText().setStringValue(description);

        // create a default empty location to avoid the MapService to throw exception
        theIncident.addNewIncidentLocation();

        //check whether is the air quality case or not.
        if (isThisAirQualityCase(headline)) {
            //add pre-given lat and long into the incident
            String aqName = getAirQualityIndexString();
            AirQualityObject obj = findAQObject(aqName);
            if (obj != null) {
                double aqlat = Double.valueOf(obj.getAqLat());
                double aqlong = Double.valueOf(obj.getAqLong());
                addPoint(aqlat, aqlong);
            }

        }

        inStr = getId();

        //clean up the air quality data object list after this case.
        cleanAQObjectList();

        inStr = getId();

    }

    public IncidentEntry(String id, String sender, String headline, String author, String category,
                         String description) {

        //load the air quality data into a list for air quality each case
        BuildAirQualityDataList();

        if (description == null)
            description = "First level incident for CAP alert";
        incident = org.uicds.incident.IncidentDocument.Factory.newInstance();
        IncidentType theIncident = incident.addNewIncident();
        super.setId(id);
        if (id != null)
            theIncident.addNewIncidentEvent().addNewActivityIdentification()
                .addNewIdentificationID().setStringValue(id);
        // theIncident.addNewActivityIdentification().addNewIdentificationID().setStringValue(id);
        if (headline != null)
            theIncident.addNewActivityName().setStringValue(headline);
        if (author != null)
            theIncident.addNewActivityCategoryText().setStringValue(author);
        else
            theIncident.addNewActivityCategoryText().setStringValue(category);
        if (description != null)
            theIncident.addNewActivityDescriptionText().setStringValue(description);

        // create a default empty location to avoid the MapService to throw exception
        theIncident.addNewIncidentLocation();

        //check whether is the air quality case or not.
        if (isThisAirQualityCase(headline)) {
            //add pre-given lat and long into the incident
            String aqName = getAirQualityIndexString();
            AirQualityObject obj = findAQObject(aqName);
            if (obj != null) {
                double aqlat = Double.valueOf(obj.getAqLat());
                double aqlong = Double.valueOf(obj.getAqLong());
                addPoint(aqlat, aqlong);
            }

        }

        //clean up the air quality data object list after this case.
        cleanAQObjectList();

    }

    @Override
    public void addPoint(double latitude, double longitude) {

        AreaType area = incident.getIncident().getIncidentLocationArray(0).addNewLocationArea();
        area.addNewAreaCircularDescriptionText()
            .setStringValue("This is the air quality point location");
        area.addNewAreaCircularRegion().set(Common.createCircle(new Double(latitude).toString(),
            new Double(longitude).toString()));
    }

    @Override
    public void addPolygon(Object polygon) {

    }

    private void BuildAirQualityDataList() {

        //load the air quality data into a list first

        if (aqList == null)
            aqList = new ArrayList<AirQualityObject>();

        //read air quality data from a text file and build the list
        try {
            // Open the file that is the first 
            // command line parameter
            FileInputStream fstream = new FileInputStream("AQSensorLatLon.txt");
            // Get the object of DataInputStream
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
                //System.out.println (strLine);
                AirQualityObject obj = new AirQualityObject();

                StringTokenizer str = new StringTokenizer(strLine, ",");
                int count = 0;
                while (str.hasMoreElements()) {
                    if (count == 0)
                        obj.setAqName((String) str.nextElement());

                    if (count == 1)
                        obj.setAqLat((String) str.nextElement());

                    if (count == 2)
                        obj.setAqLong((String) str.nextElement());
                    count++;
                }

                //for debugging, check obj
                //System.out.println ("Object details: " + obj.getAqName() +" " + obj.getAqLat() + " " + obj.getAqLong());

                aqList.add(obj);

            }
            //Close the input stream
            in.close();
        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void cleanAQObjectList() {

        aqList.clear();
        aqList = null;
    }

    public boolean findAQName(String name) {

        AirQualityObject obj = new AirQualityObject();

        Iterator<AirQualityObject> iterator = aqList.iterator();
        while (iterator.hasNext()) {
            obj = iterator.next();
            if (obj.getAqName().equalsIgnoreCase(name))
                return true;
        }

        return false;

    }

    public AirQualityObject findAQObject(String name) {

        AirQualityObject obj = new AirQualityObject();

        Iterator<AirQualityObject> iterator = aqList.iterator();
        while (iterator.hasNext()) {
            obj = iterator.next();
            if (obj.getAqName().equalsIgnoreCase(name))
                return obj;
        }

        return null;

    }

    private ActivityDateDocument getActivityDate(long timestamp) {

        ActivityDateDocument activityDate = ActivityDateDocument.Factory.newInstance();
        XmlCursor c = activityDate.addNewActivityDate().addNewDateRepresentation().newCursor();
        c.setName(new QName("http://niem.gov/niem/niem-core/2.0", "DateTime"));
        c.setTextValue(format.format(new Date(timestamp)));
        return activityDate;
    }

    /*
    public static AreaType getPoloygon(String polygonString) {

        AreaType area = AreaType.Factory.newInstance();

        String[] coords = polygonString.split(" ");
        if (coords.length >= 3) {
            int i = 0;
            for (String coord : coords) {
                LocationTwoDimensionalGeographicCoordinateDocument loc = toCoordinate(coord);
                if (loc != null) {
                    area.addNewAreaPolygonGeographicCoordinate().set(
                        loc.getLocationTwoDimensionalGeographicCoordinate());
                }
            }
        }

        return area;
    }
    
    private static LocationTwoDimensionalGeographicCoordinateDocument toCoordinate(String latLon) {

        String[] points = latLon.split(",");

        if (points.length != 2) {
            return null;
        }

        LocationTwoDimensionalGeographicCoordinateDocument loc = LocationTwoDimensionalGeographicCoordinateDocument.Factory.newInstance();

        try {
            String[] values = GeoUtil.toDegMinSec(GeoUtil.toDouble(points[0]));
            loc.addNewLocationTwoDimensionalGeographicCoordinate().addNewGeographicCoordinateLatitude().addNewLatitudeDegreeValue().setStringValue(
                values[0]);
            loc.getLocationTwoDimensionalGeographicCoordinate().getGeographicCoordinateLatitude().addNewLatitudeMinuteValue().setStringValue(
                values[1]);
            loc.getLocationTwoDimensionalGeographicCoordinate().getGeographicCoordinateLatitude().addNewLatitudeSecondValue().setStringValue(
                values[2]);
            values = GeoUtil.toDegMinSec(GeoUtil.toDouble(points[1]));
            loc.getLocationTwoDimensionalGeographicCoordinate().addNewGeographicCoordinateLongitude().addNewLongitudeDegreeValue().setStringValue(
                values[0]);
            loc.getLocationTwoDimensionalGeographicCoordinate().getGeographicCoordinateLongitude().addNewLongitudeMinuteValue().setStringValue(
                values[1]);
            loc.getLocationTwoDimensionalGeographicCoordinate().getGeographicCoordinateLongitude().addNewLongitudeSecondValue().setStringValue(
                values[2]);
        } catch (NumberFormatException e) {
              return null;
        }

        return loc;
    }
    */
    public String getAirQualityIndexString() {

        return airQualityIndexString;
    }

    @Override
    public XmlObject getContent() {

        return incident;
    }

    public boolean isThisAirQualityCase(String headline) {

        boolean isAQ = false;
        //check this is air quality case or not.
        StringTokenizer str = new StringTokenizer(headline, ",");
        int count = 0;
        String checkStr = "";
        while (str.hasMoreElements()) {
            if (count == 0)
                checkStr = (String) str.nextElement();
            break;
        }

        setAirQualityIndexString(checkStr);

        isAQ = findAQName(checkStr);

        return isAQ;
    }

    public void setAirQualityIndexString(String airQualityIndexString) {

        this.airQualityIndexString = airQualityIndexString;
    }

    public void setEventType(String eventType) {

        // logger.debug("set ActivityCategory: " + eventType);
        TextType category = TextType.Factory.newInstance();
        category.setStringValue(eventType);
        incident.getIncident().setActivityCategoryTextArray(0, category);
        // logger.debug("Incident: " + incident);
    }

    @Override
    public void setTimestamp(long timestamp) {

        super.setTimestamp(timestamp);

        incident.getIncident().addNewActivityDateRepresentation().set(getActivityDate(timestamp)
            .getActivityDate());
        XmlCursor cursor = incident.getIncident().getActivityDateRepresentationArray(0).newCursor();
        cursor.setName(new QName("http://niem.gov/niem/niem-core/2.0", "ActivityDate"));
        // cursor.setTextValue(format.format(new Date(timestamp)));
        cursor.dispose();
    }

}
