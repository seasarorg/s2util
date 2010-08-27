package org.seasar.util.io;

import java.io.InputStream;

/**
 * リソースを処理するインターフェースです。
 * 
 * @author taedium
 */
public interface ResourceHandler {

    /**
     * リソースを処理します。
     * 
     * @param path
     *            パス
     * @param is
     *            リソースを読み込むための{@link InputStream}
     */
    void processResource(String path, InputStream is);

}
