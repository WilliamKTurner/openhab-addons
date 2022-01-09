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
package org.openhab.binding.pegelonline.internal;

import static org.openhab.binding.pegelonline.internal.PegelOnlineBindingConstants.UNKNOWN;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * The {@link PegelOnlineConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
public class PegelOnlineConfiguration {

    public String uuid = UNKNOWN;
    public int warningLevel1 = Integer.MAX_VALUE;
    public int warningLevel2 = Integer.MAX_VALUE;
    public int warningLevel3 = Integer.MAX_VALUE;
    public int hq10 = Integer.MAX_VALUE;
    public int hq100 = Integer.MAX_VALUE;
    public int hqhqExtereme = Integer.MAX_VALUE;
    public int refreshInterval = 15;
}
