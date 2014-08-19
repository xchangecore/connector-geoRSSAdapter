package com.saic.uicds.clients.em.georssadapter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oasisOpen.docs.wsn.b2.NotificationMessageHolderType;
/* orginial version
import com.sun.syndication.feed.module.georss.GeoRSSModule;
import com.sun.syndication.feed.module.georss.GeoRSSUtils;
import com.sun.syndication.feed.module.georss.SimpleParser;
import com.sun.syndication.feed.module.georss.geometries.AbstractGeometry;
import com.sun.syndication.feed.module.georss.geometries.Point;
import com.sun.syndication.feed.module.georss.geometries.Polygon;
*/
//rome-tool version
import org.rometools.feed.module.georss.GeoRSSModule;
import org.rometools.feed.module.georss.GeoRSSUtils;
import org.rometools.feed.module.georss.SimpleParser;
import org.rometools.feed.module.georss.geometries.AbstractGeometry;
import org.rometools.feed.module.georss.geometries.Point;
import org.rometools.feed.module.georss.geometries.Polygon;
//rome-tool version
import org.rometools.fetcher.FetcherEvent;
import org.rometools.fetcher.FetcherException;
import org.rometools.fetcher.FetcherListener;
import org.rometools.fetcher.impl.HashMapFeedInfoCache;
import org.rometools.fetcher.impl.HttpURLFeedFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.uicds.alertService.CreateAlertRequestDocument;
import org.uicds.alertService.CreateAlertResponseDocument;
import org.uicds.incident.IncidentDocument;
import org.uicds.incidentManagementService.ArchiveIncidentRequestDocument;
import org.uicds.incidentManagementService.ArchiveIncidentResponseDocument;
import org.uicds.incidentManagementService.CloseIncidentRequestDocument;
import org.uicds.incidentManagementService.CloseIncidentResponseDocument;
import org.uicds.incidentManagementService.CreateIncidentRequestDocument;
import org.uicds.incidentManagementService.CreateIncidentResponseDocument;
import org.uicds.incidentManagementService.UpdateIncidentRequestDocument;
import org.uicds.incidentManagementService.UpdateIncidentResponseDocument;
import org.uicds.notificationService.GetMessagesRequestDocument;
import org.uicds.notificationService.GetMessagesResponseDocument;
import org.uicds.resourceInstanceService.GetResourceInstanceListRequestDocument;
import org.uicds.resourceInstanceService.GetResourceInstanceListResponseDocument;
import org.uicds.resourceInstanceService.RegisterRequestDocument;
import org.uicds.resourceInstanceService.RegisterResponseDocument;
import org.uicds.resourceInstanceService.ResourceInstance;
import org.uicds.resourceProfileService.CreateProfileRequestDocument;
import org.uicds.resourceProfileService.CreateProfileResponseDocument;
import org.uicds.resourceProfileService.GetProfileListRequestDocument;
import org.uicds.resourceProfileService.GetProfileListResponseDocument;
import org.uicds.resourceProfileService.Interest;
import org.uicds.resourceProfileService.ResourceProfile;
import org.uicds.workProductService.ArchiveProductRequestDocument;
import org.uicds.workProductService.ArchiveProductResponseDocument;
import org.uicds.workProductService.AssociateWorkProductToInterestGroupRequestDocument;
import org.uicds.workProductService.AssociateWorkProductToInterestGroupResponseDocument;
import org.uicds.workProductService.CloseProductRequestDocument;
import org.uicds.workProductService.CloseProductResponseDocument;
import org.uicds.workProductService.WorkProductPublicationResponseType;
import org.w3.x2005.x08.addressing.ToDocument;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;
/* orginial version
import com.sun.syndication.fetcher.FetcherEvent;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.FetcherListener;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
*/
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.ResponseType.Enum;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.saic.precis.x2009.x06.base.AssociatedGroupsDocument.AssociatedGroups;
import com.saic.precis.x2009.x06.base.IdentificationType;
import com.saic.precis.x2009.x06.base.ProcessingStateType;
import com.saic.precis.x2009.x06.structures.WorkProductDocument;
import com.saic.precis.x2009.x06.structures.WorkProductDocument.WorkProduct;
import com.saic.uicds.clients.em.georssadapter.data.AlertEntry;
import com.saic.uicds.clients.em.georssadapter.data.AlertODEntry;
import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
import com.saic.uicds.clients.em.georssadapter.data.Entry;
import com.saic.uicds.clients.em.georssadapter.data.FeedConfiguration;
import com.saic.uicds.clients.em.georssadapter.data.IncidentEntry;
import com.saic.uicds.clients.util.Common;
import com.sun.syndication.feed.WireFeed;
import com.sun.syndication.feed.atom.Feed;
import com.sun.syndication.feed.module.Module;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;

public class GeoRSSReader extends Thread implements FetcherListener, ConstantData {

    private static final Logger logger = LoggerFactory.getLogger(GeoRSSReader.class);
    private static final String GeoRSSAdapterProfileName = "GeoRSSAdapterProfile";
    private static final String IncidentType = "Incident";
    private static final String GeoRSSAdapterName = "GeoRSSAdapter";

    private static String resourceInstanceID = null;

    private static WebServiceTemplate wsClient;

    private final Map<String, IdentificationType> identificationMap =
        new HashMap<String, IdentificationType>();
    private Map<String, Entry> entryMap = new HashMap<String, Entry>();
    private HttpURLFeedFetcher feedFetcher;
    private FeedConfiguration configuration;
    private String feedIgId = null;
    private boolean isAtomFeed = true;
    private volatile boolean keepRunning = true;

    //for alert optional data
    private boolean isUpdate = false;
    private Map<String, String> umap = null;
    private Set<String> umethods = null;
    private String alertFirstLevelID = null;
    private String alertFirstLevelIDPrevious = null;
    private int rerunCount = 0;
    private final int threadReTryCount = 30;

    //private Entry firstentry=null;
    //private Entry previousentry=null;

    private int updateEntryCount = 0;
    private int sydEntryCount = 0;

    public GeoRSSReader() {

    }

