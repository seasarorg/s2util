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
package org.seasar.util.message;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.NullArgumentException;
import org.seasar.util.io.ResourceUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author shot
 * @author higa
 */
public class MessageResourceBundleFacadeTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final String PATH = "MSGMessages.properties";

    private static final String PATH2 = "MSGMessages_ja.properties";

    /**
     * @throws Exception
     */
    @Test
    public void testCreateProperties_url() throws Exception {
        URL url = ResourceUtil.getResource(PATH);
        Properties props = MessageResourceBundleFacade.createProperties(url);
        assertThat(props, is(notNullValue()));
        assertThat(props.getProperty("EMSG0001"), is("{0} not found"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateProperties_file() throws Exception {
        File file = ResourceUtil.getResourceAsFile(PATH);
        Properties props = MessageResourceBundleFacade.createProperties(file);
        assertThat(props, is(notNullValue()));
        assertThat(props.getProperty("EMSG0001"), is("{0} not found"));

        file = ResourceUtil.getResourceAsFile(PATH2);
        props = MessageResourceBundleFacade.createProperties(file);
        System.out.println(props.get("EMSG0001"));
        assertThat(props.getProperty("EMSG0001"), is("{0}が見つかりません"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsModified_hot() throws Exception {
        MessageResourceBundleFacade.setEager(true);
        try {
            URL url = ResourceUtil.getResource(PATH);
            File file = ResourceUtil.getFile(url);
            MessageResourceBundleFacade facade =
                new MessageResourceBundleFacade(url);

            assertThat(facade.isModified(), is(not(true)));
            Thread.sleep(500);
            file.setLastModified(new Date().getTime());
            assertThat(facade.isModified(), is(true));
        } finally {
            MessageResourceBundleFacade.setEager(false);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsModified_cool() throws Exception {
        URL url = ResourceUtil.getResource(PATH);
        File file = ResourceUtil.getFile(url);
        MessageResourceBundleFacade facade =
            new MessageResourceBundleFacade(url);

        assertThat(facade.isModified(), is(not(true)));
        Thread.sleep(500);
        file.setLastModified(new Date().getTime());
        assertThat(facade.isModified(), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSetup() throws Exception {
        URL url = ResourceUtil.getResource(PATH);
        MessageResourceBundleFacade facade =
            new MessageResourceBundleFacade(url);
        MessageResourceBundle bundle = facade.getBundle();
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.get("EMSG0001"), is("{0} not found"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testParent() throws Exception {
        URL url = ResourceUtil.getResource(PATH2);
        MessageResourceBundleFacade facade =
            new MessageResourceBundleFacade(url);
        URL parentUrl = ResourceUtil.getResource(PATH);
        MessageResourceBundleFacade parent =
            new MessageResourceBundleFacade(parentUrl);
        facade.setParent(parent);
        MessageResourceBundle bundle = facade.getBundle();
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
    }

    /**
     * Test method for
     * {@link org.seasar.util.message.MessageResourceBundleFacade#MessageResourceBundleFacade(URL)}
     * .
     */
    @Test
    public void testMessageResourceBundleFacade() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[url]がnullです。"));
        new MessageResourceBundleFacade(null);
    }

    /**
     * Test method for
     * {@link org.seasar.util.message.MessageResourceBundleFacade#createProperties(java.io.InputStream)}
     * .
     */
    @Test
    public void testCreateProperties() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[is]がnullです。"));
        InputStream is = null;
        MessageResourceBundleFacade.createProperties(is);
    }

}
