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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.seasar.util.exception.IllegalAccessRuntimeException;
import org.seasar.util.exception.InstantiationRuntimeException;
import org.seasar.util.exception.InvocationTargetRuntimeException;

/**
 * {@link Constructor}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ConstructorUtil {

    /**
     * 指定された初期化パラメータで、コンストラクタの宣言クラスの新しいインスタンスを作成および初期化します。
     * 
     * @param <T>
     *            コンストラクタの宣言クラス
     * @param constructor
     *            コンストラクタ
     * @param args
     *            コンストラクタ呼び出しに引数として渡すオブジェクトの配列
     * @return コンストラクタを呼び出すことで作成される新規オブジェクト
     * @throws InstantiationRuntimeException
     *             基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
     * @throws IllegalAccessRuntimeException
     *             実パラメータ数と仮パラメータ数が異なる場合、 プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
     *             メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
     *             このコンストラクタが列挙型に関連している場合
     * @see Constructor#newInstance(Object[])
     */
    public static <T> T newInstance(final Constructor<T> constructor,
            final Object... args) throws InstantiationRuntimeException,
            IllegalAccessRuntimeException {
        try {
            return constructor.newInstance(args);
        } catch (final InstantiationException e) {
            throw new InstantiationRuntimeException(
                constructor.getDeclaringClass(),
                e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(
                constructor.getDeclaringClass(),
                e);
        } catch (final InvocationTargetException e) {
            throw new InvocationTargetRuntimeException(
                constructor.getDeclaringClass(),
                e);
        }
    }

}