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
package org.seasar.util.net;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.jar.JarFile;

import org.seasar.util.exception.IORuntimeException;

/**
 * {@link JarURLConnection}用のユーティリティクラスです。
 * 
 * @author higa
 * 
 */
public class JarURLConnectionUtil {

    /**
     * インスタンスを構築します。
     */
    protected JarURLConnectionUtil() {
    }

    /**
     * {@link JarURLConnection#getJarFile()}の例外処理をラップするメソッドです。
     * 
     * @param conn
     * @return {@link JarFile}
     * @throws IORuntimeException
     *             {@link IOException}が発生した場合
     */
    public static JarFile getJarFile(JarURLConnection conn)
            throws IORuntimeException {
        try {
            return conn.getJarFile();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }
}