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

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.seasar.util.exception.SQLRuntimeException;
import org.seasar.util.lang.ClassUtil;

/**
 * {@link java.sql.DriverManager}のためのユーティリティクラスです。
 * 
 * @author koichik
 */
public abstract class DriverManagerUtil {

    /**
     * JDBCドライバを登録します。
     * 
     * @param driverClassName
     *            登録するJDBCドライバのクラス名
     */
    public static void registerDriver(final String driverClassName) {
        final Class<Driver> clazz = ClassUtil.forName(driverClassName);
        registerDriver(clazz);
    }

    /**
     * JDBCドライバを登録します。
     * 
     * @param driverClass
     *            登録するJDBCドライバのクラス
     */
    public static void registerDriver(final Class<Driver> driverClass) {
        registerDriver(ClassUtil.newInstance(driverClass));
    }

    /**
     * JDBCドライバを登録します。
     * 
     * @param driver
     *            登録するJDBCドライバ
     */
    public static void registerDriver(final Driver driver) {
        try {
            DriverManager.registerDriver(driver);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * JDBCドライバを登録解除します。
     * 
     * @param driver
     *            登録解除するJDBCドライバ
     */
    public static void deregisterDriver(final Driver driver) {
        try {
            DriverManager.deregisterDriver(driver);
        } catch (final SQLException e) {
            throw new SQLRuntimeException(e);
        }
    }

    /**
     * 現在のクラスローダに結びつけられている全てのJDBCドライバを登録解除します。
     */
    public static synchronized void deregisterAllDrivers() {
        for (final Enumeration<Driver> e = DriverManager.getDrivers(); e
            .hasMoreElements();) {
            deregisterDriver(e.nextElement());
        }
    }

}
