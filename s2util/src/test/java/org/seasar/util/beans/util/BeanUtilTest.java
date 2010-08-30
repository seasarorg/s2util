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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.seasar.util.beans.converter.DateConverter;
import org.seasar.util.beans.converter.NumberConverter;
import org.seasar.util.exception.NullArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.beans.util.CopyOptionsUtil.*;
import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * @author higa
 */
public class BeanUtilTest {

    /**
     * @see org.junit.rules.ExpectedException
     */
    @Rule
    public ExpectedException exception = ExpectedException.none();

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_BeanToBean() throws Exception {
        MyClass src = new MyClass();
        src.setAaa("111");
        src.setCcc("333");

        MyClass2 dest = new MyClass2();
        dest.setAaa("aaa");
        dest.setBbb("bbb");
        dest.setDdd("ddd");

        BeanUtil.copyBeanToBean(src, dest);
        assertThat(dest.getAaa(), is("111"));
        assertThat(dest.getBbb(), is(nullValue()));
        assertThat(dest.getDdd(), is("ddd"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_BeanToBean_ExcludeNull() throws Exception {
        MyClass src = new MyClass();
        src.setAaa("111");
        src.setCcc("333");

        MyClass2 dest = new MyClass2();
        dest.setAaa("aaa");
        dest.setBbb("bbb");
        dest.setDdd("ddd");

        BeanUtil.copyBeanToBean(src, dest, excludeNull());
        assertThat(dest.getAaa(), is("111"));
        assertThat(dest.getBbb(), is("bbb"));
        assertThat(dest.getDdd(), is("ddd"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_BeanToMap() throws Exception {
        HogeDto hoge = new HogeDto();
        hoge.setA("A");
        hoge.setB(true);
        hoge.setC(3);
        Map<String, Object> map = newHashMap();
        BeanUtil.copyBeanToMap(hoge, map);
        assertThat(map, is(notNullValue()));
        assertThat(map.get("a"), is((Object) "A"));
        assertThat(map.get("b"), is((Object) true));
        assertThat(map.get("c"), is((Object) 3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_MapToBean() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("a", "A");
        map.put("b", true);
        map.put("c", 3);
        map.put("d", 1.4);
        HogeDto hoge = new HogeDto();
        BeanUtil.copyMapToBean(map, hoge);
        assertThat(hoge.getA(), is("A"));
        assertThat(hoge.isB(), is(true));
        assertThat(hoge.getC(), is(3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        src.eee = "1";
        DestBean dest = new DestBean();
        BeanUtil.copyBeanToBean(src, dest);
        assertThat(dest.bbb, is(nullValue()));
        assertThat(dest.ccc, is("ccc"));
        assertThat(dest.ddd, is(nullValue()));
        assertThat(dest.eee, is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_includes() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        MyBean dest = new MyBean();
        BeanUtil.copyBeanToBean(src, dest, include(BeanNames.aaa()));
        assertThat(dest.aaa, is("aaa"));
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_excludes() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        MyBean dest = new MyBean();
        BeanUtil.copyBeanToBean(src, dest, exclude(BeanNames.bbb()));
        assertThat(dest.aaa, is("aaa"));
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_null() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyBeanToBean(src, dest);
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_excludesNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyBeanToBean(src, dest, excludeNull());
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_whitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        DestBean dest = new DestBean();
        BeanUtil.copyBeanToBean(src, dest);
        assertThat(dest.ccc, is(" "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_excludesWhitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyBeanToBean(src, dest, excludeWhitespace());
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_prefix() throws Exception {
        SrcBean src = new SrcBean();
        src.search_eee$fff = "hoge";
        DestBean dest = new DestBean();
        BeanUtil.copyBeanToBean(src, dest, prefix(BeanNames.search_()));
        assertThat(dest.eee$fff, is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_converter() throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        Bean2 bean2 = new Bean2();
        BeanUtil.copyBeanToBean(bean, bean2, converter(new NumberConverter(
            "#,##0")));
        assertThat(bean2.aaa, is(new Integer(1000)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyBeanToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is(nullValue()));
        assertThat(dest.get("ccc"), is((Object) "ccc"));
        assertThat(dest.get("ddd"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_includes() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyBeanToMap(src, dest, include(BeanNames.aaa()));
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_excludes() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyBeanToMap(src, dest, exclude(BeanNames.ccc()));
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_null() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        Map<String, Object> dest = newHashMap();
        dest.put("ccc", "ccc");
        BeanUtil.copyBeanToMap(src, dest);
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_excludesNull() throws Exception {
        SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        Map<String, Object> dest = newHashMap();
        dest.put("ccc", "ccc");
        BeanUtil.copyBeanToMap(src, dest, excludeNull());
        assertThat(dest.get("ccc"), is((Object) "ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_whitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        Map<String, Object> dest = newHashMap();
        dest.put("ccc", "ccc");
        BeanUtil.copyBeanToMap(src, dest);
        assertThat(dest.get("ccc"), is((Object) " "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_excludesWhitespace() throws Exception {
        SrcBean src = new SrcBean();
        src.ccc = " ";
        Map<String, Object> dest = newHashMap();
        dest.put("ccc", "ccc");
        BeanUtil.copyBeanToMap(src, dest, excludeWhitespace());
        assertThat(dest.get("ccc"), is((Object) "ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_prefix() throws Exception {
        SrcBean src = new SrcBean();
        src.search_eee$fff = "hoge";
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyBeanToMap(src, dest, prefix(BeanNames.search_()));
        assertThat(dest.get("eee.fff"), is((Object) "hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_converter() throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        Map<String, Object> map = newHashMap();
        BeanUtil.copyBeanToMap(
            bean,
            map,
            converter(new NumberConverter("#,##0")));
        assertEquals("1,000", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_converter2() throws Exception {
        Bean bean = new Bean();
        bean.aaa = "1,000";
        Map<String, Object> map = newHashMap();
        BeanUtil.copyBeanToMap(
            bean,
            map,
            converter(new NumberConverter("#,##0"), BeanNames.aaa()));
        assertThat(map.get("aaa"), is((Object) 1000L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_converter3() throws Exception {
        Bean2 bean2 = new Bean2();
        bean2.aaa = new Integer(1000);
        Map<String, Object> map = newHashMap();
        BeanUtil.copyBeanToMap(bean2, map, converter(new NumberConverter(
            "#,##0")));
        assertThat(map.get("aaa"), is((Object) "1,000"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_converter() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("aaa", new Integer(1000));
        Bean bean = new Bean();
        BeanUtil.copyMapToBean(
            map,
            bean,
            converter(new NumberConverter("#,##0")));
        assertThat(bean.aaa, is((Object) "1,000"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_converter2() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("aaa", "1,000");
        Bean2 bean2 = new Bean2();
        BeanUtil.copyMapToBean(map, bean2, converter(new NumberConverter(
            "#,##0")));
        assertThat(bean2.aaa, is(new Integer(1000)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean() throws Exception {
        Map<String, String> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        BeanUtil.copyMapToBean(src, dest);
        assertThat(dest.bbb, is("bbb"));
        assertThat(dest.ccc, is("ccc"));
        assertThat(dest.ddd, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_includes() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        BeanUtil.copyMapToBean(src, dest, include(BeanNames.bbb()));
        assertThat(dest.bbb, is("bbb"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_excludes() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        DestBean dest = new DestBean();
        BeanUtil.copyMapToBean(src, dest, exclude(BeanNames.ccc()));
        assertThat(dest.bbb, is("bbb"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_null() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyMapToBean(src, dest);
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_excludesNull() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyMapToBean(src, dest, excludeNull());
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_whitespace() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("ccc", " ");
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyMapToBean(src, dest);
        assertThat(dest.ccc, is(" "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_excludesWhitespace() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("ccc", " ");
        DestBean dest = new DestBean();
        dest.ccc = "ccc";
        BeanUtil.copyMapToBean(src, dest, excludeWhitespace());
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_prefix() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("search_eee.fff", "hoge");
        DestBean dest = new DestBean();
        BeanUtil.copyMapToBean(src, dest, prefix(BeanNames.search_()));
        assertThat(dest.eee$fff, is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", new Date(0));
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyMapToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is((Object) new Date(0)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_includes() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyMapToMap(src, dest, include(BeanNames.aaa()));
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_excludes() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyMapToMap(src, dest, exclude(BeanNames.bbb()));
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_null() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        Map<String, Object> dest = newHashMap();
        dest.put("bbb", "bbb");
        BeanUtil.copyMapToMap(src, dest);
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_excludesNull() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        Map<String, Object> dest = newHashMap();
        dest.put("bbb", "bbb");
        BeanUtil.copyMapToMap(src, dest, excludeNull());
        assertThat(dest.get("bbb"), is((Object) "bbb"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_whitespace() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("bbb", " ");
        Map<String, Object> dest = newHashMap();
        dest.put("bbb", "bbb");
        BeanUtil.copyMapToMap(src, dest);
        assertThat(dest.get("bbb"), is((Object) " "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_excludesWhitespace() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("bbb", " ");
        Map<String, Object> dest = newHashMap();
        dest.put("bbb", "bbb");
        BeanUtil.copyMapToMap(src, dest, excludeWhitespace());
        assertThat(dest.get("bbb"), is((Object) "bbb"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_prefix() throws Exception {
        Map<String, Object> src = newHashMap();
        src.put("search_eee.fff", "hoge");
        Map<String, Object> dest = newHashMap();
        BeanUtil.copyMapToMap(src, dest, prefix(BeanNames.search_()));
        assertThat(dest.get("eee.fff"), is((Object) "hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_converter() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("aaa", new Integer(1000));
        Map<String, Object> map2 = newHashMap();
        BeanUtil.copyMapToMap(
            map,
            map2,
            converter(new NumberConverter("#,##0")));
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_converter2() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("aaa", "1,000");
        Map<String, Object> map2 = newHashMap();
        BeanUtil.copyMapToMap(
            map,
            map2,
            converter(new NumberConverter("#,##0")));
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_converter3() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("aaa", "1,000");
        Map<String, Object> map2 = newHashMap();
        BeanUtil.copyMapToMap(
            map,
            map2,
            converter(new NumberConverter("#,##0"), "aaa"));
        assertThat(map2.get("aaa"), is((Object) 1000L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_BeanToNewMap() throws Exception {
        HogeDto2 hoge = new HogeDto2();
        hoge.aaa = "1";
        hoge.search_bbb = "2";
        hoge.search_ccc$ddd = "3";
        Map<String, Object> map = BeanUtil.copyBeanToNewMap(hoge);
        assertThat(map.size(), is(4));
        assertThat(map.get("aaa"), is((Object) "1"));
        assertThat(map.get("search_bbb"), is((Object) "2"));
        assertThat(map.get("search_ccc.ddd"), is((Object) "3"));
        assertThat(map.get("employee.name"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_BeanToNewMap_Prefix() throws Exception {
        HogeDto2 hoge = new HogeDto2();
        hoge.aaa = "1";
        hoge.search_bbb = "2";
        hoge.search_ccc$ddd = "3";
        hoge.search_employee$name = "4";
        Map<String, Object> map =
            BeanUtil
                .copyBeanToNewMap(hoge, new CopyOptions().prefix("search_"));
        assertThat(map.size(), is(3));
        assertThat(map.get("bbb"), is((Object) "2"));
        assertThat(map.get("ccc.ddd"), is((Object) "3"));
        assertThat(map.get("employee.name"), is((Object) "4"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = new MyBean();
        BeanUtil.copyBeanToBean(src, dest);
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
        BeanUtil.copyBeanToBean(src, dest, converter(new DateConverter(
            "yyyy/MM/dd")));
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
        BeanUtil.copyBeanToMap(src, dest);
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
        BeanUtil.copyMapToBean(src, dest);
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
        BeanUtil.copyMapToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToNewBean() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        MyBean dest = BeanUtil.copyBeanToNewBean(src, MyBean.class);
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToNewMap() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        Map<String, Object> dest = BeanUtil.copyBeanToNewMap(src);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_beanToNewMap_specificClass() throws Exception {
        MyBean src = new MyBean();
        src.aaa = "aaa";
        @SuppressWarnings("unchecked")
        Map<String, Object> dest =
            BeanUtil.copyBeanToNewMap(src, HashMap.class);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        BeanMap dest2 = BeanUtil.copyBeanToNewMap(src, BeanMap.class);
        assertThat(dest2.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_mapToNewBean() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        MyBean dest = BeanUtil.copyMapToNewBean(src, MyBean.class);
        assertThat(dest.aaa, is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_mapToNewMap() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        Map<String, Object> dest = BeanUtil.copyMapToNewMap(src);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopy_mapToNewMap_specificClass() throws Exception {
        BeanMap src = new BeanMap();
        src.put("aaa", "aaa");
        @SuppressWarnings("unchecked")
        Map<String, Object> dest = BeanUtil.copyMapToNewMap(src, HashMap.class);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        BeanMap dest2 = BeanUtil.copyMapToNewMap(src, BeanMap.class);
        assertThat(dest2.get("aaa"), is((Object) "aaa"));
    }

    /**
     * 
     */
    @Test
    public void testCopyMapToBean_SrcNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[src]がnullです。"));
        BeanUtil.copyMapToBean(null, new Object());
    }

    /**
     * 
     */
    @Test
    public void testCopyBeanToMap_DestNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[dest]がnullです。"));
        BeanUtil.copyBeanToMap(new Object(), null);
    }

    /**
     * 
     */
    @Test
    public void testCopyBeanToNewBean_DestClassNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[destClass]がnullです。"));
        BeanUtil.copyBeanToNewBean(new Object(), (Class<?>) null);
    }

    /**
     * 
     */
    @Test
    public void testCopyMapToNewBean_SrcNull() {
        exception.expect(NullArgumentException.class);
        exception.expectMessage(is("[EUTL0008]引数[src]がnullです。"));
        BeanUtil.copyMapToNewBean(null, Bean.class);
    }

    /**
     * 
     */
    public static class HogeDto {

        private String a;

        private boolean b;

        private int c;

        /**
         * @return String
         */
        public String getA() {
            return a;
        }

        /**
         * @param a
         */
        public void setA(String a) {
            this.a = a;
        }

        /**
         * @return boolean
         */
        public boolean isB() {
            return b;
        }

        /**
         * @param b
         */
        public void setB(boolean b) {
            this.b = b;
        }

        /**
         * @return int
         */
        public int getC() {
            return c;
        }

        /**
         * @param c
         */
        public void setC(int c) {
            this.c = c;
        }

    }

    /**
     * 
     */
    public static class HogeDto2 {

        /**
         * 
         */
        public String aaa;

        /**
         * 
         */
        public String search_bbb;

        /**
         * 
         */
        public String search_ccc$ddd;

        /**
         * 
         */
        public String search_employee$name;

    }

    /**
     * 
     */
    public static class MyClass {

        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * @return Returns the aaa.
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            The aaa to set.
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @return Returns the bbb.
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param bbb
         *            The bbb to set.
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return Returns the ccc.
         */
        public String getCcc() {
            return ccc;
        }

        /**
         * @param ccc
         *            The ccc to set.
         */
        public void setCcc(String ccc) {
            this.ccc = ccc;
        }

    }

    /**
     * 
     */
    public static class MyClass2 {

        private String aaa;

        private String bbb;

        private String ddd;

        /**
         * @return Returns the aaa.
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         *            The aaa to set.
         */
        public void setAaa(String aaa) {
            this.aaa = aaa;
        }

        /**
         * @return Returns the bbb.
         */
        public String getBbb() {
            return bbb;
        }

        /**
         * @param bbb
         *            The bbb to set.
         */
        public void setBbb(String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return Returns the ddd.
         */
        public String getDdd() {
            return ddd;
        }

        /**
         * @param ddd
         *            The ddd to set.
         */
        public void setDdd(String ddd) {
            this.ddd = ddd;
        }

    }

    /**
     * 
     */
    @SuppressWarnings("unused")
    public static class SrcBean {

        private String aaa;

        private String bbb;

        private String ccc;

        /**
         * 
         */
        public String eee;

        /**
         * 
         */
        public String search_eee$fff;

        /**
         * @return String
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param aaa
         */
        public void setAaa(final String aaa) {
            this.aaa = aaa;
        }

        /**
         * @param bbb
         */
        public void setBbb(final String bbb) {
            this.bbb = bbb;
        }

        /**
         * @return String
         */
        public String getCcc() {
            return ccc;
        }

    }

    /**
     * 
     */
    public static class DestBean {

        private String bbb;

        private String ccc;

        private String ddd;

        /**
         * 
         */
        public int eee;

        /**
         * 
         */
        public String eee$fff;

        /**
         * @param bbb
         */
        public void setBbb(final String bbb) {
            this.bbb = bbb;
        }

        /**
         * @param ccc
         */
        public void setCcc(final String ccc) {
            this.ccc = ccc;
        }

        /**
         * @return String
         */
        public String getDdd() {
            return ddd;
        }

        /**
         * @param ddd
         */
        public void setDdd(final String ddd) {
            this.ddd = ddd;
        }

    }

    /**
     * 
     */
    public static class Bean {

        /**
         * 
         */
        public String aaa;
    }

    /**
     * 
     */
    public static class Bean2 {

        /**
         * 
         */
        public Integer aaa;

    }

    /**
     * 
     */
    public static class Bean3 {

        /**
         * 
         */
        public Date aaa;

    }

    /**
     * @author kato
     */
    public static class BeanNames {

        /**
         * CharSequenceを作成します。
         * 
         * @param name
         * @return CharSequence
         */
        protected static CharSequence createCharSequence(final String name) {
            return new CharSequence() {

                @Override
                public String toString() {
                    return name;
                }

                @Override
                public char charAt(final int index) {
                    return name.charAt(index);
                }

                @Override
                public int length() {
                    return name.length();
                }

                @Override
                public CharSequence subSequence(final int start, final int end) {
                    return name.subSequence(start, end);
                }

            };
        }

        /**
         * @return CharSequence
         */
        public static CharSequence aaa() {
            return createCharSequence("aaa");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence bbb() {
            return createCharSequence("bbb");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence ccc() {
            return createCharSequence("ccc");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence hoge() {
            return createCharSequence("hoge");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence hoge2() {
            return createCharSequence("hoge2");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence hoge3() {
            return createCharSequence("hoge3");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence search_aaa() {
            return createCharSequence("search_aaa");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence abc_exclude() {
            return createCharSequence("abc_exclude");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence search_() {
            return createCharSequence("search_");
        }

        /**
         * @return CharSequence
         */
        public static CharSequence abc_() {
            return createCharSequence("abc_");
        }

    }

}
