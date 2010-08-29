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
package org.seasar.util.io;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.NullArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author taichi
 * 
 */
public class FileUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    String root;

    File src;

    File dest;

    /**
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        root = ResourceUtil.getBuildDir(getClass()).getCanonicalPath();
        String srcTxt = root + "/org/seasar/util/io/src.txt";
        src = new File(srcTxt);
        String destTxt = root + "/org/seasar/util/io/dest.txt";
        dest = new File(destTxt);
    }

    /**
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
        if (dest.exists()) {
            dest.delete();
        }
    }

    /**
     * Test method for {@link org.seasar.util.io.FileUtil#copy(File, File)}
     * 
     * @throws Exception
     */
    @Test
    public void testCopy_New() throws Exception {
        assertTrue(src.exists());
        assertFalse(dest.exists());

        FileUtil.copy(src, dest);
        assertEquals(TextUtil.readText(src), TextUtil.readText(dest));
    }

    /**
     * Test method for {@link org.seasar.util.io.FileUtil#copy(File, File)}
     * 
     * @throws Exception
     */
    @Test
    public void testCopy_Exists() throws Exception {
        assertTrue(src.exists());
        assertFalse(dest.exists());

        dest.createNewFile();
        FileUtil.copy(src, dest);
        assertEquals(TextUtil.readText(src), TextUtil.readText(dest));
    }

    /**
     * Test method for {@link org.seasar.util.io.FileUtil#write(String, byte[])}
     */
    @Test
    public void testWritePathNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[path]がnullです。"));
        FileUtil.write(null, new byte[] {});
    }

    /**
     * Test method for {@link org.seasar.util.io.FileUtil#write(String, byte[])}
     */
    @Test
    public void testWriteDataNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[data]がnullです。"));
        FileUtil.write("hoge", null);
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.FileUtil#write(String, byte[], int, int)}
     */
    @Test
    public void testWriteOffsetPathNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[path]がnullです。"));
        FileUtil.write(null, new byte[] {}, 0, 0);
    }

    /**
     * Test method for
     * {@link org.seasar.util.io.FileUtil#write(String, byte[], int, int)}
     */
    @Test
    public void testWriteOffsetDataNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[data]がnullです。"));
        FileUtil.write("hoge", null, 0, 0);
    }

}
