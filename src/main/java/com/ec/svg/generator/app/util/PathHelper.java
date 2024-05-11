package com.ec.svg.generator.app.util;

import com.ec.svg.generator.app.interfaces.SVGElement;
import com.ec.svg.generator.app.model.domain.Letter;
import com.ec.svg.generator.app.model.domain.SVGPathD;
import com.ec.svg.generator.app.model.domain.enums.AttributeType;
import com.ec.svg.generator.app.model.domain.enums.AxisPlane;
import com.ec.svg.generator.app.model.domain.enums.MathBound;
import com.ec.svg.generator.app.model.domain.enums.TagName;
import com.ec.svg.generator.app.model.domain.path.*;
import com.ec.svg.generator.app.model.domain.tags.PathTag;
import com.ec.svg.generator.app.model.domain.SVGPathSection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ec.svg.generator.app.model.domain.enums.CommandType.M;
import static com.ec.svg.generator.app.model.domain.enums.CommandType.C;
import static com.ec.svg.generator.app.model.domain.enums.CommandType.Z;

public class PathHelper {
    private static final Logger logger = LoggerFactory.getLogger(PathHelper.class);

    public static final String FONT_MAX_X_REFERENCE_HEIGHT = "140.00";
    public static final String FONT_MIN_X_REFERENCE_HEIGHT = "137.00";

