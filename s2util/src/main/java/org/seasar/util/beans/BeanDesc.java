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
package org.seasar.util.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

/**
 * Bean(JavaBeans)を扱うためのインターフェースです。
 * 
 * @author higa
 */
public interface BeanDesc {

    /**
     * Beanのクラスを返します。
     * 
     * @param <T>
     *            Beanのクラス
     * @return Beanのクラス
     */
    <T> Class<T> getBeanClass();

    /**
     * 型変数から型引数へのマップを返します。
     * 
     * @return 型変数から型引数へのマップ
     */
    Map<TypeVariable<?>, Type> getTypeVariables();

    /**
     * {@link PropertyDesc}を持っているかどうかを返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return {@link PropertyDesc}を持っているかどうか
     */
    boolean hasPropertyDesc(String propertyName);

    /**
     * {@link PropertyDesc}を返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return {@link PropertyDesc}
     */
    PropertyDesc getPropertyDesc(String propertyName);

    /**
     * {@link PropertyDesc}を返します。
     * 
     * @param index
     *            {@link PropertyDesc}のインデックス
     * @return {@link PropertyDesc}
     */
    PropertyDesc getPropertyDesc(int index);

    /**
     * {@link PropertyDesc}の数を返します。
     * 
     * @return {@link PropertyDesc}の数
     */
    int getPropertyDescSize();

    /**
     * {@link Field}を持っているかどうかを返します。
     * 
     * @param fieldName
     *            フィールド名
     * @return {@link Field}を持っているかどうか
     */
    boolean hasField(String fieldName);

    /**
     * {@link Field}を返します。
     * 
     * @param fieldName
     *            フィールド名
     * @return {@link Field}
     */
    Field getField(String fieldName);

    /**
     * {@link Field}の値を返します。
     * 
     * @param <T>
     *            フィールドの型
     * @param fieldName
     * @param target
     * @return {@link Field}の値
     */
    <T> T getFieldValue(String fieldName, Object target);

    /**
     * {@link Field}を返します。
     * 
     * @param index
     *            {@link Field}のインデックス
     * @return {@link Field}
     */
    Field getField(int index);

    /**
     * {@link Field}の数を返します。
     * 
     * @return {@link Field}の数
     */
    int getFieldSize();

    /**
     * 新しいインスタンスを作成します。
     * 
     * @param <T>
     *            Beanクラスの型
     * @param args
     *            コンストラクタに渡す引数の並び
     * @return 新しいインスタンス
     */
    <T> T newInstance(Object... args);

    /**
     * 引数に応じた{@link Constructor}を返します。
     * 
     * @param <T>
     *            Beanクラスの型
     * @param args
     *            コンストラクタに渡す引数の並び
     * @return 引数に応じた{@link Constructor}
     */
    <T> Constructor<T> getSuitableConstructor(Object... args);

    /**
     * 引数の型に応じた{@link Constructor}を返します。
     * 
     * @param <T>
     *            Beanクラスの型
     * @param paramTypes
     *            コンストラクタに渡す引数型の並び
     * @return 型に応じた{@link Constructor}
     */
    <T> Constructor<T> getConstructor(Class<?>... paramTypes);

    /**
     * ターゲットのメソッドを呼び出します。
     * 
     * @param <T>
     *            メソッドの戻り値の型
     * @param target
     *            ターゲットオブジェクト
     * @param methodName
     *            メソッド名
     * @param args
     *            メソッドに渡す引数の並び
     * @return メソッドの戻り値
     */
    <T> T invoke(Object target, String methodName, Object... args);

    /**
     * staticなメソッドを呼び出します。
     * 
     * @param <T>
     *            メソッドの戻り値の型
     * @param methodName
     *            メソッド名
     * @param args
     *            メソッドに渡す引数の並び
     * @return メソッドの戻り値
     */
    <T> T invokeStatic(String methodName, Object... args);

    /**
     * 引数に応じた{@link Method}を返します。
     * 
     * @param methodName
     *            メソッド名
     * @param args
     *            メソッドの引数の並び
     * @return {@link Method} メソッド
     */
    Method getSuitableMethod(String methodName, Object... args);

    /**
     * 引数の型に応じた{@link Method}を返します。
     * 
     * @param methodName
     *            メソッド名
     * @param paramTypes
     *            メソッドの引数型の並び
     * @return {@link Method} メソッド
     */
    Method getMethod(String methodName, Class<?>... paramTypes);

    /**
     * {@link Method}を返します。見つからない場合は、nullを返します。
     * 
     * @param methodName
     *            メソッド
     * @param paramTypes
     *            メソッドの引数型の並び
     * @return {@link Method}
     */
    Method getMethodNoException(String methodName, Class<?>... paramTypes);

    /**
     * {@link Method}の配列を返します。
     * 
     * @param methodName
     * @return メソッドの配列
     */
    Method[] getMethods(String methodName);

    /**
     * {@link Method}があるかどうか返します。
     * 
     * @param methodName
     *            メソッド名
     * @return {@link Method}があるかどうか
     */
    boolean hasMethod(String methodName);

    /**
     * メソッド名の配列を返します。
     * 
     * @return メソッド名の配列
     */
    String[] getMethodNames();

}
