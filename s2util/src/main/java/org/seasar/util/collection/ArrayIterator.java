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

import java.util.Iterator;

import org.seasar.util.exception.SNoSuchElementException;
import org.seasar.util.exception.SUnsupportedOperationException;

/**
 * 配列を{@link Iterator}にするAdaptorです。
 * <p>
 * 次のように使います．
 * </p>
 * 
 * <pre>
 * import static org.seasar.util.collection.ArrayIterator.*;
 * 
 * String[] array = ...;
 * for (String element : iterable(array)) {
 *     ...
 * }
 * </pre>
 * 
 * @author shot
 * @param <T>
 *            配列の要素の型
 */
public class ArrayIterator<T> implements Iterator<T> {

    /** イテレートする要素の配列 */
    protected final T[] items;

    /** 現在参照している要素のインデックス */
    protected int index = 0;

    /**
     * for each構文で使用するために配列をラップした{@link Iterable}を返します。
     * 
     * @param <T>
     *            列挙する要素の型
     * @param array
     *            配列
     * @return 配列をラップした{@link Iterable}
     */
    public static <T> Iterable<T> iterable(final T[] array) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new ArrayIterator<T>(array);
            }
        };
    }

    /**
     * {@link ArrayIterator}を作成します。
     * 
     * @param items
     *            イテレートする要素の配列
     */
    public ArrayIterator(final T... items) {
        this.items = items;
    }

    @Override
    public boolean hasNext() {
        return index < items.length;
    }

    @Override
    public T next() {
        try {
            final T o = items[index];
            index++;
            return o;
        } catch (final IndexOutOfBoundsException e) {
            throw new SNoSuchElementException("index=" + index);
        }
    }

    @Override
    public void remove() {
        throw new SUnsupportedOperationException("remove");
    }

}
