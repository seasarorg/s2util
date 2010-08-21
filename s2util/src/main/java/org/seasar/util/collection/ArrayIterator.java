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
import java.util.NoSuchElementException;

/**
 * 配列を{@link Iterator}にするAdaptorです。
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
            throw new NoSuchElementException("index=" + index);
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove");
    }

}