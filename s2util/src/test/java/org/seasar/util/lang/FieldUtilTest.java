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

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;
import org.seasar.util.exception.SIllegalArgumentException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.TestUtil.*;

/**
 * @author y-komori
 * 
 */
public class FieldUtilTest {

    /** */
    public Object objectField;

    /** */
    public int intField;

    /** */
    public String stringField;

    /** */
    public static final int INT_DATA = 987654321;

    /** */
    public static final String STRING_DATA = "Hello World!";

    /**
     * @throws Exception
     */
    @Test
    public void testGet() throws Exception {
        Field field = getClass().getField("objectField");
        Integer testData = new Integer(123);
        FieldUtil.set(field, this, testData);
        assertThat((Integer) FieldUtil.get(field, this), is(testData));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetIntField() throws Exception {
        Field field = getClass().getField("intField");
        int testData = 1234567890;
        FieldUtil.set(field, this, new Integer(testData));
        assertThat(FieldUtil.getInt(field, this), is(testData));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetIntFieldObject() throws Exception {
        Field field = getClass().getField("INT_DATA");
        assertThat(FieldUtil.getInt(field), is(INT_DATA));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetStringField() throws Exception {
        Field field = getClass().getField("stringField");
        String testData = "Hello World!";
        FieldUtil.set(field, this, testData);
        assertThat(FieldUtil.getString(field, this), is(testData));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetStringFieldObject() throws Exception {
        Field field = getClass().getField("STRING_DATA");
        assertThat(FieldUtil.getString(field), is(STRING_DATA));
    }

    /**
     * @throws Exception
     */
    @Test(expected = SIllegalArgumentException.class)
    public void testSet() throws Exception {
        Field field = getClass().getField("intField");
        FieldUtil.set(field, this, "abc");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetElementType_Rawtype() throws Exception {
        assertThat(FieldUtil.getElementTypeOfCollection(Baz.class
            .getField("collectionOfRawtype")), is(nullValue()));
    }

    /**
     * @throws Exception
     */
    public void testGetElementTypeOfCollection() throws Exception {
        assertEquals(
            String.class,
            FieldUtil.getElementTypeOfCollection(Baz.class
                .getField("collectionOfString")));
    }

    /**
     * @throws Exception
     */
    public void testGetKeyTypeOfMap() throws Exception {
        Field f = ClassUtil.getField(Baz.class, "map");
        assertThat(FieldUtil.getKeyTypeOfMap(f), is(sameClass(String.class)));
        assertThat(FieldUtil.getValueTypeOfMap(f), is(sameClass(Integer.class)));
    }

    /**
     * 
     */
    public static class Baz {

        /** */
        @SuppressWarnings("rawtypes")
        public Collection collectionOfRawtype;

        /** */
        public Collection<String> collectionOfString;

        /** */
        public Map<String, Integer> map;

    }

}
