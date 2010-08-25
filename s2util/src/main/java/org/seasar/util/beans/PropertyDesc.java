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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

/**
 * プロパティを扱うためのインターフェースです。
 * 
 * @author higa
 */
public interface PropertyDesc {

    /**
     * プロパティ名を返します。
     * 
     * @return プロパティ名
     */
    String getPropertyName();

    /**
     * プロパティの型を返します。
     * 
     * @param <T>
     *            プロパティの型
     * @return プロパティの型
     */
    <T> Class<T> getPropertyType();

    /**
     * getterメソッドを返します。
     * 
     * @return getterメソッド
     */
    Method getReadMethod();

    /**
     * getterメソッドを設定します。
     * 
     * @param readMethod
     *            getterメソッド
     */
    void setReadMethod(Method readMethod);

    /**
     * getterメソッドを持っているかどうか返します。
     * 
     * @return getterメソッドを持っているかどうか
     */
    boolean hasReadMethod();

    /**
     * setterメソッドを返します。
     * 
     * @return setterメソッド
     */
    Method getWriteMethod();

    /**
     * setterメソッドを設定します。
     * 
     * @param writeMethod
     *            setterメソッド
     */
    void setWriteMethod(Method writeMethod);

    /**
     * setterメソッドを持っているかどうか返します。
     * 
     * @return setterメソッドを持っているかどうか
     */
    boolean hasWriteMethod();

    /**
     * プロパティの値が取得できるかどうかを返します。
     * 
     * @return プロパティの値が取得できるかどうか
     */
    boolean isReadable();

    /**
     * プロパティの値が設定できるかどうかを返します。
     * 
     * @return プロパティの値が設定できるかどうか
     */
    boolean isWritable();

    /**
     * プロパティとして認識しているpublicフィールドを返します。
     * 
     * @return プロパティとして認識するpublicフィールド
     */
    Field getField();

    /**
     * プロパティとして認識しているpublicフィールドを設定します。
     * 
     * @param field
     *            プロパティとして認識するpublicフィールド
     */
    void setField(Field field);

    /**
     * プロパティの値を返します。
     * 
     * @param <T>
     *            プロパティの型
     * @param target
     *            ターゲットオブジェクト
     * @return プロパティの値
     */
    <T> T getValue(Object target);

    /**
     * プロパティに値を設定します。
     * 
     * @param target
     *            ターゲットオブジェクト
     * @param value
     *            プロパティに設定する値
     */
    void setValue(Object target, Object value);

    /**
     * プロパティの型に応じて必要なら適切に変換します。
     * 
     * @param <T>
     *            変換された型
     * @param value
     *            値
     * @return 変換された値
     */
    <T> T convertIfNeed(Object value);

    /**
     * {@link BeanDesc}を返します。
     * 
     * @return {@link BeanDesc}
     */
    BeanDesc getBeanDesc();

    /**
     * このプロパティがパラメタ化された型の場合に<code>true</code>を返します。
     * 
     * @return このプロパティがパラメタ化された型の場合に<code>true</code>
     */
    boolean isParameterized();

    /**
     * このプロパティがパラメタ化された型の場合、その情報を返します。
     * 
     * @return このプロパティがパラメタ化された型の場合、その情報
     */
    ParameterizedClassDesc getParameterizedClassDesc();

    /**
     * このプロパティがパラメタ化された{@link Collection}の場合、その要素型を返します。
     * 
     * @return このプロパティがパラメタ化された{@link Collection}の場合、その要素型
     */
    Class<?> getElementClassOfCollection();

    /**
     * このプロパティがパラメタ化された{@link Map}の場合、そのキー型を返します。
     * 
     * @return このプロパティがパラメタ化された{@link Map}の場合、そのキー型
     */
    Class<?> getKeyClassOfMap();

    /**
     * このプロパティがパラメタ化された{@link Map}の場合、その値型を返します。
     * 
     * @return このプロパティがパラメタ化された{@link Map}の場合、その値型
     */
    Class<?> getValueClassOfMap();

}
