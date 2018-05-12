package com.geeklife.common;

import java.util.HashMap;
import java.util.Map;

public enum Country {
    UK( "United Kingdom", "+44" ),
    AUSTRALIA( "Australia", "+61" ),
    SINGAPORE( "Singapore", "+51" );

    String longName, intCode;

    Country( String longName, String intCode ) {
        this.longName = longName;
        this.intCode = intCode;
    }

    // create lookup table for reverse searches
    private static final Map<String, Country> nameLookup = new HashMap<>();

    // populate lookup tables on loading
    static {
        for ( Country country : Country.values() ) {
            nameLookup.put( country.getLongName(), country );
        }
    }

    // do reverse search from longName to enum value
    public static Country getCountryFromName (String name) {
        return nameLookup.get(name);
    }

    public String getLongName() {
        return longName;
    }

    public String getIntCode() {
        return intCode;
    }


}