    public static String G_PATH_1 = "M 140.53,139.05 " +
            "C 141.14,136.90 142.18,135.21 143.62,133.98 " +
            "145.08,132.76 146.69,131.84 148.45,131.23 " +
            "150.22,130.63 151.95,130.28 153.64,130.17 " +
            "155.33,130.06 156.63,130.00 157.55,130.00 " +
            "155.55,142.62 152.91,155.59 149.61,168.91 " +
            "146.32,182.22 142.29,195.26 137.53,208.03 " +
            "132.78,220.80 127.38,233.15 121.31,245.08 " +
            "115.26,257.02 108.48,267.91 100.97,277.75 " +
            "88.09,294.68 74.37,306.68 59.80,313.77 " +
            "51.21,317.92 43.01,320.00 35.19,320.00 " +
            "28.91,320.00 23.01,318.61 17.48,315.84 " +
            "10.28,312.46 5.68,307.61 3.69,301.31 " +
            "1.69,295.93 1.84,289.47 4.14,281.94 " +
            "5.52,277.78 7.51,273.47 10.12,269.02 " +
            "12.73,264.55 15.91,260.13 19.67,255.75 " +
            "23.42,251.36 27.64,247.02 32.31,242.72 " +
            "36.99,238.41 42.01,234.41 47.38,230.72 " +
            "52.75,227.03 58.12,223.72 63.48,220.80 " +
            "68.85,217.88 74.14,215.58 79.34,213.89 " +
            "90.70,209.73 100.97,208.50 110.17,210.19 " +
            "111.09,210.19 111.39,210.57 111.09,211.34 " +
            "111.09,212.43 110.71,212.73 109.94,212.27 " +
            "101.66,210.88 92.15,211.96 81.42,215.50 " +
            "70.68,219.04 59.80,224.73 48.77,232.56 " +
            "38.18,239.95 29.14,248.10 21.62,257.02 " +
            "14.11,265.94 9.04,274.48 6.44,282.62 " +
            "3.68,291.24 3.56,297.89 6.09,302.58 " +
            "8.62,307.27 12.54,310.31 17.83,311.69 " +
            "23.12,313.07 29.18,313.04 36.00,311.58 " +
            "42.82,310.12 49.07,307.39 54.73,303.39 " +
            "65.62,296.00 75.90,285.93 85.56,273.17 " +
            "92.46,264.25 98.55,254.68 103.84,244.45 " +
            "109.13,234.22 113.81,223.84 117.88,213.30 " +
            "121.94,202.77 125.50,192.31 128.56,181.94 " +
            "131.63,171.55 134.39,161.82 136.84,152.75 " +
            "135.62,154.75 134.36,156.87 133.05,159.09 " +
            "131.74,161.32 130.25,163.67 128.56,166.12 " +
            "126.27,169.51 123.97,172.48 121.67,175.02 " +
            "119.37,177.55 117.22,179.74 115.23,181.59 " +
            "111.70,184.82 108.25,186.59 104.88,186.91 " +
            "101.50,187.21 98.51,186.48 95.91,184.70 " +
            "93.30,182.93 91.12,180.28 89.36,176.75 " +
            "87.59,173.21 86.55,169.21 86.25,164.75 " +
            "85.95,159.82 86.49,154.48 87.88,148.72 " +
            "89.25,142.95 91.31,137.37 94.06,131.98 " +
            "96.06,128.14 98.33,124.53 100.86,121.16 " +
            "103.39,117.77 106.15,114.85 109.14,112.39 " +
            "112.13,109.92 115.35,108.04 118.80,106.73 " +
            "122.24,105.43 125.80,104.86 129.48,105.00 " +
            "132.55,105.16 135.16,106.09 137.31,107.80 " +
            "139.46,109.50 140.91,111.58 141.69,114.05 " +
            "142.14,115.76 142.33,117.58 142.25,119.52 " +
            "142.18,121.45 141.79,123.26 141.09,124.97 " +
            "140.41,126.67 139.33,128.06 137.88,129.14 " +
            "136.43,130.23 134.63,130.84 132.48,131.00 " +
            "131.25,131.00 130.25,130.57 129.48,129.70 " +
            "128.72,128.83 128.34,127.78 128.34,126.55 " +
            "128.34,125.32 128.72,124.28 129.48,123.44 " +
            "130.25,122.59 131.25,122.17 132.48,122.17 " +
            "133.70,122.17 134.73,122.59 135.58,123.44 " +
            "136.42,124.28 136.84,125.32 136.84,126.55 " +
            "136.84,126.55 136.62,126.78 136.62,126.78 " +
            "137.24,126.47 137.93,125.78 138.69,124.70 " +
            "140.07,122.40 140.53,119.87 140.06,117.11 " +
            "139.76,115.28 139.07,113.63 138.00,112.17 " +
            "136.93,110.71 135.32,109.84 133.17,109.53 " +
            "130.41,109.06 127.34,110.45 123.97,113.67 " +
            "120.59,116.89 117.30,121.16 114.08,126.48 " +
            "110.86,131.80 107.91,137.60 105.22,143.89 " +
            "102.54,150.17 100.51,156.11 99.12,161.70 " +
            "97.75,167.30 97.21,172.09 97.52,176.08 " +
            "97.83,180.07 99.36,182.45 102.12,183.20 " +
            "105.19,184.28 108.79,183.25 112.92,180.11 " +
            "117.07,176.96 121.59,171.87 126.50,164.81 " +
            "129.88,159.91 132.75,155.31 135.12,151.02 " +
            "137.50,146.71 138.91,144.11 139.38,143.19 " +
            "139.38,143.19 140.53,139.05 140.53,139.05 Z ";

    public static String G_PATH_2 = "M 139.17,211.86 " +
            "C 138.40,211.55 138.05,211.13 138.12,210.59 " +
            "138.21,210.06 138.71,209.80 139.62,209.80 " +
            "152.66,209.64 164.78,201.59 175.97,185.64 " +
            "179.19,181.20 182.25,176.37 185.17,171.16 " +
            "188.09,165.94 190.77,160.95 193.22,156.20 " +
            "195.68,151.45 197.86,147.19 199.78,143.44 " +
            "201.70,139.68 203.11,136.95 204.03,135.27 " +
            "204.33,134.65 204.83,134.54 205.52,134.92 " +
            "206.21,135.31 206.33,135.89 205.88,136.66 " +
            "204.03,139.56 202.11,143.05 200.11,147.11 " +
            "198.12,151.17 195.97,155.43 193.67,159.88 " +
            "191.38,164.32 188.89,168.89 186.20,173.56 " +
            "183.52,178.24 180.65,182.73 177.58,187.02 " +
            "172.37,194.23 166.70,200.16 160.56,204.84 " +
            "154.43,209.52 147.30,211.86 139.17,211.86 Z ";

