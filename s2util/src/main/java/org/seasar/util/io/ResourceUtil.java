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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.seasar.util.exception.IORuntimeException;
import org.seasar.util.exception.ResourceNotFoundRuntimeException;
import org.seasar.util.jar.JarFileUtil;
import org.seasar.util.net.URLUtil;

import static org.seasar.util.misc.AssertionUtil.*;

/**
 * リソース用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ResourceUtil {

    /**
     * リソースパスを返します。
     * 
     * @param path
     * @param extension
     * @return リソースパス
     */
    public static String getResourcePath(final String path,
            final String extension) {
        assertArgumentNotNull("path", path);

        if (extension == null) {
            return path;
        }
        final String ext = "." + extension;
        if (path.endsWith(ext)) {
            return path;
        }
        return path.replace('.', '/') + ext;
    }

    /**
     * リソースパスを返します。
     * 
     * @param clazz
     * @return リソースパス
     */
    public static String getResourcePath(final Class<?> clazz) {
        assertArgumentNotNull("clazz", clazz);

        return clazz.getName().replace('.', '/') + ".class";
    }

    /**
     * クラスローダを返します。
     * 
     * @return クラスローダ
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * リソースを返します。
     * 
     * @param path
     * @return リソース
     * @see #getResource(String, String)
     */
    public static URL getResource(final String path) {
        assertArgumentNotEmpty("path", path);

        return getResource(path, null);
    }

    /**
     * リソースを返します。
     * 
     * @param path
     * @param extension
     * @return リソース
     */
    public static URL getResource(final String path, final String extension) {
        assertArgumentNotEmpty("path", path);

        final URL url = getResourceNoException(path, extension);
        if (url != null) {
            return url;
        }
        throw new ResourceNotFoundRuntimeException(getResourcePath(
            path,
            extension));
    }

    /**
     * リソースを返します。見つからなかった場合は<code>null</code>を返します。
     * 
     * @param path
     * @return リソース
     * @see #getResourceNoException(String, String)
     */
    public static URL getResourceNoException(final String path) {
        assertArgumentNotEmpty("path", path);

        return getResourceNoException(path, null);
    }

    /**
     * リソースを返します。見つからなかった場合は<code>null</code>を返します。
     * 
     * @param path
     * @param extension
     * @return リソース
     * @see #getResourceNoException(String, String, ClassLoader)
     */
    public static URL getResourceNoException(final String path,
            final String extension) {
        assertArgumentNotEmpty("path", path);

        return getResourceNoException(path, extension, Thread
            .currentThread()
            .getContextClassLoader());
    }

    /**
     * リソースを返します。見つからなかった場合は<code>null</code>を返します。
     * 
     * @param path
     * @param extension
     * @param loader
     * @return リソース
     * @see #getResourcePath(String, String)
     */
    public static URL getResourceNoException(final String path,
            final String extension, final ClassLoader loader) {
        assertArgumentNotNull("loader", loader);

        if (path == null || loader == null) {
            return null;
        }
        final String p = getResourcePath(path, extension);
        return loader.getResource(p);
    }

    /**
     * リソースをストリームとして返します。
     * 
     * @param path
     * @return ストリーム
     * @see #getResourceAsStream(String, String)
     */
    public static InputStream getResourceAsStream(final String path) {
        assertArgumentNotEmpty("path", path);

        return getResourceAsStream(path, null);
    }

    /**
     * リソースをストリームとして返します。
     * 
     * @param path
     * @param extension
     * @return ストリーム
     * @see #getResource(String, String)
     */
    public static InputStream getResourceAsStream(final String path,
            final String extension) {
        assertArgumentNotEmpty("path", path);

        final URL url = getResource(path, extension);
        return URLUtil.openStream(url);
    }

    /**
     * リソースをストリームとして返します。リソースが見つからなかった場合は<code>null</code>を返します。
     * 
     * @param path
     * @return ストリーム
     * @see #getResourceAsStreamNoException(String, String)
     */
    public static InputStream getResourceAsStreamNoException(final String path) {
        assertArgumentNotEmpty("path", path);

        return getResourceAsStreamNoException(path, null);
    }

    /**
     * リソースをストリームとして返します。リソースが見つからなかった場合は<code>null</code>を返します。
     * 
     * @param path
     * @param extension
     * @return ストリーム
     * @see #getResourceNoException(String, String)
     */
    public static InputStream getResourceAsStreamNoException(final String path,
            final String extension) {
        assertArgumentNotEmpty("path", path);

        final URL url = getResourceNoException(path, extension);
        if (url == null) {
            return null;
        }
        try {
            return url.openStream();
        } catch (final IOException e) {
            return null;
        }
    }

    /**
     * リソースが存在するかどうかを返します。
     * 
     * @param path
     * @return リソースが存在するかどうか
     * @see #getResourceNoException(String)
     */
    public static boolean isExist(final String path) {
        assertArgumentNotEmpty("path", path);

        return getResourceNoException(path) != null;
    }

    /**
     * プロパティファイルを返します。
     * 
     * @param path
     * @return プロパティファイル
     */
    public static Properties getProperties(final String path) {
        assertArgumentNotEmpty("path", path);

        final Properties props = new Properties();
        final InputStream is = getResourceAsStream(path);
        try {
            props.load(is);
            return props;
        } catch (final IOException ex) {
            throw new IORuntimeException(ex);
        } finally {
            CloseableUtil.close(is);
        }
    }

    /**
     * 拡張子を返します。
     * 
     * @param path
     * @return 拡張子
     */
    public static String getExtension(final String path) {
        assertArgumentNotNull("path", path);

        final int extPos = path.lastIndexOf(".");
        if (extPos >= 0) {
            return path.substring(extPos + 1);
        }
        return null;
    }

    /**
     * 拡張子を取り除きます。
     * 
     * @param path
     * @return 取り除いた後の結果
     */
    public static String removeExtension(final String path) {
        assertArgumentNotNull("path", path);

        final int extPos = path.lastIndexOf(".");
        if (extPos >= 0) {
            return path.substring(0, extPos);
        }
        return path;
    }

    /**
     * クラスファイルが置かれているルートディレクトリを返します。
     * 
     * @param clazz
     * @return ルートディレクトリ
     * @see #getBuildDir(String)
     */
    public static File getBuildDir(final Class<?> clazz) {
        assertArgumentNotNull("clazz", clazz);

        return getBuildDir(getResourcePath(clazz));
    }

    /**
     * クラスファイルが置かれているルートディレクトリを返します。
     * 
     * @param path
     * @return ルートディレクトリ
     */
    public static File getBuildDir(final String path) {
        assertArgumentNotEmpty("path", path);

        File dir = null;
        final URL url = getResource(path);
        if ("file".equals(url.getProtocol())) {
            final int num = path.split("/").length;
            dir = new File(getFileName(url));
            for (int i = 0; i < num; ++i) {
                dir = dir.getParentFile();
            }
        } else {
            dir = new File(JarFileUtil.toJarFilePath(url));
        }
        return dir;
    }

    /**
     * 外部形式に変換します。
     * 
     * @param url
     * @return 外部形式
     */
    public static String toExternalForm(final URL url) {
        assertArgumentNotNull("url", url);

        final String s = url.toExternalForm();
        return URLUtil.decode(s, "UTF8");
    }

    /**
     * ファイル名を返します。
     * 
     * @param url
     * @return ファイル名
     */
    public static String getFileName(final URL url) {
        assertArgumentNotNull("url", url);

        final String s = url.getFile();
        return URLUtil.decode(s, "UTF8");
    }

    /**
     * ファイルを返します。
     * 
     * @param url
     * @return ファイル
     */
    public static File getFile(final URL url) {
        assertArgumentNotNull("url", url);

        final File file = new File(getFileName(url));
        if (file != null && file.exists()) {
            return file;
        }
        return null;
    }

    /**
     * リソースをファイルとして返します。
     * 
     * @param path
     * @return ファイル
     * @see #getResourceAsFile(String, String)
     */
    public static File getResourceAsFile(final String path) {
        assertArgumentNotEmpty("path", path);

        return getResourceAsFile(path, null);
    }

    /**
     * リソースをファイルとして返します。
     * 
     * @param path
     * @param extension
     * @return ファイル
     * @see #getFile(URL)
     */
    public static File getResourceAsFile(final String path,
            final String extension) {
        assertArgumentNotEmpty("path", path);

        return getFile(getResource(path, extension));
    }

    /**
     * リソースをファイルとして返します。リソースが見つからない場合は<code>null</code>を返します。
     * 
     * @param clazz
     * @return ファイル
     * @see #getResourceAsFileNoException(String)
     */
    public static File getResourceAsFileNoException(final Class<?> clazz) {
        assertArgumentNotNull("clazz", clazz);

        return getResourceAsFileNoException(getResourcePath(clazz));
    }

    /**
     * リソースをファイルとして返します。リソースが見つからない場合は<code>null</code>を返します。
     * 
     * @param path
     * @return ファイル
     * @see #getResourceNoException(String)
     */
    public static File getResourceAsFileNoException(final String path) {
        assertArgumentNotEmpty("path", path);

        final URL url = getResourceNoException(path);
        if (url == null) {
            return null;
        }
        return getFile(url);
    }

    /**
     * パスを変換します。
     * 
     * @param path
     * @param clazz
     * @return 変換された結果
     */
    public static String convertPath(final String path, final Class<?> clazz) {
        assertArgumentNotEmpty("path", path);

        if (isExist(path)) {
            return path;
        }
        final String prefix =
            clazz.getName().replace('.', '/').replaceFirst("/[^/]+$", "");
        final String extendedPath = prefix + "/" + path;
        if (ResourceUtil.getResourceNoException(extendedPath) != null) {
            return extendedPath;
        }
        return path;
    }

}
