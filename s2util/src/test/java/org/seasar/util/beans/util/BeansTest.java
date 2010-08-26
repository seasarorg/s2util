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
import org.seasar.util.beans.converter.DateConverter;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 */
public class BeansTest {

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = new MyBean();
        Beans.copy(src, dest).execute();
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToBean_converter() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "2008/01/17";
        MyBean2 dest = new MyBean2();
        Beans
            .copy(src, dest)
            .converter(new DateConverter("yyyy/MM/dd"))
            .execute();
        System.out.println(dest.aaa);
        assertThat(dest.aaa, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        BeanMap dest = new BeanMap();
        Beans.copy(src, dest).execute();
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_mapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = new MyBean();
        Beans.copy(src, dest).execute();
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_mapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = new BeanMap();
        Beans.copy(src, dest).execute();
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateAndCopy_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = Beans.createAndCopy(MyBean.class, src).execute();
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateAndCopy_beanToMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        BeanMap dest = Beans.createAndCopy(BeanMap.class, src).execute();
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateAndCopy_mapToBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = Beans.createAndCopy(MyBean.class, src).execute();
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateAndCopy_mapToMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        BeanMap dest = Beans.createAndCopy(BeanMap.class, src).execute();
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

}
