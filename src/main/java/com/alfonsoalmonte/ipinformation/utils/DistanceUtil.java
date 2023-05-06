package com.alfonsoalmonte.ipinformation.utils;

import lombok.Getter;

public class DistanceUtil {
    @Getter
    private static final double ARG_LATITUDE = -34.603722;
    @Getter
    private static final double ARG_LONGITUDE = -64.381592;

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;

        return distance;
    }

    public static double calculateDistanceFromArgentina(double latitude, double longitude) {
        return calculateDistance(ARG_LATITUDE, ARG_LONGITUDE, latitude, longitude);
    }
}
