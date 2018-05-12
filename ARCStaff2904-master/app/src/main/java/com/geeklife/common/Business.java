package com.geeklife.common;

import java.util.HashMap;
import java.util.Map;

public enum Business {
    DEFENCE( "DBU", "Defence" ),
    NUCLEAR( "NBU", "Nuclear" ),
    OILGAS( "OBU", "Oil & Gas" ),
    RAIL( "RBU", "Rail" ),
    OPERATIONS( "OPS", "Operations" ),
    RESOURCE( "RMG", "Resource Management" );
    private final String buCode;
    private final String longName;


    Business( String c, String l ) {
        this.buCode = c;
        this.longName = l;
    }

    // create lookup table for reverse searches
    private static final Map<String, Business> nameLookup = new HashMap<>();

    // populate lookup tables on loading
    static {
        for ( Business bus : Business.values() ) {
            nameLookup.put( bus.getLongName(), bus );
                    }
    }

    // do reverse search from longName to enum value
    public static Business getBusFromName (String name) {
        return nameLookup.get(name);
    }


    public String getBuCode() {
        return buCode;
    }

    public String getLongName() {
        return longName;
    }
}