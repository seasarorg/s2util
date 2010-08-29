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
package org.seasar.util.misc;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.NullArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * @author wyukawa
 * 
 */
public class AssertionUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * Test method for
     * {@link org.seasar.util.misc.AssertionUtil#assertArgumentNotNull(java.lang.String, java.lang.Object)}
     * .
     */
    @Test
    public void testAssertArgumentNotNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[hoge]がnullです。"));
        assertArgumentNotNull("hoge", null);
    }

}
