/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.util.convert;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.NullArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 * 
 */
public class CalendarConversionUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        TimeZone.setDefault(null);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar() throws Exception {
        Date date = new Date();
        Calendar cal = CalendarConversionUtil.toCalendar(date);
        assertEquals(date, cal.getTime());
    }

    /**
     * 
     * @throws Exception
     */
    @Test
    public void testLocalize() throws Exception {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("JST"));
        Calendar local = CalendarConversionUtil.localize(calendar);
        assertEquals(TimeZone.getDefault(), local.getTimeZone());
    }

    /**
     * Test method for
     * {@link org.seasar.util.convert.CalendarConversionUtil#localize(Calendar)}
     * .
     */
    @Test
    public void testCreateAndCopySrcNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[calendar]がnullです。"));
        CalendarConversionUtil.localize(null);
    }
}
