/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import org.junit.Before;
import org.junit.Test;
import org.seasar.util.crypto.impl.CipherContextImpl;

import static org.junit.Assert.*;

/**
 * @author shinsuke
 * 
 */
public class CipherPoolUtilTest {
    private CipherContext blowfishContext;

    private CipherContext aesContext;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        blowfishContext = new CipherContextImpl("Blowfish", "hogefuga");
        aesContext = new CipherContextImpl("AES", "1234567890123456");
        CipherPoolUtil.encryptoQueueMap.clear();
        CipherPoolUtil.decryptoQueueMap.clear();
    }

    /**
     * 
     */
    @Test
    public void testCreateCipherInstance() {
        CipherPoolUtil.create(blowfishContext, 5);
        assertEquals(1, CipherPoolUtil.encryptoQueueMap.size());
        assertEquals(1, CipherPoolUtil.decryptoQueueMap.size());
        assertEquals(
            5,
            CipherPoolUtil.encryptoQueueMap.get(blowfishContext.getId()).size());
        assertEquals(
            5,
            CipherPoolUtil.decryptoQueueMap.get(blowfishContext.getId()).size());

        CipherPoolUtil.create(aesContext, 10);
        assertEquals(2, CipherPoolUtil.encryptoQueueMap.size());
        assertEquals(2, CipherPoolUtil.decryptoQueueMap.size());
        assertEquals(10, CipherPoolUtil.encryptoQueueMap
            .get(aesContext.getId())
            .size());
        assertEquals(10, CipherPoolUtil.decryptoQueueMap
            .get(aesContext.getId())
            .size());

    }

    /**
     * 
     */
    @Test
    public void testEncyptAndDecrypt() {
        assertEquals(0, CipherPoolUtil.encryptoQueueMap.size());
        assertEquals(0, CipherPoolUtil.decryptoQueueMap.size());

        String encryptoText1 =
            CipherPoolUtil.encryptoText(blowfishContext, "パスワード", "UTF-8");
        String encryptoText2 =
            CipherPoolUtil.encryptoText(aesContext, "パスワード", "UTF-8");
        assertEquals("KFLfwaFXEacFrNo/G1MMwg==", encryptoText1);
        assertEquals("bue4IhXtgIr5a8EajNW7CA==", encryptoText2);

        String decryptoText1 =
            CipherPoolUtil
                .decryptoText(blowfishContext, encryptoText1, "UTF-8");
        String decryptoText2 =
            CipherPoolUtil.decryptoText(aesContext, encryptoText2, "UTF-8");
        assertEquals("パスワード", decryptoText1);
        assertEquals("パスワード", decryptoText2);

        assertEquals(2, CipherPoolUtil.encryptoQueueMap.size());
        assertEquals(2, CipherPoolUtil.decryptoQueueMap.size());
        assertEquals(
            1,
            CipherPoolUtil.encryptoQueueMap.get(blowfishContext.getId()).size());
        assertEquals(
            1,
            CipherPoolUtil.decryptoQueueMap.get(blowfishContext.getId()).size());
        assertEquals(1, CipherPoolUtil.encryptoQueueMap
            .get(aesContext.getId())
            .size());
        assertEquals(1, CipherPoolUtil.decryptoQueueMap
            .get(aesContext.getId())
            .size());

        String encryptoText3 =
            CipherPoolUtil.encryptoText(blowfishContext, "パスワード", "Shift_JIS");
        String encryptoText4 =
            CipherPoolUtil.encryptoText(aesContext, "パスワード", "Shift_JIS");
        assertEquals("fzreHG+43TzWt9E8wxAzMw==", encryptoText3);
        assertEquals("aDmZZWThjIwONrXXm5edsA==", encryptoText4);

        String decryptoText3 =
            CipherPoolUtil.decryptoText(
                blowfishContext,
                encryptoText3,
                "Shift_JIS");
        String decryptoText4 =
            CipherPoolUtil.decryptoText(aesContext, encryptoText4, "Shift_JIS");
        assertEquals("パスワード", decryptoText3);
        assertEquals("パスワード", decryptoText4);

        assertEquals(2, CipherPoolUtil.encryptoQueueMap.size());
        assertEquals(2, CipherPoolUtil.decryptoQueueMap.size());
        assertEquals(
            1,
            CipherPoolUtil.encryptoQueueMap.get(blowfishContext.getId()).size());
        assertEquals(
            1,
            CipherPoolUtil.decryptoQueueMap.get(blowfishContext.getId()).size());
        assertEquals(1, CipherPoolUtil.encryptoQueueMap
            .get(aesContext.getId())
            .size());
        assertEquals(1, CipherPoolUtil.decryptoQueueMap
            .get(aesContext.getId())
            .size());
    }
}
