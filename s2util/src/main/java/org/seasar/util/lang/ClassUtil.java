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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.seasar.framework.util.StringUtil;
import org.seasar.util.exception.ClassNotFoundRuntimeException;
import org.seasar.util.exception.IllegalAccessRuntimeException;
import org.seasar.util.exception.InstantiationRuntimeException;
import org.seasar.util.exception.NoSuchConstructorRuntimeException;
import org.seasar.util.exception.NoSuchFieldRuntimeException;
import org.seasar.util.exception.NoSuchMethodRuntimeException;

import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * {@link Class}用のユーティリティクラスです。
 * 
 * @author higa
 */
public abstract class ClassUtil {

    /** ラッパー型からプリミティブ型へのマップ */
    protected static final Map<Class<?>, Class<?>> wrapperToPrimitiveMap =
        newHashMap();

    /** プリミティブ型からラッパー型へのマップ */
    protected static final Map<Class<?>, Class<?>> primitiveToWrapperMap =
        newHashMap();

    /** プリミティブ型の名前からクラスへのマップ */
    protected static final Map<String, Class<?>> primitiveNameToClassMap =
        newHashMap();

    static {
        wrapperToPrimitiveMap.put(Boolean.class, Boolean.TYPE);
        wrapperToPrimitiveMap.put(Character.class, Character.TYPE);
        wrapperToPrimitiveMap.put(Byte.class, Byte.TYPE);
        wrapperToPrimitiveMap.put(Short.class, Short.TYPE);
        wrapperToPrimitiveMap.put(Integer.class, Integer.TYPE);
        wrapperToPrimitiveMap.put(Long.class, Long.TYPE);
        wrapperToPrimitiveMap.put(Float.class, Float.TYPE);
        wrapperToPrimitiveMap.put(Double.class, Double.TYPE);

        primitiveToWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveToWrapperMap.put(Character.TYPE, Character.class);
        primitiveToWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveToWrapperMap.put(Short.TYPE, Short.class);
        primitiveToWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveToWrapperMap.put(Long.TYPE, Long.class);
        primitiveToWrapperMap.put(Float.TYPE, Float.class);
        primitiveToWrapperMap.put(Double.TYPE, Double.class);

        primitiveNameToClassMap.put(Boolean.TYPE.getName(), Boolean.TYPE);
        primitiveNameToClassMap.put(Character.TYPE.getName(), Character.TYPE);
        primitiveNameToClassMap.put(Byte.TYPE.getName(), Byte.TYPE);
        primitiveNameToClassMap.put(Short.TYPE.getName(), Short.TYPE);
        primitiveNameToClassMap.put(Integer.TYPE.getName(), Integer.TYPE);
        primitiveNameToClassMap.put(Long.TYPE.getName(), Long.TYPE);
        primitiveNameToClassMap.put(Float.TYPE.getName(), Float.TYPE);
        primitiveNameToClassMap.put(Double.TYPE.getName(), Double.TYPE);
    }

    /**
     * 現在のスレッドのコンテキストクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、
     * {@link Class}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @throws ClassNotFoundRuntimeException
     *             クラスが見つからなかった場合
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static <T> Class<T> forName(final String className)
            throws ClassNotFoundRuntimeException {
        return forName(className, Thread
            .currentThread()
            .getContextClassLoader());
    }

    /**
     * 指定されたクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた{@link Class}
     * オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @param loader
     *            クラスのロード元である必要があるクラスローダ
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @throws ClassNotFoundRuntimeException
     *             クラスが見つからなかった場合
     * @see Class#forName(String, boolean, ClassLoader)
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forName(final String className,
            final ClassLoader loader) throws ClassNotFoundRuntimeException {
        try {
            return (Class<T>) Class.forName(className, true, loader);
        } catch (final ClassNotFoundException e) {
            throw new ClassNotFoundRuntimeException(e);
        }
    }

    /**
     * 現在のスレッドのコンテキストクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、
     * {@link Class}オブジェクトを返します。
     * <p>
     * クラスが見つからなかった場合は{@code null}を返します。
     * </p>
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @see Class#forName(String)
     */
    public static <T> Class<T> forNameNoException(final String className) {
        return forNameNoException(className, Thread
            .currentThread()
            .getContextClassLoader());
    }

