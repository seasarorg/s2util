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

import org.seasar.util.beans.Converter;

/**
 * {@link CopyOptions}のインスタンス化を容易にするために{@literal static import}して使うためのユーティリティです。
 * 
 * @author koichik
 */
public abstract class CopyOptionsUtil {

    /**
     * 操作の対象に含めるプロパティ名を指定した{@link CopyOptions}を返します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return 操作の対象に含めるプロパティ名を指定した{@link CopyOptions}
     */
    public static CopyOptions include(final CharSequence... propertyNames) {
        return new CopyOptions().include(propertyNames);
    }

    /**
     * 操作の対象に含めないプロパティ名を指定した{@link CopyOptions}を返します。
     * 
     * @param propertyNames
     *            プロパティ名の配列
     * @return 操作の対象に含めないプロパティ名を指定した{@link CopyOptions}
     */
    public static CopyOptions exclude(final CharSequence... propertyNames) {
        return new CopyOptions().exclude(propertyNames);
    }

    /**
     * {@literal null}値のプロパティを操作の対象外にした{@link CopyOptions}を返します。
     * 
     * @return {@literal null}値のプロパティを操作の対象外にした{@link CopyOptions}を返します。
     */
    public static CopyOptions excludeNull() {
        return new CopyOptions().excludeNull();
    }

    /**
     * 空白のプロパティを操作の対象外にした{@link CopyOptions}を返します。
     * 
     * @return 空白のプロパティを操作の対象外にした{@link CopyOptions}
     */
    public static CopyOptions excludeWhitespace() {
        return new CopyOptions().excludeWhitespace();
    }

    /**
     * プレフィックスを指定した{@link CopyOptions}を返します。
     * 
     * @param prefix
     *            プレフィックス
     * @return プレフィックスを指定した{@link CopyOptions}
     */
    public static CopyOptions prefix(final CharSequence prefix) {
        return new CopyOptions().prefix(prefix);
    }

    /**
     * JavaBeansのデリミタを設定した{@link CopyOptions}を返します。
     * 
     * @param beanDelimiter
     *            JavaBeansのデリミタ
     * @return JavaBeansのデリミタを設定した{@link CopyOptions}
     */
    public static CopyOptions beanDelimiter(final char beanDelimiter) {
        return new CopyOptions().beanDelimiter(beanDelimiter);
    }

    /**
     * {@literal Map}のデリミタを設定した{@link CopyOptions}を返します。
     * 
     * @param mapDelimiter
     *            {@literal Map}のデリミタ
     * @return {@literal Map}のデリミタを設定した{@link CopyOptions}
     */
    public static CopyOptions mapDelimiter(final char mapDelimiter) {
        return new CopyOptions().mapDelimiter(mapDelimiter);
    }

    /**
     * コンバータを設定した{@link CopyOptions}を返します。
     * 
     * @param converter
     * @param propertyNames
     * @return コンバータを設定した{@link CopyOptions}
     */
    public static CopyOptions converter(final Converter converter,
            final CharSequence... propertyNames) {
        return new CopyOptions().converter(converter, propertyNames);
    }

    /**
     * 日付のコンバータを設定した{@link CopyOptions}を返します。
     * 
     * @param pattern
     *            日付のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return 日付のコンバータを設定した{@link CopyOptions}
     */
    public static CopyOptions dateConverter(final String pattern,
            final CharSequence... propertyNames) {
        return new CopyOptions().dateConverter(pattern, propertyNames);
    }

    /**
     * SQL用日付のコンバータを設定した{@link CopyOptions}を返します。
     * 
     * @param pattern
     *            日付のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return SQL用日付のコンバータを設定した{@link CopyOptions}
     */
    public static CopyOptions sqlDateConverter(final String pattern,
            final CharSequence... propertyNames) {
        return new CopyOptions().sqlDateConverter(pattern, propertyNames);
    }

    /**
     * 時間のコンバータを設定した{@link CopyOptions}を返します。
     * 
     * @param pattern
     *            時間のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return 時間のコンバータを設定した{@link CopyOptions}
     */
    public static CopyOptions timeConverter(final String pattern,
            final CharSequence... propertyNames) {
        return new CopyOptions().timeConverter(pattern, propertyNames);
    }

    /**
     * 日時のコンバータを設定した{@link CopyOptions}を返します。
     * 
     * @param pattern
     *            日時のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return 日時のコンバータを設定した{@link CopyOptions}
     */
    public static CopyOptions timestampConverter(final String pattern,
            final CharSequence... propertyNames) {
        return new CopyOptions().timestampConverter(pattern, propertyNames);
    }

    /**
     * 数値のコンバータを設定した{@link CopyOptions}を返します。
     * 
     * @param pattern
     *            数値のパターン
     * @param propertyNames
     *            プロパティ名の配列
     * @return 数値のコンバータを設定した{@link CopyOptions}
     */
    public static CopyOptions numberConverter(final String pattern,
            final CharSequence... propertyNames) {
        return new CopyOptions().numberConverter(pattern, propertyNames);
    }

}
