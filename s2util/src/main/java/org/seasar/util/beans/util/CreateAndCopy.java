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

import org.seasar.util.lang.ClassUtil;
import org.seasar.util.lang.ModifierUtil;

import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * JavaBeansやMapを作成し、プロパティをコピーするクラスです。
 * 
 * @author higa
 * @param <T>
 *            作成するタイプ
 */
public class CreateAndCopy<T> extends AbstractCopy<CreateAndCopy<T>> {

    /**
     * 作成対象クラス
     */
    protected final Class<T> destClass;

    /**
     * コピー元です。
     */
    protected final Object src;

    /**
     * インスタンスを構築します。
     * 
     * @param destClass
     *            作成対象クラス
     * @param src
     *            コピー元
     * @throws NullPointerException
     *             引数が<code>null</code>だった場合
     */
    public CreateAndCopy(final Class<T> destClass, final Object src)
            throws NullPointerException {
        if (destClass == null) {
            throw new NullPointerException("destClass");
        }
        if (src == null) {
            throw new NullPointerException("src");
        }
        this.destClass = destClass;
        this.src = src;
    }

    /**
     * JavaBeansやMapを作成し、プロパティをコピーします。
     * 
     * @return 作成結果
     */
    @SuppressWarnings("unchecked")
    public T execute() {
        if (Map.class.isAssignableFrom(destClass)) {
            final Map<String, Object> dest;
            if (ModifierUtil.isAbstract(destClass)) {
                dest = newHashMap();
            } else {
                dest = (Map<String, Object>) ClassUtil.newInstance(destClass);
            }
            if (src instanceof Map) {
                copyMapToMap((Map<String, Object>) src, dest);
            } else {
                copyBeanToMap(src, dest);
            }
            return (T) dest;
        }
        final T dest = ClassUtil.newInstance(destClass);
        if (src instanceof Map) {
            copyMapToBean((Map<String, Object>) src, dest);
        } else {
            copyBeanToBean(src, dest);
        }
        return dest;
    }

}
