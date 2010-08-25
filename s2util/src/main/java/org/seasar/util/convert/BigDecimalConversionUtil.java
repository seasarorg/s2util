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
package org.seasar.util.convert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import org.seasar.util.lang.StringUtil;

/**
 * {@link BigDecimal}用の変換ユーティリティです。
 * 
 * @author higa
 * 
 */
public class BigDecimalConversionUtil {

    /**
     * インスタンスを構築します。
     */
    protected BigDecimalConversionUtil() {
    }

    /**
     * {@link BigDecimal}に変換します。
     * 
     * @param o
     * @return {@link BigDecimal}に変換されたデータ
     */
    public static BigDecimal toBigDecimal(Object o) {
        return toBigDecimal(o, null);
    }

    /**
     * {@link BigDecimal}に変換します。
     * 
     * @param o
     * @param pattern
     * @return {@link BigDecimal}に変換されたデータ
     */
    public static BigDecimal toBigDecimal(Object o, String pattern) {
        if (o == null) {
            return null;
        } else if (o instanceof BigDecimal) {
            return (BigDecimal) o;
        } else if (o instanceof java.util.Date) {
            if (pattern != null) {
                return new BigDecimal(new SimpleDateFormat(pattern).format(o));
            }
            return new BigDecimal(Long.toString(((java.util.Date) o).getTime()));
        } else if (o instanceof String) {
            String s = (String) o;
            if (StringUtil.isEmpty(s)) {
                return null;
            }
            return normalize(new BigDecimal(s));
        } else {
            return normalize(new BigDecimal(o.toString()));
        }
    }

    /**
     * {@link BigDecimal}を文字列に変換します。
     * 
     * @param dec
     * @return 文字列に変換されたデータ
     */
    public static String toString(BigDecimal dec) {
        return dec.toPlainString();
    }

    /**
     * {@link BigDecimal}を正規化します。
     * 
     * @param dec
     * @return 正規化されたデータ
     */
    private static BigDecimal normalize(final BigDecimal dec) {
        return new BigDecimal(dec.toPlainString());
    }
}
