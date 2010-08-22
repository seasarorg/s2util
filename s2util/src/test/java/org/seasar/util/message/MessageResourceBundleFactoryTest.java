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

import java.util.Locale;

import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author shot
 * @author higa
 */
public class MessageResourceBundleFactoryTest {

    private static final String BASE_NAME = "MSGMessages";

    private static final String PATH = BASE_NAME + ".properties";

    /**
     * 
     */
    @After
    public void tearDown() {
        MessageResourceBundleFactory.clear();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testLoadFacade() throws Exception {
        MessageResourceBundleFacade facade =
            MessageResourceBundleFactory.loadFacade(PATH);
        assertThat(facade, is(notNullValue()));
        assertThat(facade.getBundle().get("EMSG0001"), is("{0} not found"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetNullableBundle() throws Exception {
        MessageResourceBundle bundle =
            MessageResourceBundleFactory.getNullableBundle(
                BASE_NAME,
                Locale.JAPANESE);
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
        assertThat(bundle.get("EMSG0001"), is("{0}が見つかりません"));

        bundle =
            MessageResourceBundleFactory.getNullableBundle(
                BASE_NAME,
                Locale.ITALIAN);
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(nullValue()));
        assertThat(bundle.get("EMSG0001"), is("{0} not found"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetNullabelBundle2() throws Exception {
        String baseName = "org.seasar.util.message.strings";
        MessageResourceBundle bundle =
            MessageResourceBundleFactory.getNullableBundle(
                baseName,
                Locale.JAPANESE);
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent(), is(nullValue()));
        assertThat(bundle.get("text"), is("hogehoge"));

        bundle =
            MessageResourceBundleFactory.getNullableBundle(
                baseName,
                new Locale("ja", "JP"));
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent().getParent(), is(nullValue()));
        assertThat(bundle.get("text"), is("hogehogehoge"));

        bundle =
            MessageResourceBundleFactory.getNullableBundle(
                baseName,
                new Locale("ja", "JP", "WIN"));
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent(), is(notNullValue()));
        assertThat(
            bundle.getParent().getParent().getParent(),
            is(notNullValue()));
        assertThat(
            bundle.getParent().getParent().getParent().getParent(),
            is(nullValue()));
        assertThat(bundle.get("text"), is("hogehogehogehoge"));

        bundle =
            MessageResourceBundleFactory.getNullableBundle(
                baseName,
                new Locale("en", "US"));
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent().getParent(), is(nullValue()));
        assertThat(bundle.get("text"), is("foobar"));

        bundle =
            MessageResourceBundleFactory.getNullableBundle(
                baseName,
                new Locale("en", "UK"));
        assertThat(bundle, is(notNullValue()));
        assertThat(bundle.getParent(), is(notNullValue()));
        assertThat(bundle.getParent().getParent(), is(nullValue()));
        assertThat(bundle.get("text"), is("foo"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCalcurateBundleNames() throws Exception {
        // 言語のみ
        String[] bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                Locale.JAPANESE);
        String[] expected = new String[] { BASE_NAME, BASE_NAME + "_ja" };
        assertArrayEquals(expected, bundleNames);

        // 言語と国
        bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                new Locale("ja", "JP"));
        expected =
            new String[] { BASE_NAME, BASE_NAME + "_ja", BASE_NAME + "_ja_JP" };
        assertArrayEquals(expected, bundleNames);

        // 言語と国とバリアント
        bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                new Locale("ja", "JP", "WIN"));
        expected =
            new String[] { BASE_NAME, BASE_NAME + "_ja", BASE_NAME + "_ja_JP",
                BASE_NAME + "_ja_JP_WIN" };
        assertArrayEquals(expected, bundleNames);

        // 言語とバリアント
        bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                new Locale("ja", "", "WIN"));
        expected =
            new String[] { BASE_NAME, BASE_NAME + "_ja", BASE_NAME + "_ja__WIN" };
        assertArrayEquals(expected, bundleNames);

        // 国とバリアント
        bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                new Locale("", "JP", "WIN"));
        expected =
            new String[] { BASE_NAME, BASE_NAME + "__JP",
                BASE_NAME + "__JP_WIN" };
        assertArrayEquals(expected, bundleNames);

        // 国のみ
        bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                new Locale("", "JP"));
        expected = new String[] { BASE_NAME, BASE_NAME + "__JP" };
        assertArrayEquals(expected, bundleNames);

        // バリアントのみ
        bundleNames =
            MessageResourceBundleFactory.calcurateBundleNames(
                BASE_NAME,
                new Locale("", "", "WIN"));
        expected = new String[] { BASE_NAME, BASE_NAME + "___WIN" };
        assertArrayEquals(expected, bundleNames);
    }

}
