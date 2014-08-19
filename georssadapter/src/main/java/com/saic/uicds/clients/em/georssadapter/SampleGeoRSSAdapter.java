package com.saic.uicds.clients.em.georssadapter;

import java.io.FileNotFoundException;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.saic.uicds.clients.em.georssadapter.data.ConstantData;
import com.saic.uicds.clients.em.georssadapter.ui.MainFrame;

public class SampleGeoRSSAdapter implements ConstantData {

    private static final Logger logger = LoggerFactory.getLogger(SampleGeoRSSAdapter.class);

    public static void main(String args[]) throws Exception {

        ApplicationContext context = null;
        try {
            context = new FileSystemXmlApplicationContext("./" + ContextFile);
        //	context = new FileSystemXmlApplicationContext("C:/00LiTest/05/0605--georssfeeder-doAll/" + ContextFile);
            logger.debug("Using local " + ContextFile + " file");
        } catch (BeanDefinitionStoreException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                logger.debug("Local " + ContextFile + " not found ... using context from jar");
            } else {
                logger.debug("Error reading local context: " + e.getCause().getMessage());
            }
        }

        if (context == null) {
            context =
                new ClassPathXmlApplicationContext(new String[] { "contexts/" + ContextFile });
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                new MainFrame().createAndShowGUI();
            }
        });
    }
}