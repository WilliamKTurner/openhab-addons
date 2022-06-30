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
package org.openhab.binding.mercedesme;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.openhab.binding.mercedesme.internal.utils.ChannelStateMap;
import org.openhab.binding.mercedesme.internal.utils.Mapper;

/**
 * The {@link JsonTest} Test Json conversions
 *
 * @author Bernd Weymann - Initial contribution
 */
@NonNullByDefault
class JsonTest {
    public static final String DATE_INPUT_PATTERN_STRING = "yyyy-MM-dd'T'HH:mm:ss";
    public static final DateTimeFormatter DATE_INPUT_PATTERN = DateTimeFormatter.ofPattern(DATE_INPUT_PATTERN_STRING);

    @Test
    void testOdoMapper() {
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("range:mileage 4131 km");
        String content = FileReader.readFileInString("src/test/resources/odo.json");
        JSONArray ja = new JSONArray(content);
        ja.forEach(entry -> {
            JSONObject jo = (JSONObject) entry;
            ChannelStateMap csm = Mapper.getChannelStateMap(jo);
            assertNotNull(csm);
            assertTrue(expectedResults.contains(csm.toString()));
            boolean removed = expectedResults.remove(csm.toString());
            if (!removed) {
                assertTrue(false, csm.toString() + " not removed");
            }
        });
        assertEquals(0, expectedResults.size(), "All content delivered");
    }

    @Test
    void testEVMapper() {
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("range:range-electric 325 km");
        expectedResults.add("range:soc 78 %");
        String content = FileReader.readFileInString("src/test/resources/evstatus.json");
        JSONArray ja = new JSONArray(content);
        ja.forEach(entry -> {
            JSONObject jo = (JSONObject) entry;
            ChannelStateMap csm = Mapper.getChannelStateMap(jo);
            assertNotNull(csm);
            assertTrue(expectedResults.contains(csm.toString()));
            boolean removed = expectedResults.remove(csm.toString());
            if (!removed) {
                assertTrue(false, csm.toString() + " not removed");
            }
        });
        assertEquals(0, expectedResults.size(), "All content delivered");
    }

    @Test
    void testFuelMapper() {
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("range:range-fuel 1292 km");
        expectedResults.add("range:fuel-level 90 %");
        String content = FileReader.readFileInString("src/test/resources/fuel.json");
        JSONArray ja = new JSONArray(content);
        ja.forEach(entry -> {
            JSONObject jo = (JSONObject) entry;
            ChannelStateMap csm = Mapper.getChannelStateMap(jo);
            assertNotNull(csm);
            assertTrue(expectedResults.contains(csm.toString()));
            boolean removed = expectedResults.remove(csm.toString());
            if (!removed) {
                assertTrue(false, csm.toString() + " not removed");
            }
        });
    }

    @Test
    void testLockMapper() {
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("lock:doors 0");
        expectedResults.add("lock:deck-lid ON");
        expectedResults.add("lock:flap ON");
        expectedResults.add("location:heading 120 °");
        String content = FileReader.readFileInString("src/test/resources/lock.json");
        JSONArray ja = new JSONArray(content);
        ja.forEach(entry -> {
            JSONObject jo = (JSONObject) entry;
            ChannelStateMap csm = Mapper.getChannelStateMap(jo);
            assertNotNull(csm);
            assertTrue(expectedResults.contains(csm.toString()));
            boolean removed = expectedResults.remove(csm.toString());
            if (!removed) {
                assertTrue(false, csm.toString() + " not removed");
            }
        });
    }

    @Test
    void testStatusMapper() {
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("doors:deck-lid CLOSED");
        expectedResults.add("doors:driver-front CLOSED");
        expectedResults.add("doors:passenger-front CLOSED");
        expectedResults.add("doors:driver-rear CLOSED");
        expectedResults.add("doors:passenger-rear CLOSED");
        expectedResults.add("lights:interior-front OFF");
        expectedResults.add("lights:interior-rear OFF");
        expectedResults.add("lights:light-switch 0");
        expectedResults.add("lights:reading-left OFF");
        expectedResults.add("lights:reading-right OFF");
        expectedResults.add("doors:rooftop 0");
        expectedResults.add("doors:sunroof 0");
        expectedResults.add("windows:driver-front 0");
        expectedResults.add("windows:passenger-front 0");
        expectedResults.add("windows:driver-rear 0");
        expectedResults.add("windows:passenger-rear 0");

        String content = FileReader.readFileInString("src/test/resources/status.json");
        JSONArray ja = new JSONArray(content);
        ja.forEach(entry -> {
            JSONObject jo = (JSONObject) entry;
            ChannelStateMap csm = Mapper.getChannelStateMap(jo);
            assertNotNull(csm);
            assertTrue(expectedResults.contains(csm.toString()));
            boolean removed = expectedResults.remove(csm.toString());
            if (!removed) {
                assertTrue(false, csm.toString() + " not removed");
            }
        });
        assertEquals(0, expectedResults.size(), "All content delivered");
    }

    @Test
    void testEQALightsMapper() {
        // real life example
        List<String> expectedResults = new ArrayList<String>();
        expectedResults.add("doors:passenger-front OPEN");
        expectedResults.add("windows:driver-front 1");
        expectedResults.add("windows:driver-rear 1");
        expectedResults.add("windows:passenger-rear 1");
        expectedResults.add("windows:passenger-front 1");
        expectedResults.add("lights:light-switch 0");
        expectedResults.add("lights:reading-right ON");
        expectedResults.add("lights:reading-left ON");
        expectedResults.add("doors:driver-front CLOSED");
        expectedResults.add("doors:driver-rear CLOSED");

        String content = FileReader.readFileInString("src/test/resources/eqa-light-sample.json");
        JSONArray ja = new JSONArray(content);
        ja.forEach(entry -> {
            JSONObject jo = (JSONObject) entry;
            ChannelStateMap csm = Mapper.getChannelStateMap(jo);
            assertTrue(expectedResults.contains(csm.toString()));
            boolean removed = expectedResults.remove(csm.toString());
            if (!removed) {
                assertTrue(false, csm.toString() + " not removed");
            }
        });
        assertEquals(0, expectedResults.size(), "All content delivered");
    }

    @Test
    void testTimeStamp() {
        String content = FileReader.readFileInString("src/test/resources/eqa-light-sample.json");
        JSONArray ja = new JSONArray(content);
        long lastTimestamp = 0;
        for (Iterator iterator = ja.iterator(); iterator.hasNext();) {
            JSONObject jo = (JSONObject) iterator.next();
            Set<String> s = jo.keySet();
            if (!s.isEmpty()) {
                String id = s.toArray()[0].toString();
                JSONObject val = jo.getJSONObject(id);
                if (val.has("timestamp")) {
                    lastTimestamp = val.getLong("timestamp");
                }
            }
        }
        Date d = new Date(lastTimestamp);
        LocalDateTime ld = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        assertEquals("2022-06-19T16:46:31", ld.format(DATE_INPUT_PATTERN));
    }
}
