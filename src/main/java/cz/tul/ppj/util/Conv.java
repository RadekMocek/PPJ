package cz.tul.ppj.util;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public final class Conv {

    private Conv() {
    }

    public static long iso8601ToTimestampUTCMidday(String datestamp) {
        try {
            return Instant.parse(datestamp + "T12:00:00Z").getEpochSecond();
        } catch (DateTimeParseException e) {
            return -1;
        }
    }

}
