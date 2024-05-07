package com.ec.svg.generator.app.util;

import com.ec.svg.generator.app.model.entity.AuditDetails;
import com.ec.svg.generator.app.model.entity.Path;
import com.ec.svg.generator.app.model.entity.PathRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;

import org.w3c.dom.NodeList;

public class ResourceHelper {

    public static final String SYSTEM_USER = "system";
    private static final Logger logger = LoggerFactory.getLogger(ResourceHelper.class);

    public void loadXMLResources(String resourcePath, PathRepository pathRepository) {
        try {
            logger.info("Loading XML Res");
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new File(resourcePath + "h_mask_1.svg"));
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("path");
            Node first = nodeList.item(0);
            logger.info("Got tagname: " + first.getNodeName());

            String id = first.getAttributes().getNamedItem("id").getNodeValue();
            String dPath = first.getAttributes().getNamedItem("d").getNodeValue();
            logger.info("Got id: " + id);
            logger.info("Got d: " + dPath);

            char letterName = id.charAt(0);
            id.getBytes(StandardCharsets.UTF_8);

            if (pathRepository != null) {
                logger.info("PathRepo is NOT NULL!");
                AuditDetails auditDetails = new AuditDetails();
                auditDetails.setCreatedBy(SYSTEM_USER);
                auditDetails.setVersion(Integer.valueOf(1));
                auditDetails.setCreatedTs(new Timestamp(System.currentTimeMillis()));
                auditDetails.setIsActive(Boolean.TRUE);

                Path testPath = new Path();
                testPath.setGlyphName(id);
                testPath.setPathData(dPath);
                testPath.setAuditDetails(auditDetails);
                testPath.setSource(String.valueOf(id.charAt(0)));



                pathRepository.save(testPath);
            }

        } catch (ParserConfigurationException pce) {
            logger.info("Exception loading XML res: " + pce.toString());
        } catch (SAXException sae) {
            logger.info("Exception parsing XML: " + sae.toString());

        } catch (IOException ioe) {
            logger.info("Exception reading XML: " + ioe.toString());

        }
    }
}
