package com.saic.uicds.clients.em.georssadapter.ui;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.saic.uicds.clients.em.georssadapter.FeedProcessor;
import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
import com.saic.uicds.clients.em.georssadapter.data.FeedConfiguration;
import com.saic.uicds.clients.em.georssadapter.data.FeedTreeNode;

public class FeedTree
    extends JTree
    implements TreeSelectionListener, FeedProcessor, ConstantData {

    private static final long serialVersionUID = 7751178303816115042L;
    private static final DefaultMutableTreeNode root = new DefaultMutableTreeNode(TreeRootNodeName);

    public FeedTree() {

        super(new DefaultTreeModel(root));
        setEditable(true);
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        addTreeSelectionListener(this);
        setShowsRootHandles(true);
    }

    private void addNode(String url) {

        FeedTreeNode childNode = new FeedTreeNode(url);
        root.add(childNode);
        root.add(new FeedTreeNode("Testing Testing"));
        makeVisible(new TreePath(childNode.getPath()));
    }

    @Override
    public void start(FeedConfiguration config) {

        addNode(config.getUrl());
    }

    @Override
    public void stop(String url) {

        // TODO Auto-generated method stub

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        JOptionPane.showMessageDialog(null, e.getPath().toString(), "Debug",
            JOptionPane.INFORMATION_MESSAGE);

        // Returns the last path element of the selection.
        // This method is useful only when the selection model allows a single selection.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) getLastSelectedPathComponent();

        if (node == null)
            // Nothing is selected.
            return;
    }
}
