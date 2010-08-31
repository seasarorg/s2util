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
package org.seasar.util.beans.util;

import java.sql.Time;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.seasar.util.beans.Converter;
import org.seasar.util.beans.converter.DateConverter;
import org.seasar.util.beans.converter.NumberConverter;
import org.seasar.util.beans.converter.SqlDateConverter;
import org.seasar.util.beans.converter.TimeConverter;
import org.seasar.util.beans.converter.TimestampConverter;
import org.seasar.util.convert.DateConversionUtil;
import org.seasar.util.convert.TimeConversionUtil;
import org.seasar.util.convert.TimestampConversionUtil;
import org.seasar.util.exception.ConverterRuntimeException;

import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * {@link BeanUtil}でJavaBeansや{@link Map}をコピーする際に指定するオプションです。
 * 
 * @author higa
 */
public class CopyOptions {

    /**
     * 日付用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_DATE_CONVERTER =
        new DateConverter(DateConversionUtil.getY4Pattern(Locale.getDefault()));

    /**
     * 時間用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_TIME_CONVERTER =
        new DateConverter(TimeConversionUtil.getPattern(Locale.getDefault()));

    /**
     * 日時用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_TIMESTAMP_CONVERTER =
        new DateConverter(TimestampConversionUtil.getPattern(Locale
            .getDefault()));

    /**
     * 操作の対象に含めるプロパティ名の配列です。
     */
    protected final List<String> includePropertyNames = newArrayList();

    /**
     * 操作の対象に含めないプロパティ名の配列です。
     */
    protected final List<String> excludePropertyNames = newArrayList();

    /**
     * null値のプロパティを操作の対象外にするかどうかです。
     */
    protected boolean excludesNull = false;

    /**
     * 空白のみの文字列を操作の対象外にするかどうかです。
     */
    protected boolean excludesWhitespace = false;

    /**
     * プレフィックスです。
     */
    protected String prefix;

    /**
     * JavaBeanのデリミタです。
     */
    protected char beanDelimiter = '$';

    /**
     * Mapのデリミタです。
     */
    protected char mapDelimiter = '.';

    /**
     * 特定のプロパティに関連付けられたコンバータです。
     */
    protected final Map<String, Converter> converterMap = newHashMap();

    /**
     * 特定のプロパティに関連付けられていないコンバータです。
     */
    protected final List<Converter> converters = newArrayList();

    /**
     * 操作の対象に含めるプロパティ名を追加します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions include(final CharSequence... propertyNames) {
        this.includePropertyNames.addAll(toStringList(propertyNames));
        return this;
    }

    /**
     * 操作の対象に含めないプロパティ名を追加します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions exclude(final CharSequence... propertyNames) {
        this.excludePropertyNames.addAll(toStringList(propertyNames));
        return this;
    }

    /**
     * null値のプロパティを操作の対象外にします。
     * 
     * @return このインスタンス自身
     */
    public CopyOptions excludeNull() {
        this.excludesNull = true;
        return this;
    }

    /**
     * 空白のプロパティを操作の対象外にします。
     * 
     * @return このインスタンス自身
     */
    public CopyOptions excludeWhitespace() {
        this.excludesWhitespace = true;
        return this;
    }

    /**
     * プレフィックスを指定します。
     * 
     * @param prefix
     *            プレフィックス
     * @return このインスタンス自身
     */
    public CopyOptions prefix(final CharSequence prefix) {
        this.prefix = prefix.toString();
        return this;
    }

    /**
     * JavaBeansのデリミタを設定します。
     * 
     * @param beanDelimiter
     *            JavaBeansのデリミタ
     * @return このインスタンス自身
     */
    public CopyOptions beanDelimiter(final char beanDelimiter) {
        this.beanDelimiter = beanDelimiter;
        return this;
    }

    /**
     * Mapのデリミタを設定します。
     * 
     * @param mapDelimiter
     *            Mapのデリミタ
     * @return このインスタンス自身
     */
    public CopyOptions mapDelimiter(final char mapDelimiter) {
        this.mapDelimiter = mapDelimiter;
        return this;
    }

    /**
     * コンバータを設定します。
     * 
     * @param converter
     * @param propertyNames
     * @return このインスタンス自身
     */
    public CopyOptions converter(final Converter converter,
            final CharSequence... propertyNames) {
        if (propertyNames == null || propertyNames.length == 0) {
            converters.add(converter);
        } else {
            for (final CharSequence name : propertyNames) {
                converterMap.put(name.toString(), converter);
            }
        }
        return this;
    }