    public static String G_MASK_1 = "M 83.50,139.50 " +
            "C 83.50,139.50 106.50,82.50 140.25,99.50 " +
            "149.75,107.00 142.25,127.00 132.00,128.50 " +
            "121.00,128.75 142.75,108.50 131.25,107.25 " +
            "103.00,107.00 77.50,175.25 100.75,184.50 " +
            "120.50,183.50 131.00,156.00 128.00,146.50 " +
            "132.50,124.00 158.75,115.00 146.50,146.50 " +
            "138.25,196.50 87.75,337.00 19.25,314.25 " +
            "-32.25,292.00 53.75,204.75 111.25,211.00 ";

    public static String G_MASK_2 = "M 138.50,210.75 " +
            "C 155.00,212.25 166.25,199.50 172.75,191.00 " +
            "180.25,185.75 199.75,146.00 205.00,136.00 ";
    public static String H_PATH_1 = "M 74.58,34.31 " +
            "C 75.65,38.76 75.23,44.90 73.31,52.72 " +
            "71.39,60.54 68.29,69.20 64.00,78.70 " +
            "60.32,86.84 56.33,94.20 52.03,100.80 " +
            "46.12,110.88 42.00,113.50 37.12,117.75 " +
            "37.12,117.75 25.50,127.62 21.38,131.62 " +
            "18.75,134.38 18.12,136.12 17.88,138.62 " +
            "21.50,132.50 35.50,121.75 39.12,119.25 " +
            "45.62,113.62 50.44,107.39 52.72,103.91 " +
            "57.48,96.93 61.93,88.84 66.06,79.62 " +
            "70.51,69.97 73.69,61.08 75.61,52.95 " +
            "77.52,44.83 77.95,38.39 76.88,33.62 " +
            "76.73,32.87 76.00,29.75 74.12,28.50 " +
            "72.75,28.62 73.50,30.38 73.50,30.38 " +
            "73.50,30.38 74.58,33.71 74.58,34.31 Z ";

