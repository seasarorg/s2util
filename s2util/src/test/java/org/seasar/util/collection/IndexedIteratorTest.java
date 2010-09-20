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

import java.io.StringReader;
import java.util.List;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.collection.CollectionsUtil.*;
import static org.seasar.util.collection.IndexedIterator.*;
import static org.seasar.util.io.LineIterator.*;

/**
 * @author wyukawa
 * 
 */
public class IndexedIteratorTest {

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        List<String> list = newArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        IndexedIterator<String> it =
            new IndexedIterator<String>(list.iterator());

        assertThat(it.hasNext(), is(true));

        Indexed<String> indexed1 = it.next();
        assertThat(indexed1.getIndex(), is(0));
        assertThat(indexed1.getElement(), is("aaa"));

        Indexed<String> indexed2 = it.next();
        assertThat(indexed2.getIndex(), is(1));
        assertThat(indexed2.getElement(), is("bbb"));

        Indexed<String> indexed3 = it.next();
        assertThat(indexed3.getIndex(), is(2));
        assertThat(indexed3.getElement(), is("ccc"));

        assertThat(it.hasNext(), is(not(true)));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testForEach() throws Exception {
        List<String> list = newArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        for (Indexed<String> indexed : indexed(list)) {
            System.out.println(indexed.getIndex());
            System.out.println(indexed.getElement());
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testForEachLineIterator() throws Exception {
        StringReader reader = new StringReader("aaa\nbbb\nccc\n");
        for (Indexed<String> indexed : indexed(iterable(reader))) {
            System.out.println(indexed.getIndex());
            System.out.println(indexed.getElement());
        }
    }

}
