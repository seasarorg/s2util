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

import java.net.URL;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.seasar.util.exception.ResourceNotFoundRuntimeException;
import org.seasar.util.io.ResourceUtil;
import org.seasar.util.misc.Disposable;
import org.seasar.util.misc.DisposableUtil;

import static org.seasar.util.collection.CollectionsUtil.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link MessageResourceBundle}を取得するためのクラスです。
 * 
 * @author shot
 */
public abstract class MessageResourceBundleFactory {

    /** プロパティファイルの拡張子 */
    protected static final String PROPERTIES_EXT = ".properties";

    /** リソースバンドルのキャッシュ */
    protected static Map<String, MessageResourceBundleFacade> cache =
        newHashMap();

    /** リソースバンドルのキャッシュ */
    protected static Set<String> notFounds = newHashSet();

    /** 初期化済みフラグ */
    protected static boolean initialized = false;

    /**
     * {@link MessageResourceBundle}を返します。
     * 
     * @param baseName
     * @return {@link MessageResourceBundle}
     * @see #getBundle(String, Locale)
     */
    public static MessageResourceBundle getBundle(final String baseName) {
        return getBundle(baseName, Locale.getDefault());
    }

    /**
     * {@link MessageResourceBundle}を返します。
     * 
     * @param baseName
     * @param locale
     * @return {@link MessageResourceBundle}
     * @throws ResourceNotFoundRuntimeException
     *             リソースが見つからなかった場合
     */
    public static MessageResourceBundle getBundle(final String baseName,
            final Locale locale) throws ResourceNotFoundRuntimeException {
        final MessageResourceBundle bundle =
            getNullableBundle(baseName, locale);
        if (bundle != null) {
            return bundle;
        }
        throw new ResourceNotFoundRuntimeException(baseName);
    }

    /**
     * {@link MessageResourceBundle}を返します。 リソースが見つからなかった場合は、 nullを返します。
     * 
     * @param baseName
     * @return {@link MessageResourceBundle}
     * @see #getNullableBundle(String, Locale)
     */
    public static MessageResourceBundle getNullableBundle(final String baseName) {
        return getNullableBundle(baseName, Locale.getDefault());
    }

    /**
     * {@link MessageResourceBundle}を返します。 リソースが見つからなかった場合は、 nullを返します。
     * 
     * @param baseName
     * @param locale
     * @return {@link MessageResourceBundle}
     */
    public static MessageResourceBundle getNullableBundle(
            final String baseName, final Locale locale) {
        assertArgumentNotNull("baseName", baseName);
        assertArgumentNotNull("locale", locale);

        final String base = baseName.replace('.', '/');

        final String[] bundleNames = calcurateBundleNames(base, locale);
        MessageResourceBundleFacade descendantFacade = null;
        for (final String bundleName : bundleNames) {
            final MessageResourceBundleFacade facade =
                loadFacade(bundleName + PROPERTIES_EXT);
            if (facade != null) {
                facade.setParent(descendantFacade);
                descendantFacade = facade;
            }
        }
        if (descendantFacade != null) {
            return descendantFacade.getBundle();
        }
        return null;
    }

    /**
     * メッセージリソースバンドルファザードを返します。
     * 
     * @param path
     *            パス
     * @return メッセージリソースバンドルファザード
     */
    protected static synchronized MessageResourceBundleFacade loadFacade(
            final String path) {
        if (!initialized) {
            DisposableUtil.add(new Disposable() {
                @Override
                public void dispose() {
                    clear();
                    initialized = false;
                }
            });
            initialized = true;
        }
        final MessageResourceBundleFacade cachedFacade = cache.get(path);
        if (cachedFacade != null) {
            return cachedFacade;
        }
        if (notFounds.contains(path)) {
            return null;
        }
        final URL url = ResourceUtil.getResourceNoException(path);
        if (url == null) {
            notFounds.add(path);
            return null;
        }
        final MessageResourceBundleFacade facade =
            new MessageResourceBundleFacade(url);
        cache.put(path, facade);
        return facade;
    }

    /**
     * リソースバンドル名の配列をロケールから求めて返します。
     * 
     * @param baseName
     *            リソースバンドルの基底名
     * @param locale
     *            リソースバンドルが必要なロケール
     * @return リソースバンドル名配列
     */
    protected static String[] calcurateBundleNames(final String baseName,
            final Locale locale) {
        int length = 1;
        final boolean l = locale.getLanguage().length() > 0;
        if (l) {
            length++;
        }
        final boolean c = locale.getCountry().length() > 0;
        if (c) {
            length++;
        }
        final boolean v = locale.getVariant().length() > 0;
        if (v) {
            length++;
        }
        final String[] result = new String[length];
        int index = 0;
        result[index++] = baseName;

        if (!(l || c || v)) {
            return result;
        }

        final StringBuilder buffer =
            new StringBuilder(baseName)
                .append('_')
                .append(locale.getLanguage());
        if (l) {
            result[index++] = new String(buffer);
        }

        if (!(c || v)) {
            return result;
        }
        buffer.append('_').append(locale.getCountry());
        if (c) {
            result[index++] = new String(buffer);
        }

        if (!v) {
            return result;
        }
        buffer.append('_').append(locale.getVariant());
        result[index++] = new String(buffer);

        return result;
    }

    /**
     * キャッシュしている内容をクリアします。
     */
    public static void clear() {
        cache.clear();
    }
}
