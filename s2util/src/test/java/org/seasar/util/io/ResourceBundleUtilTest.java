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
package org.seasar.util.io;

import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 * 
 */
public class ResourceBundleUtilTest {

    /**
     * @throws Exception
     */
    @Test
    public void testConvertMap() throws Exception {
        ResourceBundle bundle =
            ResourceBundleUtil.getBundle("UTLMessages", null);
        Map<String, String> map = ResourceBundleUtil.convertMap(bundle);
        String value = map.get("EUTL0001");
        System.out.println(value);
        assertThat(value, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetBundle() throws Exception {
        ResourceBundle bundle =
            ResourceBundleUtil.getBundle("UTLMessages", null, this
                .getClass()
                .getClassLoader());
        assertThat(bundle, is(notNullValue()));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetString() throws Exception {
        ResourceBundle bundle = ResourceBundleUtil.getBundle("UTLMessages");
        assertThat(bundle.getString("EUTL0001"), is(notNullValue()));
    }

}
