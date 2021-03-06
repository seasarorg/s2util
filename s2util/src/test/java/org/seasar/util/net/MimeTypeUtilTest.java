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
package org.seasar.util.net;

import java.net.URLConnection;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.EmptyArgumentException;
import org.seasar.util.io.ResourceUtil;
import org.seasar.util.lang.ClassUtil;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author shot
 */
public class MimeTypeUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * @throws Exception
     */
    @Test
    public void testGetFromStream() throws Exception {
        String path =
            ClassUtil.getPackageName(this.getClass()).replaceAll("\\.", "/")
                + "/aaa.html";
        String contentType = MimeTypeUtil.guessContentType(path);
        assertEquals("text/html", contentType);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetFromStream_gif() throws Exception {
        String path =
            ClassUtil.getPackageName(this.getClass()).replaceAll("\\.", "/")
                + "/ccc.gif";
        String contentType = MimeTypeUtil.guessContentType(path);
        assertEquals("image/gif", contentType);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetFromPath() throws Exception {
        String path =
            ClassUtil.getPackageName(this.getClass()).replaceAll("\\.", "/")
                + "/bbb.html";
        String s =
            URLConnection.guessContentTypeFromStream(ResourceUtil
                .getResourceAsStream(path));
        assertNull(s);
        String contentType = MimeTypeUtil.guessContentType(path);
        assertEquals("text/html", contentType);
    }

    /**
     * Test method for
     * {@link org.seasar.util.net.MimeTypeUtil#guessContentType(String)} .
     */
    @Test
    public void testGuessContentType() {
        exception.expect(EmptyArgumentException.class);
        exception.expectMessage(is("[EUTL0010]引数[path]がnullあるいは空文字列です。"));
        MimeTypeUtil.guessContentType(null);
    }

}
