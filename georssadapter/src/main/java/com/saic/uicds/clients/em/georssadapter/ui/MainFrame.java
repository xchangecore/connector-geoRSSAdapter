package com.saic.uicds.clients.em.georssadapter.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.saic.uicds.clients.em.georssadapter.GeoRSSReader;
import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
import com.saic.uicds.clients.em.georssadapter.data.FeedConfiguration;

public class MainFrame
    implements ActionListener, MouseListener, TreeSelectionListener, WindowListener, ConstantData /* Runnable */{

    private static final Logger logger = LoggerFactory.getLogger(MainFrame.class);
    private static Map<String, GeoRSSReader> feedMap = new HashMap<String, GeoRSSReader>();

    private static JFrame frame = new JFrame(Title_GeoRSSInjector);
    private static FeedConfigDialog feedConfigDialog = new FeedConfigDialog(frame);
    private DefaultMutableTreeNode root = new DefaultMutableTreeNode(TreeRootNodeName);
    private DefaultTreeModel treeModel;
    private JEditorPane treeViewPane;
    private JTree tree;
    private JPopupMenu feedMangementMenu = new JPopupMenu();
    private String feedSelected;
    
    //do not need those
    //private FeedConfiguration reConfig=null;
    //private String reStartUrl=null;

    public MainFrame() {

        JMenuItem menuItem = new JMenuItem(S_ModifyFeed);
        menuItem.addActionListener(this);
        feedMangementMenu.add(menuItem);

        menuItem = new JMenuItem(S_DeleteFeed);
        menuItem.addActionListener(this);
        feedMangementMenu.add(menuItem);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String command = e.getActionCommand();
        if (command.equals(S_New)) {
            getFeedConfiguration();
        //	run();
        } else if (command.equals(S_ModifyFeed)) {
            // get the right configuration and get FeedConfigDialog again
            feedConfigDialog.setConfig(feedMap.get(feedSelected).getConfig());
            feedConfigDialog.setVisible(true);

            FeedConfiguration config = feedConfigDialog.getConfig();
            feedMap.get(config.getUrl()).setConfig(config);
            treeViewPane.setText(config.toString());
        } else if (command.equals(S_DeleteFeed)) {
            String message = "Delete " + feedSelected;
            if (JOptionPane.showConfirmDialog(frame, message, "Confirmation",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                stopFeed(feedSelected);
            }
        }
    }

    public void createAndShowGUI() {

        frame.addWindowListener(this);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(createMenuBar());
        frame.setContentPane(createTreeContentPane());
        frame.setSize(768, 512);
        frame.setVisible(true);
        // place the window in the center of the screen
        frame.setLocationRelativeTo(null);
    }

    public JMenuBar createMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu(S_File);
        fileMenu.setMnemonic(70);
        menuBar.add(fileMenu);
        JMenuItem newMenuItem = new JMenuItem(S_New);
        newMenuItem.getAccessibleContext().setAccessibleDescription("To create a RSS/Atom feed");
        newMenuItem.addActionListener(this);
        fileMenu.add(newMenuItem);
        return menuBar;
    }

    public JSplitPane createTreeContentPane() {

        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        tree.addMouseListener(this);

        // Create the HTML viewing pane.
        treeViewPane = new JEditorPane();
        treeViewPane.setEditable(false);
        treeViewPane.setContentType(Type_Plain_Text);
        JScrollPane htmlView = new JScrollPane(treeViewPane);

        // Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(tree);
        splitPane.setBottomComponent(htmlView);

        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        tree.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100);
        splitPane.setPreferredSize(new Dimension(500, 300));

        return splitPane;
    }

    private void getFeedConfiguration() //throws InterruptedException 
    {

    //	suspended = false;
    	
        feedConfigDialog.resetConfig();
        feedConfigDialog.setVisible(true);

        FeedConfiguration config = feedConfigDialog.getConfig();
        
        //save that one for possible thread interruption.
        //reConfig = config;
        
        if (config.getUrl() != null) {
        //	reStartUrl = config.getUrl();
            startFeed(config);
        }
    }

    public JFrame getFrame() {

        return frame;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        // MessageBox.debug(frame, "mouseClicked: " + e.getComponent().getName());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

        // MessageBox.debug(frame, "mouseEntered: " + e.getComponent().getName());
    }

    @Override
    public void mouseExited(MouseEvent e) {

        // MessageBox.debug(frame, "mouseExited: " + e.getComponent().getName());
    }

    @Override
    public void mousePressed(MouseEvent e) {

        if (e.isPopupTrigger())
            treeNodePopupEvent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        if (e.isPopupTrigger())
            treeNodePopupEvent(e);
    }

    public void setFrame(JFrame frame) {

        this.frame = frame;
    }

    private String showTreeNode(DefaultMutableTreeNode node) {

        String feedName = node.toString();
        FeedConfiguration config = feedMap.get(feedName).getConfig();
        if (config != null) {
            treeViewPane.setText(config.toString());
        }
        return feedName;
    }

    /*
     * When the user configures a new feed, we will:
     *  - create a tree node on the top pane and display the content on the bottom pane
     *  - create a GeoRSSReader thread to retrieve the feed, parse it and save either Incident or Alert into UICDS core
     */
    private void startFeed(FeedConfiguration config) {

    	//if((updateIt == false)&&(suspended == false))
		//  {
	        // create the tree node on the top pane
	        DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(config.getUrl());
	        treeModel.insertNodeInto(childNode, root, root.getChildCount());
	        tree.scrollPathToVisible(new TreePath(childNode.getPath()));
	
	        treeViewPane.setText(config.toString());
	
	        logger.info("start GeoRSSReader for [" + config.getUrl() + "] ...");
	        
	        GeoRSSReader geoRSSReader = new GeoRSSReader(config, true);
	        geoRSSReader.start();
	                        
	        feedMap.put(config.getUrl(), geoRSSReader);
	//	  }
    	
    }

    private void stopFeed(String url) {

        logger.info("stop GeoRSSReader for [" + url + "] ...");

        // refresh the TreeViewPane
        DefaultMutableTreeNode node = null;
        int childCount = treeModel.getChildCount(root);
        for (int i = 0; i < childCount; i++) {
            node = (DefaultMutableTreeNode) treeModel.getChild(root, i);
            if (node.toString().equals(url)) {
                treeModel.removeNodeFromParent(node);
                break;
            }
        }
        childCount = treeModel.getChildCount(root);
        if (childCount > 0) {
            node = (DefaultMutableTreeNode) treeModel.getChild(root, 0);
            showTreeNode(node);
            tree.scrollPathToVisible(new TreePath(node.getParent()));
        } else {
            treeViewPane.setText("");
        }

        // stop the thread
        GeoRSSReader geoRSSReader = feedMap.get(url);
        if (geoRSSReader == null)
            return;

        geoRSSReader.interrupt();
        while (geoRSSReader.isAlive()) {
            logger.debug("Thread: [" + geoRSSReader.getConfig().getUrl()
                + "] is waiting to terminate ...");
            frame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                geoRSSReader.join();
            } catch (InterruptedException e) {
                logger.debug("Thread [" + geoRSSReader.getConfig().getUrl()
                    + "] has serious problem: " + e.getMessage());
            }
            frame.setCursor(Cursor.getDefaultCursor());
        }
        feedMap.remove(url);
        logger.debug("Thread: [" + geoRSSReader.getConfig().getUrl() + "] termintated ...");
    }

    private void treeNodePopupEvent(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();
        JTree tree = (JTree) e.getSource();
        TreePath path = tree.getPathForLocation(x, y);
        if (path == null)
            return;

        tree.setSelectionPath(path);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (node.equals(root)) {
            if (JOptionPane.showConfirmDialog(frame, Title_CreateFeed, Title_CreateFeed,
                JOptionPane.OK_CANCEL_OPTION) == 0) {
            	
            	getFeedConfiguration();
            	
            		/*
            	//1011 FLI added thread issue, not right this way
                try {
					getFeedConfiguration();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				*/
            }
        } else {
            feedMangementMenu.show(tree, x, y);
        }
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

        // Returns the last path element of the selection.
        // This method is useful only when the selection model allows a single selection.
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null)
            // Nothing is selected.
            return;

        if (node.equals(root) == false) {
            feedSelected = showTreeNode(node);
        }
    }

    @Override
    public void windowActivated(WindowEvent e) {

        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosed(WindowEvent e) {

        // TODO Auto-generated method stub

    }

    @Override
    public void windowClosing(WindowEvent e) {

        Set<String> urls = new HashSet<String>(feedMap.keySet());
        if (urls.size() > 0) {
            if (JOptionPane.showConfirmDialog(frame,
                "Close GeoRSS Injector Window will remove all the feeds from the UICDS Core",
                "Confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.CANCEL_OPTION) {
                return;
            }

            for (String url : urls) {
                stopFeed(url);
            }
        }
        System.exit(0);
    }

    @Override
    public void windowDeactivated(WindowEvent e) {

        // TODO Auto-generated method stub

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

        // TODO Auto-generated method stub

    }

    @Override
    public void windowIconified(WindowEvent e) {

        // TODO Auto-generated method stub

    }

    @Override
    public void windowOpened(WindowEvent e) {

        // TODO Auto-generated method stub

    }

    /////////////////////add more because added runable on this class ///////////////
   /*
    private volatile boolean suspended=false;
    private volatile boolean updateIt=false;
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		 try {	
			  if((updateIt == false)&&(suspended == false))
			  {
				  System.out.println("Start a thread for feed operation....");
				  getFeedConfiguration();
			  }
		    } catch (InterruptedException x) {
		      System.out.println("interrupted in getFeedConfiguration()/startFeed(...)...");
		      		      		        	
        	 //something happened, geoRSSReader interrupted, take a break.
	        	try {
	                Thread.sleep(1000);
	              } catch (InterruptedException xx) 
	              {
	            	  System.out.println("take a break after interrupted in getFeedConfiguration()/startFeed(...) and restart it...");
	              }
        	
	        	//stop it
	        	System.out.println("Stop that trouble thread.");
	        	stopFeed(reStartUrl);
	        	
	        	System.out.println("Restart a new thread to keep work continue....");
	        	
	        	//resume / start a new thread.
	        	resumeRequest();
	        	
	        	if(reConfig!=null)
	        	{
	        		startFeed(reConfig);
	        	}
		     }
		  
		 
		 updateIt = true;
	}
 
	 public void resumeRequest() {
		    suspended = false;
		    updateIt = false;
	 }
    */
}