    /**
     * 指定されたクラスローダを使って、 指定された文字列名を持つクラスまたはインタフェースに関連付けられた、 {@link Class}
     * オブジェクトを返します。
     * <p>
     * クラスが見つからなかった場合は{@code null}を返します。
     * </p>
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param className
     *            要求するクラスの完全修飾名
     * @param loader
     *            クラスのロード元である必要があるクラスローダ
     * @return 指定された名前を持つクラスの{@link Class}オブジェクト
     * @see Class#forName(String)
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> forNameNoException(final String className,
            final ClassLoader loader) {
        try {
            return (Class<T>) Class.forName(className, true, loader);
        } catch (final Throwable ignore) {
            return null;
        }
    }

    /**
     * プリミティブクラスの場合は、ラッパークラスに変換して返します。
     * 
     * @param className
     *            クラス名
     * @return {@link Class}
     * @throws ClassNotFoundRuntimeException
     *             {@link ClassNotFoundException}がおきた場合
     * @see #forName(String)
     */
    public static Class<?> convertClass(final String className)
            throws ClassNotFoundRuntimeException {
        final Class<?> clazz = primitiveNameToClassMap.get(className);
        if (clazz != null) {
            return clazz;
        }
        return forName(className);
    }

    /**
     * 指定されたクラスのデフォルトコンストラクタで、クラスの新しいインスタンスを作成および初期化します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param clazz
     *            クラスを表す{@link Class}オブジェクト
     * @return デフォルトコンストラクタを呼び出すことで作成される新規オブジェクト
     * @throws InstantiationRuntimeException
     *             基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
     * @throws IllegalAccessRuntimeException
     *             実パラメータ数と仮パラメータ数が異なる場合、 プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
     *             メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
     *             このコンストラクタが列挙型に関連している場合
     * @see Constructor#newInstance(Object[])
     */
    public static <T> T newInstance(final Class<T> clazz)
            throws InstantiationRuntimeException, IllegalAccessRuntimeException {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException e) {
            throw new InstantiationRuntimeException(clazz, e);
        } catch (final IllegalAccessException e) {
            throw new IllegalAccessRuntimeException(clazz, e);
        }
    }

    /**
     * 指定されたクラスをコンテキストクラスローダから取得し、デフォルトコンストラクタで、クラスの新しいインスタンスを作成および初期化します。
     * 
     * @param <T>
     *            生成するインスタンスの型
     * @param className
     *            クラス名
     * @return デフォルトコンストラクタを呼び出すことで作成される新規オブジェクト
     * @throws ClassNotFoundRuntimeException
     *             クラスが見つからなかった場合
     * @throws InstantiationRuntimeException
     *             基本となるコンストラクタを宣言するクラスが{@code abstract}クラスを表す場合
     * @throws IllegalAccessRuntimeException
     *             実パラメータ数と仮パラメータ数が異なる場合、 プリミティブ引数のラップ解除変換が失敗した場合、 またはラップ解除後、
     *             メソッド呼び出し変換によってパラメータ値を対応する仮パラメータ型に変換できない場合、
     *             このコンストラクタが列挙型に関連している場合
     * @see #newInstance(Class)
     * @see #forName(String)
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className)
            throws ClassNotFoundRuntimeException,
            InstantiationRuntimeException, IllegalAccessRuntimeException {
        return (T) newInstance(forName(className));
    }

    /**
     * 指定されたクラスを指定のクラスローダから取得し、デフォルトコンストラクタで、クラスの新しいインスタンスを作成および初期化します。
     * 
     * @param <T>
     *            生成するインスタンスの型
     * @param className
     *            クラス名
     * @param loader
     *            クラスローダ
     * @return 新しいインスタンス
     * @throws ClassNotFoundRuntimeException
     *             {@link ClassNotFoundException}がおきた場合
     * @throws InstantiationRuntimeException
     *             {@link InstantiationException}がおきた場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}がおきた場合
     * @see #newInstance(Class)
     * @see #forName(String, ClassLoader)
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className,
            final ClassLoader loader) throws ClassNotFoundRuntimeException,
            InstantiationRuntimeException, IllegalAccessRuntimeException {
        return (T) newInstance(forName(className, loader));
    }

    /**
     * 代入可能かどうかを返します。
     * 
     * @param toClass
     *            代入先のクラス
     * @param fromClass
     *            代入元のクラス
     * @return 代入可能かどうか
     * @see Class#isAssignableFrom(Class)
     */
    public static boolean isAssignableFrom(final Class<?> toClass,
            Class<?> fromClass) {
        if (toClass == Object.class && !fromClass.isPrimitive()) {
            return true;
        }
        if (toClass.isPrimitive()) {
            fromClass = getPrimitiveClassIfWrapper(fromClass);
        }
        return toClass.isAssignableFrom(fromClass);
    }

    /**
     * ラッパークラスをプリミティブクラスに変換します。
     * 
     * @param clazz
     *            ラッパークラス
     * @return 引数がラッパークラスならプリミティブクラス、それ以外の場合は{@literal null}
     */
    public static Class<?> getPrimitiveClass(final Class<?> clazz) {
        return wrapperToPrimitiveMap.get(clazz);
    }

    /**
     * ラッパークラスならプリミティブクラスに、 そうでなければそのままクラスを返します。
     * 
     * @param clazz
     *            クラス
     * @return 引数がラッパークラスならプリミティブクラス、それ以外の場合は引数で渡されたクラス
     */
    public static Class<?> getPrimitiveClassIfWrapper(final Class<?> clazz) {
        final Class<?> ret = getPrimitiveClass(clazz);
        if (ret != null) {
            return ret;
        }
        return clazz;
    }

    /**
     * プリミティブクラスをラッパークラスに変換します。
     * 
     * @param clazz
     *            プリミティブクラス
     * @return 引数がプリミティブクラスならラッパークラス、それ以外の場合は{@literal null}
     */
    public static Class<?> getWrapperClass(final Class<?> clazz) {
        return primitiveToWrapperMap.get(clazz);
    }

    /**
     * プリミティブの場合はラッパークラス、そうでない場合はもとのクラスを返します。
     * 
     * @param clazz
     * @return 引数がプリミティブクラスならラッパークラス、それ以外の場合は引数で渡されたクラス
     */
    public static Class<?> getWrapperClassIfPrimitive(final Class<?> clazz) {
        final Class<?> ret = getWrapperClass(clazz);
        if (ret != null) {
            return ret;
        }
        return clazz;
    }

    /**
     * {@link Class}オブジェクトが表すクラスの指定された{@code public}コンストラクタをリフレクトする
     * {@link Constructor}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param argTypes
     *            パラメータ配列
     * @return 指定された{@code argTypes}と一致する{@code public}コンストラクタの
     *         {@link Constructor}オブジェクト
     * @throws NoSuchConstructorRuntimeException
     *             一致するコンストラクタが見つからない場合
     * @see Class#getConstructor(Class...)
     */
    public static <T> Constructor<T> getConstructor(final Class<T> clazz,
            final Class<?>... argTypes)
            throws NoSuchConstructorRuntimeException {
        try {
            return clazz.getConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes)
                .initCause(e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたコンストラクタをリフレクトする
     * {@link Constructor}オブジェクトを返します。
     * 
     * @param <T>
     *            {@link Class}オブジェクトが表すクラス
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param argTypes
     *            パラメータ配列
     * @return 指定された{@code argTypes}と一致するコンストラクタの{@link Constructor}オブジェクト
     * @throws NoSuchConstructorRuntimeException
     *             一致するコンストラクタが見つからない場合
     * @see Class#getDeclaredConstructor(Class...)
     */
    public static <T> Constructor<T> getDeclaredConstructor(
            final Class<T> clazz, final Class<?>... argTypes)
            throws NoSuchConstructorRuntimeException {
        try {
            return clazz.getDeclaredConstructor(argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes)
                .initCause(e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された{@code public}メンバフィールドをリフレクトする
     * {@link Field}オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            フィールド名
     * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト
     * @throws NoSuchFieldRuntimeException
     *             指定された名前のフィールドが見つからない場合
     * @see Class#getField(String)
     */
    public static Field getField(final Class<?> clazz, final String name)
            throws NoSuchFieldRuntimeException {
        try {
            return clazz.getField(name);
        } catch (final NoSuchFieldException e) {
            throw new NoSuchFieldRuntimeException(clazz, name).initCause(e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された宣言フィールドをリフレクトする{@link Field}
     * オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            フィールド名
     * @return {@code name}で指定されたこのクラスの{@link Field}オブジェクト
     * @throws NoSuchFieldRuntimeException
     *             指定された名前のフィールドが見つからない場合
     * @see Class#getDeclaredField(String)
     */
    public static Field getDeclaredField(final Class<?> clazz, final String name)
            throws NoSuchFieldRuntimeException {
        try {
            return clazz.getDeclaredField(name);
        } catch (final NoSuchFieldException e) {
            throw new NoSuchFieldRuntimeException(clazz, name).initCause(e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定された{@code public}メンバメソッドをリフレクトする
     * {@link Method}オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            メソッドの名前
     * @param argTypes
     *            パラメータのリスト
     * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
     * @throws NoSuchMethodRuntimeException
     *             一致するメソッドが見つからない場合
     * @see Class#getMethod(String, Class...)
     */
    public static Method getMethod(final Class<?> clazz, final String name,
            final Class<?>... argTypes) throws NoSuchMethodRuntimeException {
        try {
            return clazz.getMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(clazz, name, argTypes)
                .initCause(e);
        }
    }

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたメンバメソッドをリフレクトする{@link Method}
     * オブジェクトを返します。
     * 
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            メソッドの名前
     * @param argTypes
     *            パラメータのリスト
     * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
     * @throws NoSuchMethodRuntimeException
     *             一致するメソッドが見つからない場合
     * @see Class#getDeclaredMethod(String, Class...)
     */
    public static Method getDeclaredMethod(final Class<?> clazz,
            final String name, final Class<?>... argTypes)
            throws NoSuchMethodRuntimeException {
        try {
            return clazz.getDeclaredMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new NoSuchMethodRuntimeException(clazz, name, argTypes)
                .initCause(e);
        }
    }

    /**
     * パッケージ名を返します。
     * 
     * @param clazz
     *            クラス
     * @return パッケージ名
     */
    public static String getPackageName(final Class<?> clazz) {
        final String fqcn = clazz.getName();
        final int pos = fqcn.lastIndexOf('.');
        if (pos > 0) {
            return fqcn.substring(0, pos);
        }
        return null;
    }

    /**
     * FQCNからパッケージ名を除いた名前を返します。
     * 
     * @param className
     *            クラス名
     * @return FQCNからパッケージ名を除いた名前
     */
    public static String getShortClassName(final String className) {
        final int i = className.lastIndexOf('.');
        if (i > 0) {
            return className.substring(i + 1);
        }
        return className;
    }

    /**
     * FQCNをパッケージ名とFQCNからパッケージ名を除いた名前に分けます。
     * 
     * @param className
     *            クラス名
     * @return パッケージ名とFQCNからパッケージ名を除いた名前
     */
    public static String[] splitPackageAndShortClassName(final String className) {
        final String[] ret = new String[2];
        final int i = className.lastIndexOf('.');
        if (i > 0) {
            ret[0] = className.substring(0, i);
            ret[1] = className.substring(i + 1);
        } else {
            ret[1] = className;
        }
        return ret;
    }

    /**
     * 配列の場合は要素のクラス名に{@literal []}を加えた文字列、それ以外はクラス名そのものを返します。
     * 
     * @param clazz
     *            クラス
     * @return クラス名
     */
    public static String getSimpleClassName(final Class<?> clazz) {
        if (clazz.isArray()) {
            return getSimpleClassName(clazz.getComponentType()) + "[]";
        }
        return clazz.getName();
    }

    /**
     * クラス名をリソースパスとして表現します。
     * 
     * @param clazz
     *            クラス
     * @return リソースパス
     * @see #getResourcePath(String)
     */
    public static String getResourcePath(final Class<?> clazz) {
        return getResourcePath(clazz.getName());
    }

    /**
     * クラス名をリソースパスとして表現します。
     * 
     * @param className
     *            クラス名
     * @return リソースパス
     */
    public static String getResourcePath(final String className) {
        return StringUtil.replace(className, ".", "/") + ".class";
    }

    /**
     * クラス名の要素を結合します。
     * 
     * @param s1
     *            クラス名の要素1
     * @param s2
     *            クラス名の要素2
     * @return 結合された名前
     */
    public static String concatName(final String s1, final String s2) {
        if (StringUtil.isEmpty(s1) && StringUtil.isEmpty(s2)) {
            return null;
        }
        if (!StringUtil.isEmpty(s1) && StringUtil.isEmpty(s2)) {
            return s1;
        }
        if (StringUtil.isEmpty(s1) && !StringUtil.isEmpty(s2)) {
            return s2;
        }
        return s1 + '.' + s2;
    }

}
