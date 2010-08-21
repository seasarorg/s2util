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
package org.seasar.util.collection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.seasar.framework.exception.EmptyRuntimeException;

/**
 * 配列に対するユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ArrayUtil {

    /**
     * 配列の末尾にオブジェクトを追加した配列を返します。
     * 
     * @param <T>
     *            配列の要素型
     * @param array
     *            配列
     * @param obj
     *            オブジェクト
     * @return オブジェクトが追加された結果の配列
     */
    public static <T> T[] add(final T[] array, final T obj) {
        if (array == null) {
            throw new EmptyRuntimeException("array");
        }
        final int length = array.length;
        @SuppressWarnings("unchecked")
        final T[] newArray =
            (T[]) Array.newInstance(
                array.getClass().getComponentType(),
                length + 1);
        System.arraycopy(array, 0, newArray, 0, length);
        newArray[length] = obj;
        return newArray;
    }

    /**
     * {@literal int}配列の末尾に{@literal int}の値を追加した配列を返します。
     * 
     * @param array
     *            配列
     * @param value
     *            値
     * @return 値が追加された結果の配列
     */
    public static int[] add(final int[] array, final int value) {
        if (array == null) {
            throw new EmptyRuntimeException("array");
        }
        final int[] newArray =
            (int[]) Array.newInstance(int.class, array.length + 1);
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = value;
        return newArray;
    }

    /**
     * 二つの配列を連結した配列を返します。
     * <p>
     * どちらかあるいは両方の配列が{@literal null}の場合、他方の配列がそのまま返されます。 どちらかあるいは両方の配列の長さが
     * {@literal 0}の場合、他方の配列がそのまま返されます。 いずれの場合も返される配列は引数に渡された配列そのものでコピーはされません。
     * </p>
     * 
     * @param <T>
     *            配列の要素の型
     * @param a
     *            配列1
     * @param b
     *            配列2
     * @return 配列が連結された結果の配列
     */
    public static <T> T[] add(final T[] a, final T[] b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a.length == 0) {
            return b;
        }
        if (b.length == 0) {
            return a;
        }
        @SuppressWarnings("unchecked")
        final T[] array =
            (T[]) Array.newInstance(a.getClass().getComponentType(), a.length
                + b.length);
        System.arraycopy(a, 0, array, 0, a.length);
        System.arraycopy(b, 0, array, a.length, b.length);
        return array;
    }

    /**
     * 配列中からオジェクトが最初に見つかったインデックスを返します。
     * 
     * @param <T>
     *            配列の要素の型
     * @param array
     *            配列
     * @param obj
     *            検索するオブジェクト
     * @return 配列中からオジェクトが最初に見つかったインデックス
     */
    public static <T> int indexOf(final T[] array, final T obj) {
        return indexOf(array, obj, 0);
    }

    /**
     * 配列中からオジェクトが最初に見つかったインデックスを返します。
     * 
     * @param <T>
     *            配列の要素の型
     * @param array
     *            配列
     * @param obj
     *            検索するオブジェクト
     * @param fromIndex
     *            検索を始めるインデックス
     * @return 配列中からオジェクトが最初に見つかったインデックス。見つからなかった場合は{@literal -1}
     */
    public static <T> int indexOf(final T[] array, final T obj,
            final int fromIndex) {
        if (array != null) {
            for (int i = fromIndex; i < array.length; ++i) {
                final Object o = array[i];
                if (o != null) {
                    if (o.equals(obj)) {
                        return i;
                    }
                } else if (obj == null) {
                    return i;

                }
            }
        }
        return -1;
    }

    /**
     * 配列中から文字が最初に見つかったインデックスを返します。
     * 
     * @param array
     *            配列
     * @param ch
     *            文字
     * @return 配列中から文字が最初に見つかったインデックス
     */
    public static int indexOf(final char[] array, final char ch) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                final char c = array[i];
                if (ch == c) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 配列から最初に見つかったオブジェクトを削除した結果の配列を返します。
     * <p>
     * 配列にオブジェクトが含まれていない場合は引数で渡された配列をそのまま返します。
     * </p>
     * 
     * @param <T>
     *            配列の要素の型
     * @param array
     *            配列
     * @param obj
     *            配列から削除する要素
     * @return 削除後の配列
     */
    public static <T> T[] remove(final T[] array, final T obj) {
        final int index = indexOf(array, obj);
        if (index < 0) {
            return array;
        }
        @SuppressWarnings("unchecked")
        final T[] newArray =
            (T[]) Array.newInstance(
                array.getClass().getComponentType(),
                array.length - 1);
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index < array.length - 1) {
            System.arraycopy(array, index + 1, newArray, index, newArray.length
                - index);
        }
        return newArray;
    }

    /**
     * 配列が{@literal null}または長さが0の場合は{@literal true}を返します。
     * 
     * @param <T>
     *            配列の要素の型
     * @param arrays
     *            配列
     * @return 配列が{@literal null}または長さが0の場合は{@literal true}
     */
    public static <T> boolean isEmpty(final T[] arrays) {
        return (arrays == null || arrays.length == 0);
    }

    /**
     * 配列にオブジェクトが含まれていれば{@literal true}を返します。
     * 
     * @param <T>
     *            配列の要素の型
     * @param array
     *            配列
     * @param obj
     *            オブジェクト
     * @return 配列にオブジェクトが含まれていれば{@literal true}
     */
    public static <T> boolean contains(final T[] array, final T obj) {
        return -1 < indexOf(array, obj);
    }

    /**
     * 配列に文字が含まれていれば{@literal true}を返します。
     * 
     * @param array
     *            配列
     * @param ch
     *            文字
     * @return 配列に文字が含まれていれば{@literal true}
     */
    public static boolean contains(final char[] array, final char ch) {
        return -1 < indexOf(array, ch);
    }

    /**
     * 順番は無視して2つの配列が等しければ{@literal true}を返します。
     * 
     * @param <T>
     *            配列の要素の型
     * @param array1
     *            配列1
     * @param array2
     *            配列2
     * @return 順番は無視して2つの配列が等しければ{@literal true}
     */
    public static <T> boolean equalsIgnoreSequence(final T[] array1,
            final T[] array2) {
        if (array1 == null && array2 == null) {
            return true;
        } else if (array1 == null || array2 == null) {
            return false;
        }
        if (array1.length != array2.length) {
            return false;
        }
        final T[] copyOfArray2 = Arrays.copyOf(array2, array2.length);
        for (int i = 0; i < array1.length; i++) {
            final T o1 = array1[i];
            final int j = indexOf(copyOfArray2, o1, i);
            if (j == -1) {
                return false;
            }
            if (i != j) {
                final T o2 = copyOfArray2[i];
                copyOfArray2[i] = array2[j];
                copyOfArray2[j] = o2;
            }
        }
        return true;
    }

    /**
     * 配列をオブジェクトの配列に変換します。
     * <p>
     * 変換元の配列にはプリミティブ型の配列を渡すことができます。 その場合、変換された配列の要素型はプリミティブ型に対応するラッパー型の配列となります。
     * </p>
     * 
     * @param obj
     *            配列
     * @return オブジェクトの配列
     */
    public static Object[] toObjectArray(final Object obj) {
        final int length = Array.getLength(obj);
        final Object[] array = new Object[length];
        for (int i = 0; i < length; i++) {
            array[i] = Array.get(obj, i);
        }
        return array;
    }

    /**
     * 配列をリストに変換します。
     * <p>
     * 変換元の配列にはプリミティブ型の配列を渡すことができます。その場合、変換されたリストの要素型はプリミティブ型に対応するラッパー型の配列となります。
     * </p>
     * 
     * @param <T>
     *            配列の要素の型
     * @param obj
     *            配列
     * @return リスト
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(final Object obj) {
        final int length = Array.getLength(obj);
        final List<Object> list = new ArrayList<Object>(length);
        for (int i = 0; i < length; i++) {
            list.add(Array.get(obj, i));
        }
        return (List<T>) list;
    }

}
