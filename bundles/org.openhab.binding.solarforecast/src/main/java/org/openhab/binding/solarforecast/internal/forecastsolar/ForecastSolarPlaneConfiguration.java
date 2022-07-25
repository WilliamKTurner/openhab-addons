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

/**
 * The {@link ForecastSolarPlaneConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class ForecastSolarPlaneConfiguration {
    public int declination = -1;
    public int azimuth = 360;
    public double kwp = 0;
    public int refreshInterval = -1;

    @Override
    public String toString() {
        return " Dec " + declination + " Azi " + azimuth + " KWP " + kwp + " Ref " + refreshInterval;
    }
}
