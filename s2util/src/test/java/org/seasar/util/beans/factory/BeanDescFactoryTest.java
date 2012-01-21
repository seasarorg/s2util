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
package org.seasar.util.beans.factory;

import org.junit.Test;
import org.seasar.util.beans.BeanDesc;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 */
public class BeanDescFactoryTest {

    /**
     * @throws Exception
     */
    @Test
    public void testGetBeanDesc() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(MyBean.class);
        assertThat(
            BeanDescFactory.getBeanDesc(MyBean.class),
            is(sameInstance(beanDesc)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testClear() throws Exception {
        BeanDesc beanDesc = BeanDescFactory.getBeanDesc(MyBean.class);
        BeanDescFactory.clear();
        assertThat(
            BeanDescFactory.getBeanDesc(MyBean.class),
            is(not(sameInstance(beanDesc))));
    }

    /**
     *
     */
    public static class MyBean {

        /**
         * @return String
         */
        public String getAaa() {
            return null;
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
            return null;
        }

        /**
         * @param eee
         *            eee
         */
        public void setEee(String eee) {
        }
    }

}
