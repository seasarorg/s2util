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
package org.seasar.util.crypto.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.seasar.util.crypto.CipherContext;
import org.seasar.util.exception.InvalidKeyRuntimeException;
import org.seasar.util.exception.NoSuchAlgorithmRuntimeException;
import org.seasar.util.exception.NoSuchPaddingRuntimeException;
import org.seasar.util.exception.NullArgumentException;
import org.seasar.util.exception.SIllegalStateException;
import org.seasar.util.misc.Base64Util;

/**
 * CipherContextの標準実装です。
 * 
 * @author shinsuke
 * 
 */
public class CipherContextImpl implements CipherContext {
    private final String algorithm;

    private final String key;

    private final String id;

    /**
     * コンストラクタ
     * 
     * @param algorithm
     *            アルゴリズム
     * @param key
     *            キー
     */
    public CipherContextImpl(final String algorithm, final String key) {
        if (algorithm == null) {
            throw new NullArgumentException("algorithm");
        }
        if (key == null) {
            throw new NullArgumentException("key");
        }

        this.algorithm = algorithm;
        this.key = key;
        try {
            this.id =
                Base64Util.encode(new StringBuilder()
                    .append(getClass().getName())
                    .append(algorithm)
                    .append(key)
                    .toString()
                    .getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new SIllegalStateException(e);
        }
    }

    @Override
    public Cipher getCipher(final int opmode) {
        final SecretKeySpec sksSpec =
            new SecretKeySpec(key.getBytes(), algorithm);
        try {
            final Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(opmode, sksSpec);
            return cipher;
        } catch (final InvalidKeyException e) {
            throw new InvalidKeyRuntimeException(e);
        } catch (final NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmRuntimeException(e);
        } catch (final NoSuchPaddingException e) {
            throw new NoSuchPaddingRuntimeException(e);
        }
    }

    @Override
    public String getId() {
        return id;
    }

}
