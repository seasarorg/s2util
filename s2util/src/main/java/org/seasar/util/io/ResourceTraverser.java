package org.seasar.util.io;

/**
 * リソースの集まりを表すオブジェクトです。
 * 
 * @author koichik
 */
public interface ResourceTraverser {

    /**
     * 指定されたクラス名に対応するクラスファイルがこのインスタンスが扱うリソースの中に存在すれば<code>true</code>を返します。
     * <p>
     * インスタンス構築時にルートパッケージが指定されている場合、 指定されたクラス名はルートパッケージからの相対名として解釈されます。
     * </p>
     * 
     * @param className
     *            クラス名
     * @return 指定されたクラス名に対応するクラスファイルがこのインスタンスが扱うリソースの中に存在すれば <code>true</code>
     */
    boolean isExistClass(final String className);

    /**
     * このインスタンスが扱うリソースの中に存在するクラスを探して
     * {@link ClassHandler#processClass(String, String) ハンドラ}をコールバックします。
     * <p>
     * インスタンス構築時にルートパッケージが指定されている場合は、 ルートパッケージ以下のクラスのみが対象となります。
     * </p>
     * 
     * @param handler
     *            クラスを処理するハンドラ
     */
    void forEach(ClassHandler handler);

    /**
     * このインスタンスが扱うリソースを探して
     * {@link ResourceHandler#processResource(String, java.io.InputStream) ハンドラ}
     * をコールバックします。
     * <p>
     * インスタンス構築時にルートディレクトリが指定されている場合は、 ルートディレクトリ以下のリソースのみが対象となります。
     * </p>
     * 
     * @param handler
     *            リソースを処理するハンドラ
     */
    void forEach(ResourceHandler handler);

    /**
     * リソースの後処理を行います。
     */
    void close();

}
