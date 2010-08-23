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

import org.seasar.util.exception.SRuntimeException;

/**
 * プロパティの値の設定に失敗したときにスローされる例外です。
 * 
 * @author higa
 * 
 */
public class IllegalPropertyRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 3584516316082904020L;

    private final Class<?> targetClass;

    private final String propertyName;

    /**
     * {@link IllegalPropertyRuntimeException}を作成します。
     * 
     * @param targetClass
     *            ターゲットクラス
     * @param propertyName
     *            プロパティ名
     * @param cause
     *            原因となった例外
     */
    public IllegalPropertyRuntimeException(final Class<?> targetClass,
            final String propertyName, final Throwable cause) {
        super("EUTL0059", targetClass.getName(), propertyName, cause);
        initCause(cause);
        this.targetClass = targetClass;
        this.propertyName = propertyName;
    }

    @Override
    public IllegalPropertyRuntimeException initCause(final Throwable cause) {
        return (IllegalPropertyRuntimeException) super.initCause(cause);
    }

    /**
     * ターゲットクラスを返します。
     * 
     * @return ターゲットクラス
     */
    public Class<?> getTargetClass() {
        return targetClass;
    }

    /**
     * プロパティ名を返します。
     * 
     * @return プロパティ名
     */
    public String getPropertyName() {
        return propertyName;
    }

}
