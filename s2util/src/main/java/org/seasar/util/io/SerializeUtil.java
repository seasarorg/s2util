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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.seasar.util.exception.ClassNotFoundRuntimeException;
import org.seasar.util.exception.IORuntimeException;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * オブジェクトをシリアライズするためのユーティリティです。
 * 
 * @author higa
 */
public abstract class SerializeUtil {

    private static final int BYTE_ARRAY_SIZE = 8 * 1024;

    /**
     * オブジェクトをシリアライズできるかテストします。
     * 
     * @param obj
     * @return シリアライズして復元したオブジェクト
     * @throws IORuntimeException
     * @throws ClassNotFoundRuntimeException
     */
    public static Object serialize(final Object obj) throws IORuntimeException,
            ClassNotFoundRuntimeException {
        assertArgumentNotNull("obj", obj);

        final byte[] binary = fromObjectToBinary(obj);
        return fromBinaryToObject(binary);
    }

    /**
     * オブジェクトをbyteの配列に変換します。
     * 
     * @param obj
     * @return オブジェクトのbyte配列
     */
    public static byte[] fromObjectToBinary(final Object obj) {
        assertArgumentNotNull("obj", obj);

        try {
            final ByteArrayOutputStream baos =
                new ByteArrayOutputStream(BYTE_ARRAY_SIZE);
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            try {
                oos.writeObject(obj);
            } finally {
                oos.close();
            }
            return baos.toByteArray();
        } catch (final IOException ex) {
            throw new IORuntimeException(ex);
        }
    }

    /**
     * byteの配列をオブジェクトに変換します。
     * 
     * @param bytes
     * @return 復元したオブジェクト
     */
    public static Object fromBinaryToObject(final byte[] bytes) {
        assertArgumentNotEmpty("bytes", bytes);

        try {
            final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            final ObjectInputStream ois = new ObjectInputStream(bais);
            try {
                return ois.readObject();
            } finally {
                CloseableUtil.close(ois);
            }
        } catch (final IOException ex) {
            throw new IORuntimeException(ex);
        } catch (final ClassNotFoundException ex) {
            throw new ClassNotFoundRuntimeException(ex);
        }
    }

}
