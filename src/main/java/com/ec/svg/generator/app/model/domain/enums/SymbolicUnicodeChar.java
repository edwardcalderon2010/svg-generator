package com.ec.svg.generator.app.model.domain.enums;

import lombok.Getter;

import java.util.EnumSet;

public enum SymbolicUnicodeChar {
    exc(33),
    com(44),
    que(63),
    stp(46),
    apo(39),
    amp(38),
    fsl(47),
    col(58),
    adr(64),
    lsb(91),
    btk(96),
    lcb(123),
    tld(126);

    @Getter
    private int unicodeKey;

    SymbolicUnicodeChar(int unicodeKey) {
        this.unicodeKey = unicodeKey;
    }

    public String getSymbol() {
        return String.valueOf((char)unicodeKey);
    }

    public static String getSymbolAsString(String symbolName) {
        String result = String.valueOf((char) Enum.valueOf(SymbolicUnicodeChar.class, symbolName).getUnicodeKey());

        return result;
    }

    public static String getNameFromSymbol(String unicodeString) {
        String result = null;
        SymbolicUnicodeChar symbolicChar = EnumSet.allOf(SymbolicUnicodeChar.class)
                .stream()
                .filter(elem -> unicodeString.equals(elem.getSymbol()))
                .findFirst()
                .orElse(null);

        if (symbolicChar != null) {
            result = symbolicChar.name();
        }

        return result;
    }

}
