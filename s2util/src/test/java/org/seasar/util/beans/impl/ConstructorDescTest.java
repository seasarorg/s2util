/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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

import java.lang.reflect.Constructor;

import org.junit.Test;
import org.seasar.util.beans.BeanDesc;
import org.seasar.util.beans.ConstructorDesc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author koichik
 * 
 */
public class ConstructorDescTest {

    /**
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testDefaultConstructor() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        ConstructorDesc ctor = beanDesc.getConstructorDesc();
        assertThat(ctor, is(notNullValue()));
        assertThat(ctor.getBeanDesc(), is(sameInstance(beanDesc)));
        assertThat(
            ctor.getConstructor(),
            is((Constructor) MyBean.class.getConstructor()));
        assertThat(ctor.getParameterTypes().length, is(0));
        assertThat(ctor.isPublic(), is(true));
        MyBean myBean = ctor.newInstance();
        assertThat(myBean, is(notNullValue()));
        assertThat(myBean.s, is(nullValue()));
    }

    /**
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void test1ArgConstructor() throws Exception {
        BeanDesc beanDesc = new BeanDescImpl(MyBean.class);
        ConstructorDesc ctor = beanDesc.getConstructorDesc(String.class);
        assertThat(ctor, is(notNullValue()));
        assertThat(ctor.getBeanDesc(), is(sameInstance(beanDesc)));
        assertThat(
            ctor.getConstructor(),
            is((Constructor) MyBean.class.getConstructor(String.class)));
        assertThat(ctor.getParameterTypes().length, is(1));
        assertThat(ctor.isPublic(), is(true));
        MyBean myBean = ctor.newInstance("hoge");
        assertThat(myBean, is(notNullValue()));
        assertThat(myBean.s, is("hoge"));
    }

    /**
     */
    public static class MyBean {
        /** */
        public String s;

        /**
         * 
         */
        public MyBean() {
        }

        /**
         * @param s
         */
        public MyBean(String s) {
            this.s = s;
        }
    }

}
