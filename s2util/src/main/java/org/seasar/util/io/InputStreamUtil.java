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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.seasar.util.exception.IORuntimeException;

import static org.seasar.util.io.CloseableUtil.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link InputStream}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class InputStreamUtil {

    private static final int BUF_SIZE = 4096;

    /**
     * {@link InputStream}からbyteの配列を取得します。
     * <p>
     * 入力ストリームはクローズされます。
     * <p>
     * 
     * @param is
     *            入力ストリーム
     * @return byteの配列
     */
    public static final byte[] getBytes(final InputStream is) {
        assertArgumentNotNull("is", is);

        final byte[] buf = new byte[8192];
        try {
            final ByteArrayOutputStream baos =
                new ByteArrayOutputStream(BUF_SIZE);
            int n = 0;
            while ((n = is.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, n);
            }
            return baos.toByteArray();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } finally {
            close(is);
        }
    }

    /**
     * {@link InputStream}の内容を {@link OutputStream}にコピーします。
     * <p>
     * 入力ストリームおよび出力ストリームはクローズされません。
     * </p>
     * 
     * @param is
     *            入力ストリーム
     * @param os
     *            出力ストリーム
     */
    public static final void copy(final InputStream is, final OutputStream os) {
        assertArgumentNotNull("is", is);
        assertArgumentNotNull("os", os);

        final byte[] buf = new byte[BUF_SIZE];
        try {
            int n = 0;
            while ((n = is.read(buf, 0, buf.length)) != -1) {
                os.write(buf, 0, n);
            }
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link InputStream#available()}の例外処理をラップしたメソッドです。
     * 
     * @param is
     *            入力ストリーム
     * @return 可能なサイズ
     */
    public static int available(final InputStream is) {
        assertArgumentNotNull("is", is);

        try {
            return is.available();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link InputStream}をリセットします。
     * 
     * @param is
     *            入力ストリーム
     * @see InputStream#reset()
     */
    public static void reset(final InputStream is) {
        assertArgumentNotNull("is", is);

        try {
            is.reset();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
