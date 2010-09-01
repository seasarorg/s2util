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
package org.seasar.util.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.seasar.util.exception.IllegalAccessRuntimeException;
import org.seasar.util.exception.SIllegalArgumentException;

import static org.seasar.util.collection.ArrayUtil.*;

/**
 * {@link Field}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class FieldUtil {

    /**
     * {@link Field}によって表される{@code static}フィールドの値を返します。
     * 
     * @param <T>
     *            フィールドの型
     * @param field
     *            フィールド
     * @return {@code static}フィールドで表現される値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#get(Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(final Field field)
            throws IllegalAccessRuntimeException {
        return (T) get(field, null);
    }

    /**
     * 指定されたオブジェクトについて、{@link Field}によって表されるフィールドの値を返します。
     * 
     * @param <T>
     *            フィールドの型
     * @param field
     *            フィールド
     * @param target
     *            表現されるフィールド値の抽出元オブジェクト
     * @return オブジェクト{@code obj}内で表現される値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#get(Object)
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(final Field field, final Object target)
            throws IllegalAccessRuntimeException {
        try {
            return (T) field.get(target);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(
                field.getDeclaringClass(),
                ex);
        }
    }

    /**
     * staticな {@link Field}の値をintとして取得します。
     * 
     * @param field
     *            フィールド
     * @return フィールドの値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see #getInt(Field, Object)
     */
    public static int getInt(final Field field)
            throws IllegalAccessRuntimeException {
        return getInt(field, null);
    }

    /**
     * {@link Field}の値をintとして取得します。
     * 
     * @param field
     *            フィールド
     * @param target
     *            ターゲットオブジェクト
     * @return フィールドの値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Field#getInt(Object)
     */
    public static int getInt(final Field field, final Object target)
            throws IllegalAccessRuntimeException {
        try {
            return field.getInt(target);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(
                field.getDeclaringClass(),
                ex);
        }
    }

    /**
     * staticな {@link Field}の値を {@link String}として取得します。
     * 
     * @param field
     *            フィールド
     * @return フィールドの値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see #getString(Field, Object)
     */
    public static String getString(final Field field)
            throws IllegalAccessRuntimeException {
        return getString(field, null);
    }

    /**
     * {@link Field}の値を {@link String}として取得します。
     * 
     * @param field
     *            フィールド
     * @param target
     *            ターゲットオブジェクト
     * @return フィールドの値
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Field#get(Object)
     */
    public static String getString(final Field field, final Object target)
            throws IllegalAccessRuntimeException {
        try {
            return (String) field.get(target);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(
                field.getDeclaringClass(),
                ex);
        }
    }

    /**
     * {@link Field}オブジェクトによって表される{@code static}フィールドを、指定された新しい値に設定します。
     * 
     * @param field
     *            フィールド
     * @param value
     *            {@code static}フィールドの新しい値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#set(Object, Object)
     */
    public static void set(final Field field, final Object value)
            throws IllegalAccessRuntimeException {
        set(field, null, value);
    }

    /**
     * {@link Field}オブジェクトによって表される指定されたオブジェクト引数のフィールドを、指定された新しい値に設定します。
     * 
     * @param field
     *            フィールド
     * @param target
     *            フィールドを変更するオブジェクト
     * @param value
     *            変更中の{@code target}の新しいフィールド値
     * @throws IllegalAccessRuntimeException
     *             基本となるフィールドにアクセスできない場合
     * @see Field#set(Object, Object)
     */
    public static void set(final Field field, final Object target,
            final Object value) throws IllegalAccessRuntimeException {
        try {
            field.set(target, value);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(
                field.getDeclaringClass(),
                e);
        } catch (final IllegalArgumentException e) {
            final Class<?> clazz = field.getDeclaringClass();
            final Class<?> fieldClass = field.getType();
            final Class<?> valueClass = value == null ? null : value.getClass();
            final Class<?> targetClass =
                target == null ? field.getDeclaringClass() : target.getClass();
            throw new SIllegalArgumentException("field", "EUTL0094", asArray(
                clazz.getName(),
                clazz.getClassLoader(),
                fieldClass.getName(),
                fieldClass.getClassLoader(),
                field.getName(),
                valueClass == null ? null : valueClass.getName(),
                valueClass == null ? null : valueClass.getClassLoader(),
                value,
                targetClass == null ? null : targetClass.getName(),
                targetClass == null ? null : targetClass.getClassLoader()), e);
        }
    }

    /**
     * インスタンスフィールドかどうか返します。
     * 
     * @param field
     *            フィールド
     * @return インスタンスフィールドかどうか
     */
    public static boolean isInstanceField(final Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

    /**
     * パブリックフィールドかどうか返します。
     * 
     * @param field
     *            フィールド
     * @return パブリックフィールドかどうか
     */
    public static boolean isPublicField(final Field field) {
        return Modifier.isPublic(field.getModifiers());
    }

    /**
     * ファイナルフィールドかどうか返します。
     * 
     * @param field
     *            フィールド
     * @return ファイナルフィールドかどうか
     */
    public static boolean isFinalField(final Field field) {
        return Modifier.isFinal(field.getModifiers());
    }

    /**
     * パラメタ化されたコレクション型のフィールドの要素型を返します。
     * 
     * @param field
     *            パラメタ化されたコレクション型のフィールド
     * @return コレクションの要素型
     */
    public static Class<?> getElementTypeOfCollection(final Field field) {
        final Type type = field.getGenericType();
        return GenericsUtil.getRawClass(GenericsUtil
            .getElementTypeOfCollection(type));
    }

    /**
     * パラメタ化されたマップ型のフィールドのキー型を返します。
     * 
     * @param field
     *            パラメタ化されたマップ型のフィールド
     * @return マップのキー型
     */
    public static Class<?> getKeyTypeOfMap(final Field field) {
        final Type type = field.getGenericType();
        return GenericsUtil.getRawClass(GenericsUtil.getKeyTypeOfMap(type));
    }

    /**
     * パラメタ化されたマップ型のフィールドの値型を返します。
     * 
     * @param field
     *            パラメタ化されたマップ型のフィールド
     * @return マップの値型
     */
    public static Class<?> getValueTypeOfMap(final Field field) {
        final Type type = field.getGenericType();
        return GenericsUtil.getRawClass(GenericsUtil.getValueTypeOfMap(type));
    }

}
