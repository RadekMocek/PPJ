package cz.tul.ppj.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public final class Conv {

    private Conv() {
    }

    public static float kelvinToCelsius(float k) {
        return k - 273.15f;
    }

    public static long iso8601ToTimestampUTCMidday(String datestamp) {
        try {
            return Instant.parse(datestamp + "T12:00:00Z").getEpochSecond();
        } catch (DateTimeParseException e) {
            return -1;
        }
    }

    public static String[] separateCityNameCommaStateId(String cityNameAndStateId) {
        if (cityNameAndStateId.contains(",")) {
            int idx = cityNameAndStateId.lastIndexOf(",");
            return new String[]{cityNameAndStateId.substring(0, idx), cityNameAndStateId.substring(idx + 1)};
        }
        return null;
    }

}
