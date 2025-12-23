package com.energy.smsplus.util;

public class LocationDesensitizationUtil {
    public static String desensitize(String location) {
        return location.substring(0, 2) + "*" + location.substring(3);
    }
}
