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
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.seasar.util.jar.JarFileUtil;
import org.seasar.util.lang.ClassLoaderUtil;
import org.seasar.util.lang.ClassUtil;
import org.seasar.util.lang.StringUtil;
import org.seasar.util.log.Logger;
import org.seasar.util.zip.ZipFileUtil;
import org.seasar.util.zip.ZipInputStreamUtil;

import static org.seasar.util.collection.ArrayUtil.*;
import static org.seasar.util.collection.CollectionsUtil.*;
import static org.seasar.util.misc.AssertionUtil.*;

/**
 * ファイルシステム上やJarファイル中に展開されているリソースの集まりを扱うユーティリティクラスです。
 * <p>
 * 次のプロトコルをサポートしています。
 * </p>
 * <ul>
 * <li><code>file</code></li>
 * <li><code>jar</code></li>
 * <li><code>wsjar</code>(WebShpere独自プロトコル、<code>jar</code>の別名)</li>
 * <li><code>zip</code>(WebLogic独自プロトコル)</li>
 * <li><code>code-source</code>(Oracle AS(OC4J)独自プロトコル)</li>
 * <li><code>vfsfile</code>(JBossAS5独自プロトコル、<code>file</code>の別名)</li>
 * <li><code>vfszip</code>(JBossAS5独自プロトコル)</li>
 * </ul>
 * 
 * @author koichik
 * @see URLUtil#toCanonicalProtocol(String)
 */
public abstract class ResourceTraverserUtil {

    /** 空の{@link ResourceTraverser}の配列です。 */
    protected static final ResourceTraverser[] EMPTY_ARRAY =
        new ResourceTraverser[0];

    private static final Logger logger = Logger
        .getLogger(ResourceTraverserUtil.class);

    /** URLのプロトコルをキー、{@link ResourceTraverserFactory}を値とするマッピングです。 */
    protected static final ConcurrentMap<String, ResourceTraverserFactory> resourcesTypeFactories =
        newConcurrentHashMap();
    static {
        addResourcesFactory("file", new ResourceTraverserFactory() {
            @Override
            public ResourceTraverser create(final URL url,
                    final String rootPackage, final String rootDir) {
                return new FileSystemResourceTraverser(
                    getBaseDir(url, rootDir),
                    rootPackage,
                    rootDir);
            }
        });
        addResourcesFactory("jar", new ResourceTraverserFactory() {
            @Override
            public ResourceTraverser create(final URL url,
                    final String rootPackage, final String rootDir) {
                return new JarFileResourceTraverser(url, rootPackage, rootDir);
            }
        });
        addResourcesFactory("zip", new ResourceTraverserFactory() {
            @Override
            public ResourceTraverser create(final URL url,
                    final String rootPackage, final String rootDir) {
                return new JarFileResourceTraverser(
                    JarFileUtil.create(new File(ZipFileUtil.toZipFilePath(url))),
                    rootPackage,
                    rootDir);
            }
        });
        addResourcesFactory("code-source", new ResourceTraverserFactory() {
            @Override
            public ResourceTraverser create(final URL url,
                    final String rootPackage, final String rootDir) {
                return new JarFileResourceTraverser(URLUtil.create("jar:file:"
                    + url.getPath()), rootPackage, rootDir);
            }
        });
        addResourcesFactory("vfszip", new ResourceTraverserFactory() {
            @Override
            public ResourceTraverser create(final URL url,
                    final String rootPackage, final String rootDir) {
                return new VfsZipResourceTraverser(url, rootPackage, rootDir);
            }
        });
    }

    /**
     * {@link ResourceTraverserFactory}を追加します。
     * 
     * @param protocol
     *            URLのプロトコル
     * @param factory
     *            プロトコルに対応する{@link ResourceTraverser}のファクトリ
     */
    public static void addResourcesFactory(final String protocol,
            final ResourceTraverserFactory factory) {
        assertArgumentNotEmpty("protocol", protocol);
        assertArgumentNotNull("factory", factory);

        resourcesTypeFactories.put(protocol, factory);
    }

