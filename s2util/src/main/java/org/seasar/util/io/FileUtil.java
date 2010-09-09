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
import java.io.IOException;
import java.net.URL;

import org.seasar.util.exception.IORuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link File}を扱うユーティリティ・クラスです。
 * 
 * @author higa
 */
public abstract class FileUtil {

    private static final int BUF_SIZE = 4096;

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

        final ByteArrayOutputStream os = new ByteArrayOutputStream(BUF_SIZE);
        CopyUtil.copy(file, os);
        return os.toByteArray();
    }

}
