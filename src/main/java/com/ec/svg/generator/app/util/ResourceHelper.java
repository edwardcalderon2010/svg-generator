package com.ec.svg.generator.app.util;

import com.ec.svg.generator.app.dto.AuditDetailsDTO;
import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.model.entity.AuditDetails;
import com.ec.svg.generator.app.model.entity.Path;
import com.ec.svg.generator.app.model.entity.PathRepository;
import org.modelmapper.ModelMapper;
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
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;

import static com.ec.svg.generator.app.model.domain.FontCharacter.KEY_FOR_033_UNICODE;
import static com.ec.svg.generator.app.util.StringUtils.REGEX_PATH_UNICODE_ID;
import static com.ec.svg.generator.app.util.StringUtils.REGEX_PATH_SEQUENCE;

public class ResourceHelper {

    public static final String SYSTEM_USER = "system";
    public static final String MASK_ID = "mask";

    private static final Logger logger = LoggerFactory.getLogger(ResourceHelper.class);

    private static DocumentBuilder documentBuilder;

    public void loadXMLResources(String resourcePath, PathRepository pathRepository) {
        File resDir = new File(resourcePath);

        if (resDir.exists() && resDir.list() != null && resDir.list().length > 0) {
            logger.info("Given path for " + resourcePath + " has " + resDir.list().length + " items present.");
            Arrays.stream(resDir.list())
                    .filter(fileName -> fileName.endsWith(".svg"))
                    .sorted()
                    .forEach(fileName -> processResourceFile(resourcePath, fileName, pathRepository));
        }

    }

    public void loadFromXMLMasterFile(String masterResource, PathRepository pathRepository, ModelMapper modelMapper) {
        File masterFile = new File(masterResource);


        List<Path> existingPaths = new ArrayList<>();
        pathRepository.findAll().forEach(elem -> existingPaths.add(elem));

        if (existingPaths.size() < 1) {
            if (masterFile.exists() ) {
                logger.info("Processing master file: " + masterResource);
                NodeList nodeList = getPathXMLNodes(masterResource);
                List<PathDTO> generatedPathDTOs = new ArrayList<>();

                AuditDetailsDTO defaultAuditDetails = generateAuditDetails();

                if (nodeList != null && nodeList.getLength() > 0) {
                    PathDTO tempDTO = null;
                    for (int nodeIdx=0; nodeIdx < nodeList.getLength(); nodeIdx++)
                    {
                        tempDTO = processNode(nodeList.item(nodeIdx));
                        tempDTO.setAuditDetails(defaultAuditDetails);
                        generatedPathDTOs.add(tempDTO);
                    }
                }

                if (generatedPathDTOs.size() > 0) {
                    persistPathDTOs(generatedPathDTOs, pathRepository, modelMapper);
                }
            }
        } else {
            logger.info("Skipping DB load");
        }

    }

    private void persistPathDTOs(List<PathDTO> inputDTOs, PathRepository pathRepository, ModelMapper modelMapper) {

        if (modelMapper != null) {
            logger.info("Persisting DTOs");
            inputDTOs.forEach(pathDTO -> pathRepository.save(mapToPath(pathDTO,modelMapper)));
        }
    }

    private Path mapToPath(PathDTO pathDTO, ModelMapper modelMapper) {
        Path path = modelMapper.map(pathDTO, Path.class);

        logger.info("Got path entity: " + path.toString());

        return path;
    }

    private AuditDetailsDTO generateAuditDetails() {
        AuditDetailsDTO auditDetails = new AuditDetailsDTO();
        auditDetails.setCreatedBy(SYSTEM_USER);
        auditDetails.setVersion(Integer.valueOf(1));
        auditDetails.setCreatedTs(new Timestamp(System.currentTimeMillis()));
        auditDetails.setIsActive(Boolean.TRUE);

        return auditDetails;
    }
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

