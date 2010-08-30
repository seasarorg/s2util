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
package org.seasar.util.convert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.SIllegalArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author wyukawa
 * 
 */
public class BinaryConversionUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test method for
     * {@link org.seasar.util.convert.BinaryConversionUtil#toBinary(java.lang.Object)}
     * .
     */
    @Test
    public void testToBinary() {
        assertThat(BinaryConversionUtil.toBinary(null), nullValue());
        byte[] b = { 0x00, 0x01 };
        assertThat(BinaryConversionUtil.toBinary(b), is(b));
        assertThat(BinaryConversionUtil.toBinary("hoge"), is("hoge".getBytes()));
    }

    /**
     * Test method for
     * {@link org.seasar.util.convert.BinaryConversionUtil#toBinary(java.lang.Object)}
     * .
     */
    @Test
    public void testToBinaryException() {
        exception.expect(SIllegalArgumentException.class);
        exception
            .expectMessage(is("[EUTL0009]引数[o]が不正です。理由はclass java.lang.Object"));
        BinaryConversionUtil.toBinary(new Object());
    }

}
