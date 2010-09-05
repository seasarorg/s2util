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
import java.sql.ResultSet;
import java.sql.SQLException;

import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.exception.SQLRuntimeException;
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
    public static void closeSilently(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (final IOException e) {
            logger.log(format("EUTL0040", e.getMessage()), e);
        }
    }

    /**
     * {@link Closeable}を閉じます。
     * 
     * @param closeable
     *            クローズ可能なオブジェクト
     * @see Closeable#close()
     * 
     * @throws IORuntimeException
     *             {@link IOException} closeに失敗したときにthrowされる例外
     * 
     */
    public static void close(final Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (final IOException e) {
            throw new IORuntimeException(e);
        }
    }

    /**
     * {@link ResultSet}を閉じます。
     * 
     * @param resultSet
     *            結果セット
     * @see ResultSet#close()
     * 
     * @throws SQLRuntimeException
     *             {@link SQLException} closeに失敗したときにthrowされる例外
     * 
     */
    public static void close(final ResultSet resultSet) {
        if (resultSet == null) {
            return;
        }
        try {
            if (resultSet.isClosed() == false) {
                resultSet.close();
            }
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * {@link ResultSet}を閉じます。
     * 
     * @param resultSet
     *            結果セット
     * @see ResultSet#close()
     * 
     */
    public static void closeSilently(final ResultSet resultSet) {
        if (resultSet == null) {
            return;
        }
        try {
            if (resultSet.isClosed() == false) {
                resultSet.close();
            }
        } catch (final SQLException e) {
            logger.log(format("EUTL0072", e.getMessage()), e);
        }
    }

}
