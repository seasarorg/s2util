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
package org.seasar.util.collection;

import java.lang.reflect.Array;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 * 
 */
public class ArrayUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void testAdd() throws Exception {
        String[] array = new String[] { "111" };
        String[] newArray = ArrayUtil.add(array, "222");
        assertThat(newArray.length, is(2));
        assertThat(newArray[0], is("111"));
        assertThat(newArray[1], is("222"));

        String[] emptyArray = new String[0];
        assertThat(ArrayUtil.add(null, null), is(nullValue()));
        assertThat(ArrayUtil.add(null, emptyArray).length, is(0));
        assertThat(
            ArrayUtil.add(emptyArray, null),
            is(sameInstance(emptyArray)));
        assertThat(
            ArrayUtil.add(emptyArray, null),
            is(sameInstance(emptyArray)));
        assertThat(
            ArrayUtil.add(emptyArray, emptyArray),
            is(sameInstance(emptyArray)));
        assertThat(ArrayUtil.add(emptyArray, array), is(sameInstance(array)));
        assertThat(ArrayUtil.add(array, emptyArray), is(sameInstance(array)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testAdd2() throws Exception {
        String[] a = new String[] { "1", "2" };
        String[] b = new String[] { "3" };
        a = ArrayUtil.add(a, b);
        assertThat(a.length, is(3));
        assertThat(a[0], is("1"));
        assertThat(a[1], is("2"));
        assertThat(a[2], is("3"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testAdd_int() throws Exception {
        int[] array = new int[] { 1 };
        int[] newArray = ArrayUtil.add(array, 2);
        assertThat(newArray.length, is(2));
        assertThat(newArray[0], is(1));
        assertThat(newArray[1], is(2));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIndexOf() throws Exception {
        String[] array = new String[] { "111", "222", "333", "222" };
        assertThat(ArrayUtil.indexOf(array, "222"), is(1));
        assertThat(ArrayUtil.indexOf(array, "222", 2), is(3));
        assertThat(ArrayUtil.indexOf(array, new Object()), is(-1));
        assertThat(ArrayUtil.indexOf(array, null), is(-1));
        array[1] = null;
        assertThat(ArrayUtil.indexOf(array, null), is(1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testIndexOf_character() throws Exception {
        char[] array = new char[] { 'a', 'b', 'c' };
        assertThat(ArrayUtil.indexOf(array, 'a'), is(0));
        assertThat(ArrayUtil.indexOf(array, 'd'), is(-1));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testRemoveFirst() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = ArrayUtil.remove(array, "111");
        assertThat(newArray.length, is(2));
        assertThat(newArray[0], is("222"));
        assertThat(newArray[1], is("333"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testRemoveMiddle() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = ArrayUtil.remove(array, "222");
        assertThat(newArray.length, is(2));
        assertThat(newArray[0], is("111"));
        assertThat(newArray[1], is("333"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testRemoveLast() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = ArrayUtil.remove(array, "333");
        assertThat(newArray.length, is(2));
        assertThat(newArray[0], is("111"));
        assertThat(newArray[1], is("222"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testRemoveNothing() throws Exception {
        String[] array = new String[] { "111", "222", "333" };
        String[] newArray = ArrayUtil.remove(array, "444");
        assertThat(newArray, is(sameInstance(array)));
    }

    /**
     * 
     */
    @Test
    public void testIsEmpty() {
        assertTrue(ArrayUtil.isEmpty(null));
        assertTrue(ArrayUtil.isEmpty(new Object[] {}));
        assertFalse(ArrayUtil.isEmpty(new Object[] { "" }));
        assertFalse(ArrayUtil.isEmpty(new Object[] { "aaa" }));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testContains() throws Exception {
        assertThat(ArrayUtil.contains(new Object[] { "1" }, "1"), is(true));
        assertThat(ArrayUtil.contains(new Object[] { "1" }, "2"), is(not(true)));
        assertThat(ArrayUtil.contains(new Object[] { "2", "1" }, "1"), is(true));
        assertThat(ArrayUtil.contains((Object[]) null, "1"), is(not(true)));
        assertThat(ArrayUtil.contains((Object[]) null, null), is(not(true)));
        assertThat(ArrayUtil.contains(new Object[] { null }, null), is(true));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testContains_character() throws Exception {
        assertThat(ArrayUtil.contains(new char[] { '1', '2' }, '1'), is(true));
        assertThat(ArrayUtil.contains(new char[] { '1' }, '2'), is(not(true)));
        assertThat(ArrayUtil.contains(new char[] { '2', '1' }, '1'), is(true));
        assertThat(ArrayUtil.contains((char[]) null, '1'), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testEqualsIgnoreSequence() throws Exception {
        assertThat(ArrayUtil.equalsIgnoreSequence(
            new Object[] { "1" },
            new Object[] { "1" }), is(true));
        assertThat(ArrayUtil.equalsIgnoreSequence(
            new Object[] { "1", "2", "3" },
            new Object[] { "2", "3", "1" }), is(true));
        assertThat(ArrayUtil.equalsIgnoreSequence(
            new Object[] { "1" },
            new Object[] { "2" }), is(not(true)));
        assertThat(ArrayUtil.equalsIgnoreSequence(
            new Object[] { "1" },
            new Object[] {}), is(not(true)));
        assertThat(ArrayUtil.equalsIgnoreSequence(new Object[] { new Integer(
            "1") }, new Object[] { "1" }), is(not(true)));
        assertThat(ArrayUtil.equalsIgnoreSequence(
            new Object[] { "1", "1" },
            new Object[] { "1", "2" }), is(not(true)));
        assertThat(ArrayUtil.equalsIgnoreSequence(new Object[] { "1", "2", "1",
            "2" }, new Object[] { "2", "2", "1", "1" }), is(true));

        assertThat(ArrayUtil.equalsIgnoreSequence(null, null), is(true));
        assertThat(
            ArrayUtil.equalsIgnoreSequence(null, new Object[] {}),
            is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testSetArrayValue() throws Exception {
        Object o = Array.newInstance(int.class, 3);
        Array.set(o, 0, new Integer(1));
        Array.set(o, 1, new Integer(2));
        Array.set(o, 2, new Integer(3));
        int[] num = (int[]) o;
        assertArrayEquals(new int[] { 1, 2, 3 }, num);
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToObjectArray() throws Exception {
        final Object[] a = ArrayUtil.toObjectArray(new int[] { 1, 5, 2 });
        assertArrayEquals(new Integer[] { new Integer(1), new Integer(5),
            new Integer(2) }, a);
    }

    /**
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testToObjectArray_NoArray() throws Exception {
        ArrayUtil.toObjectArray("a");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testToList() throws Exception {
        final Object a = new int[] { 1, 5 };
        List<Integer> list = ArrayUtil.toList(a);
        assertThat(list.get(0), is(Integer.valueOf(1)));
        assertThat(list.get(1), is(Integer.valueOf(5)));
    }

}
