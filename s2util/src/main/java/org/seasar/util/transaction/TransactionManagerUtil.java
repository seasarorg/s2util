/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
package org.seasar.util.transaction;

import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.seasar.util.exception.SystemRuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link TransactionManager}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class TransactionManagerUtil {

    /**
     * トランザクションを返します。
     * 
     * @param tm
     *            トランザクションマネージャ。{@literal null}であってはいけません
     * @return トランザクション
     * @throws SystemRuntimeException
     *             {@link SystemException}が発生した場合
     */
    public static Transaction getTransaction(final TransactionManager tm)
            throws SystemRuntimeException {
        assertArgumentNotNull("tm", tm);

        try {
            return tm.getTransaction();
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * トランザクションがアクティブかどうか返します。
     * 
     * @param tm
     *            トランザクションマネージャ。{@literal null}であってはいけません
     * @return トランザクションがアクティブかどうか
     */
    public static boolean isActive(final TransactionManager tm) {
        assertArgumentNotNull("tm", tm);
        return getStatus(tm) != Status.STATUS_NO_TRANSACTION;
    }

    /**
     * ステータスを返します。
     * 
     * @param tm
     *            トランザクションマネージャ。{@literal null}であってはいけません
     * @return ステータス
     * @throws SystemRuntimeException
     *             {@link SystemException}が発生した場合
     */
    public static int getStatus(final TransactionManager tm)
            throws SystemRuntimeException {
        assertArgumentNotNull("tm", tm);

        try {
            return tm.getStatus();
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * ロールバックオンリーに設定します。
     * 
     * @param tm
     *            トランザクションマネージャ。{@literal null}であってはいけません
     * @throws SystemRuntimeException
     *             {@link SystemException}が発生した場合
     */
    public static void setRollbackOnly(final TransactionManager tm)
            throws SystemRuntimeException {
        assertArgumentNotNull("tm", tm);

        try {
            tm.setRollbackOnly();
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

}