    public static String H_PATH_2 = "M 75.73,29.69 " +
            "C 75.73,29.53 75.58,29.30 75.27,28.98 " +
            "75.27,28.98 75.27,28.77 75.27,28.77 " +
            "73.89,26.62 71.82,25.16 69.06,24.39 " +
            "63.23,23.01 56.10,31.44 47.67,49.69 " +
            "44.45,56.59 41.04,64.99 37.44,74.88 " +
            "33.83,84.76 30.04,95.99 26.05,108.56 " +
            "22.67,119.30 19.45,129.96 16.39,140.55 " +
            "13.33,151.12 10.65,160.59 8.34,168.95 " +
            "6.04,177.31 4.20,184.09 2.83,189.31 " +
            "1.45,194.52 0.83,197.12 0.98,197.12 " +
            "5.89,198.81 9.88,199.08 12.95,197.94 " +
            "16.02,196.78 18.16,195.28 19.38,193.44 " +
            "19.53,193.44 19.61,193.36 19.61,193.20 " +
            "19.61,192.75 19.69,192.37 19.84,192.06 " +
            "20.91,189.92 22.45,186.89 24.44,182.98 " +
            "26.44,179.07 28.66,174.85 31.11,170.33 " +
            "33.57,165.81 36.21,161.29 39.05,156.77 " +
            "41.88,152.23 44.75,148.21 47.67,144.69 " +
            "50.59,141.16 53.46,138.48 56.30,136.64 " +
            "59.13,134.80 61.70,134.26 64.00,135.02 " +
            "65.69,135.63 66.72,136.98 67.11,139.05 " +
            "67.49,141.12 67.49,143.65 67.11,146.64 " +
            "66.72,149.63 65.99,152.93 64.92,156.53 " +
            "63.85,160.14 62.70,163.86 61.47,167.69 " +
            "57.48,179.79 56.10,189.95 57.33,198.16 " +
            "58.55,206.35 62.77,210.99 69.98,212.06 " +
            "73.05,212.53 76.03,212.15 78.94,210.92 " +
            "81.85,209.69 84.61,208.04 87.22,205.97 " +
            "89.83,203.91 92.33,201.53 94.70,198.84 " +
            "97.08,196.16 99.34,193.51 101.48,190.91 " +
            "108.08,182.94 113.68,175.20 118.28,167.69 " +
            "122.88,160.17 127.48,152.50 132.08,144.69 " +
            "132.08,144.69 137.14,136.41 137.14,136.41 " +
            "137.60,135.64 137.52,135.10 136.91,134.80 " +
            "136.14,134.33 135.61,134.40 135.30,135.02 " +
            "134.54,136.40 133.69,137.79 132.77,139.17 " +
            "131.85,140.55 131.00,142.00 130.23,143.53 " +
            "125.48,151.36 120.69,159.06 115.86,166.66 " +
            "111.04,174.24 105.40,181.95 98.95,189.77 " +
            "93.59,196.51 88.76,201.37 84.47,204.36 " +
            "80.18,207.35 76.88,208.38 74.58,207.47 " +
            "71.82,206.24 70.63,202.48 71.02,196.20 " +
            "71.40,189.91 73.74,180.64 78.03,168.38 " +
            "79.56,164.09 80.71,159.83 81.47,155.61 " +
            "82.24,151.39 82.35,147.48 81.81,143.88 " +
            "81.28,140.27 79.98,137.28 77.91,134.91 " +
            "75.84,132.53 72.74,131.04 68.59,130.42 " +
            "63.69,129.81 59.01,131.27 54.56,134.80 " +
            "50.12,138.32 45.91,142.88 41.92,148.48 " +
            "37.93,154.08 34.25,160.09 30.88,166.53 " +
            "27.50,172.97 24.51,178.72 21.91,183.78 " +
            "22.83,180.41 23.87,176.27 25.02,171.36 " +
            "26.16,166.45 27.50,161.12 29.03,155.38 " +
            "30.57,149.62 32.19,143.61 33.88,137.33 " +
            "35.56,131.04 37.40,124.75 39.39,118.45 " +
            "39.39,118.45 40.56,114.78 40.56,114.78 " +
            "47.31,93.16 53.52,75.37 59.19,61.42 " +
            "64.86,47.46 69.93,37.65 74.38,31.98 " +
            "74.68,32.60 78.25,34.38 75.73,29.69 Z ";

    public static String H_MASK_1 = "M 17.88,137.38 " +
            "C 20.12,134.00 29.25,124.88 41.12,116.50 " +
            "68.62,85.50 81.49,42.00 73.38,28.50 ";

    public static String H_MASK_2 = "M 78.25,25.25 " +
            "C 62.00,16.75 35.25,104.50 35.25,104.50 " +
            "33.25,111.50 4.75,203.50 7.25,208.00 " +
            "27.25,156.75 52.50,127.88 65.75,126.38 " +
            "91.38,128.12 43.38,215.75 76.25,209.50 " +
            "85.62,209.88 104.00,190.88 136.75,134.88 ";

    public static String EXCLAMATION_GLYPH = "M 64.17,45.62 " +
            "C 62.94,47.62 61.25,51.58 59.11,57.48 " +
            "56.96,63.38 54.58,70.28 51.97,78.17 " +
            "49.36,86.07 46.64,94.39 43.81,103.12 " +
            "40.98,111.87 38.25,120.11 35.64,127.86 " +
            "33.04,135.60 30.74,142.31 28.75,147.98 " +
            "26.76,153.65 25.30,157.33 24.38,159.02 " +
            "23.91,159.02 23.15,158.95 22.08,158.80 " +
            "21.00,158.64 19.89,158.37 18.73,157.98 " +
            "17.59,157.60 16.59,157.10 15.75,156.48 " +
            "14.91,155.87 14.56,155.11 14.72,154.19 " +
            "14.72,154.19 47.16,41.72 47.16,41.72 " +
            "47.76,41.56 49.05,41.45 51.05,41.38 " +
            "53.05,41.30 55.08,41.38 57.16,41.61 " +
            "59.23,41.84 60.99,42.26 62.44,42.88 " +
            "63.89,43.49 64.47,44.41 64.17,45.62 Z " +
            "M 2.77,190.61 " +
            "C 3.52,188.13 4.98,186.08 7.12,184.45 " +
            "9.27,182.82 11.49,182.00 13.80,182.00 " +
            "16.25,182.00 18.02,182.82 19.09,184.45 " +
            "20.16,186.08 20.32,188.13 19.55,190.61 " +
            "18.79,192.95 17.33,194.93 15.17,196.56 " +
            "13.02,198.19 10.73,199.00 8.28,199.00 " +
            "5.98,199.00 4.29,198.19 3.22,196.56 " +
            "2.14,194.93 1.99,192.95 2.77,190.61 Z ";

