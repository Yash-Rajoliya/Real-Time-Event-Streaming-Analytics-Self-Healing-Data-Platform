package com.platform.analytics.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Thread-safe utility methods for date, time, and timestamp manipulations across event processing pipelines.
 */
public final class DateUtils {

    public static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final DateTimeFormatter UTC_FORMATTER = DateTimeFormatter.ofPattern(ISO_8601_PATTERN)
            .withZone(ZoneOffset.UTC);

    private DateUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Converts epoch milliseconds to ISO-8601 UTC formatted string.
     */
    public static String formatEpochMillisToIso(long epochMillis) {
        return UTC_FORMATTER.format(Instant.ofEpochMilli(epochMillis));
    }

    /**
     * Parses an ISO-8601 formatted timestamp string to epoch milliseconds.
     */
    public static long parseIsoToEpochMillis(String isoTimestamp) {
        Objects.requireNonNull(isoTimestamp, "Timestamp string cannot be null");
        return Instant.parse(isoTimestamp).toEpochMilli();
    }

    /**
     * Obtains the current UTC timestamp in milliseconds.
     */
    public static long currentEpochMillis() {
        return System.currentTimeMillis();
    }

    /**
     * Truncates an epoch millisecond timestamp to the start of a given time unit (e.g., minute windowing).
     */
    public static long truncateTo(long epochMillis, ChronoUnit unit) {
        return Instant.ofEpochMilli(epochMillis)
                .truncatedTo(unit)
                .toEpochMilli();
    }

    /**
     * Converts epoch milliseconds to ZonedDateTime in a target time zone.
     */
    public static ZonedDateTime toZonedDateTime(long epochMillis, String zoneId) {
        return Instant.ofEpochMilli(epochMillis).atZone(ZoneId.of(zoneId));
    }

    /**
     * Checks if a given timestamp falls within a specific time window relative to now.
     */
    public static boolean isWithinWindow(long timestampMs, long windowDurationMs) {
        long now = System.currentTimeMillis();
        return Math.abs(now - timestampMs) <= windowDurationMs;
    }
}