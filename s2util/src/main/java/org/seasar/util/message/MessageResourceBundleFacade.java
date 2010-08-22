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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.seasar.framework.util.AssertionUtil;
import org.seasar.util.io.FileInputStreamUtil;
import org.seasar.util.io.InputStreamUtil;
import org.seasar.util.io.PropertiesUtil;
import org.seasar.util.io.ResourceUtil;
import org.seasar.util.io.URLUtil;

/**
 * {@link MessageResourceBundle}をキャッシュするクラスです。
 * 
 * @author shot
 * @author higa
 */
public class MessageResourceBundleFacade {

    /** Eagerモード */
    protected static volatile boolean eager;

    /** プロパティファイル */
    protected File file;

    /** プロパティファイルの最終更新時刻 */
    protected long lastModified;

    /** リソースバンドル */
    protected MessageResourceBundle bundle;

    /** 親のリソースバンドル */
    protected volatile MessageResourceBundleFacade parent;

    /**
     * {@link MessageResourceBundleFacade}を作成します。
     * 
     * @param url
     */
    public MessageResourceBundleFacade(final URL url) {
        setup(url);
    }

    /**
     * 
     */
    protected MessageResourceBundleFacade() {
    }

    /**
     * Eagerモードであれば{@literal true}を返します。
     * 
     * @return Eagerモードであれば{@literal true}
     */
    public static boolean isEager() {
        return eager;
    }

    /**
     * Eagerモードであれば{@literal true}に設定します。
     * 
     * @param eager
     *            Eagerモードであれば{@literal true}
     */
    public static void setEager(final boolean eager) {
        MessageResourceBundleFacade.eager = eager;
    }

    /**
     * {@link MessageResourceBundle}を返します。
     * 
     * @return {@link MessageResourceBundle}
     */
    public synchronized MessageResourceBundle getBundle() {
        if (isModified()) {
            bundle = createBundle(file);
        }
        if (parent != null) {
            bundle.setParent(parent.getBundle());
        }
        return bundle;
    }

    /**
     * 親のリソースバンドルを返します。
     * 
     * @return 親のリソースバンドル
     */
    public MessageResourceBundleFacade getParent() {
        return parent;
    }

    /**
     * 親のリソースバンドルを設定します。
     * 
     * @param parent
     *            親のリソースバンドル
     */
    public void setParent(final MessageResourceBundleFacade parent) {
        this.parent = parent;
    }

    /**
     * 更新されているかどうかを返します。
     * 
     * @return 更新されているかどうか
     */
    protected boolean isModified() {
        if (file != null && file.lastModified() > lastModified) {
            return true;
        }
        return false;
    }

    /**
     * セットアップをします。
     * 
     * @param url
     *            URL
     */
    protected void setup(final URL url) {
        AssertionUtil.assertNotNull("url", url);
        if (isEager()) {
            file = ResourceUtil.getFile(url);
        }
        if (file != null) {
            lastModified = file.lastModified();
            bundle = createBundle(file);
        } else {
            bundle = createBundle(url);
        }
        if (parent != null) {
            bundle.setParent(parent.getBundle());
        }
    }

    /**
     * メッセージリソースバンドルを作成します。
     * 
     * @param file
     *            ファイル
     * @return メッセージリソースバンドル
     */
    protected static MessageResourceBundle createBundle(final File file) {
        return new MessageResourceBundleImpl(createProperties(file));
    }

    /**
     * メッセージリソースバンドルを作成します。
     * 
     * @param url
     *            URL
     * @return メッセージリソースバンドル
     */
    protected static MessageResourceBundle createBundle(final URL url) {
        return new MessageResourceBundleImpl(createProperties(url));
    }

    /**
     * {@link Properties}を作成します。
     * 
     * @param file
     *            ファイル
     * @return {@link Properties}
     */
    protected static Properties createProperties(final File file) {
        return createProperties(FileInputStreamUtil.create(file));
    }

    /**
     * {@link Properties}を作成します。
     * 
     * @param url
     *            URL
     * @return {@link Properties}
     */
    protected static Properties createProperties(final URL url) {
        return createProperties(URLUtil.openStream(url));
    }

    /**
     * {@link Properties}を作成します。
     * 
     * @param is
     *            入力ストリーム
     * @return {@link Properties}
     */
    protected static Properties createProperties(InputStream is) {
        AssertionUtil.assertNotNull("is", is);
        if (!(is instanceof BufferedInputStream)) {
            is = new BufferedInputStream(is);
        }
        try {
            final Properties properties = new Properties();
            PropertiesUtil.load(properties, is);
            return properties;
        } finally {
            InputStreamUtil.close(is);
        }
    }

}
