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

import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import static org.seasar.util.collection.CollectionsUtil.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link ResourceBundle}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ResourceBundleUtil {

    /**
     * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
     * 
     * @param name
     *            リソースバンドの名前
     * @return {@link ResourceBundle}
     * @see ResourceBundle#getBundle(String)
     */
    public static final ResourceBundle getBundle(final String name) {
        assertArgumentNotEmpty("name", name);

        try {
            return ResourceBundle.getBundle(name);
        } catch (final MissingResourceException ignore) {
            return null;
        }
    }

    /**
     * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
     * 
     * @param name
     *            リソースバンドの名前
     * @param locale
     *            ロケール
     * @return {@link ResourceBundle}
     * @see ResourceBundle#getBundle(String, Locale)
     */
    public static final ResourceBundle getBundle(final String name,
            final Locale locale) {
        assertArgumentNotEmpty("name", name);

        try {
            return ResourceBundle.getBundle(name, locale != null ? locale
                : Locale.getDefault());
        } catch (final MissingResourceException ignore) {
            return null;
        }
    }

    /**
     * バンドルを返します。 見つからない場合は、<code>null</code>を返します。
     * 
     * @param name
     *            リソースバンドルの名前
     * @param locale
     *            ロケール
     * @param classLoader
     *            クラスローダ
     * @return {@link ResourceBundle}
     * @see ResourceBundle#getBundle(String, Locale, ClassLoader)
     */
    public static final ResourceBundle getBundle(final String name,
            final Locale locale, final ClassLoader classLoader) {
        assertArgumentNotNull("name", name);
        assertArgumentNotNull("classLoader", classLoader);

        try {
            return ResourceBundle.getBundle(name, locale != null ? locale
                : Locale.getDefault(), classLoader);
        } catch (final MissingResourceException ignore) {
            return null;
        }
    }

    /**
     * リソースバンドルから指定されたキーの文字列を返します。
     * 
     * @param bundle
     *            リソースバンドル
     * @param key
     *            キー
     * @return 指定されたキーの文字列
     * @see ResourceBundle#getString(String)
     */
    public static String getString(final ResourceBundle bundle, final String key) {
        try {
            return bundle.getString(key);
        } catch (final Throwable t) {
            return null;
        }
    }

    /**
     * リソースバンドルを{@link Map}に変換します。
     * 
     * @param bundle
     *            リソースバンドル
     * @return {@link Map}
     */
    public static final Map<String, String> convertMap(
            final ResourceBundle bundle) {
        assertArgumentNotNull("bundle", bundle);

        final Map<String, String> ret = newHashMap();
        for (final Enumeration<String> e = bundle.getKeys(); e
            .hasMoreElements();) {
            final String key = e.nextElement();
            final String value = bundle.getString(key);
            ret.put(key, value);
        }
        return ret;
    }

    /**
     * リソースバンドルを{@link Map}に変換して返します。
     * 
     * @param name
     *            リソースバンドルの名前
     * @param locale
     *            ロケール
     * @return {@link Map}
     */
    public static final Map<String, String> convertMap(final String name,
            final Locale locale) {
        assertArgumentNotNull("name", name);

        final ResourceBundle bundle = getBundle(name, locale);
        return convertMap(bundle);
    }

}
