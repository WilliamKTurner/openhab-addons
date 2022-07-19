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
package org.openhab.binding.solarforecast.internal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.JSONObject;
import org.openhab.core.library.types.QuantityType;
import org.openhab.core.library.unit.Units;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

/**
 * The {@link ForecastObject} holds complete data for forecast
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ForecastObject {
    private static final double UNDEF = -1;
    private final TreeMap<LocalDateTime, Double> dataMap = new TreeMap<LocalDateTime, Double>();
    private Optional<String> rawData = Optional.empty();
    private boolean valid = false;
    private int constructionHour;

    public ForecastObject() {
    }

    public ForecastObject(String content, LocalDateTime now) {
        constructionHour = now.getHour();
        if (!content.equals(SolarForecastBindingConstants.EMPTY)) {
            rawData = Optional.of(content);
            JSONObject contentJson = new JSONObject(content);
            JSONObject resultJson = contentJson.getJSONObject("result");
            JSONObject wattsJson = resultJson.getJSONObject("watt_hours");
            Iterator<String> iter = wattsJson.keys();
            // put all values of the current day into sorted tree map
            while (iter.hasNext()) {
                String dateStr = iter.next();
                // convert date time into machine readable format
                LocalDateTime ldt = LocalDateTime.parse(dateStr.replace(" ", "T"));
                if (ldt.getDayOfMonth() == now.getDayOfMonth()) {
                    dataMap.put(ldt, wattsJson.getDouble(dateStr));
                }
            }
            valid = true;
        }
    }

    public boolean isValid() {
        return valid && constructionHour == LocalDateTime.now().getHour() && !dataMap.isEmpty();
    }

    public double getActualValue(LocalDateTime now) {
        if (dataMap.isEmpty()) {
            return UNDEF;
        }
        Entry<LocalDateTime, Double> f = dataMap.floorEntry(now);
        Entry<LocalDateTime, Double> c = dataMap.ceilingEntry(now);
        if (f != null) {
            if (c != null) {
                // we're during suntime!
                double production = c.getValue() - f.getValue();
                int interpolation = now.getMinute() - f.getKey().getMinute();
                double interpolationProduction = production * interpolation / 60;
                double actualProduction = f.getValue() + interpolationProduction;
                return Math.round(actualProduction) / 1000.0;
            } else {
                // sun is down
                return Math.round(f.getValue()) / 1000.0;
            }
        } else {
            // no floor - sun not rised yet
            return 0;
        }
    }

    public double getDayTotal(LocalDateTime now, int offset) {
        if (rawData.isEmpty()) {
            return -1;
        }
        LocalDate ld = now.plusDays(offset).toLocalDate();
        JSONObject contentJson = new JSONObject(rawData.get());
        JSONObject resultJson = contentJson.getJSONObject("result");
        JSONObject wattsDay = resultJson.getJSONObject("watt_hours_day");

        if (wattsDay.has(ld.toString())) {
            return Math.round(wattsDay.getDouble(ld.toString())) / 1000.0;
        }
        return UNDEF;
    }

    public double getRemainingProduction(LocalDateTime now) {
        if (dataMap.isEmpty()) {
            return UNDEF;
        }
        return getDayTotal(now, 0) - getActualValue(now);
    }

    public static State getStateObject(double d) {
        if (d < 0) {
            return UnDefType.UNDEF;
        } else {
            return QuantityType.valueOf(d, Units.KILOWATT_HOUR);
        }
    }

    @Override
    public String toString() {
        return "Hour: " + constructionHour + ", Valid: " + valid + ", Data:" + dataMap;
    }
}