    /**
     * 日付のコンバータを設定します。
     * 
     * @param pattern
     *            日付のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions dateConverter(final String pattern,
            final CharSequence... propertyNames) {
        return converter(new DateConverter(pattern), propertyNames);
    }

    /**
     * SQL用日付のコンバータを設定します。
     * 
     * @param pattern
     *            日付のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions sqlDateConverter(final String pattern,
            final CharSequence... propertyNames) {
        return converter(new SqlDateConverter(pattern), propertyNames);
    }

    /**
     * 時間のコンバータを設定します。
     * 
     * @param pattern
     *            時間のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions timeConverter(final String pattern,
            final CharSequence... propertyNames) {
        return converter(new TimeConverter(pattern), propertyNames);
    }

    /**
     * 日時のコンバータを設定します。
     * 
     * @param pattern
     *            日時のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions timestampConverter(final String pattern,
            final CharSequence... propertyNames) {
        return converter(new TimestampConverter(pattern), propertyNames);
    }

    /**
     * 数値のコンバータを設定します。
     * 
     * @param pattern
     *            数値のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    public CopyOptions numberConverter(final String pattern,
            final CharSequence... propertyNames) {
        return converter(new NumberConverter(pattern), propertyNames);
    }

    /**
     * {@literal CharSequence}の配列を{@literal String}の{@literal List}に変換します。
     * 
     * @param array
     *            {@literal CharSequence}の配列
     * @return {@literal String}の{@literal List}
     */
    protected static List<String> toStringList(final CharSequence[] array) {
        final List<String> list = newArrayList(array.length);
        for (final CharSequence element : array) {
            list.add(element.toString());
        }
        return list;
    }

    /**
     * 対象のプロパティかどうかを返します。
     * 
     * @param name
     *            プロパティ名
     * @return 対象のプロパティかどうか
     */
    protected boolean isTargetProperty(final String name) {
        if (prefix != null && !name.startsWith(prefix)) {
            return false;
        }
        if (!includePropertyNames.isEmpty()) {
            for (final String includeName : includePropertyNames) {
                if (includeName.equals(name)) {
                    for (final String excludeName : excludePropertyNames) {
                        if (excludeName.equals(name)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        if (!excludePropertyNames.isEmpty()) {
            for (final String excludeName : excludePropertyNames) {
                if (excludeName.equals(name)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    /**
     * 値がコピーの対象なら{@literal true}を返します。
     * 
     * @param value
     *            コピー元の値
     * @return 値がコピーの対象なら{@literal true}
     */
    protected boolean isTargetValue(final Object value) {
        if (value == null) {
            return !excludesNull;
        }
        if (value instanceof String && excludesWhitespace
            && ((String) value).trim().isEmpty()) {
            return !excludesWhitespace;
        }
        return true;
    }

    /**
     * コピー元のプロパティ名をコピー先となる{@literal Map}用のプロパティ名に変換して返します。
     * 
     * @param srcPropertyName
     *            コピー元のプロパティ名
     * @return コピー先のプロパティ名
     */
    protected String toMapDestPropertyName(final String srcPropertyName) {
        return trimPrefix(srcPropertyName.replace(beanDelimiter, mapDelimiter));
    }

    /**
     * コピー元のプロパティ名をコピー先となるBean用のプロパティ名に変換して返します。
     * 
     * @param srcPropertyName
     *            コピー元のプロパティ名
     * @return コピー先のプロパティ名
     */
    protected String toBeanDestPropertyName(final String srcPropertyName) {
        return trimPrefix(srcPropertyName.replace(mapDelimiter, beanDelimiter));
    }

    /**
     * プレフィックスを削ります。
     * 
     * @param propertyName
     *            プロパティ名
     * @return 削った結果
     */
    protected String trimPrefix(final String propertyName) {
        if (prefix == null) {
            return propertyName;
        }
        return propertyName.substring(prefix.length());
    }

    /**
     * 値を変換します。
     * 
     * @param value
     *            値
     * @param destPropertyName
     *            コピー先のプロパティ名
     * @param destPropertyClass
     *            コピー先のプロパティクラス
     * @return 変換後の値
     */
    protected Object convertValue(final Object value,
            final String destPropertyName, final Class<?> destPropertyClass) {
        if (value == null || value.getClass() != String.class
            && destPropertyClass != null && destPropertyClass != String.class) {
            return value;
        }
        Converter converter = converterMap.get(destPropertyName);
        if (converter == null) {
            final Class<?> targetClass;
            if (value.getClass() != String.class) {
                targetClass = value.getClass();
            } else {
                targetClass = destPropertyClass;
            }
            if (targetClass == null) {
                return value;
            }
            for (Class<?> clazz = targetClass; clazz != Object.class
                && clazz != null; clazz = clazz.getSuperclass()) {
                converter = findConverter(clazz);
                if (converter != null) {
                    break;
                }
            }
            if (converter == null && destPropertyClass != null) {
                converter = findDefaultConverter(targetClass);
            }
            if (converter == null) {
                return value;
            }
        }
        try {
            if (value.getClass() == String.class) {
                return converter.getAsObject((String) value);
            }
            return converter.getAsString(value);
        } catch (final Throwable cause) {
            throw new ConverterRuntimeException(destPropertyName, value, cause);
        }
    }

    /**
     * クラスに対応するコンバータを探します。
     * 
     * @param clazz
     *            クラス
     * @return コンバータ
     */
    protected Converter findConverter(final Class<?> clazz) {
        for (final Converter c : converters) {
            if (c.isTarget(clazz)) {
                return c;
            }
        }
        return null;
    }

    /**
     * クラスに対応するデフォルトのコンバータを探します。
     * 
     * @param clazz
     *            クラス
     * @return コンバータ
     */
    protected Converter findDefaultConverter(final Class<?> clazz) {
        if (clazz == java.sql.Date.class) {
            return DEFAULT_DATE_CONVERTER;
        }
        if (clazz == Time.class) {
            return DEFAULT_TIME_CONVERTER;
        }
        if (java.util.Date.class.isAssignableFrom(clazz)) {
            return DEFAULT_TIMESTAMP_CONVERTER;
        }
        return null;
    }

}
