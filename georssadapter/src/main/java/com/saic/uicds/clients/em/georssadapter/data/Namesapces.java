package com.saic.uicds.clients.em.georssadapter.data;

public interface Namesapces {

    public static final String NSPrefix_Atom = "atom";
    public static final String NSPrefix_RSS = "rss";

    public static final String P_AtomFeed = "/feed";
    public static final String P_AtomEntry = "/feed/entry";
    public static final String P_AtomEntryId = "/feed/entry/id";
    public static final String P_AtomEntryTitle = "/feed/entry/title";

    public static final String P_RssChannel = "/rss/channel";
    public static final String P_RssItem = "/rss/channel/item";
    public static final String P_Link = "link";
    public static final String p_Title = "title";
    public static final String P_RssItemLink = "/rss/channel/item/" + P_Link;
    public static final String P_RssItemTitle = "/rss/channel/item/" + p_Title;
    public static final String P_RssItemDescription = "/rss/channel/item/description";
}
