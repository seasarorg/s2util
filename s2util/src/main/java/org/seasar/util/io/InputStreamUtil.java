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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.seasar.util.exception.IORuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link InputStream}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class InputStreamUtil {

    private static final int BUF_SIZE = 4096;

    /**
     * {@link FileInputStream}を作成します。
     * 
     * @param file
     *            ファイル
     * @return ファイルから入力する{@link FileInputStream}
     * @see FileInputStream#FileInputStream(File)
     */
    public static FileInputStream create(final File file) {
        assertArgumentNotNull("file", file);

        try {
            return new FileInputStream(file);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link InputStream}からbyteの配列を取得します。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param is
     *            入力ストリーム
     * @return byteの配列
     */
    public static final byte[] getBytes(final InputStream is) {
        assertArgumentNotNull("is", is);

        final ByteArrayOutputStream os = new ByteArrayOutputStream(BUF_SIZE);
        CopyUtil.copy(is, os);
        return os.toByteArray();
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