    public NodeList getPathXMLNodes(String resource) {
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

    private String getMatchingValue(String pathId, String regex, int group) {
        String result = null;
        Pattern charPattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher charMatcher = charPattern.matcher(pathId);
        boolean matchFound = charMatcher.find();

        if(matchFound) {
            result = charMatcher.group(group);

        }

        return result;
    }

    private String getRawUnicode(String pathId) {

        String rawUnicode = getMatchingValue(pathId,REGEX_PATH_UNICODE_ID,1);

        if (rawUnicode.length() == 3) {

            if (rawUnicode.equals(KEY_FOR_033_UNICODE)) {
                // Process a special character; e.g. '!'
                // which will be represented as the id "exc_path_1"
                rawUnicode = "!";
            }

        }

        return rawUnicode;
    }

    private String safeNodeValueFetch(Node node, String namedItem) {
        String result = null;
        Node fetchedNode = node.getAttributes().getNamedItem(namedItem);
        if (fetchedNode != null) {
            result = fetchedNode.getNodeValue();
        }
        return result;
    }
    private PathDTO processNode(Node node) {
        PathDTO resultDTO = new PathDTO();
        String id = node.getAttributes().getNamedItem("id").getNodeValue();

        String rawUnicode = getRawUnicode(id);
        Integer numericUnicode = (int) rawUnicode.charAt(0);

        if (numericUnicode != null) {
            logger.info("Processing id: " + id + " as " + numericUnicode);

            resultDTO.setGlyphName(id);
            resultDTO.setPathData(node.getAttributes().getNamedItem("d").getNodeValue());
            resultDTO.setUnicode(numericUnicode);

            resultDTO.setIsMask(Boolean.FALSE);
            if (id.contains(MASK_ID)) {
                resultDTO.setIsMask(Boolean.TRUE);
                resultDTO.setPathStroke(safeNodeValueFetch(node, "stroke"));
                resultDTO.setPathStrokeFill(safeNodeValueFetch(node, "fill"));
                resultDTO.setPathStrokeWidth(safeNodeValueFetch(node, "stroke-width"));
            }
            String rawSeq = getMatchingValue(id,REGEX_PATH_SEQUENCE,1);

            if (rawSeq != null) {
                resultDTO.setSequence(Integer.valueOf(rawSeq));
            }

        }

        return resultDTO;
    }

    private void processResourceFile(String resourcePath, String fileName, PathRepository pathRepository) {
        try {
            logger.info("Reading from file: " + resourcePath + fileName);
            NodeList nodeList = getPathXMLNodes(resourcePath + fileName);
            Node first = nodeList.item(0);
            //logger.info("Got tagname: " + first.getNodeName());

            String id = first.getAttributes().getNamedItem("id").getNodeValue();
            logger.info("Got id: " + id);
            String dPath = first.getAttributes().getNamedItem("d").getNodeValue();
            //logger.info("Got d: " + dPath);

            char letterName = id.charAt(0);
            id.getBytes(StandardCharsets.UTF_8);

            if (pathRepository != null) {
                //logger.info("PathRepo is NOT NULL!");

                Pattern charPattern = Pattern.compile("(\\w+?)_", Pattern.CASE_INSENSITIVE);
                Matcher charMatcher = charPattern.matcher(fileName);
                boolean matchFound = charMatcher.find();

                if (matchFound) {
                    //logger.info("File name is correct format");
                    String rawUnicode = charMatcher.group(1);
                    Integer numericUnicode = Integer.valueOf(rawUnicode);
                    logger.info("Processing " + rawUnicode + " as " + (char)numericUnicode.intValue() );

                    AuditDetails auditDetails = new AuditDetails();
                    auditDetails.setCreatedBy(SYSTEM_USER);
                    auditDetails.setVersion(Integer.valueOf(1));
                    auditDetails.setCreatedTs(new Timestamp(System.currentTimeMillis()));
                    auditDetails.setIsActive(Boolean.TRUE);

                    Path testPath = new Path();
                    testPath.setGlyphName(id);
                    testPath.setPathData(dPath);
                    testPath.setAuditDetails(auditDetails);
                    testPath.setUnicode(Integer.valueOf(rawUnicode));

                    if (fileName.contains(MASK_ID)) {
                        testPath.setIsMask(Boolean.TRUE);
                    }

                    Pattern seqPattern = Pattern.compile("\\w+_\\w+_(\\w+)\\.svg", Pattern.CASE_INSENSITIVE);
                    Matcher seqMatcher = seqPattern.matcher(fileName);
                    boolean seqMatch = seqMatcher.find();

                    if (seqMatch) {
                        testPath.setSequence(Integer.valueOf(seqMatcher.group(1)));
                    }

                    logger.info("Saving data from file: " + resourcePath + fileName);
                    //pathRepository.save(testPath);
                    logger.info("Sleeping for 1s");
                    Thread.sleep(1000);
                }

            }

        } catch (InterruptedException inte) {
            logger.info("Exception sleeping Thread: " + inte.getMessage());

        }
    }
}
