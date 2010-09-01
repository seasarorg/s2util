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
package org.seasar.util.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import org.seasar.util.exception.IORuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link Reader}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ReaderUtil {

    private static final int BUF_SIZE = 4096;

    /**
     * {@link Reader}からテキストを読み込みます。
     * <p>
     * {@link Reader}はクローズされます。
     * </p>
     * 
     * @param reader
     *            読み込み文字ストリーム
     * @return テキスト
     */
    public static String readText(final Reader reader) {
        assertArgumentNotNull("reader", reader);

        final StringBuilder out = new StringBuilder(BUF_SIZE);
        try {
            final BufferedReader in = new BufferedReader(reader);
            try {
                final char[] buf = new char[BUF_SIZE];
                int n;
                while ((n = in.read(buf)) >= 0) {
                    out.append(buf, 0, n);
                }
            } finally {
                CloseableUtil.close(in);
            }
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
        return new String(out);
    }

}
