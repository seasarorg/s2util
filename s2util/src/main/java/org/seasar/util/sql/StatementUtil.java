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
package org.seasar.util.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.seasar.util.exception.SQLRuntimeException;

/**
 * {@link Statement}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class StatementUtil {

    /**
     * SQLを実行します。
     * 
     * @param statement
     * @param sql
     * @return 実行した結果
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#execute(String)
     */
    public static boolean execute(final Statement statement, final String sql)
            throws SQLRuntimeException {
        try {
            return statement.execute(sql);
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * フェッチサイズを設定します。
     * 
     * @param statement
     * @param fetchSize
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#setFetchSize(int)
     */
    public static void setFetchSize(final Statement statement,
            final int fetchSize) throws SQLRuntimeException {
        try {
            statement.setFetchSize(fetchSize);
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 最大行数を設定します。
     * 
     * @param statement
     * @param maxRows
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#setMaxRows(int)
     */
    public static void setMaxRows(final Statement statement, final int maxRows)
            throws SQLRuntimeException {
        try {
            statement.setMaxRows(maxRows);
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * クエリタイムアウトを設定します。
     * 
     * @param statement
     * @param queryTimeout
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#setQueryTimeout(int)
     */
    public static void setQueryTimeout(final Statement statement,
            final int queryTimeout) throws SQLRuntimeException {
        try {
            statement.setQueryTimeout(queryTimeout);
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * {@link Statement}を閉じます。
     * 
     * @param statement
     * @throws SQLRuntimeException
     *             {@link SQLException}が発生した場合
     * @see Statement#close()
     */
    public static void close(final Statement statement)
            throws SQLRuntimeException {
        if (statement == null) {
            return;
        }
        try {
            statement.close();
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 結果セットを返します。
     * 
     * @param statement
     * @return 結果セット
     * @throws SQLRuntimeException
     */
    public static ResultSet getResultSet(final Statement statement)
            throws SQLRuntimeException {
        try {
            return statement.getResultSet();
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * 更新カウントを返します。
     * 
     * @param statement
     *            ステートメント
     * @return 更新カウント
     * @see Statement#getUpdateCount()
     */
    public static int getUpdateCount(final Statement statement) {
        try {
            return statement.getUpdateCount();
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }
}
