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
package org.seasar.util.exception;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author wyukawa
 * 
 */
public class NullArgumentExceptionTest {

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
        NullArgumentException nullArgumentException =
            new NullArgumentException("hoge");
        assertThat(nullArgumentException.getArgName(), is("hoge"));
        assertThat(
            nullArgumentException.getMessage(),
            is("[EUTL0008]引数[hoge]がnullです。"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testErrorMessage_en() throws Exception {
        // ## Arrange ##
        Locale.setDefault(Locale.ENGLISH);
        NullArgumentException nullArgumentException =
            new NullArgumentException("hoge");
        assertThat(nullArgumentException.getArgName(), is("hoge"));
        assertThat(
            nullArgumentException.getMessage(),
            is("[EUTL0008]argument[hoge] is null."));
    }
}
