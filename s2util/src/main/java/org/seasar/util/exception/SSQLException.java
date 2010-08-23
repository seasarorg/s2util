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
package org.seasar.util.exception;

import java.sql.SQLException;

import org.seasar.util.message.MessageFormatter;

/**
 * S2Util用の{@link SQLException}です。
 * 
 * @author higa
 */
public class SSQLException extends SQLException {

    private static final long serialVersionUID = 4098267431221202677L;

    private final String messageCode;

    private final Object[] args;

    private final String sql;

    /**
     * {@link SSQLException}を作成します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数の並び
     */
    public SSQLException(final String messageCode, final Object... args) {
        this(null, null, 0, messageCode, args);
    }

    /**
     * {@link SSQLException}を作成します。
     * 
     * @param sqlState
     *            SQLステート
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数の並び
     */
    public SSQLException(final String sqlState, final String messageCode, final Object... args) {
        this(null, sqlState, 0, messageCode, args);
    }

    /**
     * {@link SSQLException}を作成します。
     * 
     * @param sqlState
     *            SQLステート
     * @param vendorCode
     *            ベンダーコード
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数の並び
     */
    public SSQLException(final String sqlState, final int vendorCode, final String messageCode,
            final Object... args) {
        this(null, sqlState, vendorCode, messageCode, args);
    }

    /**
     * {@link SSQLException}を作成します。
     * 
     * @param sql
     *            SQL文字列
     * @param sqlState
     *            SQLステート
     * @param vendorCode
     *            ベンダーコード
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数の並び
     */
    public SSQLException(final String sql, final String sqlState, final int vendorCode,
            final String messageCode, final Object... args) {
        super(
            MessageFormatter.getMessage(messageCode, args),
            sqlState,
            vendorCode);
        this.messageCode = messageCode;
        this.args = args;
        this.sql = sql;
    }

    @Override
    public SSQLException initCause(final Throwable cause) {
        if (cause instanceof SQLException) {
            setNextException((SQLException) cause);
        }
        return (SSQLException) super.initCause(cause);
    }

    /**
     * メッセージコードを返します。
     * 
     * @return メッセージコード
     */
    public String getMessageCode() {
        return messageCode;
    }

    /**
     * 引数の配列を返します。
     * 
     * @return 引数の配列
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * SQLを返します。
     * 
     * @return SQL
     */
    public String getSql() {
        return sql;
    }

}
