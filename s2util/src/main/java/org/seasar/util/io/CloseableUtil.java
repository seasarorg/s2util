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

import java.io.Closeable;
import java.io.IOException;

import org.seasar.util.log.Logger;

import static org.seasar.util.log.Logger.*;

/**
 * {@link Closeable}用のユーティリティクラスです。
 * 
 * @author koichik
 */
public abstract class CloseableUtil {

    private static final Logger logger = Logger.getLogger(CloseableUtil.class);

    /**
     * {@link Closeable}を閉じます。
     * 
     * @param closeable
     *            クローズ可能なオブジェクト
     * @see Closeable#close()
     */
    public static void close(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (final IOException e) {
            logger.log(format("EUTL0017", e.getMessage()), e);
        }
    }

}