    public GeoRSSReader(FeedConfiguration configuration) {

        this.configuration = configuration;
        logger.debug("GeoRSSReader: configuration:\n" + configuration.toString());
    }

    public GeoRSSReader(FeedConfiguration config, boolean useFetcher) {

        this(config);
        // check whether the feed existed already
        feedFetcher = new HttpURLFeedFetcher(new HashMapFeedInfoCache());
        feedFetcher.addFetcherEventListener(this);
        createSubscription();
    }

    private void associateAlertODToInterestGroup(Entry entry) {

        AssociateWorkProductToInterestGroupRequestDocument request =
            AssociateWorkProductToInterestGroupRequestDocument.Factory.newInstance();
        request.addNewAssociateWorkProductToInterestGroupRequest().addNewIncidentID()
            .setStringValue(feedIgId);
        request.getAssociateWorkProductToInterestGroupRequest().addNewWorkProductID()
            .setStringValue(entry.getWpId());
        try {
            AssociateWorkProductToInterestGroupResponseDocument response =
                (AssociateWorkProductToInterestGroupResponseDocument) sendAndReceive(request);
            if (entry.getIgId() == null) {
                String igId =
                    getIgIdFromWorkProduct(response
                        .getAssociateWorkProductToInterestGroupResponse().getWorkProduct());
                if (igId != null)
                    entry.setIgId(igId);
            }
            entry.setIdentification(Common.getIdentificationElement(response
                .getAssociateWorkProductToInterestGroupResponse().getWorkProduct()));
        } catch (Throwable e) {
            logger.error("Cannot associate " + entry.getWpId() + " to " + feedIgId);
        }
    }

    private void associateAlertsODToInterestGroup() {

        Set<String> ids = entryMap.keySet();
        for (String id : ids)
            associateAlertODToInterestGroup(entryMap.get(id));
    }

    private void associateAlertsToInterestGroup() {

        Set<String> ids = entryMap.keySet();
        for (String id : ids)
            associateAlertToInterestGroup(entryMap.get(id));
    }

    private void associateAlertToInterestGroup(Entry entry) {

        AssociateWorkProductToInterestGroupRequestDocument request =
            AssociateWorkProductToInterestGroupRequestDocument.Factory.newInstance();
        request.addNewAssociateWorkProductToInterestGroupRequest().addNewIncidentID()
            .setStringValue(feedIgId);
        request.getAssociateWorkProductToInterestGroupRequest().addNewWorkProductID()
            .setStringValue(entry.getWpId());
        try {
            AssociateWorkProductToInterestGroupResponseDocument response =
                (AssociateWorkProductToInterestGroupResponseDocument) sendAndReceive(request);
            if (entry.getIgId() == null) {
                String igId =
                    getIgIdFromWorkProduct(response
                        .getAssociateWorkProductToInterestGroupResponse().getWorkProduct());
                if (igId != null)
                    entry.setIgId(igId);
            }
            entry.setIdentification(Common.getIdentificationElement(response
                .getAssociateWorkProductToInterestGroupResponse().getWorkProduct()));
        } catch (Throwable e) {
            logger.error("Cannot associate " + entry.getWpId() + " to " + feedIgId);
        }
    }

    private void cleanupEntries(Set<String> retrieveIds) {

        // create a set of id
        Set<String> currentIds = new HashSet<String>(entryMap.keySet());

        for (String id : retrieveIds) {
            boolean isExisted = currentIds.remove(id);
            if (!isExisted)
                logger.error(id + " has not been created yet...");
        }
        for (String id : currentIds) {
            logger.debug("remove " + id + " now ...");
            closeAndArchive(id);
        }
    }

    /*
     * clean up the whole feed
     */
    private void cleanupFeed() {

        if (configuration.isIncident())
            cleanupEntries(new HashSet<String>());
        else {
            // close the interest group will clean up everything
            boolean isSuccess = closeAndArchiveInterestGroup(feedIgId);
            logger.info("CloseAndArchive Interest Group: [" +
                        feedIgId +
                        "]" +
                        (isSuccess ? " success" : " failure"));
            entryMap.clear();
        }
    }

    private boolean closeAndArchive(String id) {

        logger.info("close/archive: [" + id + "] / WPId: " + entryMap.get(id).getWpId());
        boolean isSuccess = closeAndArchiveEntry(entryMap.get(id));
        if (isSuccess) {
            logger.info("close/archive: [" + id + "] successfully");

            // remove the incident's identification if it existed
            identificationMap.remove(entryMap.get(id).getWpId());
            // remove the entry
            entryMap.remove(id);
            return true;
        } else {
            logger.info("close/archive: [" + id + "] failed");
            return false;
        }
    }

    private boolean closeAndArchiveEntry(Entry entry) {

        XmlObject o = entry.getContent();
        if (o instanceof IncidentDocument) {
            logger.debug("CloseAndArchive: " + entry.getIgId());
            return closeAndArchiveInterestGroup(entry.getIgId());
        }
        try {

            // close and archive the work product
            logger.debug("CloseAndArchive: " + entry.getWpId());
            CloseProductRequestDocument closeRequest =
                CloseProductRequestDocument.Factory.newInstance();
            closeRequest.addNewCloseProductRequest().setWorkProductIdentification(entry
                .getIdentification());
            CloseProductResponseDocument closeResponse =
                (CloseProductResponseDocument) sendAndReceive(closeRequest);
            if (closeResponse.getCloseProductResponse().getWorkProductPublicationResponse()
                .getWorkProductProcessingStatus().getStatus().equals(ProcessingStateType.ACCEPTED)) {
                logger.debug("close: [" + entry.getWpId() + "] successfully");
                logger.debug("Delay 5 second after closed the incident...");
                //==============================================
                //after close that incident, we give a 5 second delay, then do the
                //archive for that incident.
                try {
                    Thread.sleep(5000);

                } catch (InterruptedException ie) {
                    logger.error("Delay 5 second after closed the incident [" +
                                 entry.getWpId() +
                                 "] : " +
                                 ie.getMessage());
                }
                //==============================================

                ArchiveProductRequestDocument archiveRequest =
                    ArchiveProductRequestDocument.Factory.newInstance();
                archiveRequest.addNewArchiveProductRequest().setWorkProductIdentification(entry
                    .getIdentification());
                ArchiveProductResponseDocument archiveResponse =
                    (ArchiveProductResponseDocument) sendAndReceive(archiveRequest);
                if (archiveResponse.getArchiveProductResponse().getWorkProductProcessingStatus()
                    .getStatus().equals(ProcessingStateType.ACCEPTED)) {
                    logger.debug("archive: [" + entry.getWpId() + "] successfully");
                    return true;
                } else
                    logger.error("archive: [" +
                                 entry.getWpId() +
                                 "]  failed: " +
                                 archiveResponse.getArchiveProductResponse()
                                     .getWorkProductProcessingStatus().getMessage()
                                     .getStringValue());
            } else
                logger.error("close: [" +
                             entry.getWpId() +
                             "]  failed: " +
                             closeResponse.getCloseProductResponse()
                                 .getWorkProductPublicationResponse()
                                 .getWorkProductProcessingStatus().getMessage().getStringValue());
        } catch (Throwable e) {

            logger.error("CloseAndArchive Product id: [" +
                         entry.getId() +
                         "], wpId: [" +
                         entry.getWpId() +
                         "] : " +
                         e.getMessage());

        }

        return false;
    }