    public static String EXCLAMATION_GLYPH_MASK = "M 56.50,39.00 C 2.50,206.25 9.50,192.50 12.25,200.00";

    public static BigDecimal getMinXReferencePointAtY(List<? extends SVGElement> svgElements) {

        Coordinate targetY = new Coordinate(new BigDecimal(FONT_MIN_X_REFERENCE_HEIGHT), AxisPlane.Y);
        ParameterContext minXParamContainingY = new ParameterContext(MathBound.MIN, AxisPlane.X, targetY);

        return getReferencePoint(minXParamContainingY,svgElements).getXValue();
    }

    public static BigDecimal getMaxXReferencePointAtY(List<? extends SVGElement> svgElements) {

        Coordinate targetY = new Coordinate(new BigDecimal(FONT_MAX_X_REFERENCE_HEIGHT), AxisPlane.Y);
        ParameterContext maxXParamContainingY = new ParameterContext(MathBound.MAX, AxisPlane.X, targetY);

        return getReferencePoint(maxXParamContainingY,svgElements).getXValue();
    }

    public static BigDecimal getMinXBounds(List<? extends SVGElement> svgElements) {
        ParameterContext minXParam = new ParameterContext(MathBound.MIN, AxisPlane.X);

        return getReferenceCurve(minXParam,svgElements);
    }

    public static BigDecimal getMaxXBounds(List<? extends SVGElement> svgElements) {
        ParameterContext maxXParam = new ParameterContext(MathBound.MAX, AxisPlane.X);

        return getReferenceCurve(maxXParam,svgElements);
    }

    public static BigDecimal getReferenceCurve(ParameterContext paramContext, List<? extends SVGElement> svgElements) {
        BigDecimal result = BigDecimal.ZERO;

        CubicCurve refCurve = svgElements.stream()
                .map(pathTag -> pathTag.getAttribute(AttributeType.d))
                .map(svgattr -> (SVGPathD)svgattr)
                .map(svgPathD -> svgPathD.getReferenceCurve(paramContext))
                .reduce((a,b) -> a.compare(paramContext,b))
                .orElse(null);

        if (refCurve != null) {
            result = refCurve.getReferenceValue(paramContext);
        }

        return result;
    }

    public static Point getReferencePoint(ParameterContext paramContext, List<? extends SVGElement> svgElements) {
        Point resultPoint  = svgElements
                .stream()
                .map(pathTag -> pathTag.getAttribute(AttributeType.d))
                .map(svgattr -> (SVGPathD)svgattr)
                .map(pathD -> pathD.getReferencePoint(paramContext))
                .filter(Objects::nonNull)
                .reduce((a,b) -> a.compareReduce(paramContext,b))
                .orElse(Point.ZERO);

        return resultPoint;
    }



    public static void initLetter() {
        List<String> letterPaths = new ArrayList<>();
        letterPaths.add(G_PATH_1);
        letterPaths.add(G_PATH_2);
        List<String> letterMasks = new ArrayList<>();
        letterMasks.add(G_MASK_1);
        letterMasks.add(G_MASK_2);

        Letter letterHLc = new Letter("g", letterPaths, letterMasks, new BigDecimal(476),new BigDecimal(0));
        letterHLc.addClassName(TagName.path, "letter_path");
        letterHLc.addClassName(TagName.mask, "strokeMask");


        logger.info(letterHLc.toString());

    }

