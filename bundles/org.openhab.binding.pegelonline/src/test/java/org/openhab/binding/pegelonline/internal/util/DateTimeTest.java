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
package org.openhab.binding.pegelonline.internal.util;

import static org.openhab.binding.pegelonline.internal.PegelOnlineBindingConstants.GSON;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.junit.jupiter.api.Test;
import org.openhab.binding.pegelonline.internal.dto.Measure;
import org.openhab.core.library.types.DateTimeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DateTimeTest} Test helper utils
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
class DateTimeTest {
    private final Logger logger = LoggerFactory.getLogger(DateTimeTest.class);

    @Test
    void test() {
        String content = FileReader.readFileInString("src/test/resources/measure.json");
        logger.info("Content {}", content);
        Measure m = GSON.fromJson(content, Measure.class);
        DateTimeType dtt = DateTimeType.valueOf(m.timestamp);
        logger.info("DateTimeType {}", dtt.toFullString());
    }
}
