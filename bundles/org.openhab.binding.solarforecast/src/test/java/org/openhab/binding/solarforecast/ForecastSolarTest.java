/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
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
package org.openhab.binding.solarforecast;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.measure.quantity.Energy;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.solarforecast.internal.ForecastObject;
import org.openhab.core.library.types.PointType;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.types.State;

/**
 * The {@link ForecastSolarTest} tests responses from forecast solar website
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
class ForecastSolarTest {
    public static final String DATE_INPUT_PATTERN_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter DATE_INPUT_PATTERN = DateTimeFormatter.ofPattern(DATE_INPUT_PATTERN_STRING);

    @Test
    void testForecastObject() {
        String content = FileReader.readFileInString("src/test/resources/forecastsolar/result.json");
        LocalDateTime now = LocalDateTime.of(2022, 7, 17, 16, 23);
        ForecastObject fo = new ForecastObject(content, now);
        assertEquals(49.431, fo.getActualValue(now), 0.001, "Current Production");
        assertEquals(14.152, fo.getRemainingProduction(now), 0.001, "Current Production");
        assertEquals(fo.getDayTotal(now, 0), fo.getActualValue(now) + fo.getRemainingProduction(now), 0.001,
                "Total production");
        assertEquals(fo.getDayTotal(now, 0), fo.getActualValue(now) + fo.getRemainingProduction(now), 0.001,
                "Total production");
    }

    @Test
    void testInterpolation() {
        String content = FileReader.readFileInString("src/test/resources/forecastsolar/result.json");
        LocalDateTime now = LocalDateTime.of(2022, 7, 17, 16, 0);
        ForecastObject fo = new ForecastObject(content, now);
        double previousValue = 0;
        for (int i = 0; i < 60; i++) {
            now = now.plusMinutes(1);
            assertTrue(previousValue < fo.getActualValue(now));
            previousValue = fo.getActualValue(now);
        }
    }

    @Test
    void testForecastSum() {
        String content = FileReader.readFileInString("src/test/resources/forecastsolar/result.json");
        LocalDateTime now = LocalDateTime.of(2022, 7, 17, 16, 23);
        ForecastObject fo = new ForecastObject(content, now);
        QuantityType<Energy> actual = QuantityType.valueOf(0, Units.KILOWATT_HOUR);
        State st = ForecastObject.getStateObject(fo.getActualValue(now));
        assertTrue(st instanceof QuantityType);
        actual = actual.add((QuantityType<Energy>) st);
        assertEquals(49.431, actual.floatValue(), 0.001, "Current Production");
        actual = actual.add((QuantityType<Energy>) st);
        assertEquals(98.862, actual.floatValue(), 0.001, "Doubled Current Production");
    }

    @Test
    void testErrorCases() {
        ForecastObject fo = new ForecastObject();
        assertFalse(fo.isValid());
        LocalDateTime now = LocalDateTime.of(2022, 7, 17, 16, 23);
        assertEquals(-1.0, fo.getActualValue(now), 0.001, "Actual Production");
        assertEquals(-1.0, fo.getDayTotal(now, 0), 0.001, "Today Production");
        assertEquals(-1.0, fo.getRemainingProduction(now), 0.001, "Remaining Production");
    }

    @Test
    void testLocation() {
        PointType pt = PointType.valueOf("1.234,9.876");
        System.out.println(pt);
    }
}
