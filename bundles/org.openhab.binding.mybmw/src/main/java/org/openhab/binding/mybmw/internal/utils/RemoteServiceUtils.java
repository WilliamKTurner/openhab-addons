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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.binding.mybmw.internal.handler.RemoteServiceHandler.RemoteService;
import org.openhab.core.types.StateOption;

/**
 * Helper class for Remote Service Commands
 *
 * @author Norbert Truchsess - Initial contribution
 */
@NonNullByDefault
public class RemoteServiceUtils {

    private static final Map<String, RemoteService> COMMAND_SERVICES = Stream.of(RemoteService.values())
            .collect(Collectors.toUnmodifiableMap(RemoteService::getId, service -> service));

    // [tofdo] Not working yet
    private static final Set<RemoteService> ELECTRIC_SERVICES = Collections.<RemoteService> emptySet();// =
    // EnumSet.of(RemoteService.CHARGE_NOW,RemoteService.CHARGING_CONTROL);

    public static Optional<RemoteService> getRemoteService(final String command) {
        return Optional.ofNullable(COMMAND_SERVICES.get(command));
    }

    public static List<StateOption> getOptions(final boolean isElectric) {
        return Stream.of(RemoteService.values())
                .filter(service -> isElectric ? true : !ELECTRIC_SERVICES.contains(service))
                .map(service -> new StateOption(service.getId(), service.getLabel()))
                .collect(Collectors.toUnmodifiableList());
    }
}
