package com.ec.svg.generator.app.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class SAXHelper {

    private static final Logger logger = LoggerFactory.getLogger(SAXHelper.class);

    private static DocumentBuilder documentBuilder;

    public static DocumentBuilder getDocumentBuilder() {
        if (documentBuilder == null) {
            try {
                documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            } catch (ParserConfigurationException pce) {
                logger.info("Exception creating new document builder");
                pce.printStackTrace();
            }
        }

        return documentBuilder;
    }

    public static NodeList getPathXMLNodes(String resource) {
        DocumentBuilder builder = getDocumentBuilder();
        NodeList result = null;

        try {
            Document doc = builder.parse(new File(resource));
            doc.getDocumentElement().normalize();
            result = doc.getElementsByTagName("path");
        } catch (SAXException sae) {
            logger.info("Exception parsing XML: " + sae.toString());

        } catch (IOException ioe) {
            logger.info("Exception reading XML: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        return result;
    }
    public static NodeList getPathXMLNodes(InputStream inputStream) {
        DocumentBuilder builder = getDocumentBuilder();
        NodeList result = null;

        try {
            Document doc = builder.parse(inputStream);
            doc.getDocumentElement().normalize();
            result = doc.getElementsByTagName("path");
        } catch (SAXException sae) {
            logger.info("Exception parsing XML: " + sae.toString());

        } catch (IOException ioe) {
            logger.info("Exception reading XML: " + ioe.getMessage());
            ioe.printStackTrace();
        }
        return result;
    }

}
