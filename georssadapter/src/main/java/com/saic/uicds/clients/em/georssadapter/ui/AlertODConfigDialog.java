package com.saic.uicds.clients.em.georssadapter.ui;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import x1.oasisNamesTcEmergencyCap1.AlertDocument;
import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
//import com.saic.uicds.clients.em.georssadapter.ui.ListHscrollCellRenderer;

public class AlertODConfigDialog
    extends JDialog
    implements ActionListener, ConstantData {

    private static final long serialVersionUID = 5369582786037416224L;

    //private static JLabel label = new JLabel("--");
    
    private static JButton okButton = new JButton(S_Ok);
    private static JButton cancelButton = new JButton(S_Cancel);

    private static final JLabel[] cbLabels = {
    	new JLabel(S_Identifier + ":"),
        new JLabel( S_Sender + ":"), 
        new JLabel( S_SentDateTime + ":"),  
        new JLabel( S_MessageType + ":"),  
        
        new JLabel(S_Event + ":"),
        new JLabel(S_Description + ":"),
        new JLabel(S_Headline + ":"),
        new JLabel(S_Contact + ":"),
        new JLabel(S_Address + ":"), 
        
        new JLabel(S_Status + ":"),
        new JLabel(S_Scope+ ":"),
        new JLabel(S_Category+ ":"),
        new JLabel(S_Severity+ ":"),
        new JLabel(S_Urgency+ ":"),
        new JLabel(S_Certainty+ ":"), 
 //   };
    
 //   private static final JLabel[] cbLabels2 = {
        
        new JLabel( S_Source + ":"), 
        new JLabel( S_Restriction + ":"), 
        //    new JLabel( S_Code), 
        new JLabel( S_Note + ":"), 
        new JLabel( S_Reference + ":"), 
      //  new JLabel( S_ResponseType + ":"), 
       // new JLabel( S_EventCode), 
         
        new JLabel( S_Language + ":"),  
        new JLabel( S_Effective + ":"),  
        new JLabel( S_Onset + ":"),  
        new JLabel( S_Expires + ":"),  
    //    new JLabel( S_Sendername + ":"),  
        new JLabel( S_Instruction + ":"), 
        new JLabel( S_Web + ":"), 
     //   new JLabel( S_Parameter + ":"),  
        new JLabel( S_Resource + ":"),  
        new JLabel( S_Area + ":"), 
        new JLabel( S_CircleLatLong + ":"),
        new JLabel( S_Polygon + ":"), 
     //   new JLabel( S_Geocode + ":"),
        
     
    };
    

  //  private static JComboBox[] comboBoxes1 = new JComboBox[cbLabels.length];
    private static WideComboBox[] comboBoxes1 = new WideComboBox[cbLabels.length];
    
  //  private static JComboBox[] comboBoxes2 = new JComboBox[cbLabels2.length];
    
    // private JFrame frame;
    private AlertDocument alert;

    private String[] xpaths = null;
    private boolean isCancelled = true;
    private Map<String, String> elementMap = null;
    private String category;
    
    public AlertODConfigDialog(JFrame frame, AlertDocument alert, String[] xpaths,
        Map<String, String> elementMap) {

        super(frame, true);

        this.alert = alert;
        this.xpaths = xpaths;
        this.elementMap = elementMap;

        initConfig();
        setTitle(Title_AlertODConfig);
        pack();
        
        Dimension dm=new Dimension(500, 600);
        setSize(dm);      
    //    setSize(getPreferredSize());
        setLocationRelativeTo(frame);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JButton) {
            if (e.getActionCommand().equals(S_Ok)) {
            	takeData();
                isCancelled = false;
            } else if (e.getActionCommand().equals(S_Cancel)) {
                isCancelled = true;
            }
            setVisible(false);
        } else if (e.getSource() instanceof JComboBox) {
        	//takeData();
            setXPath((JComboBox) e.getSource());
        }
    }

       
    public AlertDocument getAlert() {

        return alert;
    }

    public Map<String, String> getElementMap() {

        return elementMap;
    }

    class ETeamAdjustmentListener implements AdjustmentListener {
        public void adjustmentValueChanged(AdjustmentEvent e) {
         // label.setText("    New Value is " + e.getValue() + "      ");
        //  int x = e.getValue();
        
          repaint();
        }
      }
    
    private void initConfig() {

        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setupComboBox();
                
        JPanel container = new JPanel();     
        //container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
               
        GroupLayout layout = new GroupLayout(container);     
        container.setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        GroupLayout.SequentialGroup horizontalGroups = layout.createSequentialGroup();

        GroupLayout.ParallelGroup firstColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JLabel label : cbLabels) {
            firstColumn.addComponent(label);
        }
            
        GroupLayout.ParallelGroup secondColumn = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (int i = 0; i < cbLabels.length; i++) {
            secondColumn.addComponent(comboBoxes1[i]);
        }

        /*
        GroupLayout.ParallelGroup firstColumnB = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (JLabel label : cbLabels2) {
            firstColumnB.addComponent(label);
        }
            
        GroupLayout.ParallelGroup secondColumnB = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (int i = 0; i < cbLabels2.length; i++) {
            secondColumnB.addComponent(comboBoxes2[i]);
        }
        */
        GroupLayout.SequentialGroup buttonGroup = layout.createSequentialGroup();
        buttonGroup.addComponent(okButton);
        buttonGroup.addComponent(cancelButton);
        secondColumn.addGroup(buttonGroup);

        horizontalGroups.addGroup(firstColumn);
        horizontalGroups.addGroup(secondColumn);
    //   horizontalGroups.addGroup(firstColumnB);
    //    horizontalGroups.addGroup(secondColumnB);
    //    horizontalGroups.addComponent(hbar);
        
        layout.setHorizontalGroup(horizontalGroups);

        GroupLayout.SequentialGroup verticalGroups = layout.createSequentialGroup();
        for (int i = 0; i < cbLabels.length; i++) {
            GroupLayout.ParallelGroup row = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            row.addComponent(cbLabels[i]);
            row.addComponent(comboBoxes1[i]);
      //      row.addComponent(cbLabels2[i]);
      //      row.addComponent(comboBoxes2[i]);
            verticalGroups.addGroup(row);
        }
         
        GroupLayout.ParallelGroup buttonRow = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        buttonRow.addComponent(okButton);
        buttonRow.addComponent(cancelButton);
        verticalGroups.addGroup(buttonRow);
        layout.setVerticalGroup(verticalGroups);
             
        /////////////////////////////
        /*
         *  the other not move with scroll bar move. too much.
         *  
         *  
            
        setLayout(new BorderLayout());

        JScrollBar hbar = new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 300);
        JScrollBar vbar = new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 300);

        hbar.setUnitIncrement(2);
        hbar.setBlockIncrement(1);

        hbar.addAdjustmentListener(new ETeamAdjustmentListener());
        vbar.addAdjustmentListener(new ETeamAdjustmentListener());
               
   //     container.add(hbar);
   //     container.add(vbar);
  //      container.add(hbar, BorderLayout.SOUTH);
  //      container.add(vbar, BorderLayout.EAST);
        
        this.add(hbar, BorderLayout.SOUTH);
        this.add(vbar, BorderLayout.EAST);
                  */
        
        //  setupAlert();
        this.add(container);
        
    }

    public boolean isCancelled() {

        return isCancelled;
    }
    
    private void setupComboBox() {

        boolean isInit = elementMap.size() == 0 ? true : false;

        for (int i = 0; i < cbLabels.length; i++) {

        	WideComboBox cb = new WideComboBox(xpaths);
        	cb.setPreferredSize(new Dimension(180, 20));
            cb.setWide(true);

            /* not work.
            	{
                    @Override
                    public Dimension getMaximumSize() {
                        Dimension dim = super.getMinimumSize(); //getMaximumSize();
                        dim.height = 20; //getPreferredSize().height;
                        dim.width=400;
                        return dim;
                    }
            };
           */
          
        	
            cb.addActionListener(this);

            String element = cbLabels[i].getText().replaceAll(":", "");
            // if it's initial setup then set the blank one
            if (isInit) {
            	
             //here all items are set value as index =1, not good enough 	
             //   cb.setSelectedIndex(1);
                elementMap.put(element, xpaths[xpaths.length - 1]);
                
              //here we set the item value which is for the label/element if it existed
                //02121001 FLI add to set combox init string if it existed.
                cb.setSelectedIndex(getItem(element)); //, xpaths));
                
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
             comboBoxes1[i] = cb;
            // comboBoxes1[i].setSize(new Dimension(400, 20));
                    
        }
            
        /*
        for (int i = 0; i < cbLabels2.length; i++) {

            JComboBox cb = new JComboBox(xpaths);
            cb.addActionListener(this);

            String element = cbLabels2[i].getText().replaceAll(":", "");
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
            comboBoxes2[i] = cb;
        }
        */
        
    }

    //get the item value to match the label and return the index
    //02121001 FLI add to set combox init string if it existed.
    private int getItem(String name) //, String[] all)
    {
    
    	//remove blank space in the name string is any.
    	name.replaceAll(" ", "");
    	
    	//set lower case because the whole string is lower case only.
    	name = name.toLowerCase();
    	for (int j = 0; j < xpaths.length; j++) 
    	{
    	
    		String value = xpaths[j];
    		
    		//>0 found.
    		int x = value.indexOf(name);
    		
    		if(x>0)
    		{
    			return j;
    		}
    	
    	}
    	
    	//otherwise, return the index = 1 item as combox text value
    	return 1;
    }
    
    
    private void setXPath(JComboBox cb) {

        for (int i = 0; i < cbLabels.length; i++) 
        {
            if (cb.equals(comboBoxes1[i])) 
            {
                String selected = (String) cb.getSelectedItem();                
                int index = selected.indexOf('=');
                       
                elementMap.put(cbLabels[i].getText().replaceAll(":", ""),
                    index != -1 ? selected.substring(0, index) : selected);
            }
        }
        
    
    }
    
    
    private void takeData() {

        for (int i = 0; i < comboBoxes1.length; i++) 
        {            
                String selected = (String) comboBoxes1[i].getSelectedItem();                
                int index = selected.indexOf('=');
                      
                if(index !=-1)
                {
                   	String str = selected.substring(0,index);
                 	elementMap.put(cbLabels[i].getText().replaceAll(":", ""), str);                           
                }
                else
                {
                	elementMap.put(cbLabels[i].getText().replaceAll(":", ""),selected);
                }
                
                if(cbLabels[i].getText().replaceAll(":", "").equalsIgnoreCase("category"))
                {
                	int len = selected.length();
                	String str = selected.substring(index+2, len-1);
                	setCategory(str);
                }
        }
                   
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
    
}
