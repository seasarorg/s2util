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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.convert.DateConversionUtil.*;

/**
 * @author higa
 */
public class DateConversionUtilTest {

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
        System.out.println(((SimpleDateFormat) DateFormat.getDateTimeInstance(
            DateFormat.FULL,
            DateFormat.FULL)).toPattern());
        Date date = toDate("10/9/7");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_MediumStyle() throws Exception {
        Date date = toDate("2010/9/7");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_LongStyle() throws Exception {
        Date date = toDate("2010/09/07");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_FullStyle() throws Exception {
        Date date = toDate("2010年9月7日");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_PlainFormat() throws Exception {
        Date date = toDate("20100907");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_JdbcEscapeFormat() throws Exception {
        Date date = toDate("2010-09-07");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_SpecificLocale() throws Exception {
        Date date = toDate("SEP 7, 2010", Locale.US);
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToDate_SpecificPattern() throws Exception {
        Date date = toDate("07//09//10", "dd//MM//yy");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
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
        Calendar calendar = toCalendar("10/9/7");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_MediumStyle() throws Exception {
        Calendar calendar = toCalendar("2010/9/7");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_LongStyle() throws Exception {
        Calendar calendar = toCalendar("2010/09/07");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_FullStyle() throws Exception {
        Calendar calendar = toCalendar("2010年9月7日");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_PlainFormat() throws Exception {
        Calendar calendar = toCalendar("20100907");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_JdbcEscapeFormat() throws Exception {
        Calendar calendar = toCalendar("2010-09-07");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_SpecificLocale() throws Exception {
        Calendar calendar = toCalendar("SEP 7, 2010", Locale.US);
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToCalendar_SpecificPattern() throws Exception {
        Calendar calendar = toCalendar("07//09//10", "dd//MM//yy");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime()),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_Null() throws Exception {
        assertThat(toSqlDateJdbcEscape(null), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_EmptyString() throws Exception {
        assertThat(toSqlDateJdbcEscape(""), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_ShortStyle() throws Exception {
        java.sql.Date date = toSqlDate("10/9/7");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_MediumStyle() throws Exception {
        java.sql.Date date = toSqlDate("2010/9/7");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_LongStyle() throws Exception {
        java.sql.Date date = toSqlDate("2010/09/07");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_FullStyle() throws Exception {
        java.sql.Date date = toSqlDate("2010年9月7日");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_PlainFormat() throws Exception {
        java.sql.Date date = toSqlDate("20100907");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_JdbcEscapeFormat() throws Exception {
        java.sql.Date date = toSqlDate("2010-09-07");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_SpecificLocale() throws Exception {
        java.sql.Date date = toSqlDate("SEP 7, 2010", Locale.US);
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToSqlDate_SpecificPattern() throws Exception {
        java.sql.Date date = toSqlDate("07//09//10", "dd//MM//yy");
        assertThat(
            new SimpleDateFormat("yyyy/MM/dd").format(date),
            is("2010/09/07"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToPlainPattern() throws Exception {
        assertThat(toPlainPattern("y/M/d"), is("yyMMdd"));
    }

}
