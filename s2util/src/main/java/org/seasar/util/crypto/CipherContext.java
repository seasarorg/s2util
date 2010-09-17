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

import javax.crypto.Cipher;

/**
 * {@link Cipher}の情報を管理するクラスです。
 * 
 * @author shinsuke
 * 
 */
public interface CipherContext {

    /**
     * {@link Cipher}のインスタンスを取得します。
     * 
     * @param opmode
     *            暗号の操作モード (ENCRYPT_MODE、DECRYPT_MODE、WRAP_MODE、または UNWRAP_MODE
     *            のどれか)
     * @return {@link Cipher}のインスタンス
     */
    public abstract Cipher getCipher(int opmode);

    /**
     * CipherContextの識別子を返します。 この値が{@link CipherPoolUtil}のプールのキーになります。
     * 
     * @return CipherContextの識別子
     */
    public abstract String getId();

}