    private boolean closeAndArchiveInterestGroup(String igId) {

        try {
            CloseIncidentRequestDocument closeRequest =
                CloseIncidentRequestDocument.Factory.newInstance();
            closeRequest.addNewCloseIncidentRequest().setIncidentID(igId);
            CloseIncidentResponseDocument closeResponse =
                (CloseIncidentResponseDocument) sendAndReceive(closeRequest);
            if (closeResponse.getCloseIncidentResponse().getWorkProductProcessingStatus()
                .getStatus().equals(ProcessingStateType.ACCEPTED)) {
                ArchiveIncidentRequestDocument archiveRequest =
                    ArchiveIncidentRequestDocument.Factory.newInstance();
                archiveRequest.addNewArchiveIncidentRequest().setIncidentID(igId);
                ArchiveIncidentResponseDocument archiveResponse =
                    (ArchiveIncidentResponseDocument) sendAndReceive(archiveRequest);
                if (archiveResponse.getArchiveIncidentResponse().getWorkProductProcessingStatus()
                    .getStatus().equals(ProcessingStateType.ACCEPTED))
                    return true;
                else
                    logger.error("archive: [" +
                                 igId +
                                 "] failed: " +
                                 archiveResponse.getArchiveIncidentResponse()
                                     .getWorkProductProcessingStatus().getMessage()
                                     .getStringValue());
            } else {
                logger.error("close: [" +
                             igId +
                             "]  failed: " +
                             closeResponse.getCloseIncidentResponse()
                                 .getWorkProductProcessingStatus().getMessage().getStringValue());
                return false;
            }
        } catch (Throwable e) {
            logger.error("CloseAndArchive Interest Group: [" + igId + "] : " + e.getMessage());
        }
        return false;
    }

