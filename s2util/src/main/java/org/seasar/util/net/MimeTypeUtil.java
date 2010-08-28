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
import java.io.InputStream;
import java.net.URLConnection;

import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.io.InputStreamUtil;
import org.seasar.util.io.ResourceUtil;
import org.seasar.util.misc.AssertionUtil;

/**
 * Mimeタイプ用のユーティリティクラスです。
 * 
 * @author shot
 */
public abstract class MimeTypeUtil {

    /**
     * コンテントタイプを予想します。
     * 
     * @param path
     *            パス
     * @return コンテントタイプ
     */
    public static String guessContentType(final String path) {
        AssertionUtil.assertNotNull("path is null.", path);
        final InputStream is = ResourceUtil.getResourceAsStream(path);
        try {
            final String mimetype =
                URLConnection.guessContentTypeFromStream(is);
            if (mimetype != null) {
                return mimetype;
            }
            return URLConnection.guessContentTypeFromName(path);
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        } finally {
            InputStreamUtil.close(is);
        }
    }

}
