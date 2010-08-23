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
package org.seasar.util.exception;

/**
 * {@link NoSuchFieldException}をラップする例外です。
 * 
 * @author higa
 */
public class NoSuchFieldRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 6609175673610180338L;

    private final Class<?> targetClass;

    private final String fieldName;

    /**
     * {@link NoSuchFieldRuntimeException}を作成します。
     * 
     * @param targetClass
     *            ターゲットクラス
     * @param fieldName
     *            フィールド名
     */
    public NoSuchFieldRuntimeException(final Class<?> targetClass, final String fieldName) {
        super("EUTL0070", targetClass.getName(), fieldName);
        this.targetClass = targetClass;
        this.fieldName = fieldName;
    }

    @Override
    public NoSuchFieldRuntimeException initCause(final Throwable cause) {
        return (NoSuchFieldRuntimeException) super.initCause(cause);
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
     * フィールド名を返します。
     * 
     * @return フィールド名
     */
    public String getFieldName() {
        return fieldName;
    }

}