    @SuppressWarnings("unused")
    private Calendar ConvertStringToCalender(String value) {

        {
            Calendar cal = null;
            DateFormat formatter;
            Date date = null;
            formatter = new SimpleDateFormat("yyyy-mm-dd");

            try {
                if (value != "" && value != null && value.length() < 30)
                    date = formatter.parse(value);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (cal != null) {
                cal = Calendar.getInstance();
                cal.setTime(date);
            } else {
                cal = Calendar.getInstance();
                cal.setTimeInMillis(1000000);
            }

            return cal;
        }
    }

    private boolean createEntry(Entry entry) {

        //  	 String idStr= entry.getId();
        //  	 logger.info("createEntry called, entry id : " + entry.getId()); 

        XmlObject o = entry.getContent();
        WorkProductPublicationResponseType status = null;
        // try {
        if (o instanceof IncidentDocument) {
            CreateIncidentRequestDocument request =
                org.uicds.incidentManagementService.CreateIncidentRequestDocument.Factory
                    .newInstance();
            request.addNewCreateIncidentRequest().addNewIncident().set(((IncidentDocument) o)
                .getIncident());
            CreateIncidentResponseDocument response =
                (CreateIncidentResponseDocument) sendAndReceive(request);
            if (response == null)
                return false;
            status = response.getCreateIncidentResponse().getWorkProductPublicationResponse();

            //        idStr= entry.getId();
            //   	    logger.info("createEntry called, entry id : " + entry.getId()); 

        } else {
            CreateAlertRequestDocument request =
                org.uicds.alertService.CreateAlertRequestDocument.Factory.newInstance();
            request.addNewCreateAlertRequest().addNewAlert().set(((AlertDocument) o).getAlert());
            CreateAlertResponseDocument response =
                (CreateAlertResponseDocument) sendAndReceive(request);
            if (response == null)
                return false;
            status = response.getCreateAlertResponse().getWorkProductPublicationResponse();

            //        idStr= entry.getId();
            //   	    logger.info("createEntry called, entry id : " + entry.getId()); 

        }

        processStatusResponse(entry, status);

        // if the entry is an Alert then we need to assoicate to an interest group
        if (o instanceof AlertDocument && feedIgId != null)
            associateAlertToInterestGroup(entry);
        logger.info("Create [" + entry.getId() + "], wpId: [" + entry.getWpId() + "] successfully");

        return true;
    }

    private boolean createODEntry(Entry entry) {

        // 	String idStr= entry.get
        // logger.info("createODEntry called, entry id : " + entry.getId()); 

        XmlObject o = entry.getContent();
        WorkProductPublicationResponseType status = null;
        // try {
        if (o instanceof IncidentDocument) {
            CreateIncidentRequestDocument request =
                org.uicds.incidentManagementService.CreateIncidentRequestDocument.Factory
                    .newInstance();
            request.addNewCreateIncidentRequest().addNewIncident().set(((IncidentDocument) o)
                .getIncident());
            CreateIncidentResponseDocument response =
                (CreateIncidentResponseDocument) sendAndReceive(request);
            if (response == null)
                return false;
            status = response.getCreateIncidentResponse().getWorkProductPublicationResponse();

            //        idStr= entry.getId();
            //   	    logger.info("createODEntry called, entry id : " + entry.getId()); 

        } else {
            CreateAlertRequestDocument request =
                org.uicds.alertService.CreateAlertRequestDocument.Factory.newInstance();
            request.addNewCreateAlertRequest().addNewAlert().set(((AlertDocument) o).getAlert());
            CreateAlertResponseDocument response =
                (CreateAlertResponseDocument) sendAndReceive(request);
            if (response == null)
                return false;
            status = response.getCreateAlertResponse().getWorkProductPublicationResponse();

            //      idStr= entry.getId();
            //  	    logger.info("createODEntry called, entry id : " + entry.getId()); 
        }

        processStatusResponse(entry, status);

        // if the entry is an Alert then we need to assoicate to an interest group
        if (o instanceof AlertDocument)
            if (feedIgId != null)
                associateAlertToInterestGroup(entry);

        //save the first level ig id here.
        if (updateEntryCount == 1 && sydEntryCount == 0)
            //	firstentry = entry;
            alertFirstLevelID = entry.getIgId();

        logger.info("Create entry igid is [" + entry.getIgId() + "]");

        logger.info("Create [" + entry.getId() + "], wpId: [" + entry.getWpId() + "] successfully");

        return true;
    }

    private boolean createSubscription() {

        if (resourceInstanceID != null)
            return true;

        String resourceProfileID = null;

        GetProfileListRequestDocument getProfileListRequest =
            GetProfileListRequestDocument.Factory.newInstance();
        getProfileListRequest.addNewGetProfileListRequest();
        GetProfileListResponseDocument getProfileListResponse =
            (GetProfileListResponseDocument) sendAndReceive(getProfileListRequest);
        boolean found = false;
        if (getProfileListResponse.isNil() == false &&
            getProfileListResponse.getGetProfileListResponse().isNil() == false &&
            getProfileListResponse.getGetProfileListResponse().getProfileList().isNil() == false) {
            ResourceProfile[] profiles =
                getProfileListResponse.getGetProfileListResponse().getProfileList()
                    .getResourceProfileArray();
            for (ResourceProfile profile : profiles)
                if (found = profile.getID().getStringValue().equals(GeoRSSAdapterProfileName)) {
                    resourceProfileID = GeoRSSAdapterProfileName;
                    break;
                }
        }
        // if the GeoRSSAdapterProfile is not existed, create one
        if (found == false) {
            // create the ResourceProfile first
            CreateProfileRequestDocument createProfileRequest =
                CreateProfileRequestDocument.Factory.newInstance();
            createProfileRequest.addNewCreateProfileRequest().addNewProfile().addNewID()
                .setStringValue(GeoRSSAdapterProfileName);
            createProfileRequest.getCreateProfileRequest().getProfile().addNewInterests();
            Interest[] interests = new Interest[1];
            interests[0] = Interest.Factory.newInstance();
            interests[0].setTopicExpression(IncidentType);
            createProfileRequest.getCreateProfileRequest().getProfile().getInterests()
                .setInterestArray(interests);

            CreateProfileResponseDocument createResourceProfileResponse =
                (CreateProfileResponseDocument) sendAndReceive(createProfileRequest);
            if (createResourceProfileResponse.isNil() == false &&
                createResourceProfileResponse.getCreateProfileResponse().isNil() == false &&
                createResourceProfileResponse.getCreateProfileResponse().getProfile().isNil() == false)
                resourceProfileID =
                    createResourceProfileResponse.getCreateProfileResponse().getProfile().getID()
                        .getStringValue();
        }

        GetResourceInstanceListRequestDocument getResourceInstanceListRequest =
            GetResourceInstanceListRequestDocument.Factory.newInstance();
        getResourceInstanceListRequest.addNewGetResourceInstanceListRequest();
        GetResourceInstanceListResponseDocument getResourceInstanceListResponse =
            (GetResourceInstanceListResponseDocument) sendAndReceive(getResourceInstanceListRequest);
        if (getResourceInstanceListResponse.isNil() == false &&
            getResourceInstanceListResponse.getGetResourceInstanceListResponse().isNil() == false &&
            getResourceInstanceListResponse.getGetResourceInstanceListResponse()
                .getResourceInstanceList().isNil() == false) {
            ResourceInstance[] instances =
                getResourceInstanceListResponse.getGetResourceInstanceListResponse()
                    .getResourceInstanceList().getResourceInstanceArray();
            for (ResourceInstance instance : instances)
                if (instance.getID().getStringValue().equals(GeoRSSAdapterName)) {
                    resourceInstanceID = instance.getID().getStringValue();
                    break;
                }
        }
        if (resourceInstanceID == null) {
            RegisterRequestDocument registerRequest = RegisterRequestDocument.Factory.newInstance();
            registerRequest.addNewRegisterRequest().addNewID().setStringValue(GeoRSSAdapterName);
            registerRequest.getRegisterRequest().addNewResourceProfileID()
                .setStringValue(resourceProfileID);
            RegisterResponseDocument registerResponse =
                (RegisterResponseDocument) sendAndReceive(registerRequest);
            if (registerResponse.isNil() == false &&
                registerResponse.getRegisterResponse().isNil() == false &&
                registerResponse.getRegisterResponse().getResourceInstance().isNil() == false)
                resourceInstanceID =
                    registerResponse.getRegisterResponse().getResourceInstance().getID()
                        .getStringValue();
        }

        return true;
    }

    private Entry entry2UicdsData(SyndEntry entry) {

        if (configuration.getFilter() != null) {
            String filter = configuration.getFilter().trim().toLowerCase();
            // logger.debug("filter: " + configuration.getFilter());
            boolean found = true;
            if (entry.getTitle() != null)
                found = (entry.getTitle().toLowerCase().indexOf(filter) == -1 ? false : true);

            if (!found)
                if (entry.getUri() != null)
                    found = (entry.getUri().toLowerCase().indexOf(filter) == -1 ? false : true);

            if (!found)
                if (entry.getDescription() != null)
                    found =
                        (entry.getDescription().getValue().toLowerCase().indexOf(filter) == -1 ? false : true);
            if (!found)
                return null;
        }

        if (configuration.isIncident())
            return new IncidentEntry(entry.getUri(), getIncidentId(), entry.getTitle(), entry
                .getAuthor(), entry.getDescription() == null ? null : entry.getDescription()
                .getValue());
        else if (configuration.isAlertOD())
        //09/13/2012 FLI for alert optional data case
        {

            //set one more time
            if (isUpdate) {
                //redo parse here for update
                AlertDocument alert = (AlertDocument) configuration.getAlert().copy();
                configuration.setAlert(alert);
            }

            String Category = configuration.getCategory();

            AlertODEntry alertODEntry =
                new AlertODEntry(configuration.getAlert(),
                    entry.getUri(),
                    getIncidentId(),
                    entry.getAuthor(),
                    entry.getTitle(),
                    Category,
                    entry.getDescription() == null ? "First level incident" : entry
                        .getDescription().getValue());
            transformForOD((AlertDocument) alertODEntry.getContent());

            return alertODEntry;

            /*  			
            if(entry.getDescription()!=null)
            {
            if(entry.getDescription().getValue()==null)
            {
            	String desNew = "test";
            	AlertODEntry alertODEntry = new AlertODEntry(configuration.getAlert(), entry.getUri(),
                          getIncidentId(), entry.getAuthor(), entry.getTitle(), Category,       desNew);
            	transformForOD((AlertDocument) alertODEntry.getContent());
                  
                  return alertODEntry;
            }
            else
            {
              	AlertODEntry alertODEntry = new AlertODEntry(configuration.getAlert(), entry.getUri(),
                          getIncidentId(), entry.getAuthor(), entry.getTitle(), Category,
                          entry.getDescription() == null ? null : entry.getDescription().getValue());
              	transformForOD((AlertDocument) alertODEntry.getContent());
                  
                  return alertODEntry;
            }
                         
            } 
            */
        }
        //for alert case
        else {
            AlertEntry alertEntry =
                new AlertEntry(configuration.getAlert(), entry.getUri(), getIncidentId(), entry
                    .getAuthor(), entry.getTitle(), entry.getDescription() == null ? null : entry
                    .getDescription().getValue());
            transform((AlertDocument) alertEntry.getContent());
            return alertEntry;
        }

        //     return null;
    }

    private Entry feed2Incident(SyndFeed feed) {

        return new IncidentEntry(feed.getLink(),
            getIncidentId(),
            feed.getTitle(),
            feed.getAuthor(),
            feed.getDescription());
    }

    @Override
    public void fetcherEvent(FetcherEvent event) {

        logger.info("fetch: [" + configuration.getUrl() + "] type: " + event.getEventType());

        if (event.getEventType().equals("FEED_RETRIEVED")) {

            updateEntryCount = 0;
            sydEntryCount = 0;

            SyndFeed inputFeed = event.getFeed();
            logger.debug("[" +
                         event.getUrlString() +
                         "] is a " +
                         (inputFeed instanceof Feed ? "ATOM" : "RSS") +
                         " feed");

            if (configuration.isAlertOD()) {

                if (isUpdate == false) {
                    processFeed(inputFeed);

                    //save that ig id for next update run if any
                    alertFirstLevelIDPrevious = alertFirstLevelID;
                    // 	previousentry = firstentry;

                    isUpdate = true;
                } else {
                    updateProcessFeed(inputFeed);

                    if (isUpdate && configuration.isAlertOD())
                        //also remove the first level
                        if (alertFirstLevelIDPrevious != null)
                            closeAndArchiveInterestGroup(alertFirstLevelIDPrevious);

                    //update the new ig id and save it for next run.
                    alertFirstLevelIDPrevious = alertFirstLevelID;
                    // previousentry = firstentry;
                }
            } else
                processFeed(inputFeed);
        }
    }

    private x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.Enum getCatgory(String value) {

        x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.Enum x = null;
        x = x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category.Enum.forString(value);
        return x;
    }

    public FeedConfiguration getConfig() {

        return configuration;
    }

    public Map<String, Entry> getEntryMap() {

        return entryMap;
    }

    public Enum getEnum(String value) {

        x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.ResponseType.Enum x = null;
        x =
            x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.ResponseType.Enum
                .forString(value);
        return x;
    }

    public String getFeedIgId() {

        return feedIgId;
    }

    private String getIgIdFromWorkProduct(WorkProduct workProduct) {

        AssociatedGroups associatedGroup =
            Common.getPropertiesElement(workProduct).getAssociatedGroups();
        if (associatedGroup != null && associatedGroup.sizeOfIdentifierArray() > 0)
            return associatedGroup.getIdentifierArray(0).getStringValue();
        else
            return null;
    }

    public String getIncidentId() {

        return configuration.getUrl();
    }

    private String getValue(String[] xpath, String ele) {

        ele = ele.toLowerCase();
        String value = "";
        for (String selected : xpath) {
            int len = selected.length();
            int index = selected.indexOf(ele);
            int index2 = selected.indexOf('=');

            if (index != -1) {
                value = selected.substring(index2 + 2, len - 1);
                return value;
            }
        }

        return value;

    }

    private void insertGeometry(AbstractGeometry geometry, Entry entry) {

        if (geometry instanceof Point)
            entry.addPoint(((Point) geometry).getPosition().getLatitude(), ((Point) geometry)
                .getPosition().getLongitude());
        else if (geometry instanceof Polygon)
            logger.error("It's polygon: " + geometry.toString());
    }

    private boolean isKeepRunning() {

        return keepRunning && !Thread.currentThread().isInterrupted();
    }

    private Entry ODfeed2Incident(SyndFeed feed, String category) {

        return new IncidentEntry(feed.getLink(),
            getIncidentId(),
            feed.getTitle(),
            feed.getAuthor(),
            category,
            feed.getDescription());
    }

    private void parseContent(String contentText, Entry entry) {

        try {
            Document document =
                new SAXBuilder().build(new ByteArrayInputStream(contentText.getBytes()));
            Element rootElement = document.getRootElement();

            if (rootElement != null) {
                SimpleParser parser = new SimpleParser();
                GeoRSSModule georssModule = (GeoRSSModule) parser.parse(rootElement);
                if (georssModule != null)
                    insertGeometry(georssModule.getGeometry(), entry);
            }

        } catch (Exception e) {
            logger.error("parseContent failed: " + e.getMessage());
        }
    }

    public void processFeed(SyndFeed inputFeed) {

        WireFeed wireFeed = inputFeed.createWireFeed();
        if (wireFeed instanceof Channel) {
            isAtomFeed = false;
            // convert into atom feed
            inputFeed.setFeedType(FeedType_Atom);
        }

        // if it's incident feed, then we need to update incident's identification first
        if (configuration.isIncident())
            refreshIncidents();

        // 09/11/2012 FLi, if it's alert optional data type, what do we do?
        if (configuration.isAlertOD()) {

            //save the count
            updateEntryCount++;

            //parse again
            if (isUpdate == true)
                configuration.createFeedParser();

            Entry ig = ODfeed2Incident(inputFeed, configuration.getCategory());

            if (inputFeed.getPublishedDate() != null)
                ig.setTimestamp(inputFeed.getPublishedDate().getTime());

            if (createODEntry(ig) == false) {
                // what is the best way to stop this thread
                stopIt();
                return;
            }

            if (ig.getIgId() == null)
                logger.error("Cannot create the alert container");
            else {
                setFeedIgId(ig.getIgId());
                associateAlertsODToInterestGroup();
            }
        }

        // if it's alert, we need to create an interest group as a container
        if (configuration.isAlertOD() == false &&
            configuration.isIncident() == false &&
            feedIgId == null) {
            Entry ig = feed2Incident(inputFeed);
            // if it's RSS feed then there is no Published Date associated
            // do we need one ??? does the time stamp is used for expiration ?
            if (inputFeed.getPublishedDate() != null)
                ig.setTimestamp(inputFeed.getPublishedDate().getTime());
            if (createEntry(ig) == false) {
                // what is the best way to stop this thread
                stopIt();
                return;
            }
            if (ig.getIgId() == null)
                logger.error("Cannot create the alert container");
            else {
                setFeedIgId(ig.getIgId());
                associateAlertsToInterestGroup();
            }
        }

        Set<String> retrieveIds = new HashSet<String>();
        List<SyndEntry> inputEntries = inputFeed.getEntries();

        //reset it for each entry
        sydEntryCount = 0;
        for (SyndEntry inputEntry : inputEntries) {

            // convert the entry into UicdsData
            Entry entry = entry2UicdsData(inputEntry);
            if (entry == null) {
                logger.debug("entry:" +
                             inputEntry.getTitle() +
                             " filter out by filter: " +
                             configuration.getFilter());
                continue;
            }
            logger.debug("Entry: " + entry);
            retrieveIds.add(entry.getId());

            if (configuration.getEventType() != null && entry instanceof IncidentEntry) {
                ((IncidentEntry) entry).setEventType(configuration.getEventType());
            }

            // extrac the geometry from the module
            List<Module> inputModules = inputEntry.getModules();
            for (Module inputModule : inputModules)
                if (inputModule.getInterface().equals(GeoRSSModule.class)) {
                    GeoRSSModule geoRSSEntry = GeoRSSUtils.getGeoRSS(inputEntry);
                    if (geoRSSEntry != null)
                        insertGeometry(geoRSSEntry.getGeometry(), entry);
                }

            // extract geometry from the content
            if (inputEntry.getContents().size() > 0) {
                List<SyndContent> contents = inputEntry.getContents();
                for (SyndContent content : contents)
                    parseContent(content.getValue(), entry);
            }

            Date publishedDate =
                isAtomFeed ? inputEntry.getUpdatedDate() : inputEntry.getPublishedDate();

            // if the entry doesn't have UpdateDate or PublishDate then we will use current time
            if (publishedDate == null)
                publishedDate = new Date();

            entry.setTimestamp(publishedDate.getTime());

            if (entryMap.containsKey(entry.getId())) {

                logger.debug("Delay 1 second when close and arachive the incident...");
                //==============================================
                //after close that incident, we give a 1 second delay, then do the
                //archive for that incident.
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException ie) {
                    logger.debug("Delay 1 second when close and arachive the incident [" +
                                 entry.getWpId() +
                                 "] : " +
                                 ie.getMessage());
                }
                //==============================================

                //2012/0920 FLI test  
                //long t1= entry.getTimestamp();
                //long t2 = entryMap.get(entry.getId()).getTimestamp();

                //if (t1 > t2) {
                if (updateEntry(entry))
                    entryMap.put(entry.getId(), entry);

            } else if (!configuration.isAlertOD()) {
                if (createEntry(entry))
                    entryMap.put(entry.getId(), entry);
                else
                    retrieveIds.remove(entry.getId());
            } else if (createODEntry(entry))
                entryMap.put(entry.getId(), entry);
            else
                retrieveIds.remove(entry.getId());

            sydEntryCount++;
        }

        cleanupEntries(retrieveIds);

    }

