package com.saic.uicds.clients.em.georssadapter.ui;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;


import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Category;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Certainty;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Severity;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Info.Urgency;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Scope;
import x1.oasisNamesTcEmergencyCap1.AlertDocument.Alert.Status;

import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
//import com.saic.uicds.clients.em.georssadapter.ui.ListHscrollCellRenderer;

public class AlertConfigDialog
    extends JDialog
    implements ActionListener, ConstantData {

    private static final long serialVersionUID = 5369582786037416224L;

    private static JButton okButton = new JButton(S_Ok);
    private static JButton cancelButton = new JButton(S_Cancel);

    private static final JLabel[] cbLabels = {
        new JLabel(S_Event + ":"),
        new JLabel(S_Description + ":"),
        new JLabel(S_Headline + ":"),
        new JLabel(S_Contact + ":"),
        new JLabel(S_Address + ":"), };

    private static final JLabel[] rbLabels = {
        new JLabel(S_Status+ ":"),
        new JLabel(S_Scope+ ":"),
        new JLabel(S_Category + ":"),
        new JLabel(S_Severity+ ":"),
        new JLabel(S_Urgency+ ":"),
        new JLabel(S_Certainty+ ":"), };

    private static final Status.Enum[] StatusEnum = {
        AlertDocument.Alert.Status.ACTUAL,
        AlertDocument.Alert.Status.DRAFT,
        AlertDocument.Alert.Status.EXERCISE,
        AlertDocument.Alert.Status.SYSTEM,
        AlertDocument.Alert.Status.TEST, };

    private static final String[] Statuses = {
        AlertDocument.Alert.Status.ACTUAL.toString(),
        AlertDocument.Alert.Status.DRAFT.toString(),
        AlertDocument.Alert.Status.EXERCISE.toString(),
        AlertDocument.Alert.Status.SYSTEM.toString(),
        AlertDocument.Alert.Status.TEST.toString(), };

    private static final Scope.Enum[] ScopeEnum = {
        AlertDocument.Alert.Scope.PUBLIC,
        AlertDocument.Alert.Scope.PRIVATE,
        AlertDocument.Alert.Scope.RESTRICTED, };

    private static final String[] Scopes = {
        AlertDocument.Alert.Scope.PUBLIC.toString(),
        AlertDocument.Alert.Scope.PRIVATE.toString(),
        AlertDocument.Alert.Scope.RESTRICTED.toString(), };

    private static final Category.Enum[] CategoryEnum = {
        AlertDocument.Alert.Info.Category.CBRNE,
        AlertDocument.Alert.Info.Category.ENV,
        AlertDocument.Alert.Info.Category.FIRE,
        AlertDocument.Alert.Info.Category.GEO,
        AlertDocument.Alert.Info.Category.HEALTH,
        AlertDocument.Alert.Info.Category.INFRA,
        AlertDocument.Alert.Info.Category.MET,
        AlertDocument.Alert.Info.Category.OTHER,
        AlertDocument.Alert.Info.Category.RESCUE,
        AlertDocument.Alert.Info.Category.SAFETY,
        AlertDocument.Alert.Info.Category.SECURITY,
        AlertDocument.Alert.Info.Category.TRANSPORT, };

    private static final String[] Categories = {
        AlertDocument.Alert.Info.Category.CBRNE.toString(),
        AlertDocument.Alert.Info.Category.ENV.toString(),
        AlertDocument.Alert.Info.Category.FIRE.toString(),
        AlertDocument.Alert.Info.Category.GEO.toString(),
        AlertDocument.Alert.Info.Category.HEALTH.toString(),
        AlertDocument.Alert.Info.Category.INFRA.toString(),
        AlertDocument.Alert.Info.Category.MET.toString(),
        AlertDocument.Alert.Info.Category.OTHER.toString(),
        AlertDocument.Alert.Info.Category.RESCUE.toString(),
        AlertDocument.Alert.Info.Category.SAFETY.toString(),
        AlertDocument.Alert.Info.Category.SECURITY.toString(),
        AlertDocument.Alert.Info.Category.TRANSPORT.toString(), };

    private static final Urgency.Enum[] UrgencyEnum = {
        AlertDocument.Alert.Info.Urgency.EXPECTED,
        AlertDocument.Alert.Info.Urgency.FUTURE,
        AlertDocument.Alert.Info.Urgency.IMMEDIATE,
        AlertDocument.Alert.Info.Urgency.PAST,
        AlertDocument.Alert.Info.Urgency.UNKNOWN, };

    private static final String[] Urgencies = {
        AlertDocument.Alert.Info.Urgency.EXPECTED.toString(),
        AlertDocument.Alert.Info.Urgency.FUTURE.toString(),
        AlertDocument.Alert.Info.Urgency.IMMEDIATE.toString(),
        AlertDocument.Alert.Info.Urgency.PAST.toString(),
        AlertDocument.Alert.Info.Urgency.UNKNOWN.toString(), };

    private static final Severity.Enum[] SeverityEnum = {
        AlertDocument.Alert.Info.Severity.EXTREME,
        AlertDocument.Alert.Info.Severity.SEVERE,
        AlertDocument.Alert.Info.Severity.MODERATE,
        AlertDocument.Alert.Info.Severity.MINOR,
        AlertDocument.Alert.Info.Severity.UNKNOWN, };

    private static final String[] Severities = {
        AlertDocument.Alert.Info.Severity.EXTREME.toString(),
        AlertDocument.Alert.Info.Severity.SEVERE.toString(),
        AlertDocument.Alert.Info.Severity.MODERATE.toString(),
        AlertDocument.Alert.Info.Severity.MINOR.toString(),
        AlertDocument.Alert.Info.Severity.UNKNOWN.toString(), };

    /* creation, update and cancel will set this value
    private static final String[] MsgTypes = {
        AlertDocument.Alert.MsgType.ACK.toString(),
        AlertDocument.Alert.MsgType.ALERT.toString(),
        AlertDocument.Alert.MsgType.CANCEL.toString(),
        AlertDocument.Alert.MsgType.ERROR.toString(),
        AlertDocument.Alert.MsgType.UPDATE.toString(), };
    */

    private static final Certainty.Enum[] CertaintyEnum = {
        AlertDocument.Alert.Info.Certainty.OBSERVED,
        AlertDocument.Alert.Info.Certainty.LIKELY,
        AlertDocument.Alert.Info.Certainty.POSSIBLE,
        AlertDocument.Alert.Info.Certainty.UNLIKELY,
        AlertDocument.Alert.Info.Certainty.UNKNOWN, };

    private static final String[] Certainties = {
        AlertDocument.Alert.Info.Certainty.OBSERVED.toString(),
        AlertDocument.Alert.Info.Certainty.LIKELY.toString(),
        AlertDocument.Alert.Info.Certainty.POSSIBLE.toString(),
        AlertDocument.Alert.Info.Certainty.UNLIKELY.toString(),
        AlertDocument.Alert.Info.Certainty.UNKNOWN.toString(), };

    private static final JRadioButton[][] radioButtonArray = {
        new JRadioButton[Statuses.length],
        new JRadioButton[Scopes.length],
        new JRadioButton[Categories.length],
        new JRadioButton[Severities.length],
        new JRadioButton[Urgencies.length],
        new JRadioButton[Certainties.length], };

    private static final ButtonGroup[] radioButtonGroups = {
        new ButtonGroup(),
        new ButtonGroup(),
        new ButtonGroup(),
        new ButtonGroup(),
        new ButtonGroup(),
        new ButtonGroup(), };

    private static final String[][] radioButtonNameArray = {
        Statuses,
        Scopes,
        Categories,
        Severities,
        Urgencies,
        Certainties, };

    static {
        for (int i = 0; i < rbLabels.length; i++) {
            createRadioButtonGroup(radioButtonNameArray[i], radioButtonArray[i],
                radioButtonGroups[i]);
        }
    }

    private static JComboBox[] comboBoxes = new JComboBox[cbLabels.length];

    private static void createRadioButtonGroup(String[] names, JRadioButton[] radioButtons,
        ButtonGroup buttonGroup) {

        for (int i = 0; i < names.length; i++) {
            JRadioButton rb = new JRadioButton(names[i]);
            if (i == 0) {
                rb.setSelected(true);
            }
            buttonGroup.add(rb);
            radioButtons[i] = rb;
        }
    }

    // private JFrame frame;
    private AlertDocument alert;

    private String[] xpaths = null;
    private boolean isCancelled = true;
    private Map<String, String> elementMap = null;
    public AlertConfigDialog(JFrame frame, AlertDocument alert, String[] xpaths,
        Map<String, String> elementMap) {

        super(frame, true);

        this.alert = alert;
        this.xpaths = xpaths;
        this.elementMap = elementMap;

        initConfig();
        setTitle(Title_AlertConfig);
        pack();
        setSize(getPreferredSize());
        setLocationRelativeTo(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals(S_Ok)) {
                isCancelled = false;
            } else if (e.getActionCommand().equals(S_Cancel)) {
                isCancelled = true;
            }
            setVisible(false);
        } else if (e.getSource() instanceof JRadioButton) {
            JRadioButton rb = (JRadioButton) e.getSource();
            for (int i = 0; i < radioButtonGroups.length; i++) {
                for (Enumeration element = radioButtonGroups[i].getElements(); element.hasMoreElements();) {
                    if (element.nextElement().equals(rb)) {
                        resetRadioButtonValue(i, e.getActionCommand());
                        return;
                    }
                }
            }
        } else if (e.getSource() instanceof JComboBox) {
            setXPath((JComboBox) e.getSource());
        }
    }

    /*
        private JComboBox createComboBox(String[] items) {

            JComboBox cb = new JComboBox(items);
            cb.setSelectedIndex(0);
            cb.addActionListener(this);
            return cb;
        }
    */
    private SequentialGroup createRadioButtonGroup(GroupLayout layout, int index) {

        GroupLayout.SequentialGroup group = layout.createSequentialGroup();
        for (int i = 0; i < radioButtonArray[index].length; i++) {
            radioButtonArray[index][i].addActionListener(this);
            /*
            if (i == 0) {
                radioButtonArray[index][i].setSelected(true);
            }
            */
            group.addComponent(radioButtonArray[index][i]);
        }

        return group;
    }

    public AlertDocument getAlert() {

        return alert;
    }

    public Map<String, String> getElementMap() {

        return elementMap;
    }

    private void initConfig() {

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setupComboBox();

        JPanel container = new JPanel();

        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.SequentialGroup horizontalGroups = layout.createSequentialGroup();

        GroupLayout.ParallelGroup firstColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JLabel label : cbLabels) {
            firstColumn.addComponent(label);
        }
        for (JLabel label : rbLabels) {
            firstColumn.addComponent(label);
        }

        GroupLayout.ParallelGroup secondColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (int i = 0; i < cbLabels.length; i++) {
            secondColumn.addComponent(comboBoxes[i]);
        }

        for (int i = 0; i < rbLabels.length; i++) {
            secondColumn.addGroup(createRadioButtonGroup(layout, i));
        }

        GroupLayout.SequentialGroup buttonGroup = layout.createSequentialGroup();
        buttonGroup.addComponent(okButton);
        buttonGroup.addComponent(cancelButton);
        secondColumn.addGroup(buttonGroup);

        horizontalGroups.addGroup(firstColumn);
        horizontalGroups.addGroup(secondColumn);

        layout.setHorizontalGroup(horizontalGroups);

        GroupLayout.SequentialGroup verticalGroups = layout.createSequentialGroup();
        for (int i = 0; i < cbLabels.length; i++) {
            GroupLayout.ParallelGroup row = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            row.addComponent(cbLabels[i]);
            row.addComponent(comboBoxes[i]);
            verticalGroups.addGroup(row);
        }
        for (int i = 0; i < rbLabels.length; i++) {
            GroupLayout.ParallelGroup row = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            row.addComponent(rbLabels[i]);
            for (int j = 0; j < radioButtonArray[i].length; j++) {
                row.addComponent(radioButtonArray[i][j]);
            }
            verticalGroups.addGroup(row);
        }

        GroupLayout.ParallelGroup buttonRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        buttonRow.addComponent(okButton);
        buttonRow.addComponent(cancelButton);
        verticalGroups.addGroup(buttonRow);

        layout.setVerticalGroup(verticalGroups);

        setupAlert();
        this.add(container);
    }

    public boolean isCancelled() {

        return isCancelled;
    }

    private void resetRadioButtonValue(int index, String value) {

        switch (index) {
        case 0:
            for (int i = 0; i < Statuses.length; i++) {
                if (Statuses[i].equals(value)) {
                    alert.getAlert().setStatus(StatusEnum[i]);
                    return;
                }
            }
            break;
        case 1:
            for (int i = 0; i < Scopes.length; i++) {
                if (Scopes[i].equals(value)) {
                    alert.getAlert().setScope(ScopeEnum[i]);
                    return;
                }
            }
            break;
        case 2:
            for (int i = 0; i < Categories.length; i++) {
                if (Categories[i].equals(value)) {
                    alert.getAlert().getInfoArray(0).setCategoryArray(0, CategoryEnum[i]);
                    return;
                }
            }
            break;
        case 3:
            for (int i = 0; i < Severities.length; i++) {
                if (Severities[i].equals(value)) {
                    alert.getAlert().getInfoArray(0).setSeverity(SeverityEnum[i]);
                    return;
                }
            }
            break;
        case 4:
            for (int i = 0; i < Urgencies.length; i++) {
                if (Urgencies[i].equals(value)) {
                    alert.getAlert().getInfoArray(0).setUrgency(UrgencyEnum[i]);
                    return;
                }
            }
            break;
        case 5:
            for (int i = 0; i < Certainties.length; i++) {
                if (Certainties[i].equals(value)) {
                    alert.getAlert().getInfoArray(0).setCertainty(CertaintyEnum[i]);
                    return;
                }
            }
            break;
        }
    }

    private void setupAlert() {

        // set the radio buttons
        for (int i = 0; i < StatusEnum.length; i++) {
            if (alert.getAlert().getStatus().equals(StatusEnum[i])) {
                radioButtonArray[0][i].setSelected(true);
                break;
            }
        }
        for (int i = 0; i < ScopeEnum.length; i++) {
            if (alert.getAlert().getScope().equals(ScopeEnum[i])) {
                radioButtonArray[1][i].setSelected(true);
                break;
            }
        }
        for (int i = 0; i < CategoryEnum.length; i++) {
            if (alert.getAlert().getInfoArray(0).getCategoryArray(0).equals(CategoryEnum[i])) {
                radioButtonArray[2][i].setSelected(true);
                break;
            }
        }
        for (int i = 0; i < SeverityEnum.length; i++) {
            if (alert.getAlert().getInfoArray(0).getSeverity().equals(SeverityEnum[i])) {
                radioButtonArray[3][i].setSelected(true);
                break;
            }
        }
        for (int i = 0; i < UrgencyEnum.length; i++) {
            if (alert.getAlert().getInfoArray(0).getUrgency().equals(UrgencyEnum[i])) {
                radioButtonArray[4][i].setSelected(true);
                break;
            }
        }
        for (int i = 0; i < CertaintyEnum.length; i++) {
            if (alert.getAlert().getInfoArray(0).getCertainty().equals(CertaintyEnum[i])) {
                radioButtonArray[5][i].setSelected(true);
                break;
            }
        }
    }

    private void setupComboBox() {

        boolean isInit = elementMap.size() == 0 ? true : false;

        for (int i = 0; i < cbLabels.length; i++) {

            JComboBox cb = new JComboBox(xpaths);
            cb.addActionListener(this);

            String element = cbLabels[i].getText().replaceAll(":", "");
            // if it's initial setup then set the blank one
            if (isInit) {
                cb.setSelectedIndex(0);
                elementMap.put(element, xpaths[xpaths.length - 1]);
            } else {
                // else reset the previous selection
                for (int j = 0; j < xpaths.length; j++) {
                    String value = xpaths[j];
                    int index = value.indexOf("=");
                    value = (index != -1 ? value.substring(0, index) : value);
                    if (elementMap.get(element).equals(value)) {
                        cb.setSelectedIndex(j);
                        break;
                    }
                }
            }
            comboBoxes[i] = cb;
        }
    }

    private void setXPath(JComboBox cb) {

        for (int i = 0; i < cbLabels.length; i++) {
            if (cb.equals(comboBoxes[i])) {
                String selected = (String) cb.getSelectedItem();
                int index = selected.indexOf('=');
                elementMap.put(cbLabels[i].getText().replaceAll(":", ""),
                    index != -1 ? selected.substring(0, index) : selected);
            }
        }
    }
}
