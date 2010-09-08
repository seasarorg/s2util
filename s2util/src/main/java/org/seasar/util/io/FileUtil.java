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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.exception.NullArgumentException;

import static org.seasar.util.io.CloseableUtil.*;
import static org.seasar.util.message.MessageFormatter.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link File}を扱うユーティリティ・クラスです。
 * 
 * @author higa
 */
public abstract class FileUtil {

    /**
     * この抽象パス名の正規の形式を返します。
     * 
     * @param file
     *            ファイル
     * @return この抽象パス名と同じファイルまたはディレクトリを示す正規パス名文字列
     */
    public static String getCanonicalPath(final File file) {
        assertArgumentNotNull("file", file);

        try {
            return file.getCanonicalPath();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * この抽象パス名を<code>file:</code> URLに変換します。
     * 
     * @param file
     *            ファイル
     * @return ファイルURLを表すURLオブジェクト
     */
    public static URL toURL(final File file) {
        assertArgumentNotNull("file", file);

        try {
            return file.toURI().toURL();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * ファイルの内容をバイト配列に読み込んで返します。
     * 
     * @param file
     *            ファイル
     * @return ファイルの内容を読み込んだバイト配列
     */
    public static byte[] getBytes(final File file) {
        assertArgumentNotNull("file", file);

        FileInputStream is = FileInputStreamUtil.create(file);
        try {
            return InputStreamUtil.getBytes(is);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * <code>src</code>の内容を<code>dest</code>にコピーします。
     * 
     * @param src
     *            コピー元のファイル
     * @param dest
     *            コピー先のファイル
     */
    public static void copy(final File src, final File dest) {
        assertArgumentNotNull("src", src);
        assertArgument(
            "src",
            src.exists() && src.canRead(),
            getMessage("EUTL0101", src));
        assertArgumentNotNull("dest", dest);
        assertArgument(
            "dest",
            !dest.exists() || dest.canWrite(),
            getMessage("EUTL0102", src));

        final BufferedInputStream in =
            new BufferedInputStream(FileInputStreamUtil.create(src));
        try {
            final BufferedOutputStream out =
                new BufferedOutputStream(FileOutputStreamUtil.create(dest));
            try {
                InputStreamUtil.copy(in, out);
            } finally {
                close(out);
            }
        } finally {
            close(in);
        }
    }

    /**
     * バイトの配列をファイルに書き出します。
     * 
     * @param path
     *            ファイルのパス
     * @param data
     *            バイトの配列
     * 
     * @throws NullArgumentException
     *             pathやdataがnullの場合。
     */
    public static void write(final String path, final byte[] data) {
        assertArgumentNotNull("path", path);
        assertArgumentNotNull("data", data);

        write(path, data, 0, data.length);
    }

    /**
     * バイトの配列をファイルに書き出します。
     * 
     * @param path
     *            ファイルのパス
     * @param data
     *            バイトの配列
     * @param offset
     *            オフセット
     * @param length
     *            配列の長さ
     * @throws NullArgumentException
     *             pathやdataがnullの場合。
     */
    public static void write(final String path, final byte[] data,
            final int offset, final int length) {
        assertArgumentNotNull("path", path);
        assertArgumentNotNull("data", data);

        try {
            final OutputStream out =
                new BufferedOutputStream(new FileOutputStream(path));
            try {
                out.write(data, offset, length);
            } finally {
                close(out);
            }
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
