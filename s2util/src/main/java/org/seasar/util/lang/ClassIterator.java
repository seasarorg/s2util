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
package org.seasar.util.lang;

import java.util.Iterator;

import org.seasar.util.exception.SNoSuchElementException;
import org.seasar.util.exception.SUnsupportedOperationException;
import org.seasar.util.message.MessageFormatter;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * クラスの継承階層を親クラスに向かって反復する{@link Iterator}です。
 * <p>
 * 次のように使います．
 * </p>
 * 
 * <pre>
 * import static org.seasar.util.lang.ClassIterator.*;
 * 
 * Class<?> someClass = ...;
 * for (Class<?> clazz : iterable(someClass)) {
 *     ...
 * }
 * </pre>
 * <p>
 * デフォルトでは{@link Object}クラスも反復の対象となります。 反復の対象に{@link Object}を含めたくない場合は、
 * {@link #iterable(Class, boolean)}または{@link #ClassIterator(Class, boolean)}
 * の第2引数に{@literal false}を指定します。
 * </p>
 * 
 * @author koichik
 */
public class ClassIterator implements Iterator<Class<?>> {

    /** クラス */
    protected Class<?> clazz;

    /** {@link Object}クラスも反復する場合は {@literal true} */
    protected final boolean includeObject;

    /**
     * for each構文で使用するために{@link ClassIterator}をラップした{@link Iterable}を返します。
     * 
     * @param clazz
     *            クラス
     * @return {@link ClassIterator}をラップした{@link Iterable}
     */
    public static Iterable<Class<?>> iterable(final Class<?> clazz) {
        return iterable(clazz, true);
    }

    /**
     * for each構文で使用するために{@link ClassIterator}をラップした{@link Iterable}を返します。
     * 
     * @param clazz
     *            クラス
     * @param includeObject
     *            {@link Object}クラスも反復する場合は {@literal true}
     * @return {@link ClassIterator}をラップした{@link Iterable}
     */
    public static Iterable<Class<?>> iterable(final Class<?> clazz,
            final boolean includeObject) {
        return new Iterable<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                return new ClassIterator(clazz, includeObject);
            }
        };
    }

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     *            クラス
     */
    public ClassIterator(final Class<?> clazz) {
        this(clazz, true);
    }

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     *            クラス
     * @param includeObject
     *            {@link Object}クラスも反復する場合は {@literal true}
     */
    public ClassIterator(final Class<?> clazz, final boolean includeObject) {
        assertArgumentNotNull("clazz", clazz);
        assertArgument(
            "clazz",
            !clazz.isInterface(),
            MessageFormatter.getSimpleMessage("EUTL0103", clazz));

        this.clazz = clazz;
        this.includeObject = includeObject;
    }

    @Override
    public boolean hasNext() {
        if (!includeObject && clazz == Object.class) {
            return false;
        }
        return clazz != null;
    }

    @Override
    public Class<?> next() {
        if (!hasNext()) {
            throw new SNoSuchElementException();
        }
        final Class<?> result = clazz;
        clazz = clazz.getSuperclass();
        return result;
    }

    @Override
    public void remove() {
        throw new SUnsupportedOperationException("remove");
    }

}
