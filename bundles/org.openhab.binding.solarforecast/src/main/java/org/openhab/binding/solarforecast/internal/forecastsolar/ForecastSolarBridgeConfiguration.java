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
package org.openhab.binding.solarforecast.internal.forecastsolar;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.solarforecast.internal.SolarForecastBindingConstants;

/**
 * The {@link ForecastSolarBridgeConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ForecastSolarBridgeConfiguration {
    public String location = "0.0,0.0";
    public int channelRefreshInterval = -1;
    public String apiKey = SolarForecastBindingConstants.EMPTY;

    @Override
    public String toString() {
        return "Loc " + location + " Ref " + channelRefreshInterval;
    }
}
