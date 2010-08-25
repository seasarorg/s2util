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
package org.seasar.util.beans.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.seasar.util.beans.BeanDesc;
import org.seasar.util.beans.ParameterizedClassDesc;
import org.seasar.util.beans.PropertyDesc;
import org.seasar.util.beans.factory.BeanDescFactory;
import org.seasar.util.exception.MethodNotFoundRuntimeException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.lang.ClassUtilTest.*;

/**
 * @author higa
 * @author manhole
 */
public class BeanDescImplTest {

    /**
     * @throws Exception
     */
    @Test
    public void testPropertyDesc() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertThat(beanDesc.getPropertyDescSize(), is(5));
        PropertyDesc propDesc = beanDesc.getPropertyDesc("aaa");
        assertThat(propDesc.getPropertyName(), is("aaa"));
        assertThat(propDesc.getPropertyType(), isSameClass(String.class));
        assertThat(propDesc.getReadMethod(), is(notNullValue()));
        assertThat(propDesc.getWriteMethod(), is(nullValue()));
        assertThat(propDesc.getField(), is(notNullValue()));

        propDesc = beanDesc.getPropertyDesc("CCC");
        assertThat(propDesc.getPropertyName(), is("CCC"));
        assertThat(propDesc.getPropertyType(), isSameClass(boolean.class));
        assertThat(propDesc.getReadMethod(), is(notNullValue()));
        assertThat(propDesc.getWriteMethod(), is(nullValue()));

        propDesc = beanDesc.getPropertyDesc("eee");
        assertThat(propDesc.getPropertyName(), is("eee"));
        assertThat(propDesc.getPropertyType(), isSameClass(String.class));
        assertThat(propDesc.getReadMethod(), is(notNullValue()));
        assertThat(propDesc.getWriteMethod(), is(notNullValue()));

        propDesc = beanDesc.getPropertyDesc("fff");
        assertThat(propDesc.getPropertyName(), is("fff"));
        assertThat(propDesc.getPropertyType(), isSameClass(Boolean.class));

