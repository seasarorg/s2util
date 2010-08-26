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

import org.seasar.util.beans.BeanDesc;
import org.seasar.util.beans.PropertyDesc;
import org.seasar.util.beans.factory.BeanDescFactory;
import org.seasar.util.lang.StringUtil;

import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * JavaBeans用のユーティリティです。
 * 
 * @author Kimura Satoshi
 * @author higa
 */
public abstract class BeanUtil {

    /**
     * マップの値をJavaBeansにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     */
    public static void copyProperties(final Map<String, ?> src,
            final Object dest) {
        if (src == null || dest == null) {
            return;
        }
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(dest.getClass());
        for (final String key : src.keySet()) {
            if (!beanDesc.hasPropertyDesc(key)) {
                continue;
            }
            final PropertyDesc pd = beanDesc.getPropertyDesc(key);
            if (pd.isWritable()) {
                pd.setValue(dest, src.get(key));
            }
        }
    }

    /**
     * JavaBeansの値をマップにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     */
    public static void copyProperties(final Object src,
            final Map<String, Object> dest) {
        if (src == null || dest == null) {
            return;
        }
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(src.getClass());
        final int size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; ++i) {
            final PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (pd.isReadable()) {
                final Object value = pd.getValue(src);
                dest.put(pd.getPropertyName(), value);
            }
        }
    }

    /**
     * JavaBeansの値をJavaBeansにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     */
    public static void copyProperties(final Object src, final Object dest) {
        copyProperties(src, dest, true);
    }

    /**
     * JavaBeansの値をJavaBeansにコピーします。
     * 
     * @param src
     *            ソース
     * @param dest
     *            あて先
     * @param includesNull
     *            <code>null</code>を含めるかどうか
     */
    public static void copyProperties(final Object src, final Object dest,
            final boolean includesNull) {
        final BeanDesc srcBeanDesc =
            BeanDescFactory.getBeanDesc(src.getClass());
        final BeanDesc destBeanDesc =
            BeanDescFactory.getBeanDesc(dest.getClass());

        final int propertyDescSize = destBeanDesc.getPropertyDescSize();
        for (int i = 0; i < propertyDescSize; i++) {
            final PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(i);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            final String propertyName = destPropertyDesc.getPropertyName();
            if (!srcBeanDesc.hasPropertyDesc(propertyName)) {
                continue;
            }
            final PropertyDesc srcPropertyDesc =
                srcBeanDesc.getPropertyDesc(propertyName);
            if (!srcPropertyDesc.isReadable()) {
                continue;
            }
            final Object value = srcPropertyDesc.getValue(src);
            if (value != null || includesNull) {
                destPropertyDesc.setValue(dest, srcPropertyDesc.getValue(src));
            }
        }
    }

    /**
     * JavaBeansの値からマップを作成します。
     * 
     * @param src
     *            ソース
     * @return JavaBeansの値を持つマップ
     */
    public static Map<String, Object> createProperties(final Object src) {
        return createProperties(src, null);
    }

    /**
     * JavaBeansの値からマップを作成します。
     * 
     * @param src
     *            ソース
     * @param prefix
     *            プレフィックス
     * @return JavaBeansの値を持つマップ
     */
    public static Map<String, Object> createProperties(final Object src,
            final String prefix) {
        final Map<String, Object> map = newHashMap();
        if (src == null) {
            return map;
        }
        final BeanDesc beanDesc = BeanDescFactory.getBeanDesc(src.getClass());
        final int size = beanDesc.getPropertyDescSize();
        for (int i = 0; i < size; ++i) {
            final PropertyDesc pd = beanDesc.getPropertyDesc(i);
            if (!pd.isReadable()) {
                continue;
            }
            final Object value = pd.getValue(src);
            final String destName = pd.getPropertyName().replace('$', '.');
            if (StringUtil.isEmpty(prefix)) {
                map.put(destName, value);
            } else if (destName.startsWith(prefix)) {
                map.put(destName.substring(prefix.length()), value);
            }
        }
        return map;
    }

}
