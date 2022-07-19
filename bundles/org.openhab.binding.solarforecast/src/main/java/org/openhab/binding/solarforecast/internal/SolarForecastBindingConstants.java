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

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link SolarForecastBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class SolarForecastBindingConstants {

    private static final String BINDING_ID = "solarforecast";

    public static final ThingTypeUID FORECAST_SOLAR_SINGLE_STRING = new ThingTypeUID(BINDING_ID, "single");
    public static final ThingTypeUID FORECAST_SOLAR_MULTI_STRING = new ThingTypeUID(BINDING_ID, "multi");
    public static final ThingTypeUID FORECAST_SOLAR_PART_STRING = new ThingTypeUID(BINDING_ID, "part");
    public static final Set<ThingTypeUID> SUPPORTED_THING_SET = Set.of(FORECAST_SOLAR_SINGLE_STRING,
            FORECAST_SOLAR_MULTI_STRING, FORECAST_SOLAR_PART_STRING);

    public static final String CHANNEL_TODAY = "today";
    public static final String CHANNEL_ACTUAL = "actual";
    public static final String CHANNEL_REMAINING = "remaining";
    public static final String CHANNEL_TOMORROW = "tomorrow";
    public static final String CHANNEL_RAW = "raw";

    public static final String BASE_URL = "https://api.forecast.solar/estimate/";

    public static final String AUTODETECT = "AUTODETECT";
    public static final String SLASH = "/";
    public static final String EMPTY = "";
}
