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
package org.seasar.util.message;

import java.util.Properties;

/**
 * {@link MessageResourceBundle}の実装クラスです。
 * 
 * @author shot
 */
public class MessageResourceBundleImpl implements MessageResourceBundle {

    /** プロパティ */
    protected Properties prop;

    /** 親のリソースバンドル */
    protected MessageResourceBundle parent;

    /**
     * {@link MessageResourceBundleImpl}を作成します。
     * 
     * @param prop
     */
    public MessageResourceBundleImpl(final Properties prop) {
        this.prop = prop;
    }

    /**
     * {@link MessageResourceBundleImpl}を作成します。
     * 
     * @param prop
     * @param parent
     */
    public MessageResourceBundleImpl(final Properties prop,
            final MessageResourceBundle parent) {
        this(prop);
        setParent(parent);
    }

    @Override
    public String get(final String key) {
        if (key == null) {
            return null;
        }
        if (prop.containsKey(key)) {
            return prop.getProperty(key);
        }
        return (parent != null) ? parent.get(key) : null;
    }

    @Override
    public MessageResourceBundle getParent() {
        return parent;
    }

    @Override
    public void setParent(final MessageResourceBundle parent) {
        this.parent = parent;
    }

}
