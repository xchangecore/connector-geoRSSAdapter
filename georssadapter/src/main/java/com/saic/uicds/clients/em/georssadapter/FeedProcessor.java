package com.saic.uicds.clients.em.georssadapter;

import com.saic.uicds.clients.em.georssadapter.data.FeedConfiguration;

public interface FeedProcessor {

    public void start(FeedConfiguration config);

    public void stop(String url);
}
