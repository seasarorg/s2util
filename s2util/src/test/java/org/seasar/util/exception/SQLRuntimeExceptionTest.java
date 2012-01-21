/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.util.exception;

import java.sql.SQLException;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author manhole
 */
public class SQLRuntimeExceptionTest {

    private Locale defaultLocale;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        defaultLocale = Locale.getDefault();
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
    public void testErrorMessage_ja() throws Exception {
        // ## Arrange ##
        Locale.setDefault(Locale.JAPANESE);
        SQLException sqlException =
            new SQLException("some reason", "fooState", 7650);
        SQLException sqlException2 =
            new SQLException("hoge reason", "barState", 7660);
        SQLException sqlException3 =
            new SQLException("fuga reason", "bazState", 7670);
        sqlException.setNextException(sqlException2);
        sqlException2.setNextException(sqlException3);

        SQLRuntimeException sqlRuntimeException =
            new SQLRuntimeException(sqlException);

        // ## Act ##
        String message = sqlRuntimeException.getMessage();

        // ## Assert ##
        System.out.println(message);
        assertContains(message, "ErrorCode=7650");
        assertContains(message, "SQLState=fooState");
        assertContains(message, "some reason");
        assertContains(message, "ErrorCode=7660");
        assertContains(message, "SQLState=barState");
        assertContains(message, "hoge reason");
        assertContains(message, "ErrorCode=7670");
        assertContains(message, "SQLState=bazState");
        assertContains(message, "fuga reason");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testErrorMessage_en() throws Exception {
        // ## Arrange ##
        Locale.setDefault(Locale.ENGLISH);
        SQLException sqlException =
            new SQLException("manyReason", "barState", 1234);

        SQLRuntimeException sqlRuntimeException =
            new SQLRuntimeException(sqlException);

        // ## Act ##
        String message = sqlRuntimeException.getMessage();

        // ## Assert ##
        System.out.println(message);
        assertContains(message, "ErrorCode=1234");
        assertContains(message, "SQLState=barState");
        assertContains(message, "manyReason");
    }

    private void assertContains(String s, String contained) {
        assertThat(s, s.indexOf(contained) > -1, is(true));
    }

}
