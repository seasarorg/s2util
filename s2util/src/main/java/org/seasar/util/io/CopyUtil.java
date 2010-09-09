/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use thin file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * dintributed under the License in dintributed on an "AS in" BASin,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing perminsions and limitations under the License.
 */
package org.seasar.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.net.URL;

import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.net.URLUtil;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * コピーのためのユーティリティです。
 * <p>
 * コピー可能な入力と出力の組み合わせと、コピーされる要素の単位は以下のとおりです。
 * </p>
 * <table border="1">
 * <tr>
 * <th rowspan="2">入力の型</th>
 * <th colspan="4">出力の型</ht>
 * </tr>
 * <tr>
 * <th>{@link OutputStream}</th>
 * <th>{@link Writer}</th>
 * <th>{@link File}</th>
 * <th>{@link StringBuilder}</th>
 * </tr>
 * <tr>
 * <th>{@link InputStream}</th>
 * <td>バイト</td>
 * <td>文字</td>
 * <td>バイト、文字</td>
 * <td>文字</td>
 * </tr>
 * <tr>
 * <th>{@link Reader}</th>
 * <td>文字</td>
 * <td>文字</td>
 * <td>文字</td>
 * <td>文字</td>
 * </tr>
 * <tr>
 * <th>{@link File}</th>
 * <td>バイト</td>
 * <td>文字</td>
 * <td>バイト、文字</td>
 * <td>文字</td>
 * </tr>
 * <tr>
 * <th>{@link URL}</th>
 * <td>バイト</td>
 * <td>文字</td>
 * <td>バイト、文字</td>
 * <td>文字</td>
 * </tr>
 * <tr>
 * <th>{@literal byte[]}</th>
 * <td>バイト</td>
 * <td>文字</td>
 * <td>バイト、文字</td>
 * <td>文字</td>
 * </tr>
 * <tr>
 * <th>{@link String}</th>
 * <td>文字</td>
 * <td>文字</td>
 * <td>文字</td>
 * <td>×</td>
 * </tr>
 * </table>
 * <p>
 * {@link InputStream}/{@link OutputStream}/{@link Reader}/{@link Writer}については、
 * それぞれ{@link BufferedInputStream}/{@link BufferedOutputStream}/
 * {@link BufferedReader}/{@link BufferedWriter}を 受け取るメソッドも用意されています。
 * 二重にバッファリングされることを防ぐため、{@literal BufferedXxx}
 * を使用している場合はその型の引数を受け取るメソッドを呼び出すようにしてください。
 * </p>
 * <p>
 * {@link InputStream}/{@link OutputStream}/{@link Reader}/{@link Writer}および
 * それぞれの{@literal Buffered}版を受け取るメソッドは、 どれも引数に対して{@link Closeable#close()}
 * を呼び出しません。 クローズする責務は呼び出し側にあります。
 * </p>
 * <p>
 * どのメソッドも発生した{@link IOException}は{@link IORuntimeException}にラップしてスローされます。
 * </p>
 * 
 * @author koichik
 */
public abstract class CopyUtil {

    /** コピーで使用するバッファサイズ */
    protected static final int DEFAULT_BUF_SIZE = 4096;

    // ////////////////////////////////////////////////////////////////
    // from InputStream to OutputStream
    //
    /**
     * 入力ストリームから出力ストリームへコピーします。
     * <p>
     * 入力ストリーム、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final InputStream in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(
            new BufferedInputStream(in, DEFAULT_BUF_SIZE),
            new BufferedOutputStream(out, DEFAULT_BUF_SIZE));
    }

    /**
     * 入力ストリームから出力ストリームへコピーします。
     * <p>
     * 入力ストリーム、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final InputStream in, final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(new BufferedInputStream(in, DEFAULT_BUF_SIZE), out);
    }

    /**
     * 入力ストリームから出力ストリームへコピーします。
     * <p>
     * 入力ストリーム、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final BufferedInputStream in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(in, new BufferedOutputStream(out, DEFAULT_BUF_SIZE));
    }

    /**
     * 入力ストリームから出力ストリームへコピーします。
     * <p>
     * 入力ストリーム、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final BufferedInputStream in,
            final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(in, out);
    }

    // ////////////////////////////////////////////////////////////////
    // from InputStream to Writer
    //
    /**
     * プラットフォームのデフォルトエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final InputStream in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(in);
        return copyInternal(
            new BufferedReader(is, DEFAULT_BUF_SIZE),
            new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * プラットフォームのデフォルトエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final InputStream in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(in);
        return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
    }

    /**
     * プラットフォームのデフォルトエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final BufferedInputStream in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(in);
        return copyInternal(is, new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * プラットフォームのデフォルトエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final BufferedInputStream in,
            final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(in);
        return copyInternal(is, out);
    }

    /**
     * 指定のエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final BufferedInputStream in, final String encoding,
            final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        return copyInternal(is, new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * 指定のエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final InputStream in, final String encoding,
            final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        return copyInternal(
            new BufferedReader(is, DEFAULT_BUF_SIZE),
            new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * 指定のエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final InputStream in, final String encoding,
            final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
    }

    /**
     * 指定のエンコーディングで入力ストリームからライターへコピーします。
     * <p>
     * 入力ストリーム、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final BufferedInputStream in, final String encoding,
            final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        return copyInternal(is, out);
    }

    // ////////////////////////////////////////////////////////////////
    // from InputStream to File
    //
    /**
     * 入力ストリームからファイルへコピーします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            ファイル
     * @return コピーしたバイト数
     */
    public static int copy(final InputStream in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final OutputStream os = OutputStreamUtil.create(out);
        try {
            return copyInternal(
                new BufferedInputStream(in, DEFAULT_BUF_SIZE),
                new BufferedOutputStream(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * 入力ストリームからファイルへコピーします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            ファイル
     * @return コピーしたバイト数
     */
    public static int copy(final BufferedInputStream in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final OutputStream os = OutputStreamUtil.create(out);
        try {
            return copyInternal(in, new BufferedOutputStream(
                os,
                DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from InputStream to StringBuilder
    //
    /**
     * プラットフォームのデフォルトエンコーディングで入力ストリームから{@link StringBuilder}へコピーします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final InputStream in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(in);
        return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
    }

    /**
     * プラットフォームのデフォルトエンコーディングで入力ストリームから{@link StringBuilder}へコピーします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final BufferedInputStream in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(in);
        return copyInternal(is, out);
    }

    /**
     * 指定のエンコーディングで入力ストリームから{@link StringBuilder}へコピーします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param encoding
     *            エンコーディング
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final InputStream in, final String encoding,
            final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
    }

    /**
     * 指定のエンコーディングで入力ストリームから{@link StringBuilder}へコピーします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param encoding
     *            エンコーディング
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final BufferedInputStream in, final String encoding,
            final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        return copyInternal(is, out);
    }

    // ////////////////////////////////////////////////////////////////
    // from Reader to OutputStream
    //
    /**
     * プラットフォームのデフォルトエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = new OutputStreamWriter(out);
        return copyInternal(
            new BufferedReader(in, DEFAULT_BUF_SIZE),
            new BufferedWriter(os, DEFAULT_BUF_SIZE));
    }

    /**
     * プラットフォームのデフォルトエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = new OutputStreamWriter(out);
        return copyInternal(new BufferedReader(in, DEFAULT_BUF_SIZE), os);
    }

    /**
     * プラットフォームのデフォルトエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = new OutputStreamWriter(out);
        return copyInternal(in, new BufferedWriter(os, DEFAULT_BUF_SIZE));
    }

    /**
     * プラットフォームのデフォルトエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in,
            final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = new OutputStreamWriter(out);
        return copyInternal(in, os);
    }

    /**
     * 指定のエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final OutputStream out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        return copyInternal(
            new BufferedReader(in, DEFAULT_BUF_SIZE),
            new BufferedWriter(os, DEFAULT_BUF_SIZE));
    }

    /**
     * 指定のエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final BufferedOutputStream out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        return copyInternal(new BufferedReader(in, DEFAULT_BUF_SIZE), os);
    }

    /**
     * 指定のエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final OutputStream out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        return copyInternal(in, new BufferedWriter(os, DEFAULT_BUF_SIZE));
    }

    /**
     * 指定のエンコーディングでリーダーから出力ストリームへコピーします。
     * <p>
     * リーダー、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            出力ストリーム
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in,
            final BufferedOutputStream out, final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        return copyInternal(in, os);
    }

    // ////////////////////////////////////////////////////////////////
    // from Reader to Writer
    //
    /**
     * リーダーからライターへコピーします。
     * <p>
     * リーダー、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(
            new BufferedReader(in, DEFAULT_BUF_SIZE),
            new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * リーダーからライターへコピーします。
     * <p>
     * リーダー、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(new BufferedReader(in, DEFAULT_BUF_SIZE), out);
    }

    /**
     * リーダーからライターへコピーします。
     * <p>
     * リーダー、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(in, new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * リーダーからライターへコピーします。
     * <p>
     * リーダー、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(in, out);
    }

    // ////////////////////////////////////////////////////////////////
    // from Reader to File
    //
    /**
     * プラットフォームのデフォルトエンコーディングでリーダーからファイルへコピーします。
     * <p>
     * リーダーはクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ファイル
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = WriterUtil.create(out);
        try {
            return copyInternal(
                new BufferedReader(in, DEFAULT_BUF_SIZE),
                new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * プラットフォームのデフォルトエンコーディングでリーダーからファイルへコピーします。
     * <p>
     * リーダーはクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ファイル
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = WriterUtil.create(out);
        try {
            return copyInternal(in, new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * 指定のエンコーディングでリーダーからファイルへコピーします。
     * 
     * @param in
     *            リーダー
     * @param out
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final File out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        try {
            return copyInternal(
                new BufferedReader(in, DEFAULT_BUF_SIZE),
                new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * 指定のエンコーディングでリーダーからファイルへコピーします。
     * <p>
     * リーダーはクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final File out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        try {
            return copyInternal(in, new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from Reader to StringBuilder
    //
    /**
     * リーダーから{@link StringBuilder}へコピーします。
     * <p>
     * リーダーはクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final Reader in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(new BufferedReader(in, DEFAULT_BUF_SIZE), out);
    }

    /**
     * リーダーから{@link StringBuilder}へコピーします。
     * <p>
     * リーダーはクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final BufferedReader in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(in, out);
    }

    // ////////////////////////////////////////////////////////////////
    // from File to OutputStream
    //
    /**
     * ファイルから出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            ファイル
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final File in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = InputStreamUtil.create(in);
        try {
            return copyInternal(
                new BufferedInputStream(is, DEFAULT_BUF_SIZE),
                new BufferedOutputStream(out, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * ファイルから出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            ファイル
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final File in, final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = InputStreamUtil.create(in);
        try {
            return copyInternal(
                new BufferedInputStream(is, DEFAULT_BUF_SIZE),
                out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from File to Writer
    //
    /**
     * プラットフォームのデフォルトエンコーディングでファイルからライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            ファイル
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final File in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in);
        try {
            return copyInternal(
                new BufferedReader(is, DEFAULT_BUF_SIZE),
                new BufferedWriter(out, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * プラットフォームのデフォルトエンコーディングでファイルからライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            ファイル
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final File in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in);
        try {
            return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定のエンコーディングでファイルからライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final File in, final String encoding,
            final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        try {
            return copyInternal(
                new BufferedReader(is, DEFAULT_BUF_SIZE),
                new BufferedWriter(out, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定のエンコーディングでファイルからライターへコピーします。
     * 
     * @param in
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final File in, final String encoding,
            final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        try {
            return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from File to File
    //
    /**
     * ファイルからファイルへコピーします。
     * 
     * @param in
     *            入力ファイル
     * @param out
     *            出力ファイル
     * @return コピーしたバイト数
     */
    public static int copy(final File in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = InputStreamUtil.create(in);
        try {
            final OutputStream os = OutputStreamUtil.create(out);
            try {
                return copyInternal(new BufferedInputStream(
                    is,
                    DEFAULT_BUF_SIZE), new BufferedOutputStream(
                    os,
                    DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定されたエンコーディングのファイルからプラットフォームデフォルトエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            入力ファイル
     * @param encoding
     *            エンコーディング
     * @param out
     *            出力ファイル
     * @return コピーした文字数
     */
    public static int copy(final File in, final String encoding, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        try {
            final Writer os = WriterUtil.create(out);
            try {
                return copyInternal(
                    new BufferedReader(is, DEFAULT_BUF_SIZE),
                    new BufferedWriter(os, DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * プラットフォームデフォルトエンコーディングのファイルから指定されたエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            入力ファイル
     * @param out
     *            出力ファイル
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final File in, final File out, final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Reader is = ReaderUtil.create(in);
        try {
            final Writer os = WriterUtil.create(out, encoding);
            try {
                return copyInternal(
                    new BufferedReader(is, DEFAULT_BUF_SIZE),
                    new BufferedWriter(os, DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定されたエンコーディングのファイルから指定されたエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            入力ファイル
     * @param inputEncoding
     *            入力ファイルのエンコーディング
     * @param out
     *            出力ファイル
     * @param outputEncoding
     *            出力ファイルのエンコーディング
     * @return コピーした文字数
     */
    public static int copy(final File in, final String inputEncoding,
            final File out, final String outputEncoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("inputEncoding", inputEncoding);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("outputEncoding", outputEncoding);

        final Reader is = ReaderUtil.create(in, inputEncoding);
        try {
            final Writer os = WriterUtil.create(out, outputEncoding);
            try {
                return copyInternal(
                    new BufferedReader(is, DEFAULT_BUF_SIZE),
                    new BufferedWriter(os, DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from File to StringBuilder
    //
    /**
     * プラットフォームのデフォルトエンコーディングでファイルから{@link StringBuilder}へコピーします。
     * 
     * @param in
     *            ファイル
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final File in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in);
        try {
            return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定されたエンコーディングでファイルから{@link StringBuilder}へコピーします。
     * 
     * @param in
     *            ファイル
     * @param out
     *            {@link StringBuilder}
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final File in, final String encoding,
            final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is = ReaderUtil.create(in, encoding);
        try {
            return copyInternal(new BufferedReader(is, DEFAULT_BUF_SIZE), out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from URL to OutputStream
    //
    /**
     * URLから出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            URL
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final URL in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(
                new BufferedInputStream(is, DEFAULT_BUF_SIZE),
                new BufferedOutputStream(out, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * URLから出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            URL
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final URL in, final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(
                new BufferedInputStream(is, DEFAULT_BUF_SIZE),
                out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from URL to Writer
    //
    /**
     * プラットフォームのデフォルトエンコーディングでURLからライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            URL
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final URL in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(new BufferedReader(
                new InputStreamReader(is),
                DEFAULT_BUF_SIZE), new BufferedWriter(out, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * プラットフォームのデフォルトエンコーディングでURLからライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            URL
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final URL in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(new BufferedReader(
                new InputStreamReader(is),
                DEFAULT_BUF_SIZE), out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定のエンコーディングでURLからライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            URL
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final URL in, final String encoding, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(
                new BufferedReader(
                    ReaderUtil.create(is, encoding),
                    DEFAULT_BUF_SIZE),
                new BufferedWriter(out, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定のエンコーディングでURLからライターへコピーします。
     * 
     * @param in
     *            URL
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final URL in, final String encoding,
            final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(
                new BufferedReader(
                    ReaderUtil.create(is, encoding),
                    DEFAULT_BUF_SIZE),
                out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from URL to File
    //
    /**
     * URLからファイルへコピーします。
     * 
     * @param in
     *            URL
     * @param out
     *            ファイル
     * @return コピーしたバイト数
     */
    public static int copy(final URL in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            final OutputStream os = OutputStreamUtil.create(out);
            try {
                return copyInternal(new BufferedInputStream(
                    is,
                    DEFAULT_BUF_SIZE), new BufferedOutputStream(
                    os,
                    DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定されたエンコーディングのURLからプラットフォームデフォルトエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            URL
     * @param encoding
     *            エンコーディング
     * @param out
     *            出力ファイル
     * @return コピーした文字数
     */
    public static int copy(final URL in, final String encoding, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            final Writer os = WriterUtil.create(out);
            try {
                return copyInternal(
                    new BufferedReader(
                        ReaderUtil.create(is, encoding),
                        DEFAULT_BUF_SIZE),
                    new BufferedWriter(os, DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * プラットフォームデフォルトエンコーディングのURLから指定されたエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            URL
     * @param out
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final URL in, final File out, final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final InputStream is = URLUtil.openStream(in);
        try {
            final Writer os = WriterUtil.create(out, encoding);
            try {
                return copyInternal(new BufferedReader(
                    new InputStreamReader(is),
                    DEFAULT_BUF_SIZE), new BufferedWriter(os, DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定されたエンコーディングのURLから指定されたエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            URL
     * @param inputEncoding
     *            URLのエンコーディング
     * @param out
     *            ファイル
     * @param outputEncoding
     *            ファイルのエンコーディング
     * @return コピーした文字数
     */
    public static int copy(final URL in, final String inputEncoding,
            final File out, final String outputEncoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("inputEncoding", inputEncoding);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("outputEncoding", outputEncoding);

        final InputStream is = URLUtil.openStream(in);
        try {
            final Writer os = WriterUtil.create(out, outputEncoding);
            try {
                return copyInternal(
                    new BufferedReader(
                        ReaderUtil.create(is, inputEncoding),
                        DEFAULT_BUF_SIZE),
                    new BufferedWriter(os, DEFAULT_BUF_SIZE));
            } finally {
                CloseableUtil.close(os);
            }
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from URL to StringBuilder
    //
    /**
     * プラットフォームのデフォルトエンコーディングでURLから{@link StringBuilder}へコピーします。
     * 
     * @param in
     *            URL
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final URL in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(new BufferedReader(
                new InputStreamReader(is),
                DEFAULT_BUF_SIZE), out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 指定されたエンコーディングでURLから{@link StringBuilder}へコピーします。
     * 
     * @param in
     *            URL
     * @param encoding
     *            エンコーディング
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final URL in, final String encoding,
            final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final InputStream is = URLUtil.openStream(in);
        try {
            return copyInternal(
                new BufferedReader(
                    ReaderUtil.create(is, encoding),
                    DEFAULT_BUF_SIZE),
                out);
        } finally {
            CloseableUtil.close(is);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from bytes to OutputStream
    //
    /**
     * バイト配列から出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            バイト配列
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final byte[] in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(
            new ByteArrayInputStream(in),
            new BufferedOutputStream(out, DEFAULT_BUF_SIZE));
    }

    /**
     * バイト配列から出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            バイト配列
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    public static int copy(final byte[] in, final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(new ByteArrayInputStream(in), out);
    }

    // ////////////////////////////////////////////////////////////////
    // from bytes to Writer
    //
    /**
     * プラットフォームのデフォルトエンコーディングでバイト配列からライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            バイト配列
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(new ByteArrayInputStream(in));
        return copyInternal(is, new BufferedWriter(out, DEFAULT_BUF_SIZE));
    }

    /**
     * プラットフォームのデフォルトエンコーディングでバイト配列からライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            バイト配列
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(new ByteArrayInputStream(in));
        return copyInternal(is, out);
    }

    /**
     * 指定されたエンコーディングでバイト配列からライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            バイト配列
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final String encoding,
            final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        try {
            final Reader is =
                new InputStreamReader(new ByteArrayInputStream(in), encoding);
            return copyInternal(is, new BufferedWriter(out, DEFAULT_BUF_SIZE));
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * 指定されたエンコーディングでバイト配列からライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            バイト配列
     * @param encoding
     *            エンコーディング
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final String encoding,
            final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        try {
            final Reader is =
                new InputStreamReader(new ByteArrayInputStream(in), encoding);
            return copyInternal(is, out);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from bytes to File
    //
    /**
     * バイト配列からファイルへコピーします。
     * 
     * @param in
     *            バイト配列
     * @param out
     *            ファイル
     * @return コピーしたバイト数
     */
    public static int copy(final byte[] in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final OutputStream os = OutputStreamUtil.create(out);
        try {
            return copyInternal(
                new ByteArrayInputStream(in),
                new BufferedOutputStream(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * 指定されたエンコーディングのバイト配列からプラットフォームデフォルトエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            バイト配列
     * @param encoding
     *            エンコーディング
     * @param out
     *            ファイル
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final String encoding,
            final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is =
            ReaderUtil.create(new ByteArrayInputStream(in), encoding);
        final Writer os = WriterUtil.create(out);
        try {
            return copyInternal(is, new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * プラットフォームデフォルトエンコーディングのバイト配列から指定されたエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            バイト配列
     * @param out
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final File out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Reader is = new InputStreamReader(new ByteArrayInputStream(in));
        final Writer os = WriterUtil.create(out, encoding);
        try {
            return copyInternal(is, new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * 指定されたエンコーディングのバイト配列から指定されたエンコーディングのファイルへコピーします。
     * 
     * @param in
     *            バイト配列
     * @param inputEncoding
     *            入力のエンコーディング
     * @param out
     *            ファイル
     * @param outputEncoding
     *            出力のエンコーディング
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final String inputEncoding,
            final File out, final String outputEncoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("inputEncoding", inputEncoding);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("outputEncoding", outputEncoding);

        final Reader is =
            ReaderUtil.create(new ByteArrayInputStream(in), inputEncoding);
        final Writer os = WriterUtil.create(out, outputEncoding);
        try {
            return copyInternal(is, new BufferedWriter(os, DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // from bytes to StringBuilder
    //
    /**
     * プラットフォームのデフォルトエンコーディングでバイト配列から{@link StringBuilder}へコピーします。
     * 
     * @param in
     *            バイト配列
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Reader is = new InputStreamReader(new ByteArrayInputStream(in));
        return copyInternal(is, out);
    }

    /**
     * 指定されたエンコーディングのバイト配列からプラットフォームデフォルトエンコーディングの{@link StringBuilder}へコピーします。
     * 
     * @param in
     *            バイト配列
     * @param encoding
     *            エンコーディング
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    public static int copy(final byte[] in, final String encoding,
            final StringBuilder out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotEmpty("encoding", encoding);
        assertArgumentNotNull("out", out);

        final Reader is =
            ReaderUtil.create(new ByteArrayInputStream(in), encoding);
        return copyInternal(is, out);
    }

    // ////////////////////////////////////////////////////////////////
    // from String to OutputStream
    //
    /**
     * プラットフォームのデフォルトエンコーディングで文字列を出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            文字列
     * @param out
     *            出力ストリーム
     * @return コピーした文字数
     */
    public static int copy(final String in, final OutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = new OutputStreamWriter(out);
        return copyInternal(new StringReader(in), new BufferedWriter(
            os,
            DEFAULT_BUF_SIZE));
    }

    /**
     * プラットフォームのデフォルトエンコーディングで文字列を出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            文字列
     * @param out
     *            出力ストリーム
     * @return コピーした文字数
     */
    public static int copy(final String in, final BufferedOutputStream out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = new OutputStreamWriter(out);
        return copyInternal(new StringReader(in), os);
    }

    /**
     * 指定されたエンコーディングで文字列を出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            文字列
     * @param out
     *            出力ストリーム
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final String in, final OutputStream out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        return copyInternal(new StringReader(in), new BufferedWriter(
            os,
            DEFAULT_BUF_SIZE));
    }

    /**
     * 指定されたエンコーディングで文字列を出力ストリームへコピーします。
     * <p>
     * 出力ストリームはクローズされません。
     * </p>
     * 
     * @param in
     *            文字列
     * @param out
     *            出力ストリーム
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final String in, final BufferedOutputStream out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        return copyInternal(new StringReader(in), os);
    }

    // ////////////////////////////////////////////////////////////////
    // from String to Writer
    //
    /**
     * 文字列をライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            文字列
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final String in, final Writer out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(new StringReader(in), new BufferedWriter(
            out,
            DEFAULT_BUF_SIZE));
    }

    /**
     * 文字列をライターへコピーします。
     * <p>
     * ライターはクローズされません。
     * </p>
     * 
     * @param in
     *            文字列
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    public static int copy(final String in, final BufferedWriter out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        return copyInternal(new StringReader(in), out);
    }

    // ////////////////////////////////////////////////////////////////
    // from String to File
    //
    /**
     * プラットフォームのデフォルトエンコーディングで文字列をファイルへコピーします。
     * 
     * @param in
     *            文字列
     * @param out
     *            ファイル
     * @return コピーした文字数
     */
    public static int copy(final String in, final File out) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);

        final Writer os = WriterUtil.create(out);
        try {
            return copyInternal(new StringReader(in), new BufferedWriter(
                os,
                DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    /**
     * 指定されたエンコーディングで文字列をファイルへコピーします。
     * 
     * @param in
     *            文字列
     * @param out
     *            ファイル
     * @param encoding
     *            エンコーディング
     * @return コピーした文字数
     */
    public static int copy(final String in, final File out,
            final String encoding) {
        assertArgumentNotNull("in", in);
        assertArgumentNotNull("out", out);
        assertArgumentNotEmpty("encoding", encoding);

        final Writer os = WriterUtil.create(out, encoding);
        try {
            return copyInternal(new StringReader(in), new BufferedWriter(
                os,
                DEFAULT_BUF_SIZE));
        } finally {
            CloseableUtil.close(os);
        }
    }

    // ////////////////////////////////////////////////////////////////
    // internal methods
    //
    /**
     * 入力ストリームの内容を出力ストリームにコピーします。
     * <p>
     * 入力ストリーム、出力ストリームともクローズされません。
     * </p>
     * 
     * @param in
     *            入力ストリーム
     * @param out
     *            出力ストリーム
     * @return コピーしたバイト数
     */
    protected static int copyInternal(final InputStream in,
            final OutputStream out) {
        try {
            final byte[] buf = new byte[DEFAULT_BUF_SIZE];
            int len;
            int amount = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                amount += len;
            }
            out.flush();
            return amount;
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * リーダーの内容をライターにコピーします。
     * <p>
     * リーダー、ライターともクローズされません。
     * </p>
     * 
     * @param in
     *            リーダー
     * @param out
     *            ライター
     * @return コピーした文字数
     */
    protected static int copyInternal(final Reader in, final Writer out) {
        try {
            final char[] buf = new char[DEFAULT_BUF_SIZE];
            int len;
            int amount = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
                amount += len;
            }
            out.flush();
            return amount;
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * リーダーの内容を{@link StringBuilder}にコピーします。
     * 
     * @param in
     *            リーダー
     * @param out
     *            {@link StringBuilder}
     * @return コピーした文字数
     */
    protected static int copyInternal(final Reader in, final StringBuilder out) {
        try {
            final char[] buf = new char[DEFAULT_BUF_SIZE];
            int len;
            int amount = 0;
            while ((len = in.read(buf)) != -1) {
                out.append(buf, 0, len);
                amount += len;
            }
            return amount;
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
