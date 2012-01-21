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
public class SIllegalStateExceptionTest {

    /**
     * Test method for
     * {@link org.seasar.util.exception.SIllegalStateException#SIllegalStateException()}
     * .
     */
    @Test
    public void testSIllegalStateException() {
        SIllegalStateException sIllegalStateException =
            new SIllegalStateException();
        assertThat(sIllegalStateException, is(notNullValue()));

    }

    /**
     * Test method for
     * {@link org.seasar.util.exception.SIllegalStateException#SIllegalStateException(java.lang.String)}
     * .
     */
    @Test
    public void testSIllegalStateExceptionString() {
        SIllegalStateException sIllegalStateException =
            new SIllegalStateException("hoge");
        assertThat(sIllegalStateException.getMessage(), is("hoge"));
    }

    /**
     * Test method for
     * {@link org.seasar.util.exception.SIllegalStateException#SIllegalStateException(java.lang.String, java.lang.Throwable)}
     * .
     */
    @Test
    public void testSIllegalStateExceptionStringThrowable() {
        SIllegalStateException sIllegalStateException =
            new SIllegalStateException("hoge", new NullPointerException());
        assertThat(sIllegalStateException.getMessage(), is("hoge"));
        assertThat(
            sIllegalStateException.getCause(),
            instanceOf(NullPointerException.class));
    }

    /**
     * Test method for
     * {@link org.seasar.util.exception.SIllegalStateException#SIllegalStateException(java.lang.Throwable)}
     * .
     */
    @Test
    public void testSIllegalStateExceptionThrowable() {
        SIllegalStateException sIllegalStateException =
            new SIllegalStateException(new NullPointerException());
        assertThat(
            sIllegalStateException.getCause(),
            instanceOf(NullPointerException.class));
    }

}
