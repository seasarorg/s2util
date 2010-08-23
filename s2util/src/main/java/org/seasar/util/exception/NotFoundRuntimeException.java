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

import javassist.NotFoundException;

/**
 * <code>NotFoundException</code>をラップする例外です。
 * 
 * @author koichik
 */
public class NotFoundRuntimeException extends SRuntimeException {

    private static final long serialVersionUID = 1960782343618833636L;

    /**
     * {@link NotFoundRuntimeException}を作成します。
     * 
     * @param cause
     *            原因となった例外
     */
    public NotFoundRuntimeException(final NotFoundException cause) {
        super("EUTL0017", cause);
        initCause(cause);
    }

    @Override
    public NotFoundRuntimeException initCause(final Throwable cause) {
        return (NotFoundRuntimeException) super.initCause(cause);
    }

}
