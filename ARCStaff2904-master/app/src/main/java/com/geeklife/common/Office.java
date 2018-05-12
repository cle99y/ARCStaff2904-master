package com.geeklife.common;

import java.util.HashMap;
import java.util.Map;

public enum Office {
    BRISTOL( "BRI", "Bristol" ),
    EDINBURGH( "EDI", "Edinburgh" ),
    GLASGOW( "GLA", "Glasgow" ),
    LONDON( "LON", "London" ),
    MANCHESTER( "MAN", "Manchester" ),
    PERTH( "PER", "Perth" ),
    SINGAPORE( "SIN", "Singapore" ),
    SYDNEY( "SYD", "Sydney" ),
    TAUNTON( "TAU", "Taunton" );
    private String code;
    private String longName;

    // private constructor
    Office( String c, String l ) {
        this.code = c;
        this.longName = l;
    }

    // create lookup table for reverse searches
    private static final Map<String, Office> nameLookup = new HashMap<>();

    // populate lookup tables on loading
    static {
        for ( Office off : Office.values() ) {
            nameLookup.put( off.getLongName(), off );
        }
    }

    // do reverse search from longName to enum value
    public static Office getOfficeFromName (String name) {
        return nameLookup.get(name);
    }

    public String getCode() {
        return code;
    }

    public String getLongName() {
        return longName;
    }
}
