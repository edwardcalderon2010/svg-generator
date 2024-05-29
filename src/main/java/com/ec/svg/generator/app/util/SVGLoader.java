package com.ec.svg.generator.app.util;

import com.ec.svg.generator.app.config.SVGResourceProperties;
import com.ec.svg.generator.app.dto.AuditDetailsDTO;
import com.ec.svg.generator.app.dto.PathDTO;
import com.ec.svg.generator.app.model.domain.enums.SymbolicUnicodeChar;
import com.ec.svg.generator.app.model.entity.Path;
import com.ec.svg.generator.app.model.entity.PathRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ec.svg.generator.app.util.SAXHelper.getPathXMLNodes;
import static com.ec.svg.generator.app.util.StringUtils.REGEX_PATH_SEQUENCE;
import static com.ec.svg.generator.app.util.StringUtils.REGEX_PATH_UNICODE_ID;

@Component
public class SVGLoader {

    private static final Logger logger = LoggerFactory.getLogger(SVGLoader.class);

    public static final String SYSTEM_USER = "system";
    public static final String MASK_ID = "mask";

    private SVGResourceProperties svgResourceProperties;

    private ModelMapper modelMapper;

    private PathRepository pathRepository;

    private final Resource masterSvgXMLDefinitions;

    public SVGLoader(SVGResourceProperties svgResourceProperties,
                     PathRepository pathRepository,
                    @Value("${resources.masterSvgDefinitions}") Resource svgDefinitions) {
        this.svgResourceProperties = svgResourceProperties;
        this.modelMapper = new ModelMapper();
        this.pathRepository = pathRepository;
        this.masterSvgXMLDefinitions = svgDefinitions;
    }

    public void load() {

        logger.info("### Attempting to load master XML resource from CP: " + masterSvgXMLDefinitions.getFilename());

        String targetFilePath = "";

        if (masterSvgXMLDefinitions.exists() ) {
            logger.info("### Processing master file: " + masterSvgXMLDefinitions.getFilename());

            NodeList nodeList = null;
            try {
                nodeList = getPathXMLNodes(masterSvgXMLDefinitions.getInputStream());

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
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
                persistPathDTOs(generatedPathDTOs);
            }
        }
    }

    private PathDTO processNode(Node node) {
        PathDTO resultDTO = new PathDTO();
        String id = node.getAttributes().getNamedItem("id").getNodeValue();

        String rawUnicode = getRawUnicode(id);
        Integer numericUnicode = (int) rawUnicode.charAt(0);

        if (numericUnicode != null) {
            //logger.info("Processing id: " + id + " as " + numericUnicode);

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

    private void persistPathDTOs(List<PathDTO> inputDTOs) {

        if (modelMapper != null) {

            // Having processed the list of paths from the input XML file,
            // only persist those paths that don't already exist.
            List<PathDTO> targetDTOs = inputDTOs.stream()
                    .filter(elem -> pathRepository.findByUnicode(elem.getUnicode()).isEmpty())
                    .sorted(Comparator.comparing(PathDTO::getUnicode))
                    .toList();
            targetDTOs.forEach(elem -> logger.info("Persisting DTO: " + elem.getUnicode() + " " + elem.getGlyphName()));
            targetDTOs.forEach(pathDTO -> pathRepository.save(mapToPath(pathDTO,modelMapper)));
        }
    }

    private Path mapToPath(PathDTO pathDTO, ModelMapper modelMapper) {
        Path path = modelMapper.map(pathDTO, Path.class);
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

            rawUnicode = SymbolicUnicodeChar.getSymbolAsString(rawUnicode);

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

}