    /**
     * 指定のクラスを基点とするリソースの集まりを扱う{@link ResourceTraverser}を返します。
     * <p>
     * このメソッドが返す{@link ResourceTraverser}は、指定されたクラスをFQNで参照可能なパスをルートとします。
     * 例えば指定されたクラスが <code>foo.Bar</code>で、そのクラスファイルが
     * <code>classes/foo/Bar.class</code>の場合、 このメソッドが返す
     * {@link ResourceTraverser}は<code>classes</code>ディレクトリ以下のリソースの集合を扱います。
     * </p>
     * 
     * @param referenceClass
     *            基点となるクラス
     * @return 指定のクラスを基点とするリソースの集まりを扱う{@link ResourceTraverser}
     */
    public static ResourceTraverser getResourceTraverser(
            final Class<?> referenceClass) {
        assertArgumentNotNull("referenceClass", referenceClass);

        final URL url =
            ResourceUtil.getResource(toClassFile(referenceClass.getName()));
        final String path[] = referenceClass.getName().split("\\.");
        String baseUrl = url.toExternalForm();
        for (int i = 0; i < path.length; ++i) {
            final int pos = baseUrl.lastIndexOf('/');
            baseUrl = baseUrl.substring(0, pos);
        }
        return getResourceTraverser(URLUtil.create(baseUrl + '/'), null, null);
    }

    /**
     * 指定のディレクトリを基点とするリソースの集まりを扱う{@link ResourceTraverser}を返します。
     * 
     * @param rootDir
     *            ルートディレクトリ
     * @return 指定のディレクトリを基点とするリソースの集まりを扱う{@link ResourceTraverser}
     */
    public static ResourceTraverser getResourceTraverser(final String rootDir) {
        assertArgumentNotEmpty("rootDir", rootDir);

        final URL url =
            ResourceUtil.getResource(rootDir.endsWith("/") ? rootDir
                : rootDir + '/');
        return getResourceTraverser(url, null, rootDir);
    }

    /**
     * 指定のルートパッケージを基点とするリソースの集まりを扱う{@link ResourceTraverser}の配列を返します。
     * 
     * @param rootPackage
     *            ルートパッケージ
     * @return 指定のルートパッケージを基点とするリソースの集まりを扱う{@link ResourceTraverser}の配列
     */
    public static ResourceTraverser[] getResourceTraversers(
            final String rootPackage) {
        if (StringUtil.isEmpty(rootPackage)) {
            return EMPTY_ARRAY;
        }

        final String baseName = toDirectoryName(rootPackage);
        final List<ResourceTraverser> list = new ArrayList<ResourceTraverser>();
        for (final Iterator<URL> it = ClassLoaderUtil.getResources(baseName); it
            .hasNext();) {
            final URL url = it.next();
            final ResourceTraverser resourcesType =
                getResourceTraverser(url, rootPackage, baseName);
            if (resourcesType != null) {
                list.add(resourcesType);
            }
        }
        if (list.isEmpty()) {
            logger.log("WUTL0014", new Object[] { rootPackage });
            return EMPTY_ARRAY;
        }
        return list.toArray(new ResourceTraverser[list.size()]);
    }

    /**
     * URLを扱う{@link ResourceTraverser}を作成して返します。
     * <p>
     * URLのプロトコルが未知の場合は<code>null</code>を返します。
     * </p>
     * 
     * @param url
     *            リソースのURL
     * @param rootPackage
     *            ルートパッケージ
     * @param rootDir
     *            ルートディレクトリ
     * @return URLを扱う{@link ResourceTraverser}
     */
    protected static ResourceTraverser getResourceTraverser(final URL url,
            final String rootPackage, final String rootDir) {
        assertArgumentNotNull("url", url);

        final ResourceTraverserFactory factory =
            resourcesTypeFactories.get(URLUtil.toCanonicalProtocol(url
                .getProtocol()));
        if (factory != null) {
            return factory.create(url, rootPackage, rootDir);
        }
        logger.log("WUTL0013", asArray(rootPackage, url));
        return null;
    }

    /**
     * パッケージ名をディレクトリ名に変換して返します。
     * 
     * @param packageName
     *            パッケージ名
     * @return ディレクトリ名
     */
    protected static String toDirectoryName(final String packageName) {
        if (StringUtil.isEmpty(packageName)) {
            return null;
        }
        return packageName.replace('.', '/') + '/';
    }

    /**
     * クラス名をクラスファイルのパス名に変換して返します。
     * 
     * @param className
     *            クラス名
     * @return クラスファイルのパス名
     */
    protected static String toClassFile(final String className) {
        assertArgumentNotNull("className", className);

        return className.replace('.', '/') + ".class";
    }