        assertThat(beanDesc.hasPropertyDesc("hhh"), is(not(true)));
        assertThat(beanDesc.hasPropertyDesc("iii"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInvoke() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertThat((Integer) beanDesc.invoke(new MyBean(), "add", 1, 2), is(3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInvoke2() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertThat((Integer) beanDesc.invoke(new MyBean(), "add2", 1, 2), is(3));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInvoke3() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Math.class);
        assertThat((Integer) beanDesc.invokeStatic("max", 1, 3), is(3));
        assertThat((Long) beanDesc.invokeStatic("max", 1L, 2L), is(2L));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInvoke4() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Math.class);
        assertThat(
            (Double) beanDesc.invokeStatic("ceil", new BigDecimal(2.1)),
            is(3d));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInvoke5() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertThat((Integer) beanDesc.invoke(new MyBean(), "echo", 3d), is(3));
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testInvokeForException() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        beanDesc.invoke(new MyBean(), "throwException");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testNewInstance() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Integer.class);
        assertThat((Integer) beanDesc.newInstance(10), is(10));
        assertThat((Integer) beanDesc.newInstance("10"), is(10));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testNewInstance2() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(Integer.class);
        assertThat((Integer) beanDesc.newInstance(new BigDecimal(10)), is(10));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetFields() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertThat(beanDesc.hasField("HOGE"), is(true));
        Field field = beanDesc.getField("HOGE");
        assertThat((String) field.get(null), is("hoge2"));
        assertThat(beanDesc.hasField("aaa"), is(true));
        assertThat(beanDesc.hasField("aaA"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testHasMethod() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        assertThat(beanDesc.hasMethod("getAaa"), is(true));
        assertThat(beanDesc.hasMethod("getaaa"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test(expected = MethodNotFoundRuntimeException.class)
    public void testGetMethod() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Method method = beanDesc.getMethod("getAaa");
        assertThat(method, is(notNullValue()));
        assertThat(method.getName(), is("getAaa"));
        beanDesc.getMethod("getaaa");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMethodNoException() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Method method = beanDesc.getMethodNoException("getAaa");
        assertThat(method, is(notNullValue()));
        assertThat(method.getName(), is("getAaa"));
        method = beanDesc.getMethodNoException("getaaa");
        assertThat(method, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMethodNames() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(getClass());
        String[] names = beanDesc.getMethodNames();
        for (int i = 0; i < names.length; ++i) {
            System.out.println(names[i]);
        }
        assertThat(names.length > 0, is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testInvalidProperty() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean2.class);
        assertThat(beanDesc.hasPropertyDesc("aaa"), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testAddFields() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        Field eee = beanDesc.getField("eee");
        assertThat(eee.isAccessible(), is(true));
        PropertyDesc pd = beanDesc.getPropertyDesc("ggg");
        assertThat(pd, is(notNullValue()));
        assertThat(pd.getPropertyName(), is("ggg"));
        assertThat(pd.getPropertyType(), isSameClass(String.class));
    }

    /**
     * @throws Exception
     */
    public void testFieldType() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc pd = bd.getPropertyDesc("foo");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getElementClassOfCollection(), isSameClass(String.class));

        ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(List.class));
        assertThat(pcd.getArguments().length, is(1));
        assertThat(
            pcd.getArguments()[0].getRawClass(),
            isSameClass(String.class));

        pd = bd.getPropertyDesc("hoge");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getElementClassOfCollection(), isSameClass(Object.class));

        pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(List.class));
        assertThat(pcd.getArguments().length, is(1));
        assertThat(
            pcd.getArguments()[0].getRawClass(),
            isSameClass(Object.class));

        bd = BeanDescFactory.getBeanDesc(Bar.class);
        pd = bd.getPropertyDesc("list");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getElementClassOfCollection(), isSameClass(String.class));

        pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(List.class));
        assertThat(pcd.getArguments().length, is(1));
        assertThat(
            pcd.getArguments()[0].getRawClass(),
            isSameClass(String.class));
    }

    /**
     * @throws Exception
     */
    public void testGetter() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc pd = bd.getPropertyDesc("bar");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getElementClassOfCollection(), isSameClass(Integer.class));

        ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(Set.class));
        assertThat(pcd.getArguments().length, is(1));
        assertThat(
            pcd.getArguments()[0].getRawClass(),
            isSameClass(Integer.class));

        pd = bd.getPropertyDesc("fuga");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getElementClassOfCollection(), isSameClass(Enum.class));

        pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(Set.class));
        assertThat(pcd.getArguments().length, is(1));
        assertThat(pcd.getArguments()[0].getRawClass(), isSameClass(Enum.class));
    }

    /**
     * @throws Exception
     */
    public void testSetter() throws Exception {
        BeanDesc bd = BeanDescFactory.getBeanDesc(Hoge.class);
        PropertyDesc pd = bd.getPropertyDesc("baz");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getKeyClassOfMap(), isSameClass(String.class));
        assertThat(pd.getValueClassOfMap(), isSameClass(Date.class));

        ParameterizedClassDesc pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(Map.class));
        assertThat(pcd.getArguments().length, is(2));
        assertThat(
            pcd.getArguments()[0].getRawClass(),
            isSameClass(String.class));
        assertThat(pcd.getArguments()[1].getRawClass(), isSameClass(Date.class));

        pd = bd.getPropertyDesc("hege");
        assertThat(pd.isParameterized(), is(true));
        assertThat(pd.getKeyClassOfMap(), isSameClass(String.class));
        assertThat(pd.getValueClassOfMap(), isSameClass(Number.class));

        pcd = pd.getParameterizedClassDesc();
        assertThat(pcd.getRawClass(), isSameClass(Map.class));
        assertThat(pcd.getArguments().length, is(2));
        assertThat(
            pcd.getArguments()[0].getRawClass(),
            isSameClass(String.class));
        assertThat(
            pcd.getArguments()[1].getRawClass(),
            isSameClass(Number.class));
    }

    /**
     * 
     */
    public static interface MyInterface {
        /**
         * 
         */
        @SuppressWarnings("hiding")
        String HOGE = "hoge";
    }

    /**
     * 
     */
    public static interface MyInterface2 extends MyInterface {
        /**
         * 
         */
        @SuppressWarnings("hiding")
        String HOGE = "hoge2";
    }

    /**
     * 
     */
    public static class MyBean implements MyInterface2 {

        private String aaa;

        private String eee;

        /**
         * 
         */
        public String ggg;

        /**
         * @return String
         */
        public String getAaa() {
            return aaa;
        }

        /**
         * @param a
         * @return String
         */
        public String getBbb(Object a) {
            return null;
        }

        /**
         * @return boolean
         */
        public boolean isCCC() {
            return true;
        }

        /**
         * @return Object
         */
        public Object isDdd() {
            return null;
        }

        /**
         * @return String
         */
        public String getEee() {
            return eee;
        }

        /**
         * @param eee
         */
        public void setEee(String eee) {
            this.eee = eee;
        }

        /**
         * @return Boolean
         */
        public Boolean isFff() {
            return null;
        }

        /**
         * @param hhh
         * @return MyBean
         */
        public MyBean setHhh(String hhh) {
            return this;
        }

        /**
         * 
         */
        public void getIii() {
        }

        /**
         * @param arg1
         * @param arg2
         * @return Number
         */
        public Number add(Number arg1, Number arg2) {
            return new Integer(3);
        }

        /**
         * @param arg1
         * @param arg2
         * @return int
         */
        public int add2(int arg1, int arg2) {
            return arg1 + arg2;
        }

        /**
         * @param arg
         * @return Integer
         */
        public Integer echo(Integer arg) {
            return arg;
        }

        /**
         * 
         */
        public void throwException() {
            throw new IllegalStateException("hoge");
        }
    }

    /**
     * 
     */
    public class MyBean2 {
        /**
         * 
         */
        public MyBean2() {
        }

        /**
         * @param num
         * @param text
         * @param bean1
         * @param bean2
         */
        public MyBean2(int num, String text, MyBean bean1, MyBean2 bean2) {
        }

        /**
         * @param i
         */
        public void setAaa(int i) {
        }

        /**
         * @param s
         */
        public void setAaa(String s) {
        }
    }

    /** */
    public static class Hoge {

        /** */
        public List<String> foo;

        /**
         * @return Set
         */
        public Set<Integer> getBar() {
            return null;
        }

        /**
         * @param date
         */
        public void setBaz(Map<String, Date> date) {
        }

        /** */
        public List<?> hoge;

        /**
         * @return Set
         */
        public Set<? extends Enum<?>> getFuga() {
            return null;
        }

        /**
         * @param date
         */
        public void setHege(Map<? extends String, ? extends Number> date) {
        }

    }

    /**
     * @param <T>
     */
    public static class Foo<T> {

        /** */
        public List<T> list;

    }

    /** */
    public static class Bar extends Foo<String> {
    }

}