    /*
     * get/set the Identification, Interest Group Id, Work Product Id
     */
    private void processStatusResponse(Entry entry, WorkProductPublicationResponseType status) {

        if (status.getWorkProductProcessingStatus().getStatus()
            .equals(ProcessingStateType.ACCEPTED)) {
            // set the UicdsData's Identification
            entry.setIdentification(Common.getIdentificationElement(status.getWorkProduct()));
            String igId = getIgIdFromWorkProduct(status.getWorkProduct());
            if (igId != null)
                entry.setIgId(igId);
        } else
            logger.error("Request failed: " + status.getWorkProductProcessingStatus().getMessage());
    }

    private void refreshIncidents() {

        GetMessagesRequestDocument getMessageRequest =
            GetMessagesRequestDocument.Factory.newInstance();
        getMessageRequest.addNewGetMessagesRequest();
        ToDocument to = ToDocument.Factory.newInstance();
        to.addNewTo().setStringValue(resourceInstanceID);
        getMessageRequest.getGetMessagesRequest().set(to);
        getMessageRequest.getGetMessagesRequest().setMaximumNumber(BigInteger.ONE);
        GetMessagesResponseDocument getMessageResponse =
            (GetMessagesResponseDocument) sendAndReceive(getMessageRequest);
        NotificationMessageHolderType[] messages =
            getMessageResponse.getGetMessagesResponse().getNotificationMessageArray();
        for (NotificationMessageHolderType message : messages)
            try {
                WorkProductDocument wpd =
                    WorkProductDocument.Factory.parse(message.getMessage().toString());
                IdentificationType id = Common.getIdentificationElement(wpd.getWorkProduct());
                identificationMap.put(id.getIdentifier().getStringValue(), id);
            } catch (XmlException e) {
                logger.debug("The Notification Message is not a WorkProduct Mesage");
            }
    }

