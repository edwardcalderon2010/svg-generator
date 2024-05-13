package com.ec.svg.generator.app.model.domain.text;

import com.ec.svg.generator.app.engine.SVGResource;
import com.ec.svg.generator.app.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TextBlock {

    public static final Integer MAX_LINE_CHAR_COUNT = 18;

    private static final Logger logger = LoggerFactory.getLogger(TextBlock.class);

    @Getter
    private List<String> textLines = new ArrayList<>();


    public void addWord(String inputWord) {
        int currentLineCount = textLines.size();


        if (currentLineCount > 0) {
            int totalLength = inputWord.length();

            String currentLine = textLines.get(currentLineCount - 1);
            if (StringUtils.hasText(currentLine)) {
                totalLength += (currentLine.length() + 1);

                if (totalLength > MAX_LINE_CHAR_COUNT) {
                    textLines.add(inputWord);
                } else {
                    textLines.set(currentLineCount - 1, currentLine + " " + inputWord);
                }
            }
        } else {
            textLines.add(inputWord);
        }

    }

    public int getLineCount(String partialLine) {
        int lineCount = -1;

        if (StringUtils.hasText(partialLine)) {

            for (String nextLine : textLines) {
                if (nextLine.startsWith(partialLine)) {
                    lineCount++;
                    break;
                } else {
                    if (partialLine.startsWith(nextLine)) {
                        lineCount++;
                        partialLine = partialLine.substring(nextLine.length());
                    }
                }
            }
        }

        return lineCount;
    }

}
