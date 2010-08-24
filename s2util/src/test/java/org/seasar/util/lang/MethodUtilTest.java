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

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.lang.ClassUtilTest.*;

/**
 * @author higa
 */
public class MethodUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void testIsEqualsMethod() throws Exception {
        Method equalsMethod =
            ClassUtil.getMethod(getClass(), "equals", Object.class);
        assertThat(MethodUtil.isEqualsMethod(equalsMethod), is(true));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode");
        assertThat(MethodUtil.isEqualsMethod(hashCodeMethod), is(not(true)));
    }

    /**
     * 
     */
    @Test
    public void testIsHashCodeMethod() {
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode");
        assertThat(MethodUtil.isHashCodeMethod(hashCodeMethod), is(true));
        Method equalsMethod =
            ClassUtil.getMethod(getClass(), "equals", Object.class);
        assertThat(MethodUtil.isHashCodeMethod(equalsMethod), is(not(true)));
    }

    /**
     * 
     */
    @Test
    public void testIsToStringMethod() {
        Method toStringMethod = ClassUtil.getMethod(getClass(), "toString");
        assertThat(MethodUtil.isToStringMethod(toStringMethod), is(true));
        Method hashCodeMethod = ClassUtil.getMethod(getClass(), "hashCode");
        assertThat(MethodUtil.isToStringMethod(hashCodeMethod), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetElementTypeOfCollectionFromParameterType()
            throws Exception {
        Method m = Baz.class.getMethod("rawList", List.class);
        assertThat(
            MethodUtil.getElementTypeOfCollectionFromParameterType(m, 0),
            is(nullValue()));

        m = Baz.class.getMethod("genericList", List.class);
        assertThat(
            MethodUtil.getElementTypeOfCollectionFromParameterType(m, 0),
            isSameClass(Integer.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetElementTypeOfCollectionFromReturnType() throws Exception {
        Method m = Baz.class.getMethod("rawList", List.class);
        assertThat(
            MethodUtil.getElementTypeOfCollectionFromReturnType(m),
            is(nullValue()));

        m = Baz.class.getMethod("genericList", List.class);
        assertThat(
            MethodUtil.getElementTypeOfCollectionFromReturnType(m),
            isSameClass(String.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetKeyTypeOfMapFromParameterType() throws Exception {
        Method m = Baz.class.getMethod("rawMap", Map.class);
        assertThat(
            MethodUtil.getKeyTypeOfMapFromParameterType(m, 0),
            is(nullValue()));

        m = Baz.class.getMethod("genericMap", Map.class);
        assertThat(
            MethodUtil.getKeyTypeOfMapFromParameterType(m, 0),
            isSameClass(Integer.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetKeyTypeOfMapFromReturnType() throws Exception {
        Method m = Baz.class.getMethod("rawMap", Map.class);
        assertThat(MethodUtil.getKeyTypeOfMapFromReturnType(m), is(nullValue()));

        m = Baz.class.getMethod("genericMap", Map.class);
        assertThat(
            MethodUtil.getKeyTypeOfMapFromReturnType(m),
            isSameClass(String.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetValueTypeOfMapFromParameterType() throws Exception {
        Method m = Baz.class.getMethod("rawMap", Map.class);
        assertThat(
            MethodUtil.getValueTypeOfMapFromParameterType(m, 0),
            is(nullValue()));

        m = Baz.class.getMethod("genericMap", Map.class);
        assertThat(
            MethodUtil.getValueTypeOfMapFromParameterType(m, 0),
            isSameClass(String.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetValueTypeOfMapFromReturnType() throws Exception {
        Method m = Baz.class.getMethod("rawMap", Map.class);
        assertThat(
            MethodUtil.getValueTypeOfMapFromReturnType(m),
            is(nullValue()));

        m = Baz.class.getMethod("genericMap", Map.class);
        assertThat(
            MethodUtil.getValueTypeOfMapFromReturnType(m),
            isSameClass(Class.class));
    }

    /**
     *
     */
    public interface Baz {

        /**
         * @param param
         * @return 戻り値
         */
        @SuppressWarnings("rawtypes")
        List rawList(List param);

        /**
         * @param param
         * @return 戻り値
         */
        List<String> genericList(List<Integer> param);

        /**
         * @param param
         * @return 戻り値
         */
        @SuppressWarnings("rawtypes")
        Map rawMap(Map param);

        /**
         * @param param
         * @return 戻り値
         */
        Map<String, Class<?>> genericMap(Map<Integer, String> param);

    }

}
