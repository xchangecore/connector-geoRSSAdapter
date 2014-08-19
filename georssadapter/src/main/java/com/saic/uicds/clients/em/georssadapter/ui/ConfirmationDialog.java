package com.saic.uicds.clients.em.georssadapter.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.saic.uicds.clients.em.georssadapter.data.ConstantData;

public class ConfirmationDialog
    extends JDialog
    implements ActionListener, ConstantData {

    private static JButton okButton = new JButton(S_Ok);
    private static JButton cancelButton = new JButton(S_Cancel);
    private static JLabel questionLabel = new JLabel();
    private static boolean isOk = false;

    public static boolean isOk() {

        return isOk;
    }

    public static void setOk(boolean isOk) {

        ConfirmationDialog.isOk = isOk;
    }

    public ConfirmationDialog(JFrame frame, String title, String question) {

        super(frame, true);

        questionLabel.setText(question);
        isOk = false;
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        initDialog();
        pack();
        setTitle(title);
        setSize(getPreferredSize());
        setLocationRelativeTo(frame);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JButton) {
            ConfirmationDialog.isOk = e.getActionCommand().equals(S_Ok) ? true : false;
            setVisible(false);
        }
    }

    private void initDialog() {

        JPanel container = new JPanel();
        GroupLayout layout = new GroupLayout(container);
        container.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // Horizontal group
        GroupLayout.SequentialGroup horizonontalGroups = layout.createSequentialGroup();

        GroupLayout.ParallelGroup firstColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        firstColumn.addComponent(questionLabel, GroupLayout.Alignment.LEADING);
        firstColumn.addComponent(EmptyLabel);
        firstColumn.addComponent(okButton, GroupLayout.Alignment.LEADING);

        GroupLayout.ParallelGroup secondColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        secondColumn.addComponent(EmptyLabel);
        secondColumn.addComponent(EmptyLabel);
        secondColumn.addComponent(cancelButton);

        horizonontalGroups.addGroup(firstColumn);
        horizonontalGroups.addGroup(secondColumn);

        layout.setHorizontalGroup(horizonontalGroups);

        // Vertical group
        GroupLayout.SequentialGroup verticalGroups = layout.createSequentialGroup();

        GroupLayout.ParallelGroup firstRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        firstRow.addComponent(questionLabel);

        GroupLayout.ParallelGroup secondRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        secondRow.addComponent(EmptyLabel);

        GroupLayout.ParallelGroup thirdRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        thirdRow.addComponent(okButton);
        thirdRow.addComponent(EmptyLabel);
        thirdRow.addComponent(cancelButton);

        verticalGroups.addGroup(firstRow);
        verticalGroups.addGroup(secondRow);
        verticalGroups.addGroup(thirdRow);

        layout.setVerticalGroup(verticalGroups);

        this.add(container);
    }
}
