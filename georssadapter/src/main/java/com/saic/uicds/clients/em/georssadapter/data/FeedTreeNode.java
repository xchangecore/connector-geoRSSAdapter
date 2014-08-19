package com.saic.uicds.clients.em.georssadapter.data;

import javax.swing.tree.DefaultMutableTreeNode;

import com.saic.uicds.clients.em.georssadapter.FeedProcessor;

public class FeedTreeNode extends DefaultMutableTreeNode implements FeedProcessor {

    private FeedConfiguration config;

    public FeedTreeNode(FeedConfiguration config) {

        super(config.getUrl());
        setConfig(config);
    }

    public FeedConfiguration getConfig() {

        return config;
    }

    public void setConfig(FeedConfiguration config) {

        this.config = config;
    }

    public FeedTreeNode(String name) {

        super(name);
    }

    @Override
    public void start(FeedConfiguration config) {

        this.config = config;
    }

    @Override
    public void stop(String url) {

        // TODO Auto-generated method stub

    }

}
