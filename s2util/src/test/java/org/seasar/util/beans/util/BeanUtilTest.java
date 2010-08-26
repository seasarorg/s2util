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

import java.util.Map;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * @author higa
 * 
 */
public class BeanUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void testCopyProperties() throws Exception {
        MyClass src = new MyClass();
        src.setAaa("111");
        src.setCcc("333");

        MyClass2 dest = new MyClass2();
        dest.setAaa("aaa");
        dest.setBbb("bbb");
        dest.setDdd("ddd");

        BeanUtil.copyProperties(src, dest);
        assertThat(dest.getAaa(), is("111"));
        assertThat(dest.getBbb(), is(nullValue()));
        assertThat(dest.getDdd(), is("ddd"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyPropertiesWithoutNull() throws Exception {
        MyClass src = new MyClass();
        src.setAaa("111");
        src.setCcc("333");

        MyClass2 dest = new MyClass2();
        dest.setAaa("aaa");
        dest.setBbb("bbb");
        dest.setDdd("ddd");

        BeanUtil.copyProperties(src, dest, false);
        assertThat(dest.getAaa(), is("111"));
        assertThat(dest.getBbb(), is("bbb"));
        assertThat(dest.getDdd(), is("ddd"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyToMap() throws Exception {
        HogeDto hoge = new HogeDto();
        hoge.setA("A");
        hoge.setB(true);
        hoge.setC(3);
        Map<String, Object> map = newHashMap();
        BeanUtil.copyProperties(hoge, map);
        assertThat(map, is(notNullValue()));
        assertThat(map.get("a"), is((Object) "A"));
        assertThat(map.get("b"), is((Object) true));
        assertThat(map.get("c"), is((Object) 3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCopyToBean() throws Exception {
        Map<String, Object> map = newHashMap();
        map.put("a", "A");
        map.put("b", new Boolean(true));
        map.put("c", new Integer(3));
        map.put("d", new Double(1.4));
        HogeDto hoge = new HogeDto();
        BeanUtil.copyProperties(map, hoge);
        assertThat(hoge.getA(), is("A"));
        assertThat(hoge.isB(), is(true));
        assertThat(hoge.getC(), is(3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateProperties() throws Exception {
        HogeDto2 hoge = new HogeDto2();
        hoge.aaa = "1";
        hoge.search_bbb = "2";
        hoge.search_ccc$ddd = "3";
        hoge.search_employee$name = "4";
        Map<String, Object> map = BeanUtil.createProperties(hoge, "search_");
        assertThat(map.size(), is(3));
        assertThat(map.get("bbb"), is((Object) "2"));
        assertThat(map.get("ccc.ddd"), is((Object) "3"));
        assertThat(map.get("employee.name"), is((Object) "4"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testCreateProperties_nonPrefix() throws Exception {
        HogeDto2 hoge = new HogeDto2();
        hoge.aaa = "1";
        hoge.search_bbb = "2";
        hoge.search_ccc$ddd = "3";
        Map<String, Object> map = BeanUtil.createProperties(hoge);
        assertThat(map.size(), is(4));
        assertThat(map.get("aaa"), is((Object) "1"));
        assertThat(map.get("search_bbb"), is((Object) "2"));
        assertThat(map.get("search_ccc.ddd"), is((Object) "3"));
        assertThat(map.get("employee.name"), is(nullValue()));
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

}
