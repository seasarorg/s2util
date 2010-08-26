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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.seasar.util.beans.converter.DateConverter;
import org.seasar.util.beans.converter.NumberConverter;
import org.seasar.util.exception.ConverterRuntimeException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 */
public class AbstCopyTest {

    /**
     * @throws Exception
     */
    @Test
    public void testIncludes() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.includes(BeanNames.hoge()), is(sameInstance(copy)));
        assertThat(copy.includePropertyNames.length, is(1));
        assertThat(copy.includePropertyNames[0], is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testExcludes() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.excludes(BeanNames.hoge()), is(sameInstance(copy)));
        assertThat(copy.excludePropertyNames.length, is(1));
        assertThat(copy.excludePropertyNames[0], is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testPrefix() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.prefix(BeanNames.search_()), is(sameInstance(copy)));
        assertThat(copy.prefix, is("search_"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testBeanDelimiter() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.beanDelimiter('#'), is(sameInstance(copy)));
        assertThat(copy.beanDelimiter, is('#'));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testMapDelimiter() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.mapDelimiter('#'), is(sameInstance(copy)));
        assertThat(copy.mapDelimiter, is('#'));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.isTargetProperty("hoge"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty_includes() throws Exception {
        final MyCopy copy = new MyCopy().includes(BeanNames.hoge());
        assertThat(copy.isTargetProperty("hoge"), is(true));
        assertThat(copy.isTargetProperty("hoge2"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty_includes_prefix() throws Exception {
        final MyCopy copy =
            new MyCopy()
                .includes(BeanNames.search_aaa(), BeanNames.bbb())
                .prefix(BeanNames.search_());
        assertThat(copy.isTargetProperty("search_aaa"), is(true));
        assertThat(copy.isTargetProperty("bbb"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty_excludes() throws Exception {
        final MyCopy copy = new MyCopy().excludes(BeanNames.hoge());
        assertThat(copy.isTargetProperty("hoge"), is(not(true)));
        assertThat(copy.isTargetProperty("hoge2"), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty_excludes_prefix() throws Exception {
        final MyCopy copy =
            new MyCopy().prefix(BeanNames.abc_()).excludes(
                BeanNames.abc_exclude());
        assertThat(copy.isTargetProperty("abc_value"), is(true));
        assertThat(copy.isTargetProperty("abc_exclude"), is(not(true)));
        assertThat(copy.isTargetProperty("ab"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty_prefix() throws Exception {
        final MyCopy copy = new MyCopy().prefix(BeanNames.search_());
        assertThat(copy.isTargetProperty("search_aaa"), is(true));
        assertThat(copy.isTargetProperty("bbb"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsTargetProperty_includes_excludes() throws Exception {
        final MyCopy copy = new MyCopy();
        copy.includes(BeanNames.hoge(), BeanNames.hoge2());
        copy.excludes(BeanNames.hoge2(), BeanNames.hoge3());
        assertThat(copy.isTargetProperty("hoge"), is(true));
        assertThat(copy.isTargetProperty("hoge2"), is(not(true)));
        assertThat(copy.isTargetProperty("hoge3"), is(not(true)));
        assertThat(copy.isTargetProperty("hoge4"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testTrimPrefix() throws Exception {
        final MyCopy copy = new MyCopy();
        assertThat(copy.trimPrefix("aaa"), is("aaa"));
        copy.prefix(BeanNames.search_());
        assertThat(copy.trimPrefix("search_aaa"), is("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        src.eee = "1";
        final DestBean dest = new DestBean();
        new MyCopy().copyBeanToBean(src, dest);
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
        final MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        final MyBean dest = new MyBean();
        new MyCopy().includes(BeanNames.aaa()).copyBeanToBean(src, dest);
        assertThat(dest.aaa, is("aaa"));
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_excludes() throws Exception {
        final MyBean src = new MyBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        final MyBean dest = new MyBean();
        new MyCopy().excludes(BeanNames.bbb()).copyBeanToBean(src, dest);
        assertThat(dest.aaa, is("aaa"));
        assertThat(dest.bbb, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_null() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyBeanToBean(src, dest);
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_excludesNull() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesNull().copyBeanToBean(src, dest);
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_whitespace() throws Exception {
        final SrcBean src = new SrcBean();
        src.ccc = " ";
        final DestBean dest = new DestBean();
        new MyCopy().copyBeanToBean(src, dest);
        assertThat(dest.ccc, is(" "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_excludesWhitespace() throws Exception {
        final SrcBean src = new SrcBean();
        src.ccc = " ";
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesWhitespace().copyBeanToBean(src, dest);
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_prefix() throws Exception {
        final SrcBean src = new SrcBean();
        src.search_eee$fff = "hoge";
        final DestBean dest = new DestBean();
        new MyCopy().prefix(BeanNames.search_()).copyBeanToBean(src, dest);
        assertThat(dest.eee$fff, is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToBean_converter() throws Exception {
        final Bean bean = new Bean();
        bean.aaa = "1,000";
        final Bean2 bean2 = new Bean2();
        new MyCopy().converter(new NumberConverter("#,##0")).copyBeanToBean(
            bean,
            bean2);
        assertThat(bean2.aaa, is(new Integer(1000)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().copyBeanToMap(src, dest);
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
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().includes(BeanNames.aaa()).copyBeanToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_excludes() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = "ccc";
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().excludes(BeanNames.ccc()).copyBeanToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_null() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().copyBeanToMap(src, dest);
        assertThat(dest.get("ccc"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_excludesNull() throws Exception {
        final SrcBean src = new SrcBean();
        src.aaa = "aaa";
        src.bbb = "bbb";
        src.ccc = null;
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().excludesNull().copyBeanToMap(src, dest);
        assertThat(dest.get("ccc"), is((Object) "ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_whitespace() throws Exception {
        final SrcBean src = new SrcBean();
        src.ccc = " ";
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().copyBeanToMap(src, dest);
        assertThat(dest.get("ccc"), is((Object) " "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_excludesWhitespace() throws Exception {
        final SrcBean src = new SrcBean();
        src.ccc = " ";
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("ccc", "ccc");
        new MyCopy().excludesWhitespace().copyBeanToMap(src, dest);
        assertThat(dest.get("ccc"), is((Object) "ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_prefix() throws Exception {
        final SrcBean src = new SrcBean();
        src.search_eee$fff = "hoge";
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().prefix(BeanNames.search_()).copyBeanToMap(src, dest);
        assertThat(dest.get("eee.fff"), is((Object) "hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_converter() throws Exception {
        final Bean bean = new Bean();
        bean.aaa = "1,000";
        final Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyBeanToMap(
            bean,
            map);
        assertEquals("1,000", map.get("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_converter2() throws Exception {
        final Bean bean = new Bean();
        bean.aaa = "1,000";
        final Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy()
            .converter(new NumberConverter("#,##0"), BeanNames.aaa())
            .copyBeanToMap(bean, map);
        assertThat(map.get("aaa"), is((Object) 1000L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyBeanToMap_converter3() throws Exception {
        final Bean2 bean2 = new Bean2();
        bean2.aaa = new Integer(1000);
        final Map<String, Object> map = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyBeanToMap(
            bean2,
            map);
        assertThat(map.get("aaa"), is((Object) "1,000"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_converter() throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", new Integer(1000));
        final Bean bean = new Bean();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToBean(
            map,
            bean);
        assertThat(bean.aaa, is((Object) "1,000"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_converter2() throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        final Bean2 bean2 = new Bean2();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToBean(
            map,
            bean2);
        assertThat(bean2.aaa, is(new Integer(1000)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        final DestBean dest = new DestBean();
        new MyCopy().copyMapToBean(src, dest);
        assertThat(dest.bbb, is("bbb"));
        assertThat(dest.ccc, is("ccc"));
        assertThat(dest.ddd, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_includes() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        final DestBean dest = new DestBean();
        new MyCopy().includes(BeanNames.bbb()).copyMapToBean(src, dest);
        assertThat(dest.bbb, is("bbb"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_excludes() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", "ccc");
        final DestBean dest = new DestBean();
        new MyCopy().excludes(BeanNames.ccc()).copyMapToBean(src, dest);
        assertThat(dest.bbb, is("bbb"));
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_null() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyMapToBean(src, dest);
        assertThat(dest.ccc, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_excludesNull() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        src.put("ccc", null);
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesNull().copyMapToBean(src, dest);
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_whitespace() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", " ");
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().copyMapToBean(src, dest);
        assertThat(dest.ccc, is(" "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_excludesWhitespace() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("ccc", " ");
        final DestBean dest = new DestBean();
        dest.ccc = "ccc";
        new MyCopy().excludesWhitespace().copyMapToBean(src, dest);
        assertThat(dest.ccc, is("ccc"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToBean_prefix() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("search_eee.fff", "hoge");
        final DestBean dest = new DestBean();
        new MyCopy().prefix(BeanNames.search_()).copyMapToBean(src, dest);
        assertThat(dest.eee$fff, is("hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", new Date(0));
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().copyMapToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is((Object) new Date(0)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_includes() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().includes(BeanNames.aaa()).copyMapToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_excludes() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", "bbb");
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().excludes(BeanNames.bbb()).copyMapToMap(src, dest);
        assertThat(dest.get("aaa"), is((Object) "aaa"));
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_null() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyMapToMap(src, dest);
        assertThat(dest.get("bbb"), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_excludesNull() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("aaa", "aaa");
        src.put("bbb", null);
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().excludesNull().copyMapToMap(src, dest);
        assertThat(dest.get("bbb"), is((Object) "bbb"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_whitespace() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", " ");
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().copyMapToMap(src, dest);
        assertThat(dest.get("bbb"), is((Object) " "));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_excludesWhitespace() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("bbb", " ");
        final Map<String, Object> dest = new HashMap<String, Object>();
        dest.put("bbb", "bbb");
        new MyCopy().excludesWhitespace().copyMapToMap(src, dest);
        assertThat(dest.get("bbb"), is((Object) "bbb"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_prefix() throws Exception {
        final Map<String, Object> src = new HashMap<String, Object>();
        src.put("search_eee.fff", "hoge");
        final Map<String, Object> dest = new HashMap<String, Object>();
        new MyCopy().prefix(BeanNames.search_()).copyMapToMap(src, dest);
        assertThat(dest.get("eee.fff"), is((Object) "hoge"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_converter() throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", new Integer(1000));
        final Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToMap(
            map,
            map2);
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_converter2() throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        final Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy().converter(new NumberConverter("#,##0")).copyMapToMap(
            map,
            map2);
        assertEquals("1,000", map2.get("aaa"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyMapToMap_converter3() throws Exception {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put("aaa", "1,000");
        final Map<String, Object> map2 = new HashMap<String, Object>();
        new MyCopy()
            .converter(new NumberConverter("#,##0"), "aaa")
            .copyMapToMap(map, map2);
        assertThat(map2.get("aaa"), is((Object) 1000L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testConvertValue_zeroConverter() throws Exception {
        assertThat(
            new MyCopy().convertValue(new Integer(1), "aaa", null),
            is((Object) 1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testConvertValue_propertyConverter_asString() throws Exception {
        assertThat(new MyCopy()
            .converter(new NumberConverter("##0"), "aaa")
            .convertValue(new Integer(1), "aaa", null), is((Object) "1"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testConvertValue_propertyConverter_asObject() throws Exception {
        assertThat(
            new MyCopy()
                .converter(new NumberConverter("##0"), BeanNames.aaa())
                .convertValue("1", "aaa", null),
            is((Object) 1L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testConvertValue_typeConverter_asString() throws Exception {
        assertThat(new MyCopy()
            .converter(new NumberConverter("##0"))
            .convertValue(new Integer(1), "aaa", null), is((Object) "1"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testConvertValue_typeConverter_asObject() throws Exception {
        assertThat(new MyCopy()
            .converter(new NumberConverter("##0"))
            .convertValue("1", "aaa", Integer.class), is((Object) 1L));
        assertThat(
            new MyCopy().converter(new DateConverter("yyyyMMdd")).convertValue(
                new Timestamp(0),
                "aaa",
                String.class),
            is((Object) "19700101"));
    }

    /**
     * @throws Exception
     */
    @Test(expected = ConverterRuntimeException.class)
    public void testConvertValue_throwable() throws Exception {
        new MyCopy().converter(new NumberConverter("##0")).convertValue(
            "a",
            "aaa",
            Integer.class);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testConvertValue_dateToDate() throws Exception {
        final Date date = new Date(1);
        assertThat(
            new MyCopy().convertValue(date, "aaa", Date.class),
            is((Object) date));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDateConverter() throws Exception {
        assertThat(
            new MyCopy().dateConverter("yyyyMMdd").convertValue(
                new java.util.Date(0),
                "aaa",
                String.class),
            is((Object) "19700101"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSqlDateConverter() throws Exception {
        assertThat(
            new MyCopy().sqlDateConverter("yyyyMMdd").convertValue(
                new java.sql.Date(0),
                "aaa",
                String.class),
            is((Object) "19700101"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testTimeConverter() throws Exception {
        assertThat(
            new MyCopy().timeConverter("ss").convertValue(
                new java.sql.Time(0),
                "aaa",
                String.class),
            is((Object) "00"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testTimestampConverter() throws Exception {
        assertThat(
            new MyCopy().timestampConverter("yyyyMMdd ss").convertValue(
                new java.sql.Timestamp(0),
                "aaa",
                String.class),
            is((Object) "19700101 00"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFindDefaultConverter() throws Exception {
        assertThat(
            new MyCopy().findDefaultConverter(Time.class),
            is(AbstractCopy.DEFAULT_TIME_CONVERTER));
        assertThat(
            new MyCopy().findDefaultConverter(Timestamp.class),
            is(AbstractCopy.DEFAULT_TIMESTAMP_CONVERTER));
        assertThat(
            new MyCopy().findDefaultConverter(java.util.Date.class),
            is(AbstractCopy.DEFAULT_TIMESTAMP_CONVERTER));
        assertThat(
            new MyCopy().findDefaultConverter(java.sql.Date.class),
            is(AbstractCopy.DEFAULT_DATE_CONVERTER));
        assertThat(
            new MyCopy().findDefaultConverter(Integer.class),
            is(nullValue()));
    }

    /**
     * 
     */
    private static class MyCopy extends AbstractCopy<MyCopy> {
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
