/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.util.io;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Iterator;

import org.seasar.util.exception.SNoSuchElementException;
import org.seasar.util.exception.SUnsupportedOperationException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link BufferedReader}から読み込んだ行単位の文字列を反復する{@link Iterator}です。
 * <p>
 * 次のように使います．
 * </p>
 * 
 * <pre>
 * import static org.seasar.util.io.LineIterator.*;
 * 
 * BufferedReader reader = ...;
 * for (String line : iterable(reader)) {
 *     ...
 * }
 * </pre>
 * 
 * @author koichik
 */
public class LineIterator implements Iterator<String> {

    /** {@link #line}が空であることを示す{@literal String}オブジェクト */
    protected static final String EMPTY = new String();

    /** {@link BufferedReader} */
    protected final BufferedReader reader;

    /** 読み込み済みの文字列 */
    protected String line = EMPTY;

    /**
     * for each構文で使用するために{@link LineIterator}をラップした{@link Iterable}を返します。
     * 
     * @param reader
     *            文字列を読み込む{@link Reader}。{@literal null}であってはいけません
     * @return {@link LineIterator}をラップした{@link Iterable}
     */
    public static Iterable<String> iterable(final Reader reader) {
        assertArgumentNotNull("reader", reader);
        return iterable(new BufferedReader(reader));
    }

    /**
     * for each構文で使用するために{@link LineIterator}をラップした{@link Iterable}を返します。
     * 
     * @param reader
     *            文字列を読み込む{@link BufferedReader}。{@literal null}であってはいけません
     * @return {@link LineIterator}をラップした{@link Iterable}
     */
    public static Iterable<String> iterable(final BufferedReader reader) {
        assertArgumentNotNull("reader", reader);

        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new LineIterator(reader);
            }
        };
    }

    /**
     * インスタンスを構築します。
     * 
     * @param reader
     *            文字列を読み込む{@link Reader}。{@literal null}であってはいけません
     */
    public LineIterator(final Reader reader) {
        assertArgumentNotNull("reader", reader);
        this.reader = new BufferedReader(reader);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param reader
     *            文字列を読み込む{@link BufferedReader}。{@literal null}であってはいけません
     */
    public LineIterator(final BufferedReader reader) {
        assertArgumentNotNull("reader", reader);
        this.reader = reader;
    }

    @Override
    public boolean hasNext() {
        if (line == EMPTY) {
            line = ReaderUtil.readLine(reader);
        }
        return line != null;
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new SNoSuchElementException();
        }
        final String result = line;
        line = EMPTY;
        return result;
    }

    @Override
    public void remove() {
        throw new SUnsupportedOperationException("remove");
    }

}
