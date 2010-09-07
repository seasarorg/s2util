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
import java.math.BigInteger;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.seasar.util.lang.StringUtil;
import org.seasar.util.text.DecimalFormatSymbolsUtil;

/**
 * {@link Number}用の変換ユーティリティです。
 * 
 * @author higa
 */
public abstract class NumberConversionUtil {

    /**
     * 適切な {@link Number}に変換します。
     * 
     * @param type
     * @param o
     * @return 適切な {@link Number}
     */
    public static Object convertNumber(final Class<?> type, final Object o) {
        if (type == Integer.class) {
            return IntegerConversionUtil.toInteger(o);
        } else if (type == BigDecimal.class) {
            return BigDecimalConversionUtil.toBigDecimal(o);
        } else if (type == Double.class) {
            return DoubleConversionUtil.toDouble(o);
        } else if (type == Long.class) {
            return LongConversionUtil.toLong(o);
        } else if (type == Float.class) {
            return FloatConversionUtil.toFloat(o);
        } else if (type == Short.class) {
            return ShortConversionUtil.toShort(o);
        } else if (type == BigInteger.class) {
            return BigIntegerConversionUtil.toBigInteger(o);
        } else if (type == Byte.class) {
            return ByteConversionUtil.toByte(o);
        }
        return o;
    }

    /**
     * Wrapperを返します。
     * 
     * @param type
     * @param o
     * @return Wrapper
     */
    public static Object convertPrimitiveWrapper(final Class<?> type,
            final Object o) {
        if (type == int.class) {
            final Integer i = IntegerConversionUtil.toInteger(o);
            if (i != null) {
                return i;
            }
            return Integer.valueOf(0);
        } else if (type == double.class) {
            final Double d = DoubleConversionUtil.toDouble(o);
            if (d != null) {
                return d;
            }
            return new Double(0);
        } else if (type == long.class) {
            final Long l = LongConversionUtil.toLong(o);
            if (l != null) {
                return l;
            }
            return Long.valueOf(0);
        } else if (type == float.class) {
            final Float f = FloatConversionUtil.toFloat(o);
            if (f != null) {
                return f;
            }
            return new Float(0);
        } else if (type == short.class) {
            final Short s = ShortConversionUtil.toShort(o);
            if (s != null) {
                return s;
            }
            return Short.valueOf((short) 0);
        } else if (type == boolean.class) {
            final Boolean b = BooleanConversionUtil.toBoolean(o);
            if (b != null) {
                return b;
            }
            return Boolean.FALSE;
        } else if (type == byte.class) {
            final Byte b = ByteConversionUtil.toByte(o);
            if (b != null) {
                return b;
            }
            return Byte.valueOf((byte) 0);
        }
        return o;
    }

    /**
     * デリミタを削除します。
     * 
     * @param value
     * @param locale
     * @return デリミタを削除した結果
     */
    public static String removeDelimeter(String value, final Locale locale) {
        final String groupingSeparator = findGroupingSeparator(locale);
        if (groupingSeparator != null) {
            value = StringUtil.replace(value, groupingSeparator, "");
        }
        return value;
    }

    /**
     * グルーピング用のセパレータを探します。
     * 
     * @param locale
     * @return グルーピング用のセパレータ
     */
    public static String findGroupingSeparator(final Locale locale) {
        final DecimalFormatSymbols symbol = getDecimalFormatSymbols(locale);
        return Character.toString(symbol.getGroupingSeparator());
    }

    /**
     * 数値のセパレータを返します。
     * 
     * @param locale
     * @return 数値のセパレータ
     */
    public static String findDecimalSeparator(final Locale locale) {
        final DecimalFormatSymbols symbol = getDecimalFormatSymbols(locale);
        return Character.toString(symbol.getDecimalSeparator());
    }

    private static DecimalFormatSymbols getDecimalFormatSymbols(
            final Locale locale) {
        DecimalFormatSymbols symbol;
        if (locale != null) {
            symbol = DecimalFormatSymbolsUtil.getDecimalFormatSymbols(locale);
        } else {
            symbol = DecimalFormatSymbolsUtil.getDecimalFormatSymbols();
        }
        return symbol;
    }

}
