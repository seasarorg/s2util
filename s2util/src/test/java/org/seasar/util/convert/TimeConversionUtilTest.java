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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.convert.TimeConversionUtil.*;

/**
 * @author higa
 */
public class TimeConversionUtilTest {

    Locale defaultLocale = Locale.getDefault();

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        Locale.setDefault(Locale.JAPANESE);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        Locale.setDefault(defaultLocale);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_Null() throws Exception {
        assertThat(toDate(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_EmptyString() throws Exception {
        assertThat(toDate(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_ShortStyle() throws Exception {
        Date date = toDate("11:49");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:00"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_MediumStyle() throws Exception {
        Date date = toDate("11:49:10");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_LongStyle() throws Exception {
        Date date = toDate("11:49:10 JST");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_FullStyle() throws Exception {
        Date date = toDate("11時49分10秒 JST");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_PlainFormat() throws Exception {
        Date date = toDate("114910");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_JdbcEscapeFormat() throws Exception {
        Date date = toDate("11:49:10");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_SpecificLocale() throws Exception {
        Date date = toDate("11:49:10 AM", Locale.US);
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_SpecificPattern() throws Exception {
        Date date = toDate("10::49::11", "ss::mm::HH");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(date),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_Null() throws Exception {
        assertThat(toCalendar(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_EmptyString() throws Exception {
        assertThat(toCalendar(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_ShortStyle() throws Exception {
        Calendar calendar = toCalendar("11:49");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:00"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_MediumStyle() throws Exception {
        Calendar calendar = toCalendar("11:49:10");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_LongStyle() throws Exception {
        Calendar calendar = toCalendar("11:49:10 JST");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_FullStyle() throws Exception {
        Calendar calendar = toCalendar("11時49分10秒 JST");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_PlainFormat() throws Exception {
        Calendar calendar = toCalendar("114910");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_JdbcEscapeFormat() throws Exception {
        Calendar calendar = toCalendar("11:49:10");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_SpecificLocale() throws Exception {
        Calendar calendar = toCalendar("11:49:10 AM", Locale.US);
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_SpecificPattern() throws Exception {
        Calendar calendar = toCalendar("10::49::11", "ss::mm::HH");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(calendar.getTime()),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlTime_Null() throws Exception {
        assertThat(toSqlTime(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_EmptyString() throws Exception {
        assertThat(toSqlTime(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_ShortStyle() throws Exception {
        Time time = toSqlTime("11:49");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:00"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_MediumStyle() throws Exception {
        Time time = toSqlTime("11:49:10");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_LongStyle() throws Exception {
        Time time = toSqlTime("11:49:10 JST");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_FullStyle() throws Exception {
        Time time = toSqlTime("11時49分10秒 JST");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_PlainFormat() throws Exception {
        Time time = toSqlTime("114910");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_JdbcEscapeFormat() throws Exception {
        Time time = toSqlTime("11:49:10");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_SpecificLocale() throws Exception {
        Time time = toSqlTime("11:49:10 AM", Locale.US);
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToTime_SpecificPattern() throws Exception {
        Time time = toSqlTime("10::49::11", "ss::mm::HH");
        assertThat(
            new SimpleDateFormat("HH:mm:ss").format(time),
            is("11:49:10"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToPlainPattern() throws Exception {
        assertThat(toPlainPattern("H:m:s"), is("HHmmss"));
    }

}
