package com.ec.svg.generator.app.model.domain;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class FontCharacter {
    private static final Logger logger = LoggerFactory.getLogger(FontCharacter.class);

    public static final Integer UNICODE_FOR_EXC_KEY = Integer.valueOf(33);
    public static final Integer UNICODE_FOR_SPACE = Integer.valueOf(32);
    public static final String KEY_FOR_033_UNICODE = "exc";

    public static final BigDecimal FONT_HEIGHT = new BigDecimal(322);
    public static final FontCharacter BLANK_SPACE = new FontCharacter(Integer.valueOf(32),
            new BigDecimal("00.00"),
            new BigDecimal("50.00"),
            new BigDecimal("00.00"),
            new BigDecimal("50.00"));
    @Getter
    protected final Integer unicodeKey;
    @Getter
    protected BigDecimal fontWidth;

    @Getter
    protected BigDecimal fontWidthMinX;

    @Getter
    protected BigDecimal fontWidthMaxX;

    @Getter
    protected BigDecimal fontBoundsMinX;

    @Getter
    protected BigDecimal fontBoundsMaxX;

    public FontCharacter(Integer unicodeKey) {
        this.unicodeKey = unicodeKey;
    }

    public FontCharacter(Integer unicodeKey, BigDecimal fontWidthMinX, BigDecimal fontWidthMaxX,
                         BigDecimal fontBoundsMinX, BigDecimal fontBoundsMaxX) {
        this.unicodeKey = unicodeKey;
        this.fontWidthMinX = fontWidthMinX;
        this.fontWidthMaxX = fontWidthMaxX;
        this.fontBoundsMinX = fontBoundsMinX;
        this.fontBoundsMaxX = fontBoundsMaxX;
        this.fontWidth = this.fontWidthMaxX.subtract(this.fontWidthMinX);

    }

    public static String unicodeKeyToChar(Integer unicodeKey, Boolean strict) {
        String result = String.valueOf((char)unicodeKey.intValue());

        if (!strict && unicodeKey.equals(UNICODE_FOR_EXC_KEY)) {
            result = KEY_FOR_033_UNICODE;
        }

        return result;
    }

    public static String unicodeKeyToChar(Integer unicodeKey) {
        return unicodeKeyToChar(unicodeKey,Boolean.FALSE);
    }

    public static Boolean isWhitespace(Integer unicodeKey) {
        Boolean isWhitespace = Boolean.FALSE;

        if(unicodeKey.equals(UNICODE_FOR_SPACE))
        {
            isWhitespace = Boolean.TRUE;
        }

        return isWhitespace;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof FontCharacter) {
            FontCharacter target = (FontCharacter) obj;
            logger.info("EQUALS: Comparing " + this.unicodeKey + " with " + target.unicodeKey);
            if (this.unicodeKey.equals(target.unicodeKey)) {
                isEqual = true;
            }
        }

        return isEqual;
    }

}