    /**
     * ファイルを表すURLからルートパッケージの上位となるベースディレクトリを求めて返します。
     * 
     * @param url
     *            ファイルを表すURL
     * @param baseName
     *            ベース名
     * @return ルートパッケージの上位となるベースディレクトリ
     */
    protected static File getBaseDir(final URL url, final String baseName) {
        assertArgumentNotNull("url", url);

        File file = URLUtil.toFile(url);
        final String[] paths = StringUtil.split(baseName, "/");
        for (int i = 0; i < paths.length; ++i) {
            file = file.getParentFile();
        }
        return file;
    }

    /**
     * {@link ResourceTraverser}のインスタンスを作成するファクトリです。
     * 
     * @author koichik
     */
    public interface ResourceTraverserFactory {
        /**
         * {@link ResourceTraverser}のインスタンスを作成して返します。
         * 
         * @param url
         *            リソースを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         * @return URLで表されたリソースを扱う{@link ResourceTraverser}
         */
        ResourceTraverser create(URL url, String rootPackage, String rootDir);
    }

    /**
     * ファイルシステム上のリソースの集まりを扱うオブジェクトです。
     * 
     * @author koichik
     */
    public static class FileSystemResourceTraverser implements
            ResourceTraverser {

        /** ベースディレクトリです。 */
        protected final File baseDir;

        /** ルートパッケージです。 */
        protected final String rootPackage;

        /** ルートディレクトリです。 */
        protected final String rootDir;

        /**
         * インスタンスを構築します。
         * 
         * @param baseDir
         *            ベースディレクトリ
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public FileSystemResourceTraverser(final File baseDir,
                final String rootPackage, final String rootDir) {
            this.baseDir = baseDir;
            this.rootPackage = rootPackage;
            this.rootDir = rootDir;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            ディレクトリを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public FileSystemResourceTraverser(final URL url,
                final String rootPackage, final String rootDir) {
            this(URLUtil.toFile(url), rootPackage, rootDir);
        }

        @Override
        public boolean isExistClass(final String className) {
            final File file =
                new File(baseDir, toClassFile(ClassUtil.concatName(
                    rootPackage,
                    className)));
            return file.exists();
        }

        @Override
        public void forEach(final ClassHandler handler) {
            ClassTraversalUtil.forEach(baseDir, rootPackage, handler);
        }

        @Override
        public void forEach(final ResourceHandler handler) {
            ResourceTraversalUtil.forEach(baseDir, rootDir, handler);
        }

        @Override
        public void close() {
        }

    }

    /**
     * Jarファイル中のリソースの集まりを扱うオブジェクトです。
     * 
     * @author koichik
     */
    public static class JarFileResourceTraverser implements ResourceTraverser {

        /** Jarファイルです。 */
        protected final JarFile jarFile;

        /** ルートパッケージです。 */
        protected final String rootPackage;

        /** ルートディレクトリです。 */
        final protected String rootDir;

        /**
         * インスタンスを構築します。
         * 
         * @param jarFile
         *            Jarファイル
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public JarFileResourceTraverser(final JarFile jarFile,
                final String rootPackage, final String rootDir) {
            this.jarFile = jarFile;
            this.rootPackage = rootPackage;
            this.rootDir = rootDir;
        }

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            Jarファイルを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public JarFileResourceTraverser(final URL url,
                final String rootPackage, final String rootDir) {
            this(JarFileUtil.toJarFile(url), rootPackage, rootDir);
        }

        @Override
        public boolean isExistClass(final String className) {
            return jarFile.getEntry(toClassFile(ClassUtil.concatName(
                rootPackage,
                className))) != null;
        }

        @Override
        public void forEach(final ClassHandler handler) {
            ClassTraversalUtil.forEach(jarFile, new ClassHandler() {
                @Override
                public void processClass(final String packageName,
                        final String shortClassName) {
                    if (rootPackage == null
                        || (packageName != null && packageName
                            .startsWith(rootPackage))) {
                        handler.processClass(packageName, shortClassName);
                    }
                }
            });
        }

        @Override
        public void forEach(final ResourceHandler handler) {
            ResourceTraversalUtil.forEach(jarFile, new ResourceHandler() {
                @Override
                public void processResource(final String path,
                        final InputStream is) {
                    if (rootDir == null || path.startsWith(rootDir)) {
                        handler.processResource(path, is);
                    }
                }
            });
        }

        @Override
        public void close() {
            JarFileUtil.close(jarFile);
        }

    }

    /**
     * JBossAS5のvfszipプロトコルで表されるリソースの集まりを扱うオブジェクトです。
     * 
     * @author koichik
     */
    public static class VfsZipResourceTraverser implements ResourceTraverser {

        /** WAR内の.classファイルの接頭辞です。 */
        protected static final String WAR_CLASSES_PREFIX = "/WEB-INF/CLASSES/";

        /** ルートパッケージです。 */
        protected final String rootPackage;

        /** ルートディレクトリです。 */
        final protected String rootDir;

        /** ZipのURLです。 */
        protected final URL zipUrl;

        /** Zip内のエントリの接頭辞です。 */
        protected final String prefix;

        /** Zip内のエントリ名の{@link Set}です。 */
        protected final Set<String> entryNames = new HashSet<String>();

        /**
         * インスタンスを構築します。
         * 
         * @param url
         *            ルートを表すURL
         * @param rootPackage
         *            ルートパッケージ
         * @param rootDir
         *            ルートディレクトリ
         */
        public VfsZipResourceTraverser(final URL url, final String rootPackage,
                final String rootDir) {
            URL zipUrl = url;
            String prefix = "";
            if (rootPackage != null) {
                final String[] paths = rootPackage.split("\\.");
                for (int i = 0; i < paths.length; ++i) {
                    zipUrl = URLUtil.create(zipUrl, "..");
                }
            }
            loadFromZip(zipUrl);
            if (entryNames.isEmpty()) {
                final String zipUrlString = zipUrl.toExternalForm();
                if (zipUrlString.toUpperCase().endsWith(WAR_CLASSES_PREFIX)) {
                    final URL warUrl = URLUtil.create(zipUrl, "../..");
                    final String path = warUrl.getPath();
                    zipUrl =
                        FileUtil.toURL(new File(path.substring(
                            0,
                            path.length() - 1)));
                    prefix =
                        zipUrlString
                            .substring(warUrl.toExternalForm().length());
                    loadFromZip(zipUrl);
                }
            }

            this.rootPackage = rootPackage;
            this.rootDir = rootDir;
            this.zipUrl = zipUrl;
            this.prefix = prefix;
        }

        private void loadFromZip(final URL zipUrl) {
            final ZipInputStream zis =
                new ZipInputStream(URLUtil.openStream(zipUrl));
            try {
                ZipEntry entry = null;
                while ((entry = ZipInputStreamUtil.getNextEntry(zis)) != null) {
                    entryNames.add(entry.getName());
                    ZipInputStreamUtil.closeEntry(zis);
                }
            } finally {
                CloseableUtil.close(zis);
            }
        }

        @Override
        public boolean isExistClass(final String className) {
            final String entryName =
                prefix
                    + toClassFile(ClassUtil.concatName(rootPackage, className));
            return entryNames.contains(entryName);
        }

        @Override
        public void forEach(final ClassHandler handler) {
            final ZipInputStream zis =
                new ZipInputStream(URLUtil.openStream(zipUrl));
            try {
                ClassTraversalUtil.forEach(zis, prefix, new ClassHandler() {
                    @Override
                    public void processClass(final String packageName,
                            final String shortClassName) {
                        if (rootPackage == null
                            || (packageName != null && packageName
                                .startsWith(rootPackage))) {
                            handler.processClass(packageName, shortClassName);
                        }
                    }
                });
            } finally {
                CloseableUtil.close(zis);
            }
        }

        @Override
        public void forEach(final ResourceHandler handler) {
            final ZipInputStream zis =
                new ZipInputStream(URLUtil.openStream(zipUrl));
            try {
                ResourceTraversalUtil.forEach(
                    zis,
                    prefix,
                    new ResourceHandler() {
                        @Override
                        public void processResource(final String path,
                                final InputStream is) {
                            if (rootDir == null || path.startsWith(rootDir)) {
                                handler.processResource(path, is);
                            }
                        }
                    });
            } finally {
                CloseableUtil.close(zis);
            }
        }

        @Override
        public void close() {
        }

    }

}