    public static SVGPathD parseGlyphPath(String glyphPath) {
        SVGPathD svgPathD = new SVGPathD();
        SVGPathSection tempSvgPathSection = null;

        // 1. Split by M command
        List<String> mSplit = Arrays.stream(glyphPath.split(M.name())).toList();
        for (String tempPath : mSplit) {
            tempSvgPathSection = new SVGPathSection();
            if (tempPath.contains(C.name())) {
                //logger.info("Processing BezierCurve");
                String[] pathElems = tempPath.split(C.name());
                //logger.info("Element 0: >" + pathElems[0] + "<");
                //logger.info("Element 1: >" + pathElems[1] + "<");

                Command tempCmd = new Command(M);
                tempCmd.addParameter(parseXYCoord(pathElems[0]));
                tempSvgPathSection.addCommand(tempCmd);

                tempCmd = new Command(C);
                Arrays.stream(pathElems[1].trim().split(" ")).map(PathHelper::parseXYCoord).filter(Objects::nonNull).forEach(tempCmd::addParameter);
                tempSvgPathSection.addCommand(tempCmd);

            }

            if (tempPath.contains(Z.name())) {
                Command tempCmd = new Command(Z);
                tempSvgPathSection.addCommand(tempCmd);
            }

            if (tempSvgPathSection.getPathCommands() != null
                    && tempSvgPathSection.getPathCommands().size() > 0) {
                svgPathD.addPathSection(tempSvgPathSection);
                //logger.info("Updated path " + svgPathD.getValue());

            }
        }

        return svgPathD;
    }
    public static PathTag parseGlyphPath(String glyphPath, String id) {
        PathTag fontStroke = new PathTag();
        SVGPathD svgPathD = new SVGPathD();
        SVGPathSection tempSvgPathSection = null;

        fontStroke.setId(id);

//        if (StringUtils.hasText(className)) {
//            fontStroke.addClassName(className);
//        }

        // 1. Split by M command
        List<String> mSplit = Arrays.stream(glyphPath.split(M.name())).toList();
        for (String tempPath : mSplit) {
            tempSvgPathSection = new SVGPathSection();
            if (tempPath.contains(C.name())) {
                //logger.info("Processing BezierCurve");
                String[] pathElems = tempPath.split(C.name());
                //logger.info("Element 0: >" + pathElems[0] + "<");
                //logger.info("Element 1: >" + pathElems[1] + "<");

                Command tempCmd = new Command(M);
                tempCmd.addParameter(parseXYCoord(pathElems[0]));
                tempSvgPathSection.addCommand(tempCmd);

                tempCmd = new Command(C);
                Arrays.stream(pathElems[1].trim().split(" ")).map(PathHelper::parseXYCoord).filter(Objects::nonNull).forEach(tempCmd::addParameter);
                tempSvgPathSection.addCommand(tempCmd);

            }

            if (tempPath.contains(Z.name())) {
                Command tempCmd = new Command(Z);
                tempSvgPathSection.addCommand(tempCmd);
            }

            if (tempSvgPathSection.getPathCommands() != null
                    && tempSvgPathSection.getPathCommands().size() > 0) {
                svgPathD.addPathSection(tempSvgPathSection);
                //logger.info("Updated path " + svgPathD.getValue());

            }
        }
        fontStroke.addAttribute(svgPathD);
        //svgPathD.getMaxXCoordSurroundingYCoord(new BigDecimal(135));
        //logger.info(fontStroke.render());

        return fontStroke;
    }

    public static Point parseXYCoord(String xyString) {
        //logger.info("Parsing xyCoord: " + xyString);
        Point result = null;
        if (xyString != null && xyString.contains(",")) {
            result = new Point(new BigDecimal(xyString.split(",")[0].trim()),
                    new BigDecimal(xyString.split(",")[1].trim()));
            //logger.info("Parsed: " + result.toString());
        }
        return result;
    }
}
