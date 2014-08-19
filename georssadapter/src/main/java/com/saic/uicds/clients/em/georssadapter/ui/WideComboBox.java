package com.saic.uicds.clients.em.georssadapter.ui;
import javax.swing.*; 
import java.awt.*; 
import java.util.Vector; 
 
// got this workaround from the following bug: 
//      http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4618607 
public class WideComboBox extends JComboBox{ 
	 private String type;
	    private boolean layingOut = false;
	    private int widestLengh = 0;
	    private boolean wide = false;

	    public WideComboBox(Object[] objs) {
	        super(objs);
	    }

	    public boolean isWide() {
	        return wide;
	    }

	    // Setting the JComboBox wide
	    public void setWide(boolean wide) {
	        this.wide = wide;
	        widestLengh = getWidestItemWidth();

	    }

	    public Dimension getSize() {
	        Dimension dim = super.getSize();
	        if (!layingOut && isWide())
	            dim.width = Math.max(widestLengh, dim.width);
	        return dim;
	    }

	    public int getWidestItemWidth() {

	        int numOfItems = this.getItemCount();
	        Font font = this.getFont();
	        FontMetrics metrics = this.getFontMetrics(font);
	        int widest = 0;
	        for (int i = 0; i < numOfItems; i++) {
	            Object item = this.getItemAt(i);
	            int lineWidth = metrics.stringWidth(item.toString());
	            widest = Math.max(widest, lineWidth);
	        }

	        return widest  + 5;
	    }

	    public void doLayout() {
	        try {
	            layingOut = true;
	            super.doLayout();
	        } finally {
	            layingOut = false;
	        }
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String t) {
	        type = t;
	    }

	/* another one
    public WideComboBox() { 
    } 
 
    public WideComboBox(final Object items[]){ 
        super(items); 
    } 
 
    public WideComboBox(Vector items) { 
        super(items); 
    } 
 
    public WideComboBox(ComboBoxModel aModel) { 
        super(aModel); 
    } 
 
    private boolean layingOut = false; 
 
    public void doLayout(){ 
        try{ 
            layingOut = true; 
            super.doLayout(); 
        }finally{ 
            layingOut = false; 
        } 
    } 
 
    public Dimension getSize(){ 
        Dimension dim = super.getSize(); 
        if(!layingOut) 
            dim.width = Math.max(dim.width, getPreferredSize().width); 
        return dim; 
    }
    */ 
}