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
import org.seasar.util.log.Logger;

import static org.seasar.util.log.Logger.*;

/**
 * {@link Statement}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class StatementUtil {

    private static final Logger logger = Logger.getLogger(StatementUtil.class);

    /**
     * SQLを実行します。
     * 
     * @param statement
     *            {@link Statement}
     * @param sql
     *            SQL文字列
     * @return 実行した結果
     * @see Statement#execute(String)
     */
    public static boolean execute(final Statement statement, final String sql) {
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
     *            {@link Statement}
     * @param fetchSize
     *            フェッチサイズ
     * @see Statement#setFetchSize(int)
     */
    public static void setFetchSize(final Statement statement,
            final int fetchSize) {
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
     *            {@link Statement}
     * @param maxRows
     *            最大の行数
     * @see Statement#setMaxRows(int)
     */
    public static void setMaxRows(final Statement statement, final int maxRows) {
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
     *            {@link Statement}
     * @param queryTimeout
     *            クエリタイムアウト
     * @see Statement#setQueryTimeout(int)
     */
    public static void setQueryTimeout(final Statement statement,
            final int queryTimeout) {
        try {
            statement.setQueryTimeout(queryTimeout);
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

    /**
     * {@link Statement}を閉じます。
     * <p>
     * {@link Statement#close()}が例外をスローした場合はログにエラーメッセージを出力します。 例外は再スローされません。
     * </p>
     * 
     * @param statement
     *            {@link Statement}
     * @see Statement#close()
     */
    public static void close(final Statement statement) {
        if (statement == null) {
            return;
        }
        try {
            statement.close();
        } catch (final SQLException e) {
            logger.log(format("EUTL0017", e.getMessage()), e);
        }
    }

    /**
     * 結果セットを返します。
     * 
     * @param statement
     *            {@link Statement}
     * @return 結果セット
     */
    public static ResultSet getResultSet(final Statement statement) {
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
     *            {@link Statement}
     * @return 更新カウント
     */
    public static int getUpdateCount(final Statement statement) {
        try {
            return statement.getUpdateCount();
        } catch (final SQLException ex) {
            throw new SQLRuntimeException(ex);
        }
    }

}
