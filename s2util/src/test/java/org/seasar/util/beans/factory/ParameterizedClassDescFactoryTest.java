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
package org.seasar.util.beans.factory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.seasar.util.beans.ParameterizedClassDesc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.lang.ClassUtilTest.*;

/**
 * @author koichik
 */
public class ParameterizedClassDescFactoryTest {

    /**
     * @throws Exception
     */
    @Test
    public void testFieldType() throws Exception {
        Map<TypeVariable<?>, Type> map =
            ParameterizedClassDescFactory.getTypeVariables(Hoge.class);
        Field field = Hoge.class.getField("foo");
        ParameterizedClassDesc desc =
            ParameterizedClassDescFactory.createParameterizedClassDesc(
                field,
                map);
        assertThat(desc.getRawClass(), isSameClass(Map.class));

        ParameterizedClassDesc[] args = desc.getArguments();
        assertThat(args.length, is(2));

        ParameterizedClassDesc arg1 = args[0];
        assertThat(arg1.getRawClass(), isSameClass(String.class));
        assertThat(arg1.getArguments(), is(nullValue()));

        ParameterizedClassDesc arg2 = args[1];
        assertThat(arg2.getRawClass(), isSameClass(Set[].class));

        ParameterizedClassDesc[] args2 = arg2.getArguments();
        assertThat(args2.length, is(1));

        ParameterizedClassDesc arg2_1 = args2[0];
        assertThat(arg2_1.getRawClass(), isSameClass(Integer.class));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testMethodParameterType() throws Exception {
        Map<TypeVariable<?>, Type> map =
            ParameterizedClassDescFactory.getTypeVariables(Hoge.class);
        Method method = Hoge.class.getMethod("foo", Set.class, Map.class);
        ParameterizedClassDesc desc =
            ParameterizedClassDescFactory.createParameterizedClassDesc(
                method,
                0,
                map);
        assertThat(desc.getRawClass(), isSameClass(Set.class));
        ParameterizedClassDesc[] args = desc.getArguments();
        assertThat(args.length, is(1));
        assertThat(args[0].getRawClass(), isSameClass(Integer.class));

        desc =
            ParameterizedClassDescFactory.createParameterizedClassDesc(
                method,
                1,
                map);
        assertThat(desc.getRawClass(), isSameClass(Map.class));
        args = desc.getArguments();
        assertThat(args.length, is(2));
        assertThat(args[0].getRawClass(), isSameClass(String.class));
        assertThat(args[0].getArguments(), is(nullValue()));
        assertThat(args[1].getRawClass(), isSameClass(Integer.class));
        assertThat(args[1].getArguments(), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testMethodReturnType() throws Exception {
        Map<TypeVariable<?>, Type> map =
            ParameterizedClassDescFactory.getTypeVariables(Hoge.class);
        Method method = Hoge.class.getMethod("foo", Set.class, Map.class);
        ParameterizedClassDesc desc =
            ParameterizedClassDescFactory.createParameterizedClassDesc(
                method,
                map);
        assertThat(desc.getRawClass(), isSameClass(List.class));
        ParameterizedClassDesc[] args = desc.getArguments();
        assertThat(args.length, is(1));
        assertThat(args[0].getRawClass(), isSameClass(String.class));
        assertThat(args[0].getArguments(), is(nullValue()));
    }

    /**
     * @author koichik
     */
    public interface Hoge {

        /** */
        public static Map<String, Set<Integer>[]> foo = null;

        /**
         * @param arg1
         * @param arg2
         * @return List
         */
        List<String> foo(Set<Integer> arg1, Map<String, Integer> arg2);
    }

}
