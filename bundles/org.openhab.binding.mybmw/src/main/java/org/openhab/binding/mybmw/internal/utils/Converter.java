/**
 * Copyright (c) 2010-2021 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mybmw.internal.utils;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.TimeZone;

import javax.measure.quantity.Length;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.mybmw.internal.dto.charge.Time;
import org.openhab.binding.mybmw.internal.dto.properties.Coordinates;
import org.openhab.binding.mybmw.internal.dto.properties.Distance;
import org.openhab.binding.mybmw.internal.dto.properties.Location;
import org.openhab.binding.mybmw.internal.dto.properties.Range;
import org.openhab.binding.mybmw.internal.dto.status.Mileage;
import org.openhab.binding.mybmw.internal.dto.vehicle.Vehicle;
import org.openhab.core.i18n.TimeZoneProvider;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.library.unit.ImperialUnits;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * The {@link Converter} Conversion Helpers
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class Converter {
    public static final Logger LOGGER = LoggerFactory.getLogger(Converter.class);

    public static final DateTimeFormatter SERVICE_DATE_INPUT_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter SERVICE_DATE_OUTPUT_PATTERN = DateTimeFormatter.ofPattern("MMM yyyy");

    public static final String LOCAL_DATE_INPUT_PATTERN_STRING = "dd.MM.yyyy HH:mm";
    public static final DateTimeFormatter LOCAL_DATE_INPUT_PATTERN = DateTimeFormatter
            .ofPattern(LOCAL_DATE_INPUT_PATTERN_STRING);

    public static final String DATE_INPUT_PATTERN_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter DATE_INPUT_PATTERN = DateTimeFormatter.ofPattern(DATE_INPUT_PATTERN_STRING);

    public static final String DATE_INPUT_ZONE_PATTERN_STRING = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final DateTimeFormatter DATE_INPUT_ZONE_PATTERN = DateTimeFormatter
            .ofPattern(DATE_INPUT_ZONE_PATTERN_STRING);

    public static final DateTimeFormatter DATE_OUTPUT_PATTERN = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static final SimpleDateFormat ISO_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    public static final double MILES_TO_KM_RATIO = 1.60934;

    private static final Gson GSON = new Gson();
    private static final Vehicle INVALID_VEHICLE = new Vehicle();
    private static final double SCALE = 10;
    private static final String SPLIT_HYPHEN = "-";
    private static final String SPLIT_BRACKET = "\\(";

    public static Optional<TimeZoneProvider> timeZoneProvider = Optional.empty();
    // https://www.baeldung.com/gson-list
    public static final Type VEHICLE_LIST_TYPE = new TypeToken<ArrayList<Vehicle>>() {
    }.getType();
    public static int offsetMinutes = -1;

    public static double round(double value) {
        return Math.round(value * SCALE) / SCALE;
    }

    public static String getLocalDateTimeWithoutOffest(@Nullable String input) {
        if (input == null) {
            return Constants.NULL_DATE;
        }
        LocalDateTime ldt;
        if (input.contains(Constants.PLUS)) {
            ldt = LocalDateTime.parse(input, Converter.DATE_INPUT_ZONE_PATTERN);
        } else {
            ldt = LocalDateTime.parse(input, Converter.DATE_INPUT_PATTERN);
        }
        return ldt.format(Converter.DATE_INPUT_PATTERN);
    }

    public static String getZonedDateTime(String input) {
        ZonedDateTime d = ZonedDateTime.parse(input);
        return d.format(Converter.DATE_INPUT_PATTERN);

        // if (input == null) {
        // return Constants.NULL_DATE;
        // }
        //
        // LocalDateTime ldt;
        // if (input.contains(Constants.PLUS)) {
        // ldt = LocalDateTime.parse(input, Converter.DATE_INPUT_ZONE_PATTERN);
        // } else {
        // try {
        // ldt = LocalDateTime.parse(input, Converter.DATE_INPUT_PATTERN);
        // } catch (DateTimeParseException dtpe) {
        // ldt = LocalDateTime.parse(input, Converter.LOCAL_DATE_INPUT_PATTERN);
        // }
        // }
        // ZonedDateTime zdtUTC = ldt.atZone(ZoneId.of("UTC"));
        // ZonedDateTime zdtLZ;
        // zdtLZ = zdtUTC.withZoneSameInstant(ZoneId.systemDefault());
        // if (timeZoneProvider.isPresent()) {
        // zdtLZ = zdtUTC.withZoneSameInstant(timeZoneProvider.get().getTimeZone());
        // } else {
        // zdtLZ = zdtUTC.withZoneSameInstant(ZoneId.systemDefault());
        // }
        // return zdtLZ.format(Converter.DATE_INPUT_PATTERN);
    }

    public static void setTimeZoneProvider(TimeZoneProvider tzp) {
        timeZoneProvider = Optional.of(tzp);
    }

    public static String toTitleCase(@Nullable String input) {
        if (input == null) {
            return toTitleCase(Constants.UNDEF);
        } else if (input.length() == 1) {
            return input;
        } else {
            String lower = input.replaceAll(Constants.UNDERLINE, Constants.SPACE).toLowerCase();
            String converted = toTitleCase(lower, Constants.SPACE);
            converted = toTitleCase(converted, SPLIT_HYPHEN);
            converted = toTitleCase(converted, SPLIT_BRACKET);
            return converted;
        }
    }

    private static String toTitleCase(String input, String splitter) {
        String[] arr = input.split(splitter);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) {
                sb.append(splitter.replaceAll("\\\\", Constants.EMPTY));
            }
            sb.append(Character.toUpperCase(arr[i].charAt(0))).append(arr[i].substring(1));
        }
        return sb.toString().trim();
    }

    public static String capitalizeFirst(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static Gson getGson() {
        return GSON;
    }

    /**
     * Measure distance between 2 coordinates
     *
     * @param sourceLatitude
     * @param sourceLongitude
     * @param destinationLatitude
     * @param destinationLongitude
     * @return distance
     */
    public static double measureDistance(double sourceLatitude, double sourceLongitude, double destinationLatitude,
            double destinationLongitude) {
        double earthRadius = 6378.137; // Radius of earth in KM
        double dLat = destinationLatitude * Math.PI / 180 - sourceLatitude * Math.PI / 180;
        double dLon = destinationLongitude * Math.PI / 180 - sourceLongitude * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(sourceLatitude * Math.PI / 180)
                * Math.cos(destinationLatitude * Math.PI / 180) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    /**
     * Easy function but there's some measures behind:
     * Guessing the range of the Vehicle on Map. If you can drive x kilometers with your Vehicle it's not feasible to
     * project this x km Radius on Map. The roads to be taken are causing some overhead because they are not a straight
     * line from Location A to B.
     * I've taken some measurements to calculate the overhead factor based on Google Maps
     * Berlin - Dresden: Road Distance: 193 air-line Distance 167 = Factor 87%
     * Kassel - Frankfurt: Road Distance: 199 air-line Distance 143 = Factor 72%
     * After measuring more distances you'll find out that the outcome is between 70% and 90%. So
     *
     * This depends also on the roads of a concrete route but this is only a guess without any Route Navigation behind
     *
     * In future it's foreseen to replace this with BMW RangeMap Service which isn't running at the moment.
     *
     * @param range
     * @return mapping from air-line distance to "real road" distance
     */
    public static double guessRangeRadius(double range) {
        return range * 0.8;
    }

    public static State getMiles(QuantityType<Length> qtLength) {
        if (qtLength.intValue() == -1) {
            return UnDefType.UNDEF;
        }
        QuantityType<Length> qt = qtLength.toUnit(ImperialUnits.MILE);
        if (qt != null) {
            return qt;
        } else {
            LOGGER.debug("Cannot convert {} to miles", qt);
            return UnDefType.UNDEF;
        }
    }

    public static int getIndex(String fullString) {
        int index = -1;
        try {
            index = Integer.parseInt(fullString);
        } catch (NumberFormatException nfe) {
        }
        return index;
    }

    /**
     * Returns list of found vehicles
     * In case of errors return empty list
     *
     * @param json
     * @return
     */
    public static List<Vehicle> getVehicleList(String json) {
        try {
            List<Vehicle> l = GSON.fromJson(json, VEHICLE_LIST_TYPE);
            if (l != null) {
                return l;
            } else {
                return new ArrayList<Vehicle>();
            }
        } catch (JsonSyntaxException e) {
            LOGGER.warn("JsonSyntaxException {}", e.getMessage());
            return new ArrayList<Vehicle>();
        }
    }

    public static Vehicle getVehicle(String vin, String json) {
        List<Vehicle> l = getVehicleList(json);
        for (Vehicle vehicle : l) {
            if (vin.equals(vehicle.vin)) {
                // declare vehicle as valid
                vehicle.valid = true;
                return getConsistentVehcile(vehicle);
            }
        }
        return INVALID_VEHICLE;
    }

    /**
     * ensure basic data like mileage and location data are in every time
     *
     * @param v
     * @return
     */
    public static Vehicle getConsistentVehcile(Vehicle v) {
        if (v.status.currentMileage == null) {
            v.status.currentMileage = new Mileage();
            v.status.currentMileage.mileage = -1;
            v.status.currentMileage.units = "km";
        }
        if (v.properties.combustionRange == null) {
            v.properties.combustionRange = new Range();
            v.properties.combustionRange.distance = new Distance();
            v.properties.combustionRange.distance.value = -1;
            v.properties.combustionRange.distance.units = Constants.KILOMETERS_JSON;
        }
        if (v.properties.vehicleLocation == null) {
            v.properties.vehicleLocation = new Location();
            v.properties.vehicleLocation.heading = -1;
            v.properties.vehicleLocation.coordinates = new Coordinates();
            v.properties.vehicleLocation.coordinates.latitude = -1.234;
            v.properties.vehicleLocation.coordinates.longitude = -9.876;
        }
        return v;
    }

    public static String getRandomString(int size) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1).limit(size)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

        return generatedString;
    }

    public static State getLockState(boolean lock) {
        if (lock) {
            return StringType.valueOf(Constants.LOCKED);
        } else {
            return StringType.valueOf(Constants.UNLOCKED);
        }
    }

    public static State getClosedState(boolean close) {
        if (close) {
            return StringType.valueOf(Constants.CLOSED);
        } else {
            return StringType.valueOf(Constants.OPEN);
        }
    }

    public static State getConnectionState(boolean connected) {
        if (connected) {
            return StringType.valueOf(Constants.CONNECTED);
        } else {
            return StringType.valueOf(Constants.UNCONNECTED);
        }
    }

    public static String getCurrentISOTime() {
        Date date = new Date(System.currentTimeMillis());
        synchronized (ISO_FORMATTER) {
            ISO_FORMATTER.setTimeZone(TimeZone.getTimeZone("UTC"));
            return ISO_FORMATTER.format(date);
        }
    }

    public static String getTime(Time t) {
        StringBuffer time = new StringBuffer();
        if (t.hour < 10) {
            time.append("0");
        }
        time.append(Integer.toString(t.hour)).append(":");
        if (t.minute < 10) {
            time.append("0");
        }
        time.append(Integer.toString(t.minute));
        return time.toString();
    }

    public static int getOffsetMinutes() {
        if (offsetMinutes == -1) {
            ZoneOffset zo = ZonedDateTime.now().getOffset();
            offsetMinutes = zo.getTotalSeconds() / 60;
        }
        return offsetMinutes;
    }

    public static String getAnonymousFingerprint(List<Vehicle> vehicleList) {
        for (Vehicle vehicle : vehicleList) {
            vehicle.vin = Constants.ANONYMOUS;
            if (vehicle.properties.vehicleLocation != null) {
                if (vehicle.properties.vehicleLocation.address != null) {
                    vehicle.properties.vehicleLocation.address.formatted = Constants.ANONYMOUS;
                }
                if (vehicle.properties.vehicleLocation.coordinates != null) {
                    vehicle.properties.vehicleLocation.coordinates.latitude = 1.234;
                    vehicle.properties.vehicleLocation.coordinates.longitude = 9.876;
                }
            }
        }
        return Converter.getGson().toJson(vehicleList);
    }
}