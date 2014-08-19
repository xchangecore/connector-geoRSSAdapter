package com.saic.uicds.clients.em.georssadapter;

import java.util.Iterator;
import java.util.List;

import org.jdom.Element;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saic.uicds.clients.em.georssadapter.data.Namesapces;

public class FeedParserTest implements Namesapces {

    private static final Logger logger = LoggerFactory.getLogger(FeedParserTest.class);
    private static final String[] urls = {
    //
    //   "http://data.octo.dc.gov/feeds/cjis_juvenile/cjis_juvenile_current.xml",
    //   "http://earthquake.usgs.gov/earthquakes/shakemap/rss.xml",
    //   "http://www.deldot.gov/syndication.ejs?cat=WS",
    // "http://www.deldot.gov/syndication.ejs?cat=RTTA",
    // "http://www.deldot.gov/syndication.ejs?cat=STR",

    "http://earthquake.usgs.gov/earthquakes/catalogs/eqs1hour-M1.xml",
    //   "http://www.inciweb.org/feeds/rss/incidents"
        };

    private void displayNode(List nodeList) {

        Iterator iterator = nodeList.iterator();
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            String text = node.getText().trim();
            System.out.println(node.getName() + ": " + node.getText());
        }
    }

    protected String[] getConfigLocations() {

        return new String[] { "classpath:contexts/georssadapter-context.xml", };
    }

    @Test
    public void testParseFeed() throws Throwable {

        for (String url : urls) {
            FeedParser parser = new FeedParser(url);
            String[] namespaces = parser.getXPaths();
            for (String namespace : namespaces)
                System.out.println(namespace);
            if (parser.isAtomFeed()) {
                List nodeList = parser.getNodeList(P_AtomEntryId);
                Iterator iterator = nodeList.iterator();
                while (iterator.hasNext()) {
                    Element node = (Element) iterator.next();
                    List nodes = parser.getNodeById(node.getText(), P_AtomEntryTitle);
                    displayNode(nodes);
                }
            } else {
                List nodeList =
                    parser
                        .getNodeList(parser.getRssID().equals(P_Link) ? P_RssItemLink : P_RssItemTitle);
                Iterator iterator = nodeList.iterator();
                while (iterator.hasNext()) {
                    Element node = (Element) iterator.next();
                    // List nodes = parser.getNodeById(node.getText(), P_RssChannel);
                    List nodes = parser.getNodeById(node.getText(), P_RssItemDescription);
                    displayNode(nodes);
                }
            }
        }
    }

}
