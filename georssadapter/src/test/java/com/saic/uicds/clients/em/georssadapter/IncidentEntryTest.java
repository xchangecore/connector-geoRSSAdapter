package com.saic.uicds.clients.em.georssadapter;

import java.util.Date;

import org.junit.Test;

import com.saic.uicds.clients.em.georssadapter.data.IncidentEntry;

public class IncidentEntryTest {

    @Test
    public void testIncidentEntry() {

        IncidentEntry incidentEntry = new IncidentEntry("Incident-123", "Sender", "Headline",
            "Author", "Description");
        incidentEntry.setTimestamp(new Date().getTime());
        System.out.println(incidentEntry.getContentString());
    }
}
