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

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * テキストファイル用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class TextFileUtil {

    /** 日本語のエンコーディングを自動判別するためのエンコーディング名 */
    private static final String JIS_AUTO_DETECT = "JISAutoDetect";

    /** UTF-8のエンコーディング名 */
    private static final String UTF8 = "UTF-8";

    /**
     * デフォルトエンコーディングでファイルからテキストを読み込みます。
     * 
     * @param path
     *            ファイルのパス
     * @return 読み込んだテキスト
     */
    public static String readText(final String path) {
        assertArgumentNotEmpty("path", path);

        final InputStream is = ResourceUtil.getResourceAsStream(path);
        try {
            final Reader reader = new InputStreamReader(is);
            return ReaderUtil.readText(reader);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * デフォルトエンコーディングでファイルからテキストを読み込みます。
     * 
     * @param file
     *            ファイル
     * @return 読み込んだテキスト
     */
    public static String readText(final File file) {
        assertArgumentNotNull("file", file);

        final InputStream is = InputStreamUtil.create(file);
        try {
            final Reader reader = new InputStreamReader(is);
            return ReaderUtil.readText(reader);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 日本語のエンコーディングでファイルからテキストを読み込みます。
     * 
     * @param path
     *            パス
     * @return 読み込んだテキスト
     */
    public static String readJisAutoDetect(final String path) {
        assertArgumentNotEmpty("path", path);

        final InputStream is = ResourceUtil.getResourceAsStream(path);
        try {
            final Reader reader = ReaderUtil.create(is, JIS_AUTO_DETECT);
            return ReaderUtil.readText(reader);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 日本語のエンコーディングでファイルからテキストを読み込みます。
     * 
     * @param file
     *            ファイル
     * @return 読み込んだテキスト
     */
    public static String readJisAutoDetect(final File file) {
        assertArgumentNotNull("file", file);

        final InputStream is = InputStreamUtil.create(file);
        try {
            final Reader reader = ReaderUtil.create(is, JIS_AUTO_DETECT);
            return ReaderUtil.readText(reader);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * UTF8でファイルからテキストを読み込みます。
     * 
     * @param path
     *            パス
     * @return 読み込んだテキスト
     */
    public static String readUTF8(final String path) {
        assertArgumentNotEmpty("path", path);

        final InputStream is = ResourceUtil.getResourceAsStream(path);
        try {
            final Reader reader = ReaderUtil.create(is, UTF8);
            return ReaderUtil.readText(reader);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * UTF8でファイルからテキストを読み込みます。
     * 
     * @param file
     *            ファイル
     * @return 読み込んだテキスト
     */
    public static String readUTF8(final File file) {
        assertArgumentNotNull("file", file);

        final InputStream is = InputStreamUtil.create(file);
        try {
            final Reader reader = ReaderUtil.create(is, UTF8);
            return ReaderUtil.readText(reader);
        } finally {
            CloseableUtil.close(is);
        }
    }

}
