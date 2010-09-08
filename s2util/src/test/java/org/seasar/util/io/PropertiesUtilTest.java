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
package org.seasar.util.io;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.NullArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author wyukawa
 * 
 */
public class PropertiesUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(java.util.Properties, java.io.InputStream)}
     * .
     */
    @Test
    public void testLoadPropertiesInputStream() {
        InputStream inputStream =
            ResourceUtil
                .getResourceAsStream("org/seasar/util/io/test.properties");
        Properties properties = new Properties();
        PropertiesUtil.load(properties, inputStream);
        assertThat(properties.getProperty("hoge"), is("ほげ"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(java.util.Properties, java.io.InputStream)}
     * .
     */
    @Test
    public void testLoadPropertiesInputStreamPropsNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[props]がnullです。"));
        InputStream inputStream =
            ResourceUtil
                .getResourceAsStream("org/seasar/util/io/test.properties");
        PropertiesUtil.load(null, inputStream);
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(java.util.Properties, java.io.InputStream)}
     * .
     */
    @Test
    public void testLoadPropertiesInputStreamInNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[in]がnullです。"));
        Properties properties = new Properties();
        InputStream inputStream = null;
        PropertiesUtil.load(properties, inputStream);
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(java.util.Properties, java.io.Reader)}
     * .
     */
    @Test
    public void testLoadPropertiesReader() {
        InputStreamReader inputStreamReader =
            InputStreamReaderUtil.create(
                ResourceUtil
                    .getResourceAsStream("org/seasar/util/io/test.properties"),
                "UTF-8");
        Properties properties = new Properties();
        PropertiesUtil.load(properties, inputStreamReader);
        assertThat(properties.getProperty("hoge"), is("ほげ"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(java.util.Properties, java.io.Reader)}
     * .
     */
    @Test
    public void testLoadPropertiesReaderPropsNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[props]がnullです。"));
        InputStreamReader inputStreamReader =
            InputStreamReaderUtil.create(
                ResourceUtil
                    .getResourceAsStream("org/seasar/util/io/test.properties"),
                "UTF-8");
        PropertiesUtil.load(null, inputStreamReader);
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(java.util.Properties, java.io.Reader)}
     * .
     */
    @Test
    public void testLoadPropertiesReaderReaderNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[reader]がnullです。"));
        InputStreamReader inputStreamReader = null;
        Properties properties = new Properties();
        PropertiesUtil.load(properties, inputStreamReader);
    }

}
