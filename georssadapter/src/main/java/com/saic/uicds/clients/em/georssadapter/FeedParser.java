package com.saic.uicds.clients.em.georssadapter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saic.uicds.clients.em.georssadapter.data.Namesapces;

public class FeedParser implements Namesapces {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private boolean isAtomFeed = false;
    private Document document;
    private String[] xPaths;
    private String rssID = P_Link;
    private Map<String, String> elementMap = new HashMap<String, String>();

    public FeedParser(String url) {

        try {
            SAXBuilder builder = new SAXBuilder();
            document = builder.build(url);
            removedDefaultNamespace(document);
            Element root = document.getRootElement();
            isAtomFeed = P_AtomFeed.substring(1).equals(root.getName()) ? true : false;
            Set<String> xpaths = new TreeSet<String>();
            parseChild(xpaths, "", root);

            xPaths = new String[xpaths.size() + 1];
            int i = 0;
            for (String xpath : xpaths) {
                List<Element> nodes = getNodeList(xpath);
                boolean found = false;
                for (Element node : nodes) {
                    String value = node.getText();
                    if (value != null) {
                        value = value.trim();
                        if (value.length() == 0)
                            continue;
                        found = true;
                        xPaths[i++] = new String(xpath + "=[" + value.trim() + "]");
                        break;
                    }
                }
                if (!found)
                    xPaths[i++] = xpath;
            }
            xPaths[i] = "";
            if (!isAtomFeed) {
                boolean useLink = false;
                for (String xpath : xPaths)
                    if (xpath.startsWith(P_RssItemLink)) {
                        useLink = true;
                        break;
                    }
                if (useLink == false)
                    setRssID(p_Title);
                logger.debug("RSS Feed uses " + getRssID() + " as ID field");
            }
        } catch (Throwable e) {
            logger.error("Cannot Open Url: " + url + e.getMessage());
        }
    }

    public Map<String, String> getElementMap() {

        return elementMap;
    }

    public List getNodeById(String id, String path) {

        List nodeList = null;
        XPath xpath;
        try {
            if (isAtomFeed) {
                if (path.startsWith(P_AtomEntry))
                    path = path.replace(P_AtomEntry, P_AtomEntry + "[id='" + id + "']");
                xpath = XPath.newInstance(path);
            } else {
                if (path.startsWith(P_RssItem))
                    path =
                        path.replaceAll(P_RssItem, P_RssItem + "[" + getRssID() + "='" + id + "']");
                xpath = XPath.newInstance(path);
            }
            nodeList = xpath.selectNodes(document);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return nodeList;
    }

    public List getNodeList(String path) {

        List nodeList = null;
        try {
            XPath xpath = XPath.newInstance(path);
            nodeList = xpath.selectNodes(document);
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return nodeList;
    }

    public String getNodeTextById(String id, String path) {

        List nodes = getNodeById(id, path);
        if (nodes.size() > 0)
            return ((Element) nodes.get(0)).getText();
        return null;
    }

    public String getRssID() {

        return rssID;
    }

    public String[] getXPaths() {

        /*
        int size = this.xpaths.size();
        String[] namespaces = new String[size];
        return this.xpaths.toArray(namespaces);
        */
        return xPaths;
    }

    public boolean isAtomFeed() {

        return isAtomFeed;
    }

    private void parseChild(Set<String> xpaths, String path, Element current) {

        String prefix =
            current.getNamespacePrefix().length() > 0 ? current.getNamespacePrefix() + ":" : "";
        String elementPath = path + "/" + prefix + current.getName();
        xpaths.add(elementPath);
        List children = current.getChildren();
        Iterator iterator = children.iterator();
        while (iterator.hasNext()) {
            Element child = (Element) iterator.next();
            parseChild(xpaths, elementPath, child);
        }
    }

    private void removedDefaultNamespace(Document document) {

        removeEmptyNamespace(document.getRootElement());

        Iterator iterator = document.getRootElement().getDescendants(new ElementFilter());
        while (iterator.hasNext())
            removeEmptyNamespace((Element) iterator.next());
    }

    private void removeEmptyNamespace(Element element) {

        if (element.getNamespace() != null &&
            element.getNamespacePrefix().length() == 0 &&
            element.getNamespaceURI().length() > 0)
            element.setNamespace(null);
    }

    public void setElementMap(Map<String, String> elementMap) {

        this.elementMap = elementMap;
    }

    public void setRssID(String rssID) {

        this.rssID = rssID;
    }
}
