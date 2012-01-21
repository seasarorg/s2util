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
package org.seasar.util.lang;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Iterator;

import junit.framework.TestCase;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.TestUtil.*;

/**
 * @author koichik
 */
public class ClassLoaderUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void testGetClassLoader() throws Exception {
        assertThat(
            ClassLoaderUtil.getClassLoader(Object.class),
            is(sameInstance(ClassLoaderUtil.class.getClassLoader())));

        assertThat(
            ClassLoaderUtil.getClassLoader(TestCase.class),
            is(sameInstance(TestCase.class.getClassLoader())));

        ClassLoader context = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader cl =
                new URLClassLoader(new URL[0], getClass().getClassLoader());
            Thread.currentThread().setContextClassLoader(cl);
            assertThat(
                ClassLoaderUtil.getClassLoader(TestCase.class),
                is(sameInstance(cl)));
        } finally {
            Thread.currentThread().setContextClassLoader(context);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testFindLoadedClass() throws Exception {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> clazz =
            ClassLoaderUtil.findLoadedClass(loader, getClass().getName());
        assertThat(clazz, is(sameClass(getClass())));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetResources() throws Exception {
        String name = TestCase.class.getName().replace('.', '/') + ".class";
        Iterator<URL> itr = ClassLoaderUtil.getResources(this.getClass(), name);
        assertThat(itr, is(notNullValue()));
        URL url = itr.next();
        assertThat(url, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIsAncestor() throws Exception {
        ClassLoader cl1 =
            new URLClassLoader(new URL[] { new URL("file:/foo") }, null);
        ClassLoader cl2 =
            new URLClassLoader(new URL[] { new URL("file:/bar") }, cl1);
        ClassLoader cl3 =
            new URLClassLoader(new URL[] { new URL("file:/baz") }, cl2);

        assertThat(ClassLoaderUtil.isAncestor(cl3, cl2), is(true));
        assertThat(ClassLoaderUtil.isAncestor(cl3, cl1), is(true));
        assertThat(ClassLoaderUtil.isAncestor(cl2, cl1), is(true));

        assertThat(ClassLoaderUtil.isAncestor(cl1, cl2), is(not(true)));
        assertThat(ClassLoaderUtil.isAncestor(cl1, cl3), is(not(true)));
    }

}
