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
package org.seasar.util.beans.converter;

import java.util.Date;

import org.seasar.util.beans.Converter;
import org.seasar.util.convert.DateConversionUtil;
import org.seasar.util.convert.StringConversionUtil;
import org.seasar.util.exception.EmptyRuntimeException;
import org.seasar.util.lang.StringUtil;

/**
 * 日付用のコンバータです。
 * 
 * @author higa
 */
public class DateConverter implements Converter {

    /**
     * 日付のパターンです。
     */
    protected String pattern;

    /**
     * インスタンスを構築します。
     * 
     * @param pattern
     *            日付のパターン
     */
    public DateConverter(final String pattern) {
        if (StringUtil.isEmpty(pattern)) {
            throw new EmptyRuntimeException("pattern");
        }
        this.pattern = pattern;
    }

    @Override
    public Object getAsObject(final String value) {
        if (StringUtil.isEmpty(value)) {
            return null;
        }
        return DateConversionUtil.toDate(value, pattern);
    }

    @Override
    public String getAsString(final Object value) {
        return StringConversionUtil.toString((Date) value, pattern);
    }

    @Override
    public boolean isTarget(final Class<?> clazz) {
        return clazz == Date.class;
    }

}