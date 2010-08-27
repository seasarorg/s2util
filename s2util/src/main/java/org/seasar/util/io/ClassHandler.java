package org.seasar.util.io;

/**
 * クラスを横断して処理するためのハンドラです。
 * 
 * @author koichik
 */
public interface ClassHandler {

    /**
     * クラスを処理します。
     * 
     * @param packageName
     *            パッケージ名
     * @param shortClassName
     *            クラスの単純名
     */
    void processClass(String packageName, String shortClassName);

}
