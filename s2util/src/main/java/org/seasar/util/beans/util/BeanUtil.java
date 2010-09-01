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
package org.seasar.util.beans.util;

import java.util.Map;
import java.util.Map.Entry;

import org.seasar.util.beans.BeanDesc;
import org.seasar.util.beans.PropertyDesc;
import org.seasar.util.beans.factory.BeanDescFactory;
import org.seasar.util.lang.ClassUtil;

import static org.seasar.util.collection.CollectionsUtil.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * JavaBeans用のユーティリティです。
 * 
 * @author Kimura Satoshi
 * @author higa
 */
public abstract class BeanUtil {

    /** デフォルトのオプション */
    protected static final CopyOptions DEFAULT_OPTIONS = new CopyOptions();

    /**
     * BeanからBeanにコピーを行います。
     * 
     * @param src
     *            コピー元のBean
     * @param dest
     *            コピー先のBean
     */
    public static void copyBeanToBean(final Object src, final Object dest) {
        copyBeanToBean(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * BeanからBeanにコピーを行います。
     * 
     * @param src
     *            コピー元のBean
     * @param dest
     *            コピー先のBean
     * @param options
     *            コピーのオプション
     * @see CopyOptionsUtil
     */
    public static void copyBeanToBean(final Object src, final Object dest,
            final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("dest", dest);
        assertArgumentNotNull("option", options);

        final BeanDesc srcBeanDesc =
            BeanDescFactory.getBeanDesc(src.getClass());
        final BeanDesc destBeanDesc =
            BeanDescFactory.getBeanDesc(dest.getClass());
        final int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            final PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            final String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.isReadable()
                || !options.isTargetProperty(srcPropertyName)) {
                continue;
            }
            final String destPropertyName = options.trimPrefix(srcPropertyName);
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            final PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            final Object value = srcPropertyDesc.getValue(src);
            if (!options.isTargetValue(value)) {
                continue;
            }
            final Object convertedValue =
                options.convertValue(
                    value,
                    destPropertyName,
                    destPropertyDesc.getPropertyType());
            destPropertyDesc.setValue(dest, convertedValue);
        }
    }

    /**
     * Beanから{@literal Map}にコピーを行います。
     * 
     * @param src
     *            コピー元のBean
     * @param dest
     *            コピー先の{@literal Map}
     */
    public static void copyBeanToMap(final Object src,
            final Map<String, Object> dest) {
        copyBeanToMap(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * Beanから{@literal Map}にコピーを行います。
     * 
     * @param src
     *            コピー元のBean
     * @param dest
     *            コピー先の{@literal Map}
     * @param options
     *            コピーのオプション
     * @see CopyOptionsUtil
     */
    public static void copyBeanToMap(final Object src,
            final Map<String, Object> dest, final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("dest", dest);
        assertArgumentNotNull("options", options);

        final BeanDesc srcBeanDesc =
            BeanDescFactory.getBeanDesc(src.getClass());
        final int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            final PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            final String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.isReadable()
                || !options.isTargetProperty(srcPropertyName)) {
                continue;
            }
            final Object value = srcPropertyDesc.getValue(src);
            if (!options.isTargetValue(value)) {
                continue;
            }
            final String destPropertyName =
                options.toMapDestPropertyName(srcPropertyName);
            final Object convertedValue =
                options.convertValue(value, destPropertyName, null);
            dest.put(destPropertyName, convertedValue);
        }
    }

