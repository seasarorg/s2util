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
package org.seasar.util.exception;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author wyukawa
 * 
 */
public class SUnsupportedOperationExceptionTest {

    /**
     * Test method for
     * {@link org.seasar.util.exception.SUnsupportedOperationException#SUnsupportedOperationException()}
     * .
     */
    @Test
    public void testSUnsupportedOperationException() {
        SUnsupportedOperationException sUnsupportedOperationException =
            new SUnsupportedOperationException();
        assertThat(sUnsupportedOperationException, is(notNullValue()));
    }

    /**
     * Test method for
     * {@link org.seasar.util.exception.SUnsupportedOperationException#SUnsupportedOperationException(java.lang.String)}
     * .
     */
    @Test
    public void testSUnsupportedOperationExceptionString() {
        SUnsupportedOperationException sUnsupportedOperationException =
            new SUnsupportedOperationException("hoge");
        assertThat(sUnsupportedOperationException.getMessage(), is("hoge"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.exception.SUnsupportedOperationException#SUnsupportedOperationException(java.lang.String, java.lang.Throwable)}
     * .
     */
    @Test
    public void testSUnsupportedOperationExceptionStringThrowable() {
        SUnsupportedOperationException sUnsupportedOperationException =
            new SUnsupportedOperationException(
                "hoge",
                new NullPointerException());
        assertThat(sUnsupportedOperationException.getMessage(), is("hoge"));
        assertThat(
            sUnsupportedOperationException.getCause(),
            instanceOf(NullPointerException.class));
    }

    /**
     * Test method for
     * {@link org.seasar.util.exception.SUnsupportedOperationException#SUnsupportedOperationException(java.lang.Throwable)}
     * .
     */
    @Test
    public void testSUnsupportedOperationExceptionThrowable() {
        SUnsupportedOperationException sUnsupportedOperationException =
            new SUnsupportedOperationException(new NullPointerException());
        assertThat(
            sUnsupportedOperationException.getCause(),
            instanceOf(NullPointerException.class));
    }

}
