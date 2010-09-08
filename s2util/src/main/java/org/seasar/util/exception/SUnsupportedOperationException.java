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

/**
 * {@link UnsupportedOperationException}をラップする例外です。
 * 
 * @author wyukawa
 */
public class SUnsupportedOperationException extends
        UnsupportedOperationException {

    private static final long serialVersionUID = -6732367317955522602L;

    /**
     * {@link SUnsupportedOperationException}を作成します。
     */
    public SUnsupportedOperationException() {
        super();
    }

    /**
     * {@link SUnsupportedOperationException}を作成します。
     * 
     * @param message
     *            メッセージ
     */
    public SUnsupportedOperationException(String message) {
        super(message);
    }

    /**
     * {@link SUnsupportedOperationException}を作成します。
     * 
     * @param message
     *            メッセージ
     * @param cause
     *            元の例外
     */
    public SUnsupportedOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * {@link SUnsupportedOperationException}を作成します。
     * 
     * @param cause
     *            元の例外
     */
    public SUnsupportedOperationException(Throwable cause) {
        super(cause);
    }

}