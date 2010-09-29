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
package org.seasar.util.transaction;

import javax.transaction.RollbackException;
import javax.transaction.Synchronization;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.xa.XAResource;

import org.seasar.util.exception.RollbackRuntimeException;
import org.seasar.util.exception.SystemRuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * {@link Transaction}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class TransactionUtil {

    /**
     * トランザクションのステータスを返します。
     * 
     * @param tx
     *            トランザクション。{@literal null}であってはいけません
     * @return トランザクションのステータス
     */
    public static int getStatus(final Transaction tx) {
        assertArgumentNotNull("tx", tx);

        try {
            return tx.getStatus();
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * トランザクションに参加します。
     * 
     * @param tx
     *            トランザクション。{@literal null}であってはいけません
     * @param xaResource
     *            XAリソース。{@literal null}であってはいけません
     */
    public static void enlistResource(final Transaction tx,
            final XAResource xaResource) {
        assertArgumentNotNull("tx", tx);
        assertArgumentNotNull("xaResource", xaResource);

        try {
            tx.enlistResource(xaResource);
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        } catch (final RollbackException e) {
            throw new RollbackRuntimeException(e);
        }
    }

    /**
     * {@link Synchronization}を登録します。
     * 
     * @param tx
     *            トランザクション。{@literal null}であってはいけません
     * @param sync
     *            {@link Synchronization}。{@literal null}であってはいけません
     */
    public static void registerSynchronization(final Transaction tx,
            final Synchronization sync) {
        assertArgumentNotNull("tx", tx);
        assertArgumentNotNull("sync", sync);

        try {
            tx.registerSynchronization(sync);
        } catch (final SystemException e) {
            throw new SystemRuntimeException(e);
        } catch (final RollbackException e) {
            throw new RollbackRuntimeException(e);
        }
    }

}
