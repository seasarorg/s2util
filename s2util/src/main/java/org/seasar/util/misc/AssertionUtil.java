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
package org.seasar.util.misc;

import java.util.Collection;
import java.util.Map;

import org.seasar.util.collection.ArrayUtil;
import org.seasar.util.exception.EmptyRuntimeException;
import org.seasar.util.exception.NullArgumentException;
import org.seasar.util.exception.SIllegalArgumentException;
import org.seasar.util.lang.StringUtil;

import static org.seasar.util.collection.ArrayUtil.*;

/**
 * 表明についてのユーティリティクラスです。
 * 
 * @author shot
 */
public abstract class AssertionUtil {

    /**
     * 引数が<code>null</code>でないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws NullArgumentException
     *             引数が<code>null</code>の場合。
     */
    public static void assertArgumentNotNull(final String argName,
            final Object argValue) {
        if (argValue == null) {
            throw new NullArgumentException(argName);
        }
    }

    /**
     * 引数が<code>null</code>でも空文字列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空文字列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final String argValue) {
        if (StringUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0010",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final Object[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final boolean[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final byte[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final short[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final int[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final long[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final float[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final double[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の配列でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の配列の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final char[] argValue) {
        if (ArrayUtil.isEmpty(argValue)) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0011",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の{@link Collection}でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の{@link Collection}の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final Collection<?> argValue) {
        if (argValue == null || argValue.isEmpty()) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0012",
                asArray(argValue));
        }
    }

    /**
     * 引数が<code>null</code>でも空の{@link Map}でもないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param argValue
     *            引数の値
     * @throws SIllegalArgumentException
     *             引数が<code>null</code>または空の{@link Map}の場合。
     */
    public static void assertArgumentNotEmpty(final String argName,
            final Map<?, ?> argValue) {
        if (argValue == null || argValue.isEmpty()) {
            throw new SIllegalArgumentException(
                argName,
                "EUTL0013",
                asArray(argValue));
        }
    }

    /**
     * 引数が不正でないことを表明します。
     * 
     * @param argName
     *            不正であってはならない引数の名前
     * 
     * @param expression
     *            事前条件
     * 
     * @param description
     *            不正な引数であることの説明
     * 
     * @throws SIllegalArgumentException
     *             {@code expression}がfalseの場合。
     */
    public static void assertArgument(final String argName,
            final boolean expression, final String description) {
        if (!expression) {
            throw new SIllegalArgumentException(argName, "EUTL0009", asArray(
                argName,
                description));
        }
    }

    /**
     * 文字列が空あるいは<code>null</code>でないことを表明します。
     * 
     * @param message
     * @param s
     * @throws EmptyRuntimeException
     *             文字列が空あるいは<code>null</code>の場合。
     */
    public static void assertNotEmpty(final String message, final String s) {
        if (StringUtil.isEmpty(s)) {
            throw new EmptyRuntimeException(message);
        }
    }

}
