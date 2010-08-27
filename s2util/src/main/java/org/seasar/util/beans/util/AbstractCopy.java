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
import java.util.Map.Entry;

import org.seasar.util.beans.BeanDesc;
import org.seasar.util.beans.Converter;
import org.seasar.util.beans.PropertyDesc;
import org.seasar.util.beans.converter.DateConverter;
import org.seasar.util.beans.converter.NumberConverter;
import org.seasar.util.beans.converter.SqlDateConverter;
import org.seasar.util.beans.converter.TimeConverter;
import org.seasar.util.beans.converter.TimestampConverter;
import org.seasar.util.beans.factory.BeanDescFactory;
import org.seasar.util.convert.DateConversionUtil;
import org.seasar.util.convert.TimeConversionUtil;
import org.seasar.util.convert.TimestampConversionUtil;
import org.seasar.util.exception.ConverterRuntimeException;

import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * JavaBeansやMapに対して操作を行う抽象クラスです。
 * 
 * @author higa
 * @param <S>
 *            JavaBeansに対して操作を行うサブタイプです。
 */
public abstract class AbstractCopy<S extends AbstractCopy<S>> {

    /**
     * 空文字列の配列です。
     */
    protected static final String[] EMPTY_STRING_ARRAY = new String[0];

    /**
     * 日付用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_DATE_CONVERTER =
        new DateConverter(DateConversionUtil.getY4Pattern(Locale.getDefault()));

    /**
     * 日時用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_TIMESTAMP_CONVERTER =
        new DateConverter(TimestampConversionUtil.getPattern(Locale
            .getDefault()));

    /**
     * 時間用のデフォルトコンバータです。
     */
    protected static final Converter DEFAULT_TIME_CONVERTER =
        new DateConverter(TimeConversionUtil.getPattern(Locale.getDefault()));

    /**
     * 操作の対象に含めるプロパティ名の配列です。
     */
    protected String[] includePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * 操作の対象に含めないプロパティ名の配列です。
     */
    protected String[] excludePropertyNames = EMPTY_STRING_ARRAY;

    /**
     * null値のプロパティを操作の対象外にするかどうかです。
     */
    protected boolean excludesNull = false;

    /**
     * 空白を操作の対象外にするかどうかです。
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
    protected Map<String, Converter> converterMap = newHashMap();

    /**
     * 特定のプロパティに関連付けられていないコンバータです。
     */
    protected List<Converter> converters = newArrayList();

    /**
     * CharSequenceの配列をStringの配列に変換します。
     * 
     * @param charSequenceArray
     *            CharSequenceの配列
     * @return Stringの配列
     */
    protected static String[] toStringArray(
            final CharSequence[] charSequenceArray) {
        final int length = charSequenceArray.length;
        final String[] stringArray = new String[length];
        for (int index = 0; index < length; index++) {
            stringArray[index] = charSequenceArray[index].toString();
        }
        return stringArray;
    }

