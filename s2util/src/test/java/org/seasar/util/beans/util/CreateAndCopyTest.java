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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 */
public class CreateAndCopyTest {

    /**
     * @throws Exception
     */
    @Test
    public void testExecute_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = new CreateAndCopy<MyBean>(MyBean.class, src).execute();
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecute_beanToMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        BeanMap dest = new CreateAndCopy<BeanMap>(BeanMap.class, src).execute();
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecute_beanToMap_exclude_prefix() throws Exception {
        MyBean3 src = new MyBean3();
        BeanMap dest =
            new CreateAndCopy<BeanMap>(BeanMap.class, src)
                .prefix("abc_")
                .excludes("abc_exclude")
                .execute();
        assertThat(dest.size(), is(1));
        assertThat(dest.get("value"), is((Object) "abc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecute_mapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = new CreateAndCopy<MyBean>(MyBean.class, src).execute();
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExecute_mapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = new CreateAndCopy<BeanMap>(BeanMap.class, src).execute();
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

}
