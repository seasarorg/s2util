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
package org.seasar.util.collection;

import java.util.Map;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.seasar.util.collection.Maps.*;

/**
 * @author koichik
 */
public class MapsTest {

    /**
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        Map<String, Integer> map = map("a", 1).$("b", 2).$("c", 3).$();
        assertThat(map.size(), is(3));
        assertThat(map.get("a"), is(1));
        assertThat(map.get("b"), is(2));
        assertThat(map.get("c"), is(3));
    }

}
