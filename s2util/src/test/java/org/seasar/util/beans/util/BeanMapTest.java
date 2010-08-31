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
package org.seasar.util.beans.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.exception.SIllegalArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 */
public class BeanMapTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        BeanMap map = new BeanMap();
        map.put("aaa", 1);
        map.put("bbb", 2);
        assertThat(map.get("aaa"), is((Object) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGet_NotContains() throws Exception {
        exception.expect(SIllegalArgumentException.class);
        exception
            .expectMessage(is("[EUTL0009]引数[key]が不正です。理由はxxx is not found in [aaa, bbb]"));
        BeanMap map = new BeanMap();
        map.put("aaa", 1);
        map.put("bbb", 2);
        map.get("xxx");
    }

}
