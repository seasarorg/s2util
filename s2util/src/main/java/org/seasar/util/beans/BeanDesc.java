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
     * {@link FieldDesc}を持っているかどうかを返します。
     * 
     * @param fieldName
     *            フィールド名
     * @return {@link FieldDesc}を持っているかどうか
     */
    boolean hasFieldDesc(String fieldName);

    /**
     * {@link FieldDesc}を返します。
     * 
     * @param fieldName
     *            フィールド名
     * @return {@link FieldDesc}
     */
    FieldDesc getFieldDesc(String fieldName);

    /**
     * {@link FieldDesc}を返します。
     * 
     * @param index
     *            {@link FieldDesc}のインデックス
     * @return {@link FieldDesc}
     */
    FieldDesc getFieldDesc(int index);

    /**
     * {@link FieldDesc}の数を返します。
     * 
     * @return {@link FieldDesc}の数
     */
    int getFieldDescSize();

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
     * 引数の型に応じた{@link ConstructorDesc}を返します。
     * 
     * @param paramTypes
     *            コンストラクタに渡す引数型の並び
     * @return 引数の型に応じた{@link ConstructorDesc}
     */
    ConstructorDesc getConstructorDesc(Class<?>... paramTypes);

    /**
     * 引数に適合する{@link ConstructorDesc}を返します。
     * 
     * @param args
     *            コンストラクタに渡す引数の並び
     * @return 引数に適合する{@link Constructor}
     */
    ConstructorDesc getSuitableConstructorDesc(Object... args);

    /**
     * 引数の型に応じた{@link MethodDesc}を返します。
     * 
     * @param methodName
     *            メソッド名
     * @param paramTypes
     *            メソッドの引数型の並び
     * @return 引数の型に応じた{@link MethodDesc} メソッド
     */
    MethodDesc getMethodDesc(String methodName, Class<?>... paramTypes);

    /**
     * 引数の型に応じた{@link MethodDesc}を返します。見つからない場合は、{@literal null}を返します。
     * 
     * @param methodName
     *            メソッド
     * @param paramTypes
     *            メソッドの引数型の並び
     * @return 引数の型に応じた{@link MethodDesc}
     */
    MethodDesc getMethodDescNoException(String methodName,
            Class<?>... paramTypes);

    /**
     * 引数に適合する{@link MethodDesc}を返します。
     * 
     * @param methodName
     *            メソッド名
     * @param args
     *            メソッドの引数の並び
     * @return 引数に適合する{@link MethodDesc} メソッド
     */
    MethodDesc getSuitableMethodDesc(String methodName, Object... args);

    /**
     * {@link MethodDesc}があるかどうか返します。
     * 
     * @param methodName
     *            メソッド名
     * @return {@link MethodDesc}があるかどうか
     */
    boolean hasMethodDesc(String methodName);

    /**
     * {@link MethodDesc}の配列を返します。
     * 
     * @param methodName
     * @return {@link MethodDesc}の配列
     */
    MethodDesc[] getMethodDescs(String methodName);

    /**
     * メソッド名の配列を返します。
     * 
     * @return メソッド名の配列
     */
    String[] getMethodNames();

}
