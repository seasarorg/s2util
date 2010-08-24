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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

import org.seasar.framework.util.tiger.GenericUtil;
import org.seasar.util.exception.IllegalAccessRuntimeException;
import org.seasar.util.exception.InvocationTargetRuntimeException;

/**
 * {@link Method}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class MethodUtil {

    /**
     * メソッドを呼び出します．
     * 
     * @param <T>
     *            戻り値の型
     * @param method
     *            メソッド
     * @param target
     *            ターゲットオブジェクト
     * @param args
     *            引数の並び
     * @return 呼び出したメソッドの戻り値
     * @throws InvocationTargetRuntimeException
     *             {@link InvocationTargetException}が発生した場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Method#invoke(Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public static <T> T invoke(final Method method, final Object target,
            final Object... args) throws InvocationTargetRuntimeException,
            IllegalAccessRuntimeException {
        try {
            return (T) method.invoke(target, args);
        } catch (final InvocationTargetException ex) {
            final Throwable t = ex.getCause();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            if (t instanceof Error) {
                throw (Error) t;
            }
            throw new InvocationTargetRuntimeException(
                method.getDeclaringClass(),
                ex);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(
                method.getDeclaringClass(),
                ex);
        }
    }

    /**
     * staticメソッドを呼び出します．
     * 
     * @param <T>
     *            戻り値の型
     * @param method
     *            メソッド
     * @param args
     *            引数の並び
     * @return 呼び出したメソッドの戻り値
     * @throws InvocationTargetRuntimeException
     *             {@link InvocationTargetException}が発生した場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が発生した場合
     * @see Method#invoke(Object, Object...)
     */
    @SuppressWarnings("unchecked")
    public static <T> T invokeStatic(final Method method, final Object... args)
            throws InvocationTargetRuntimeException,
            IllegalAccessRuntimeException {
        return (T) invoke(method, null, args);
    }

    /**
     * <code>abstract</code>かどうかを返します。
     * 
     * @param method
     *            メソッド
     * @return <code>abstract</code>かどうか
     */
    public static boolean isAbstract(final Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }

    /**
     * <code>public</code>かどうかを返します。
     * 
     * @param method
     *            メソッド
     * @return <code>public</code>かどうか
     */
    public static boolean isPublic(final Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * <code>static</code>かどうかを返します。
     * 
     * @param method
     *            メソッド
     * @return <code>static</code>かどうか
     */
    public static boolean isStatic(final Method method) {
        return Modifier.isStatic(method.getModifiers());
    }

    /**
     * <code>final</code>かどうかを返します。
     * 
     * @param method
     *            メソッド
     * @return <code>final </code>かどうか
     */
    public static boolean isFinal(final Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    /**
     * シグニチャの文字列表現を返します。
     * 
     * @param methodName
     *            メソッド名
     * @param argTypes
     *            引数型のな浴び
     * @return シグニチャの文字列表現
     */
    public static String getSignature(final String methodName,
            final Class<?>... argTypes) {
        final StringBuilder buf = new StringBuilder(100);
        buf.append(methodName).append("(");
        if (argTypes != null && argTypes.length > 0) {
            for (final Class<?> argType : argTypes) {
                buf.append(argType.getName()).append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        return new String(buf);
    }

    /**
     * シグニチャの文字列表現を返します。
     * 
     * @param methodName
     *            メソッド名
     * @param methodArgs
     *            引数の並び
     * @return シグニチャの文字列表現
     */
    public static String getSignature(final String methodName,
            final Object... methodArgs) {
        final StringBuilder buf = new StringBuilder(100);
        buf.append(methodName).append("(");
        if (methodArgs != null && methodArgs.length > 0) {
            for (final Object arg : methodArgs) {
                buf
                    .append(arg == null ? null : arg.getClass().getName())
                    .append(", ");
            }
            buf.setLength(buf.length() - 2);
        }
        buf.append(")");
        return buf.toString();
    }

    /**
     * equalsメソッドかどうかを返します。
     * 
     * @param method
     * @return equalsメソッドかどうか
     */
    public static boolean isEqualsMethod(final Method method) {
        return method != null && method.getName().equals("equals")
            && method.getReturnType() == boolean.class
            && method.getParameterTypes().length == 1
            && method.getParameterTypes()[0] == Object.class;
    }

    /**
     * hashCodeメソッドかどうか返します。
     * 
     * @param method
     * @return hashCodeメソッドかどうか
     */
    public static boolean isHashCodeMethod(final Method method) {
        return method != null && method.getName().equals("hashCode")
            && method.getReturnType() == int.class
            && method.getParameterTypes().length == 0;
    }

    /**
     * toStringメソッドかどうか返します。
     * 
     * @param method
     * @return toStringメソッドかどうか
     */
    public static boolean isToStringMethod(final Method method) {
        return method != null && method.getName().equals("toString")
            && method.getReturnType() == String.class
            && method.getParameterTypes().length == 0;
    }

    /**
     * メソッドの引数型 (パラメタ化されたコレクション) の要素型を返します。
     * 
     * @param method
     *            メソッド
     * @param position
     *            パラメタ化されたコレクションが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたコレクションの要素型
     */
    public static Class<?> getElementTypeOfCollectionFromParameterType(
            final Method method, final int position) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        return GenericUtil.getRawClass(GenericUtil
            .getElementTypeOfCollection(parameterTypes[position]));
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたコレクションの要素型
     */
    public static Class<?> getElementTypeOfCollectionFromReturnType(
            final Method method) {
        final Type returnType = method.getGenericReturnType();
        return GenericUtil.getRawClass(GenericUtil
            .getElementTypeOfCollection(returnType));
    }

    /**
     * メソッドの引数型 (パラメタ化されたマップ) のキー型を返します。
     * 
     * @param method
     *            メソッド
     * @param position
     *            パラメタ化されたマップが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたマップのキー型
     */
    public static Class<?> getKeyTypeOfMapFromParameterType(
            final Method method, final int position) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        return GenericUtil.getRawClass(GenericUtil
            .getKeyTypeOfMap(parameterTypes[position]));
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたマップのキー型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたマップのキー型
     */
    public static Class<?> getKeyTypeOfMapFromReturnType(final Method method) {
        final Type returnType = method.getGenericReturnType();
        return GenericUtil.getRawClass(GenericUtil.getKeyTypeOfMap(returnType));
    }

    /**
     * メソッドの引数型 (パラメタ化されたマップ) の値型を返します。
     * 
     * @param method
     *            メソッド
     * @param position
     *            パラメタ化されたマップが宣言されているメソッド引数の位置
     * @return 指定されたメソッドの引数型として宣言されているパラメタ化されたマップの値型
     */
    public static Class<?> getValueTypeOfMapFromParameterType(
            final Method method, final int position) {
        final Type[] parameterTypes = method.getGenericParameterTypes();
        return GenericUtil.getRawClass(GenericUtil
            .getValueTypeOfMap(parameterTypes[position]));
    }

    /**
     * 指定されたメソッドの戻り値型として宣言されているパラメタ化されたマップの値型を返します。
     * 
     * @param method
     *            メソッド
     * @return 指定されたメソッドの戻り値型として宣言されているパラメタ化されたマップの値型
     */
    public static Class<?> getValueTypeOfMapFromReturnType(final Method method) {
        final Type returnType = method.getGenericReturnType();
        return GenericUtil.getRawClass(GenericUtil
            .getValueTypeOfMap(returnType));
    }

}
