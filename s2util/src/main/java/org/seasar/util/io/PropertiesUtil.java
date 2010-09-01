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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.seasar.util.exception.IORuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link Properties}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public abstract class PropertiesUtil {

    /**
     * {@link Properties#load(InputStream)}の例外処理をラップします。
     * <p>
     * 入力ストリームはクローズされません。
     * </p>
     * 
     * @param props
     *            プロパティセット
     * @param in
     *            入力ストリーム
     */
    public static void load(final Properties props, final InputStream in) {
        assertArgumentNotNull("props", props);
        assertArgumentNotNull("in", in);

        try {
            props.load(in);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

}
