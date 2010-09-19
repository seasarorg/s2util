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

/**
 * 
 * {@link Object}用のユーティリティクラスです。
 * 
 * @author wyukawa
 * 
 */
public abstract class ObjectUtil {

    /**
     * 
     * オブジェクトが等しいかどうか返します。どちらも<code>null</code>の場合は、<code>true</code>を返します。
     * 
     * <p>
     * 次のように使います．
     * </p>
     * 
     * <pre>
     * ObjectUtil.equals(null, null)                  = true
     * ObjectUtil.equals(null, "")                    = false
     * ObjectUtil.equals("", null)                    = false
     * ObjectUtil.equals("", "")                      = true
     * ObjectUtil.equals(Boolean.TRUE, null)          = false
     * ObjectUtil.equals(Boolean.TRUE, "true")        = false
     * ObjectUtil.equals(Boolean.TRUE, Boolean.TRUE)  = true
     * ObjectUtil.equals(Boolean.TRUE, Boolean.FALSE) = false
     * </pre>
     * 
     * @param object1
     *            オブジェクト(<code>null</code>可)
     * @param object2
     *            オブジェクト(<code>null</code>可)
     * @return 引数の2つのオブジェクトが等しい場合は<code>true</code>を返します。
     * 
     */
    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

}
