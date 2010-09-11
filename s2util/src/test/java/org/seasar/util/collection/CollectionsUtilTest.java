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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author wyukawa
 * 
 */
public class CollectionsUtilTest {

    /**
     * Test method for
     * {@link org.seasar.util.collection.CollectionsUtil#isEmpty(java.util.Collection)}
     * .
     */
    @Test
    public void testIsEmptyCollectionOfQ() {
        Collection<String> c = null;
        assertThat(CollectionsUtil.isEmpty(c), is(true));
        c = new ArrayList<String>();
        assertThat(CollectionsUtil.isEmpty(c), is(true));
    }

    /**
     * Test method for
     * {@link org.seasar.util.collection.CollectionsUtil#isEmpty(java.util.Map)}
     * .
     */
    @Test
    public void testIsEmptyMapOfQQ() {
        HashMap<String, String> map = null;
        assertThat(CollectionsUtil.isEmpty(map), is(true));
        map = new HashMap<String, String>();
        assertThat(CollectionsUtil.isEmpty(map), is(true));
    }

}