    /**
     * 操作の対象に含めるプロパティ名を指定します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S includes(final CharSequence... propertyNames) {
        this.includePropertyNames = toStringArray(propertyNames);
        return (S) this;
    }

    /**
     * 操作の対象に含めないプロパティ名を指定します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S excludes(final CharSequence... propertyNames) {
        this.excludePropertyNames = toStringArray(propertyNames);
        return (S) this;
    }

    /**
     * null値のプロパティを操作の対象外にします。
     * 
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S excludesNull() {
        this.excludesNull = true;
        return (S) this;
    }

    /**
     * 空白のプロパティを操作の対象外にします。
     * 
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S excludesWhitespace() {
        this.excludesWhitespace = true;
        return (S) this;
    }

    /**
     * プレフィックスを指定します。
     * 
     * @param prefix
     *            プレフィックス
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S prefix(final CharSequence prefix) {
        this.prefix = prefix.toString();
        return (S) this;
    }

    /**
     * JavaBeansのデリミタを設定します。
     * 
     * @param beanDelimiter
     *            JavaBeansのデリミタ
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S beanDelimiter(final char beanDelimiter) {
        this.beanDelimiter = beanDelimiter;
        return (S) this;
    }

    /**
     * Mapのデリミタを設定します。
     * 
     * @param mapDelimiter
     *            Mapのデリミタ
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S mapDelimiter(final char mapDelimiter) {
        this.mapDelimiter = mapDelimiter;
        return (S) this;
    }

    /**
     * コンバータを設定します。
     * 
     * @param converter
     * @param propertyNames
     * @return このインスタンス自身
     */
    @SuppressWarnings("unchecked")
    public S converter(final Converter converter,
            final CharSequence... propertyNames) {
        if (propertyNames.length == 0) {
            converters.add(converter);
        } else {
            for (final CharSequence name : propertyNames) {
                converterMap.put(name.toString(), converter);
            }
        }
        return (S) this;
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
    public S dateConverter(final String pattern,
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
    public S sqlDateConverter(final String pattern,
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
    public S timeConverter(final String pattern,
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
    public S timestampConverter(final String pattern,
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
    public S numberConverter(final String pattern,
            final CharSequence... propertyNames) {
        return converter(new NumberConverter(pattern), propertyNames);
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
        if (includePropertyNames.length > 0) {
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
        if (excludePropertyNames.length > 0) {
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
     * BeanからBeanにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyBeanToBean(final Object src, final Object dest) {
        final BeanDesc srcBeanDesc =
            BeanDescFactory.getBeanDesc(src.getClass());
        final BeanDesc destBeanDesc =
            BeanDescFactory.getBeanDesc(dest.getClass());
        final int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            final PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            final String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.isReadable()
                || !isTargetProperty(srcPropertyName)) {
                continue;
            }
            final String destPropertyName = trimPrefix(srcPropertyName);
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            final PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            final Object value = srcPropertyDesc.getValue(src);
            if (value instanceof String && excludesWhitespace
                && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            final Object convertedValue =
                convertValue(
                    value,
                    destPropertyName,
                    destPropertyDesc.getPropertyType());
            destPropertyDesc.setValue(dest, convertedValue);
        }
    }

    /**
     * BeanからMapにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyBeanToMap(final Object src,
            final Map<String, Object> dest) {
        final BeanDesc srcBeanDesc =
            BeanDescFactory.getBeanDesc(src.getClass());
        final int size = srcBeanDesc.getPropertyDescSize();
        for (int i = 0; i < size; i++) {
            final PropertyDesc srcPropertyDesc = srcBeanDesc.getPropertyDesc(i);
            final String srcPropertyName = srcPropertyDesc.getPropertyName();
            if (!srcPropertyDesc.isReadable()
                || !isTargetProperty(srcPropertyName)) {
                continue;
            }
            final Object value = srcPropertyDesc.getValue(src);
            if (value instanceof String && excludesWhitespace
                && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            final String destPropertyName =
                trimPrefix(srcPropertyName.replace(beanDelimiter, mapDelimiter));
            final Object convertedValue =
                convertValue(value, destPropertyName, null);
            dest.put(destPropertyName, convertedValue);
        }
    }

    /**
     * MapからBeanにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyMapToBean(final Map<String, Object> src,
            final Object dest) {
        final BeanDesc destBeanDesc =
            BeanDescFactory.getBeanDesc(dest.getClass());
        for (final Entry<String, Object> entry : src.entrySet()) {
            final String srcPropertyName = entry.getKey();
            if (!isTargetProperty(srcPropertyName)) {
                continue;
            }
            final String destPropertyName =
                trimPrefix(srcPropertyName.replace(mapDelimiter, beanDelimiter));
            if (!destBeanDesc.hasPropertyDesc(destPropertyName)) {
                continue;
            }
            final PropertyDesc destPropertyDesc =
                destBeanDesc.getPropertyDesc(destPropertyName);
            if (!destPropertyDesc.isWritable()) {
                continue;
            }
            final Object value = entry.getValue();
            if (value instanceof String && excludesWhitespace
                && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            final Object convertedValue =
                convertValue(
                    value,
                    destPropertyName,
                    destPropertyDesc.getPropertyType());
            destPropertyDesc.setValue(dest, convertedValue);
        }
    }

    /**
     * MapからMapにコピーを行います。
     * 
     * @param src
     *            コピー元
     * @param dest
     *            コピー先
     */
    protected void copyMapToMap(final Map<String, Object> src,
            final Map<String, Object> dest) {
        for (final Entry<String, Object> entry : src.entrySet()) {
            final String srcPropertyName = entry.getKey();
            if (!isTargetProperty(srcPropertyName)) {
                continue;
            }
            final String destPropertyName = trimPrefix(srcPropertyName);
            final Object value = entry.getValue();
            if (value instanceof String && excludesWhitespace
                && ((String) value).trim().length() == 0) {
                continue;
            }
            if (value == null && excludesNull) {
                continue;
            }
            final Object convertedValue =
                convertValue(value, destPropertyName, null);
            dest.put(destPropertyName, convertedValue);
        }
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
            final Class<?> targetClass =
                value.getClass() != String.class ? value.getClass()
                    : destPropertyClass;
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
