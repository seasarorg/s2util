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
package org.seasar.util.zip;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.io.FileUtil;
import org.seasar.util.log.Logger;
import org.seasar.util.net.URLUtil;

import static org.seasar.util.log.Logger.*;

/**
 * {@link java.util.zip.ZipFile}を扱うユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ZipFileUtil {

    private static final Logger logger = Logger.getLogger(ZipFileUtil.class);

    /**
     * 指定されたZipファイルを読み取るための<code>ZipFile</code>を作成して返します。
     * 
     * @param file
     *            ファイルパス
     * @return 指定されたZipファイルを読み取るための<code>ZipFile</code>
     */
    public static ZipFile create(final String file) {
        try {
            return new ZipFile(file);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 指定されたZipファイルを読み取るための<code>ZipFile</code>を作成して返します。
     * 
     * @param file
     *            ファイル
     * @return 指定されたZipファイルを読み取るための<code>ZipFile</code>
     */
    public static ZipFile create(final File file) {
        try {
            return new ZipFile(file);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 指定されたZipファイルエントリの内容を読み込むための入力ストリームを返します。
     * 
     * @param file
     *            Zipファイル
     * @param entry
     *            Zipファイルエントリ
     * @return 指定されたZipファイルエントリの内容を読み込むための入力ストリーム
     */
    public static InputStream getInputStream(final ZipFile file,
            final ZipEntry entry) {
        try {
            return file.getInputStream(entry);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * URLで指定されたZipファイルを読み取るための<code>ZipFile</code>を作成して返します。
     * 
     * @param zipUrl
     *            Zipファイルを示すURL
     * @return 指定されたZipファイルを読み取るための<code>ZipFile</code>
     */
    public static ZipFile toZipFile(final URL zipUrl) {
        return create(new File(toZipFilePath(zipUrl)));
    }

    /**
     * URLで指定されたZipファイルのパスを返します。
     * 
     * @param zipUrl
     *            Zipファイルを示すURL
     * @return URLで指定されたZipファイルのパス
     */
    public static String toZipFilePath(final URL zipUrl) {
        final String urlString = zipUrl.getPath();
        final int pos = urlString.lastIndexOf('!');
        final String zipFilePath = urlString.substring(0, pos);
        final File zipFile = new File(URLUtil.decode(zipFilePath, "UTF8"));
        return FileUtil.getCanonicalPath(zipFile);
    }

    /**
     * Zipファイルをクローズします。
     * <p>
     * {@link ZipFile#close()}が例外をスローした場合はログにエラーメッセージを出力します。 例外は再スローされません。
     * </p>
     * 
     * @param zipFile
     *            Zipファイル
     */
    public static void close(final ZipFile zipFile) {
        try {
            zipFile.close();
        } catch (final IOException e) {
            logger.log(format("EUTL0017", e.getMessage()), e);
        }
    }

}
