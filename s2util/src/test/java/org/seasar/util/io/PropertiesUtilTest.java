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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.exception.NullArgumentException;
import org.seasar.util.net.URLUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author wyukawa
 * 
 */
public class PropertiesUtilTest {

    URL url = ResourceUtil.getResource(getClass().getName().replace('.', '/')
        + ".txt");

    File inputFile = URLUtil.toFile(url);

    /**
     * 
     */
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

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
            ReaderUtil.create(
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
            ReaderUtil.create(
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

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(Properties, File)} .
     */
    @Test
    public void testLoadPropertiesFile() {
        Properties properties = new Properties();
        PropertiesUtil.load(properties, inputFile);
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#load(Properties, File, String)}
     * .
     */
    @Test
    public void testLoadPropertiesFileString() {
        Properties properties = new Properties();
        PropertiesUtil.load(properties, inputFile, "UTF-8");
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#store(Properties, java.io.OutputStream, String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testStorePropertiesOutputStreamString() throws IOException {
        Properties outProperties = new Properties();
        outProperties.setProperty("a", "A");
        File file = tempFolder.newFile("hoge.properties");
        FileOutputStream outputStream = OutputStreamUtil.create(file);
        PropertiesUtil.store(outProperties, outputStream, "comments");
        CloseableUtil.close(outputStream);
        Properties properties = new Properties();
        PropertiesUtil.load(properties, file);
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#store(Properties, java.io.Writer, String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testStorePropertiesWriterString() throws IOException {
        Properties outProperties = new Properties();
        outProperties.setProperty("a", "A");
        File file = tempFolder.newFile("hoge.properties");
        Writer writer = WriterUtil.create(file);
        PropertiesUtil.store(outProperties, writer, "comments");
        CloseableUtil.close(writer);
        Properties properties = new Properties();
        PropertiesUtil.load(properties, file);
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#store(Properties, File, String, String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testStorePropertiesFileStringString() throws IOException {
        Properties outProperties = new Properties();
        outProperties.setProperty("a", "A");
        File file = tempFolder.newFile("hoge.properties");
        PropertiesUtil.store(outProperties, file, "UTF-8", "comments");
        Properties properties = new Properties();
        PropertiesUtil.load(properties, file);
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.PropertiesUtil#store(Properties, File, String)}
     * .
     * 
     * @throws IOException
     */
    @Test
    public void testStorePropertiesFileString() throws IOException {
        Properties outProperties = new Properties();
        outProperties.setProperty("a", "A");
        File file = tempFolder.newFile("hoge.properties");
        PropertiesUtil.store(outProperties, file, "comments");
        Properties properties = new Properties();
        PropertiesUtil.load(properties, file);
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * {@link org.seasar.util.io.PropertiesUtil#load(Properties, URL)}
     */
    @Test
    public void testLoadPropertiesUrl() {
        Properties properties = new Properties();
        PropertiesUtil.load(properties, url);
        assertThat(properties.getProperty("a"), is("A"));
    }

    /**
     * {@link org.seasar.util.io.PropertiesUtil#load(Properties, URL)}
     */
    @Test
    public void testLoadPropertiesUrlThrowIOException() {
        exception.expect(IORuntimeException.class);
        exception
            .expectMessage(is("[EUTL0040]IO例外が発生しました。理由はjava.io.IOException: load"));
        Properties properties = new IOExceptionOccurProperties();
        PropertiesUtil.load(properties, url);
    }

    private static class IOExceptionOccurProperties extends Properties {

        @Override
        public synchronized void load(InputStream inStream) throws IOException {
            throw new IOException("load");
        }

    }

}
