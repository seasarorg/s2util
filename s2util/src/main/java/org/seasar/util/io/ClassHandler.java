package org.seasar.util.io;

/**
 * クラスを処理するハンドラのインターフェースです。
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