    /**
     * {@literal Map}からBeanにコピーを行います。
     * 
     * @param src
     *            コピー元の{@literal Map}
     * @param dest
     *            コピー先のBean
     */
    public static void copyMapToBean(final Map<String, ? extends Object> src,
            final Object dest) {
        copyMapToBean(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * {@literal Map}からBeanにコピーを行います。
     * 
     * @param src
     *            コピー元の{@literal Map}
     * @param dest
     *            コピー先のBean
     * @param options
     *            コピーのオプション
     * @see CopyOptionsUtil
     */
    public static void copyMapToBean(final Map<String, ? extends Object> src,
            final Object dest, final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("dest", dest);
        assertArgumentNotNull("options", options);

        final BeanDesc destBeanDesc =
            BeanDescFactory.getBeanDesc(dest.getClass());
        for (final Entry<String, ? extends Object> entry : src.entrySet()) {
            final String srcPropertyName = entry.getKey();
            if (!options.isTargetProperty(srcPropertyName)) {
                continue;
            }
            final String destPropertyName =
                options.toBeanDestPropertyName(srcPropertyName);
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            final PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            final Object value = entry.getValue();
            if (!options.isTargetValue(value)) {
                continue;
            }
            final Object convertedValue =
                options.convertValue(
                    value,
                    destPropertyName,
                    destPropertyDesc.getPropertyType());
            destPropertyDesc.setValue(dest, convertedValue);
        }
    }

    /**
     * {@literal Map}から{@literal Map}にコピーを行います。
     * 
     * @param src
     *            コピー元の{@literal Map}
     * @param dest
     *            コピー先の{@literal Map}
     */
    public static void copyMapToMap(final Map<String, ? extends Object> src,
            final Map<String, Object> dest) {
        copyMapToMap(src, dest, DEFAULT_OPTIONS);
    }

    /**
     * {@literal Map}から{@literal Map}にコピーを行います。
     * 
     * @param src
     *            コピー元の{@literal Map}
     * @param dest
     *            コピー先の{@literal Map}
     * @param options
     *            コピーのオプション
     * @see CopyOptionsUtil
     */
    public static void copyMapToMap(final Map<String, ? extends Object> src,
            final Map<String, Object> dest, final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("dest", dest);
        assertArgumentNotNull("options", options);

        for (final Entry<String, ? extends Object> entry : src.entrySet()) {
            final String srcPropertyName = entry.getKey();
            if (!options.isTargetProperty(srcPropertyName)) {
                continue;
            }
            final Object value = src.get(srcPropertyName);
            if (!options.isTargetValue(value)) {
                continue;
            }
            final String destPropertyName = options.trimPrefix(srcPropertyName);
            final Object convertedValue =
                options.convertValue(value, destPropertyName, null);
            dest.put(destPropertyName, convertedValue);
        }
    }

    /**
     * コピー元のBeanを新しいBeanのインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となるBeanの型
     * @param src
     *            コピー元のBean
     * @param destClass
     *            コピー先となるBeanの型
     * @return コピーされた新しいBean
     */
    public static <T> T copyBeanToNewBean(final Object src,
            final Class<T> destClass) {
        return copyBeanToNewBean(src, destClass, DEFAULT_OPTIONS);
    }

    /**
     * コピー元のBeanを新しいBeanのインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となるBeanの型
     * @param src
     *            コピー元のBean
     * @param destClass
     *            コピー先となるBeanの型
     * @param options
     *            コピーのオプション
     * @return コピーされた新しいBean
     * @see CopyOptionsUtil
     */
    public static <T> T copyBeanToNewBean(final Object src,
            final Class<T> destClass, final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("destClass", destClass);
        assertArgumentNotNull("options", options);

        final T dest = ClassUtil.newInstance(destClass);
        copyBeanToBean(src, dest, options);
        return dest;
    }

    /**
     * コピー元の{@literal Map}を新しいBeanのインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となるBeanの型
     * @param src
     *            コピー元の{@literal Map}
     * @param destClass
     *            コピー先となるBeanの型
     * @return コピーされた新しい{@literal Map}
     */
    public static <T> T copyMapToNewBean(
            final Map<String, ? extends Object> src, final Class<T> destClass) {
        return copyMapToNewBean(src, destClass, DEFAULT_OPTIONS);
    }

    /**
     * コピー元の{@literal Map}を新しいBeanのインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となるBeanの型
     * @param src
     *            コピー元の{@literal Map}
     * @param destClass
     *            コピー先となるBeanの型
     * @param options
     *            コピーのオプション
     * @return コピーされた新しい{@literal Map}
     * @see CopyOptionsUtil
     */
    public static <T> T copyMapToNewBean(
            final Map<String, ? extends Object> src, final Class<T> destClass,
            final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("destClass", destClass);
        assertArgumentNotNull("options", options);

        final T dest = ClassUtil.newInstance(destClass);
        copyMapToBean(src, dest, options);
        return dest;
    }

    /**
     * コピー元のBeanを新しい{@literal LinkedHashMap}のインスタンスにコピーして返します。
     * 
     * @param src
     *            コピー元のBean
     * @return コピーされた新しいBean
     */
    public static Map<String, Object> copyBeanToNewMap(final Object src) {
        return copyBeanToNewMap(src, DEFAULT_OPTIONS);
    }

    /**
     * コピー元のBeanを新しい{@literal LinkedHashMap}のインスタンスにコピーして返します。
     * 
     * @param src
     *            コピー元のBean
     * @param options
     *            コピーのオプション
     * @return コピーされた新しいBean
     * @see CopyOptionsUtil
     */
    public static Map<String, Object> copyBeanToNewMap(final Object src,
            final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("options", options);

        final Map<String, Object> dest = newLinkedHashMap();
        copyBeanToMap(src, dest, options);
        return dest;
    }

    /**
     * コピー元のBeanを新しい{@literal Map}のインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となる{@literal Map}の型
     * @param src
     *            コピー元のBean
     * @param destClass
     *            コピー先となる{@literal Map}の型
     * @return コピーされた新しい{@literal Map}
     */
    public static <T extends Map<String, Object>> T copyBeanToNewMap(
            final Object src, final Class<? extends T> destClass) {
        return copyBeanToNewMap(src, destClass, DEFAULT_OPTIONS);
    }

    /**
     * コピー元のBeanを新しい{@literal Map}のインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となる{@literal Map}の型
     * @param src
     *            コピー元のBean
     * @param destClass
     *            コピー先となる{@literal Map}の型
     * @param options
     *            コピーのオプション
     * @return コピーされた新しい{@literal Map}
     * @see CopyOptionsUtil
     */
    public static <T extends Map<String, Object>> T copyBeanToNewMap(
            final Object src, final Class<? extends T> destClass,
            final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("destClass", destClass);
        assertArgumentNotNull("options", options);

        final T dest = ClassUtil.newInstance(destClass);
        copyBeanToMap(src, dest, options);
        return dest;
    }

    /**
     * コピー元の{@literal Map}を新しい{@literal LinkedHashMap}のインスタンスにコピーして返します。
     * 
     * @param src
     *            コピー元の{@literal Map}
     * @return コピーされた新しい{@literal Map}
     */
    public static Map<String, Object> copyMapToNewMap(
            final Map<String, ? extends Object> src) {
        return copyMapToNewMap(src, DEFAULT_OPTIONS);
    }

    /**
     * コピー元の{@literal Map}を新しい{@literal LinkedHashMap}のインスタンスにコピーして返します。
     * 
     * @param src
     *            コピー元の{@literal Map}
     * @param options
     *            コピーのオプション
     * @return コピーされた新しい{@literal Map}
     * @see CopyOptionsUtil
     */
    public static Map<String, Object> copyMapToNewMap(
            final Map<String, ? extends Object> src, final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("options", options);

        final Map<String, Object> dest = newLinkedHashMap();
        copyMapToMap(src, dest, options);
        return dest;
    }

    /**
     * コピー元の{@literal Map}を新しい{@literal Map}のインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となる{@literal Map}の型
     * @param src
     *            コピー元の{@literal Map}
     * @param destClass
     *            コピー先となる{@literal Map}の型
     * @return コピーされた新しい{@literal Map}
     */
    public static <T extends Map<String, Object>> T copyMapToNewMap(
            final Map<String, ? extends Object> src,
            final Class<? extends T> destClass) {
        return copyMapToNewMap(src, destClass, DEFAULT_OPTIONS);
    }

    /**
     * コピー元の{@literal Map}を新しい{@literal Map}のインスタンスにコピーして返します。
     * 
     * @param <T>
     *            コピー先となる{@literal Map}の型
     * @param src
     *            コピー元の{@literal Map}
     * @param destClass
     *            コピー先となる{@literal Map}の型
     * @param options
     *            コピーのオプション
     * @return コピーされた新しい{@literal Map}
     * @see CopyOptionsUtil
     */
    public static <T extends Map<String, Object>> T copyMapToNewMap(
            final Map<String, ? extends Object> src,
            final Class<? extends T> destClass, final CopyOptions options) {
        assertArgumentNotNull("src", src);
        assertArgumentNotNull("destClass", destClass);
        assertArgumentNotNull("options", options);

        final T dest = ClassUtil.newInstance(destClass);
        copyMapToMap(src, dest, options);
        return dest;
    }

}
