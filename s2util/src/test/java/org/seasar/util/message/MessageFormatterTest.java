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
package org.seasar.util.message;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * @author higa
 */
public class MessageFormatterTest {

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessage() throws Exception {
        String s = MessageFormatter.getMessage("EMSG0000");
        System.out.println(s);
        assertThat(s, is("[EMSG0000]test"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessageWithArgs() throws Exception {
        String s = MessageFormatter.getMessage("EMSG0001", "hoge");
        System.out.println(s);
        assertThat(s, is("[EMSG0001]hogeが見つかりません"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessageIllegalSystemName() throws Exception {
        String s = MessageFormatter.getMessage("EXXX0001");
        System.out.println(s);
        assertThat(s, is("[EXXX0001]"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessageIllegalMessageCode() throws Exception {
        String s = MessageFormatter.getMessage("EMSGxxxx");
        System.out.println(s);
        assertThat(s, is("[EMSGxxxx]"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessageIllegalMessageCode2() throws Exception {
        String s = MessageFormatter.getMessage(null);
        System.out.println(s);
        assertThat(s, is("[]"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessageIllegalArgs() throws Exception {
        String s = MessageFormatter.getMessage("EMSG0001");
        System.out.println(s);
        assertThat(s, is("[EMSG0001]{0}が見つかりません"));
    }

    /**
     * @throws Exception
     */
    @Test
    public void testGetMessageLongForm() throws Exception {
        String s = MessageFormatter.getMessage("EMsgLongSystemName0001");
        System.out.println(s);
        assertThat(s, is(notNullValue()));
        assertThat(s, is(equalTo("[EMsgLongSystemName0001]Hoge Hoge")));
    }

}
