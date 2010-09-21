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
package org.seasar.util.beans.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.seasar.util.beans.ParameterizedClassDesc;
import org.seasar.util.beans.PropertyDesc;
import org.seasar.util.beans.impl.ParameterizedClassDescImpl;
import org.seasar.util.collection.Indexed;

import static org.seasar.util.collection.ArrayIterator.*;
import static org.seasar.util.collection.IndexedIterator.*;
import static org.seasar.util.lang.GenericsUtil.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * フィールの型やメソッドの引数型、戻り値型を表現する{@link ParameterizedClassDesc}を作成するファクトリです。
 * 
 * @author koichik
 * @see PropertyDesc#getParameterizedClassDesc()
 */
public abstract class ParameterizedClassDescFactory {

    /**
     * パラメータ化された型(クラスまたはインタフェース)が持つ型変数をキー、型引数を値とする{@link Map}を返します。
     * <p>
     * 型がパラメタ化されていない場合は空の{@link Map}を返します。
     * </p>
     * 
     * @param beanClass
     *            パラメータ化された型(クラスまたはインタフェース)
     * @return パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     */
    public static Map<TypeVariable<?>, Type> getTypeVariables(
            final Class<?> beanClass) {
        assertArgumentNotNull("beanClass", beanClass);

        return getTypeVariableMap(beanClass);
    }

    /**
     * フィールドの型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param field
     *            フィールド
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return フィールドの型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Field field, final Map<TypeVariable<?>, Type> map) {
        assertArgumentNotNull("field", field);
        assertArgumentNotNull("map", map);

        return createParameterizedClassDesc(field.getGenericType(), map);
    }

    /**
     * コンストラクタの引数型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param constructor
     *            コンストラクタ
     * @param index
     *            引数の位置
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Constructor<?> constructor, final int index,
            final Map<TypeVariable<?>, Type> map) {
        assertArgumentNotNull("constructor", constructor);
        assertArgumentNotNull("map", map);

        return createParameterizedClassDesc(
            constructor.getGenericParameterTypes()[index],
            map);
    }

    /**
     * メソッドの引数型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param method
     *            メソッド
     * @param index
     *            引数の位置
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return メソッドの引数型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final int index,
            final Map<TypeVariable<?>, Type> map) {
        assertArgumentNotNull("method", method);
        assertArgumentArrayIndex(
            "index",
            index,
            method.getParameterTypes().length);
        assertArgumentNotNull("map", map);

        return createParameterizedClassDesc(
            method.getGenericParameterTypes()[index],
            map);
    }

    /**
     * メソッドの戻り値型を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param method
     *            メソッド
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return メソッドの戻り値型を表現する{@link ParameterizedClassDesc}
     */
    public static ParameterizedClassDesc createParameterizedClassDesc(
            final Method method, final Map<TypeVariable<?>, Type> map) {
        assertArgumentNotNull("method", method);
        assertArgumentNotNull("map", map);

        return createParameterizedClassDesc(method.getGenericReturnType(), map);
    }

    /**
     * {@link Type}を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param type
     *            型
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return 型を表現する{@link ParameterizedClassDesc}
     */
    protected static ParameterizedClassDesc createParameterizedClassDesc(
            final Type type, final Map<TypeVariable<?>, Type> map) {
        final Class<?> rowClass = getActualClass(type, map);
        if (rowClass == null) {
            return null;
        }
        final ParameterizedClassDescImpl desc =
            new ParameterizedClassDescImpl(rowClass);
        final Type[] parameterTypes = getGenericParameters(type);
        if (parameterTypes == null) {
            return desc;
        }
        final ParameterizedClassDesc[] parameterDescs =
            new ParameterizedClassDesc[parameterTypes.length];
        for (final Indexed<Type> parameterType : indexed(iterable(parameterTypes))) {
            parameterDescs[parameterType.getIndex()] =
                createParameterizedClassDesc(parameterType.getElement(), map);
        }
        desc.setArguments(parameterDescs);
        return desc;
    }
}
