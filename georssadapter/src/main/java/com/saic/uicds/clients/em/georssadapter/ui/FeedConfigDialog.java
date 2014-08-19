package com.saic.uicds.clients.em.georssadapter.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;

import com.saic.uicds.clients.em.georssadapter.data.AlertEntry;
import com.saic.uicds.clients.em.georssadapter.data.AlertODEntry;
import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
import com.saic.uicds.clients.em.georssadapter.data.FeedConfiguration;

public class FeedConfigDialog
    extends JDialog
    implements ActionListener, PropertyChangeListener, ConstantData {

    private static final long serialVersionUID = 5768582589860829772L;

    private static JLabel urlLabel = new JLabel(S_Url);
    private static JLabel typeLabel = new JLabel(S_Type);
    private static JLabel pollingIntervalLabel = new JLabel(S_PollingInterval);
    private static JTextField urlText = new JTextField(40);
    private static JTextField pollingIntervalText = new JTextField(3);
    
    
    //for filtering and event type
    private static JLabel filteringLabel = new JLabel(S_Filtering);
    private static JTextField filteringText = new JTextField(40);
    private static JLabel eventTypeLabel = new JLabel(S_EventType);
    private static JTextField eventTypeText = new JTextField(40);
    
    //three radio boxes
    private static JRadioButton incidentRB = new JRadioButton(S_Incident);
    private static JRadioButton alertRB = new JRadioButton(S_Alert);
    private static JRadioButton alertODRB = new JRadioButton(S_Alert_Optional_Data);
    
    private static ButtonGroup typeButtonGroup = new ButtonGroup();
    private static JButton okButton = new JButton(S_Ok);
    private static JButton cancelButton = new JButton(S_Cancel);
    
    private static boolean isIncident = false;
    private static boolean isAlertOD = false;

    private static JFrame frame;
    private FeedConfiguration configuration;

    public FeedConfigDialog(JFrame frame) {

        super(frame, true);

        FeedConfigDialog.frame = frame;

        initDialog();

        setTitle(Title_FeedConfig);
        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(FeedConfigDialog.frame);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    	boolean alertHelp=false;
    	boolean alertODHelp=false;
    	
        if (actionEvent.getSource() instanceof JRadioButton) {
            isIncident = actionEvent.getActionCommand().equals(S_Incident);
            isAlertOD = actionEvent.getActionCommand().equals(S_Alert_Optional_Data);
        } else if (actionEvent.getSource() instanceof JButton) {

            boolean isReConfigured = false;

            if (actionEvent.getActionCommand().equals(S_Ok)) {

                if (configuration.getUrl() != null) {
                    isReConfigured = true;
                }
                configuration.setUrl(urlText.getText());
                configuration.setIncident(isIncident);
                configuration.setAlertOD(isAlertOD);
                configuration.setPollingInterval(new Long(pollingIntervalText.getText()).longValue());
                configuration.setFilter(filteringText.getText());
                configuration.setEventType(eventTypeText.getText());

                // collect configuration
                if (isIncident == false) {
                	
                    if (!isReConfigured) 
                    {
                        if (configuration.createFeedParser() == false) {
                            JOptionPane.showMessageDialog(FeedConfigDialog.frame,
                                "Cannot parse Feed: " + configuration.getUrl(),
                                "Error Message for parsing Feed", JOptionPane.ERROR_MESSAGE);
                            configuration.setUrl(null);
                        } else if(isAlertOD==true)
                        {
                            configuration.setAlert(AlertODEntry.createDefaultAlertOD());
                        	alertODHelp =true;
                        }
                        
                        else {
                            configuration.setAlert(AlertEntry.createDefaultAlert());
                            alertHelp = true;
                        }
                    }
                    
                    // parse the feed fine
                    if((alertHelp == true) && (alertODHelp==false))
                    {
	                    AlertDocument alert = (AlertDocument) configuration.getAlert().copy();
	                    Map<String, String> map = new HashMap<String, String>(
	                        configuration.getParser().getElementMap());
	                    AlertConfigDialog alertConfigDialog = new AlertConfigDialog(frame, alert,
	                        configuration.getXPaths(), map);
	                    alertConfigDialog.setVisible(true);
	
	                    // if the Cancel is selected, the alert will be nulled
	                    if (alertConfigDialog.isCancelled() == false) {
	                        configuration.setAlert(alertConfigDialog.getAlert());
	                        map = alertConfigDialog.getElementMap();
	                        if (map.size() > 0) {
	                            configuration.getParser().setElementMap(map);
	                        }
	                    }
                    }else if((alertHelp == false) && (alertODHelp == true) )
                    {
                    	AlertDocument alert = (AlertDocument) configuration.getAlert().copy();
 	                    Map<String, String> map = new HashMap<String, String>(
 	                        configuration.getParser().getElementMap());
 	                    
 	                    AlertODConfigDialog alertODConfigDialog = new AlertODConfigDialog(frame, alert,
 	                        configuration.getXPaths(), map);
 	                   alertODConfigDialog.setVisible(true);
 	
 	                    // if the Cancel is selected, the alert will be nulled
 	                    if (alertODConfigDialog.isCancelled() == false) 
 	                    {
 	                        configuration.setAlert(alertODConfigDialog.getAlert());
 	                        map = alertODConfigDialog.getElementMap();
 	                        if (map.size() > 0) {
 	                            configuration.getParser().setElementMap(map);
 	                        }
 	                        
 	                        String category = alertODConfigDialog.getCategory();
 	                        configuration.setCategory(category);
 	                    }
                    }
                    
                    
                    
                }
            } else {
                configuration.setUrl(null);
            }
            setVisible(false);
        }
    }

    public FeedConfiguration getConfig() {

        return configuration;
    }

    public String getUrl() {

        return urlText.getText();
    }

    private void initDialog() {

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        JPanel container = new JPanel();

        incidentRB.addActionListener(this);
        alertRB.addActionListener(this);
        
        //added alert optional data radio box
        alertODRB.addActionListener(this);
        
        // setup the radio button group
        typeButtonGroup.add(incidentRB);
        typeButtonGroup.add(alertRB);
        typeButtonGroup.add(alertODRB);

        // Components layouting
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.SequentialGroup horizontalGroup = layout.createSequentialGroup();

        // Horizontal group

        GroupLayout.ParallelGroup firstColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        firstColumn.addComponent(urlLabel, GroupLayout.Alignment.TRAILING);
        firstColumn.addComponent(typeLabel, GroupLayout.Alignment.TRAILING);
        firstColumn.addComponent(pollingIntervalLabel, GroupLayout.Alignment.TRAILING);
        //for filtering and event type
        firstColumn.addComponent(filteringLabel, GroupLayout.Alignment.TRAILING);
        firstColumn.addComponent(eventTypeLabel, GroupLayout.Alignment.TRAILING);

        GroupLayout.ParallelGroup secondColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        secondColumn.addComponent(urlText);

        GroupLayout.SequentialGroup rbHGroup = layout.createSequentialGroup();
        rbHGroup.addComponent(alertRB);
        rbHGroup.addComponent(alertODRB);
        rbHGroup.addComponent(incidentRB);
        secondColumn.addGroup(rbHGroup);

        secondColumn.addComponent(pollingIntervalText);
        
        //filtering and event type
        secondColumn.addComponent(filteringText);
        secondColumn.addComponent(eventTypeText);

        // add a empty row
        secondColumn.addGap(5);

        GroupLayout.SequentialGroup buttonHGroup = layout.createSequentialGroup();
        buttonHGroup.addComponent(okButton);
        buttonHGroup.addComponent(cancelButton);
        secondColumn.addGroup(buttonHGroup);

        horizontalGroup.addGroup(firstColumn);
        horizontalGroup.addGroup(secondColumn);

        layout.setHorizontalGroup(horizontalGroup);

        // Vertical group
        GroupLayout.SequentialGroup verticalGroup = layout.createSequentialGroup();

        GroupLayout.ParallelGroup firstRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        firstRow.addComponent(urlLabel);
        firstRow.addComponent(urlText);
        verticalGroup.addGroup(firstRow);

        GroupLayout.ParallelGroup secondRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        secondRow.addComponent(typeLabel);
        secondRow.addComponent(alertRB);
        secondRow.addComponent(alertODRB);
        secondRow.addComponent(incidentRB);
        verticalGroup.addGroup(secondRow);

        GroupLayout.ParallelGroup thirdRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        thirdRow.addComponent(pollingIntervalLabel);
        thirdRow.addComponent(pollingIntervalText);
        verticalGroup.addGroup(thirdRow);
        
        //filtering
        GroupLayout.ParallelGroup fourthRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        fourthRow.addComponent(filteringLabel);
        fourthRow.addComponent(filteringText);
        verticalGroup.addGroup(fourthRow);
        
        //event type
        GroupLayout.ParallelGroup fifthRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        fifthRow.addComponent(eventTypeLabel);
        fifthRow.addComponent(eventTypeText);
        verticalGroup.addGroup(fifthRow);

        // verticalGroup.addGroup(fourthRow);
        verticalGroup.addGap(10);

        GroupLayout.ParallelGroup sixthRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        sixthRow.addComponent(okButton);
        sixthRow.addComponent(cancelButton);
        verticalGroup.addGroup(sixthRow);

        layout.setVerticalGroup(verticalGroup);

        this.add(container);
    }

    public boolean isIncident() {

        return isIncident;
    }

    public boolean isAlertOD() {
		return isAlertOD;
	}
    
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {

    }

    public void resetConfig() {

        configuration = new FeedConfiguration();

        urlText.setText("");
        urlText.setEditable(true);
        pollingIntervalText.setText(new Long(Default_PollingInterval).toString());
        //filter
        filteringText.setText("");
        eventTypeText.setText("");
        
        alertRB.setSelected(true);
        
        alertODRB.setEnabled(true);
        incidentRB.setEnabled(true);
        alertRB.setEnabled(true);
        
        isIncident = false;
        isAlertOD = false;
    }

    public void setConfig(FeedConfiguration config) {

        this.configuration = config;
        urlText.setText(this.configuration.getUrl());
        urlText.setEditable(false);
        pollingIntervalText.setText(new Long(config.getPollingInterval()).toString());
        //filtering
        filteringText.setText(this.configuration.getFilter());
        
        if (config.isIncident()) {
            incidentRB.setSelected(true);
            alertRB.setSelected(false);
            alertODRB.setSelected(false);
        } else if(config.isAlertOD())
        {
        	alertODRB.setSelected(true);
        	incidentRB.setSelected(false);
            alertRB.setSelected(false);	
        }
        else {
        	incidentRB.setSelected(false);
            alertRB.setSelected(true);
            alertODRB.setSelected(false);
        }
        isIncident = config.isIncident() ? true : false;
        isAlertOD = config.isAlertOD() ? true : false;
        incidentRB.setEnabled(false);
        alertRB.setEnabled(false);
        alertODRB.setEnabled(false);
    }

	
}
