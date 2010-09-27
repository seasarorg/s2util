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
package org.seasar.util.crypto;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.crypto.Cipher;

import org.seasar.util.crypto.impl.CipherContextImpl;
import org.seasar.util.exception.SIllegalStateException;
import org.seasar.util.misc.Base64Util;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link Cipher}を扱うユーティリティです。
 * <p>
 * Cipher のインスタンスをプールします。基本的な使い方は次のようになります。
 * </p>
 * <p>
 * 暗号化や複合化の情報を保持する{@link CipherContext}のインスタンスを定数として定義します。 {@link CipherContext}
 * の標準的な実装クラスとして {@link CipherContextImpl}が用意されています (利用可能なアルゴリズムは{@link Cipher}
 * のJavadoc等を参照してください)。
 * </p>
 * 
 * <pre>
 * public static final CipherContext BLOWFISH_CONTEXT =
 *     new CipherContextImpl("Blowfish", "hogefuga");
 * </pre>
 * <p>
 * {@link Cipher}は初期化にコストがかかるので、{@link CipherPoolUtil}は{@link CipherContext}
 * のインスタンスをプールします。 あらかじめインスタンスをプールしておくには{@link #create(CipherContext, int)}
 * メソッドを使います。
 * </p>
 * 
 * <pre>
 * CipherPoolUtil.create(BLOWFISH_CONTEXT, 5);
 * </pre>
 * <p>
 * 文字列を暗号化するには{@link #encryptoText(CipherContext, String, String)}を使います。
 * </p>
 * 
 * <pre>
 * String encryptoText =
 *     CipherPoolUtil.encryptoText(BLOWFISH_CONTEXT, "パスワード", "UTF-8");
 * </pre>
 * <p>
 * 文字列を復号化するには{@link #decryptoText(CipherContext, String, String)}を使います。
 * </p>
 * 
 * <pre>
 * String decryptoText1 =
 *     CipherPoolUtil
 *         .decryptoText(BLOWFISH_CONTEXT, encryptoText1, "UTF-8");
 * </pre>
 * 
 * @author shinsuke
 */
public abstract class CipherPoolUtil {
    /**
     * encrypto用のCipherプール
     */
    protected static final Map<String, Queue<Cipher>> encryptoQueueMap =
        new ConcurrentHashMap<String, Queue<Cipher>>();

    /**
     * decrypto用のCipherプール
     */
    protected static final Map<String, Queue<Cipher>> decryptoQueueMap =
        new ConcurrentHashMap<String, Queue<Cipher>>();

    /**
     * {@link CipherContext}の{@link Cipher}インスタンスを事前にプールします。
     * 
     * @param context
     *            生成するCipherContext
     * @param size
     *            生成するCipherインスタンス数
     */
    public static void create(final CipherContext context, final int size) {
        assertArgumentNotNull("context", context);

        final Queue<Cipher> encryptoCipherQueue =
            getEncryptoCipherQueue(context);
        final Queue<Cipher> decryptoCipherQueue =
            getDecryptoCipherQueue(context);
        for (int i = 0; i < size; i++) {
            encryptoCipherQueue.add(context.getCipher(Cipher.ENCRYPT_MODE));
            decryptoCipherQueue.add(context.getCipher(Cipher.DECRYPT_MODE));
        }
    }

    /**
     * {@link CipherContext}の{@link Cipher}でバイト配列を暗号化します。
     * 
     * @param context
     *            利用するCipherContext
     * @param data
     *            暗号化するバイト配列
     * @return 暗号化されたバイト配列
     */
    public static byte[] encrypto(final CipherContext context, final byte[] data) {
        assertArgumentNotNull("context", context);
        assertArgumentNotNull("data", data);

        final Cipher cipher = getEncryptoCipher(context);
        try {
            final byte[] encrypted = cipher.doFinal(data);
            putEncryptoCipher(context, cipher);
            return encrypted;
        } catch (final Exception e) {
            throw new SIllegalStateException(e);
        }
    }

    /**
     * {@link CipherContext}の{@link Cipher}で文字列を暗号化します。
     * 
     * @param context
     *            利用するCipherContext
     * @param text
     *            暗号化する文字列
     * @param charsetName
     *            エンコーディング名
     * @return 暗号化された文字列
     */
    public static String encryptoText(final CipherContext context,
            final String text, final String charsetName) {
        assertArgumentNotNull("context", context);
        assertArgumentNotNull("text", text);
        assertArgumentNotEmpty("charsetName", charsetName);

        try {
            return Base64Util.encode(encrypto(
                context,
                text.getBytes(charsetName)));
        } catch (final Exception e) {
            throw new SIllegalStateException(e);
        }
    }

    /**
     * {@link CipherContext}の{@link Cipher}でバイト配列を復号化します。
     * 
     * @param context
     *            利用するCipherContext
     * @param data
     *            復号化するバイト配列
     * @return 復号化されたバイト配列
     */
    public static byte[] decrypto(final CipherContext context, final byte[] data) {
        assertArgumentNotNull("context", context);
        assertArgumentNotNull("data", data);

        final Cipher cipher = getDecryptoCipher(context);
        try {
            final byte[] decrypted = cipher.doFinal(data);
            putDecryptoCipher(context, cipher);
            return decrypted;
        } catch (final Exception e) {
            throw new SIllegalStateException(e);
        }
    }

    /**
     * {@link CipherContext}の{@link Cipher}で文字列を暗号化します。
     * 
     * @param context
     *            利用するCipherContext
     * @param text
     *            復号化する文字列
     * @param charsetName
     *            エンコーディング名
     * @return 復号化された文字列
     */
    public static String decryptoText(final CipherContext context,
            final String text, final String charsetName) {
        assertArgumentNotNull("context", context);
        assertArgumentNotNull("text", text);
        assertArgumentNotEmpty("charsetName", charsetName);

        try {
            return new String(
                decrypto(context, Base64Util.decode(text)),
                charsetName);
        } catch (final UnsupportedEncodingException e) {
            throw new SIllegalStateException(e);
        }
    }

    private static Cipher getEncryptoCipher(final CipherContext context) {
        final Cipher cipher = getEncryptoCipherQueue(context).poll();
        if (cipher == null) {
            return context.getCipher(Cipher.ENCRYPT_MODE);
        }
        return cipher;
    }

    private static void putEncryptoCipher(final CipherContext context,
            final Cipher cipher) {
        getEncryptoCipherQueue(context).offer(cipher);
    }

    private static Queue<Cipher> getEncryptoCipherQueue(
            final CipherContext context) {
        Queue<Cipher> queue = encryptoQueueMap.get(context.getId());
        if (queue == null) {
            queue = new ConcurrentLinkedQueue<Cipher>();
            encryptoQueueMap.put(context.getId(), queue);
        }
        return queue;
    }

    private static Cipher getDecryptoCipher(final CipherContext context) {
        final Cipher cipher = getDecryptoCipherQueue(context).poll();
        if (cipher == null) {
            return context.getCipher(Cipher.DECRYPT_MODE);
        }
        return cipher;
    }

    private static void putDecryptoCipher(final CipherContext context,
            final Cipher cipher) {
        getDecryptoCipherQueue(context).offer(cipher);
    }

    private static Queue<Cipher> getDecryptoCipherQueue(
            final CipherContext context) {
        Queue<Cipher> queue = decryptoQueueMap.get(context.getId());
        if (queue == null) {
            queue = new ConcurrentLinkedQueue<Cipher>();
            decryptoQueueMap.put(context.getId(), queue);
        }
        return queue;
    }

}
