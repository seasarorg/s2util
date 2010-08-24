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
package org.seasar.util.lang;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.seasar.util.exception.NoSuchConstructorRuntimeException;
import org.seasar.util.exception.NoSuchFieldRuntimeException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 * 
 */
public class ClassUtilTest {

    /**
     * 
     */
    public static final String HOGE = "hoge";

    /**
     * 
     */
    @Test
    public void testGetPrimitiveClass() {
        assertThat(
            ClassUtil.getPrimitiveClass(Integer.class),
            isSameClass(int.class));
        assertThat(ClassUtil.getPrimitiveClass(String.class), is(nullValue()));
        assertThat(
            ClassUtil.getPrimitiveClass(Byte.class),
            isSameClass(byte.class));
    }

    /**
     * 
     */
    @Test
    public void testGetPrimitiveClassIfWrapper() {
        assertThat(
            ClassUtil.getPrimitiveClassIfWrapper(Integer.class),
            isSameClass(int.class));
        assertThat(
            ClassUtil.getPrimitiveClassIfWrapper(String.class),
            isSameClass(String.class));
        assertThat(
            ClassUtil.getPrimitiveClassIfWrapper(Byte.class),
            isSameClass(byte.class));
    }

    /**
     * 
     */
    @Test
    public void testGetWrapperClass() {
        assertThat(
            ClassUtil.getWrapperClass(int.class),
            isSameClass(Integer.class));
        assertThat(ClassUtil.getWrapperClass(String.class), is(nullValue()));
        assertThat(
            ClassUtil.getWrapperClass(byte.class),
            isSameClass(Byte.class));
    }

    /**
     * 
     */
    @Test
    public void testGetWrapperClassIfWrapper() {
        assertThat(
            ClassUtil.getWrapperClassIfPrimitive(int.class),
            isSameClass(Integer.class));
        assertThat(
            ClassUtil.getWrapperClassIfPrimitive(String.class),
            isSameClass(String.class));
        assertThat(
            ClassUtil.getWrapperClassIfPrimitive(byte.class),
            isSameClass(Byte.class));
    }

    /**
     * 
     */
    @Test
    public void testIsAssignableFrom() {
        assertThat(
            ClassUtil.isAssignableFrom(Number.class, Integer.class),
            is(true));
        assertThat(
            ClassUtil.isAssignableFrom(Integer.class, Number.class),
            is(not(true)));
        assertThat(
            ClassUtil.isAssignableFrom(int.class, Integer.class),
            is(true));
    }

    /**
     * 
     */
    @Test
    public void testGetPackageName() {
        assertThat(
            ClassUtil.getPackageName(getClass()),
            is("org.seasar.util.lang"));
    }

    /**
     * 
     */
    @Test
    public void testGetShortClassName() {
        assertThat(
            ClassUtil.getShortClassName(getClass().getName()),
            is("ClassUtilTest"));
    }

    /**
     * 
     */
    @Test(expected = NoSuchConstructorRuntimeException.class)
    public void testGetConstructor() {
        ClassUtil.getConstructor(ClassUtilTest.class, Integer.class);
    }

    /**
     * 
     */
    @Test(expected = NoSuchFieldRuntimeException.class)
    public void testGetField() {
        ClassUtil.getField(getClass(), "aaa");
    }

    /**
     * 
     */
    @Test
    public void testGetSimpleClassName() {
        assertThat(ClassUtil.getSimpleClassName(int.class), is("int"));
        assertThat(
            ClassUtil.getSimpleClassName(String.class),
            is("java.lang.String"));
        assertThat(ClassUtil.getSimpleClassName(int[].class), is("int[]"));
        assertThat(
            ClassUtil.getSimpleClassName(String[][].class),
            is("java.lang.String[][]"));
    }

    /**
     * 
     */
    @Test
    public void testConcatName() {
        assertThat(ClassUtil.concatName("aaa", "bbb"), is("aaa.bbb"));
        assertThat(ClassUtil.concatName("aaa", null), is("aaa"));
        assertThat(ClassUtil.concatName("aaa", ""), is("aaa"));
        assertThat(ClassUtil.concatName(null, "bbb"), is("bbb"));
        assertThat(ClassUtil.concatName("", "bbb"), is("bbb"));
        assertThat(ClassUtil.concatName("", "bbb"), is("bbb"));
        assertThat(ClassUtil.concatName(null, null), is(nullValue()));
        assertThat(ClassUtil.concatName(null, ""), is(nullValue()));
        assertThat(ClassUtil.concatName("", null), is(nullValue()));
        assertThat(ClassUtil.concatName("", ""), is(nullValue()));
    }

    /**
     * 
     */
    @Test
    public void testGetResourcePath() {
        assertThat(
            ClassUtil.getResourcePath(getClass()),
            is("org/seasar/util/lang/ClassUtilTest.class"));
    }

    /**
     * 
     */
    @Test
    public void testSplitPackageAndShortClassName() {
        String[] ret = ClassUtil.splitPackageAndShortClassName("aaa.Hoge");
        assertThat(ret[0], is("aaa"));
        assertThat(ret[1], is("Hoge"));
        ret = ClassUtil.splitPackageAndShortClassName("Hoge");
        assertThat(ret[0], is(nullValue()));
        assertThat(ret[1], is("Hoge"));
    }

    /**
     * 
     */
    @Test
    public void testConvertClass() {
        assertThat(ClassUtil.convertClass("int"), isSameClass(int.class));
        assertThat(
            ClassUtil.convertClass("java.lang.String"),
            isSameClass(String.class));
    }

    static Matcher<Object> isSameClass(Class<?> clazz) {
        return is((Object) clazz);
    }

}
