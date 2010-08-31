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

import org.seasar.util.exception.EmptyRuntimeException;
import org.seasar.util.exception.NullArgumentException;
import org.seasar.util.exception.SIllegalArgumentException;
import org.seasar.util.lang.StringUtil;

/**
 * 表明についてのユーティリティクラスです。
 * 
 * @author shot
 */
public class AssertionUtil {

    /**
     * インスタンスを構築します。
     */
    protected AssertionUtil() {
    }

    /**
     * <code>null</code>でないことを表明します。
     * 
     * @param argName
     *            {@code null} であってはならない引数の名前
     * @param obj
     * @throws NullArgumentException
     *             <code>null</code>の場合。
     */
    public static void assertArgumentNotNull(String argName, Object obj)
            throws NullArgumentException {
        if (obj == null) {
            throw new NullArgumentException(argName);
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
    public static void assertArgument(String argName, boolean expression,
            String description) throws SIllegalArgumentException {
        if (!expression) {
            throw new SIllegalArgumentException("EUTL0009", new Object[] {
                argName, description });
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
    public static void assertNotEmpty(String message, String s)
            throws EmptyRuntimeException {
        if (StringUtil.isEmpty(s)) {
            throw new EmptyRuntimeException(message);
        }
    }

}
