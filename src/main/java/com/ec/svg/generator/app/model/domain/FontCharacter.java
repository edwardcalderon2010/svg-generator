package com.ec.svg.generator.app.model.domain;

import com.ec.svg.generator.app.model.domain.enums.SymbolicUnicodeChar;
import com.ec.svg.generator.app.util.StringUtils;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import static com.ec.svg.generator.app.model.domain.enums.SymbolicUnicodeChar.*;

public class FontCharacter {
    private static final Logger logger = LoggerFactory.getLogger(FontCharacter.class);

    public static final Integer UNICODE_FOR_A_CAP_CHAR_KEY = Integer.valueOf(65);

    public static final Integer UNICODE_FOR_Z_CAP_CHAR_KEY = Integer.valueOf(90);

    public static final Integer UNICODE_FOR_A_CHAR_KEY = Integer.valueOf(97);

    public static final Integer UNICODE_FOR_Z_CHAR_KEY = Integer.valueOf(122);

    public static final Integer UNICODE_FOR_SPACE = Integer.valueOf(32);

    public static final BigDecimal FONT_HEIGHT = new BigDecimal(322);

    public static final String FONT_MAX_X_REFERENCE_HEIGHT = "140.00";

    public static final String FONT_MIN_X_REFERENCE_HEIGHT = "137.00";

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

    public static Boolean isUpperCaseCharacter(Integer unicodeKey) {
        Boolean isUpper = Boolean.FALSE;
        if (unicodeKey >= FontCharacter.UNICODE_FOR_A_CAP_CHAR_KEY
                && unicodeKey <= FontCharacter.UNICODE_FOR_Z_CAP_CHAR_KEY) {
            isUpper = Boolean.TRUE;
        }

        return isUpper;
    }

    public static Boolean isSymbolicCharacter(Integer unicodeKey) {

        Boolean isSymbol = Boolean.FALSE;

        if ((exc.getUnicodeKey() <= unicodeKey && unicodeKey <= fsl.getUnicodeKey()) ||
                (col.getUnicodeKey() <= unicodeKey && unicodeKey <= adr.getUnicodeKey()) ||
                (lsb.getUnicodeKey() <= unicodeKey && unicodeKey <= btk.getUnicodeKey()) ||
                (lcb.getUnicodeKey() <= unicodeKey && unicodeKey <= tld.getUnicodeKey()) ) {
            isSymbol = Boolean.TRUE;
        }

        return isSymbol;
    }

    public static String unicodeKeyToChar(Integer unicodeKey, Boolean strict) {
        String result = String.valueOf((char)unicodeKey.intValue());

        if (!strict) {
            String symbolChar = SymbolicUnicodeChar.getNameFromSymbol(result);
            if (StringUtils.hasText(symbolChar)) {
                result = symbolChar;
            }
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
            if (this.unicodeKey.equals(target.unicodeKey)) {
                isEqual = true;
            }
        }

        return isEqual;
    }

}