    @Override
    public void run() {

        rerunCount = 0;
        logger.info("... start the GeoRSS thread: " + Thread.currentThread().getId() + " ...");

        try {

            runAgain:

            while (isKeepRunning()) {
                try {

                    if (rerunCount > 0) {
                        //show up some rerun infomaiton
                        System.out.println("After some exception, and take a break and rerun." +
                                           rerunCount);

                        //take a two second break.
                        try {
                            Thread.sleep(2000);
                            System.out.println("take a 10 second break after exception...");
                        } catch (InterruptedException xx) {
                            System.out.println("Georss thread time break exception...");
                        }
                    }

                    feedFetcher.retrieveFeed(new URL(configuration.getUrl()));
                    //	feedFetcher2.retrieveFeed(new URL("http://www.inciweb.org/feeds/rss/incidents")); 

                } catch (ConnectException e) {
                    logger.error("Cannot access the feed: " +
                                 configuration.getUrl() +
                                 ": " +
                                 e.getMessage());
                    //try to re run the thread.
                    if (rerunCount < threadReTryCount) {
                        rerunCount++;
                        System.out.println("thread got some exception, re-try it.");

                        continue runAgain;
                    }
                } catch (IllegalArgumentException e) {
                    logger.error("Illegal retrieveFeed exception: " + e.getMessage());
                    //try to re run the thread.
                    if (rerunCount < threadReTryCount) {
                        rerunCount++;
                        System.out.println("thread got some exception, re-try it : " + rerunCount);

                        continue runAgain;
                    }
                } catch (IOException e) {
                    logger.error("IO exception accessing feed " +
                                 configuration.getUrl() +
                                 ": " +
                                 e.getMessage());
                    //try to re run the thread.
                    if (rerunCount < threadReTryCount) {
                        rerunCount++;
                        System.out.println("thread got some exception, re-try it : " + rerunCount);

                        continue runAgain;
                    }

                } catch (FeedException e) {
                    logger.error("FeedException accessing feed " +
                                 configuration.getUrl() +
                                 ": " +
                                 e.getMessage());
                    //try to re run the thread.
                    if (rerunCount < threadReTryCount) {
                        rerunCount++;
                        System.out.println("thread got some exception, re-try it : " + rerunCount);

                        continue runAgain;
                    }
                } catch (FetcherException e) {
                    logger.error("FetcherException accessing feed " +
                                 configuration.getUrl() +
                                 ": " +
                                 e.getMessage() +
                                 " response code: " +
                                 e.getResponseCode());
                    //try to re run the thread.
                    if (rerunCount < threadReTryCount) {
                        rerunCount++;
                        System.out.println("thread got some exception, re-try it : " + rerunCount);

                        continue runAgain;
                    }
                }
                logger.debug("GeoRSS thread: " +
                             Thread.currentThread().getId() +
                             " sleep " +
                             configuration.getPollingInterval() +
                             " minute(s)");
                Thread.sleep(configuration.getPollingInterval() * oneMinuteInMillisecond);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        cleanupFeed();
        logger.info("... shutdown the GeoRSS thread " + Thread.currentThread().getId() + " ...");
    }

    private XmlObject sendAndReceive(XmlObject request) {

        XmlObject response = null;
        try {
            response = (XmlObject) wsClient.marshalSendAndReceive(request);
        } catch (Throwable e) {
            logger.error("WebServiceClient: Request:\n" +
                         request.toString() +
                         "\nFailure: " +
                         e.getMessage());

            //can not find that one, so, a null point warning, not failure.
            //logger.warn("WebServiceClient: Request:\n" + request.toString() + "\nWarning: "
            //      + e.getMessage());
        }
        return response;
    }

    public void setConfig(FeedConfiguration config) {

        logger.debug("GeoRSSReader: setConfig:\n" + config.toString());
        configuration = config;
    }

    protected void setEntryMap(Map<String, Entry> entryMap) {

        this.entryMap = entryMap;
    }

    public void setFeedIgId(String feedIgId) {

        this.feedIgId = feedIgId;
    }

    public void setKeepRunning(boolean keepRunning) {

        this.keepRunning = keepRunning;
    }

    public void setWsClient(WebServiceTemplate wsClient) {

        this.wsClient = wsClient;
    }

    public void stopIt() {

        logger.debug("stop thread: " + getIncidentId() + " ...");
        setKeepRunning(false);
    }

    private void transform(AlertDocument alertDocument) {

        String id = alertDocument.getAlert().getIdentifier();
        Map<String, String> map = configuration.getParser().getElementMap();
        Set<String> methods = map.keySet();
        for (String method : methods) {
            String path = map.get(method);
            if (path.length() == 0)
                continue;
            String value = configuration.getParser().getNodeTextById(id, path);
            if (value != null)
                if (method.equals(S_Event))
                    alertDocument.getAlert().getInfoArray(0).setEvent(value);
                else if (method.equals(S_Description))
                    alertDocument.getAlert().getInfoArray(0).setDescription(value);
                else if (method.equals(S_Headline))
                    alertDocument.getAlert().getInfoArray(0).setHeadline(value);
                else if (method.equals(S_Contact))
                    alertDocument.getAlert().getInfoArray(0).setContact(value);
                else if (method.equals(S_Address))
                    alertDocument.getAlert().setAddresses(value);
        }
    }

    private void transformForOD(AlertDocument alertDocument) {

        String id = alertDocument.getAlert().getIdentifier();
        Map<String, String> map = null;
        Set<String> methods = null;

        if (isUpdate == false) {
            map = configuration.getParser().getElementMap();
            methods = map.keySet();
            umap = map;
            umethods = methods;
        } else {
            map = umap;
            methods = umethods;
        }

        for (String method : methods) {
            String path = map.get(method);
            if (path.length() == 0)
                continue;
            String value = configuration.getParser().getNodeTextById(id, path);

            if (value != null) {
                if (method.equals(S_Event))
                    alertDocument.getAlert().getInfoArray(0).setEvent(value);
                else if (method.equals(S_Description))
                    alertDocument.getAlert().getInfoArray(0).setDescription(value);
                else if (method.equals(S_Headline))
                    alertDocument.getAlert().getInfoArray(0).setHeadline(value);
                else if (method.equals(S_Contact))
                    alertDocument.getAlert().getInfoArray(0).setContact(value);
                else if (method.equals(S_Address))
                    alertDocument.getAlert().setAddresses(value);

                //optional data, alert level 
                if (method.equals(S_Source))
                    alertDocument.getAlert().setSource(value);
                else if (method.equals(S_Note))
                    alertDocument.getAlert().setNote(value);
                else if (method.equals(S_Reference))
                    alertDocument.getAlert().setReferences(value);
                else if (method.equals(S_Restriction))
                    alertDocument.getAlert().setRestriction(value);
                else if (method.equals(S_Category))
                    alertDocument.getAlert().getInfoArray(0).setCategoryArray(0, getCatgory(value));
                /*
                else if(method.equals(S_ResponseType))
                {
                alertDocument.getAlert().getInfoArray(0).setResponseTypeArray(0, getEnum(value));
                }
                */
                if (method.equals(S_Onset))
                    alertDocument.getAlert().getInfoArray(0)
                        .setOnset(ConvertStringToCalender(value));
                else if (method.equals(S_Language))
                    alertDocument.getAlert().getInfoArray(0).setLanguage(value);
                else if (method.equals(S_Web))
                    alertDocument.getAlert().getInfoArray(0).setWeb(value);
                // }else if (method.equals(S_Sendername)) {
                //   alertDocument.getAlert().getInfoArray(0).setSenderName(value);
                else if (method.equals(S_Instruction))
                    alertDocument.getAlert().getInfoArray(0).setInstruction(value);
                else if (method.equals(S_Effective))
                    alertDocument.getAlert().getInfoArray(0)
                        .setEffective(ConvertStringToCalender(value));
                else if (method.equals(S_Expires))
                    alertDocument.getAlert().getInfoArray(0)
                        .setExpires(ConvertStringToCalender(value));
                else if (method.equals(S_Resource))
                    alertDocument.getAlert().getInfoArray(0).getResourceArray(0)
                        .setResourceDesc(value);
                else if (method.equals(S_Area))
                    alertDocument.getAlert().getInfoArray(0).getAreaArray(0).setAreaDesc(value);
                else if (method.equals(S_CircleLatLong)) {
                    if (value.length() < 3)
                        //according to the project description, if lat/long is null, 
                        //set as 0.000,0.000
                        value = "0.000, 0.000";

                    alertDocument.getAlert().getInfoArray(0).getAreaArray(0).setCircleArray(0,
                        value);

                } else if (method.equals(S_Polygon)) {
                    if (value.length() < 3)
                        //according to the project description, if lat/long is null, 
                        //set as 0.000,0.000
                        value = "0.000, 0.000,0.000";
                    //  	alertDocument.getAlert().getInfoArray(0).getAreaArray(0).setPolygonArray(value.split(","));
                    alertDocument.getAlert().getInfoArray(0).getAreaArray(0).setPolygonArray(0,
                        value);

                }
                /* do not know how to set geocode yet, later
                else if (method.equals(S_Geocode)) {
                    
                    	alertDocument.getAlert().getInfoArray(0).getAreaArray(0).setGeocodeArray(arg0)
                    }
                */

            }

        }
    }

    private boolean updateEntry(Entry entry) {

        IdentificationType identification = entryMap.get(entry.getId()).getIdentification();
        String igId = entryMap.get(entry.getId()).getId();
        XmlObject o = entry.getContent();
        WorkProductPublicationResponseType status = null;

        if (o instanceof IncidentDocument) {
            UpdateIncidentRequestDocument request =
                UpdateIncidentRequestDocument.Factory.newInstance();
            request.addNewUpdateIncidentRequest().addNewIncident().set(((IncidentDocument) o)
                .getIncident());
            IdentificationType newIdentification =
                identificationMap.remove(identification.getIdentifier().getStringValue());
            if (newIdentification != null) {
                logger.debug("Incident has been changed and new version is:\n" + newIdentification);
                request.getUpdateIncidentRequest().addNewWorkProductIdentification()
                    .set(newIdentification);
            } else
                request.getUpdateIncidentRequest().addNewWorkProductIdentification()
                    .set(identification);

            UpdateIncidentResponseDocument response =
                (UpdateIncidentResponseDocument) sendAndReceive(request);
            status = response.getUpdateIncidentResponse().getWorkProductPublicationResponse();
        } else if (configuration.isAlertOD()) {
            if (closeAndArchiveEntry(entry) == false || createODEntry(entry) == false)
                return false;

        } else if (closeAndArchiveEntry(entry) == false || createEntry(entry) == false)
            return false;

        processStatusResponse(entry, status);

        logger
            .info("update: [" + entry.getId() + "], wpId: [" + entry.getWpId() + "] successfully");
        return true;
    }

    public void updateProcessFeed(SyndFeed inputFeed) {

        processFeed(inputFeed);
    }

}
