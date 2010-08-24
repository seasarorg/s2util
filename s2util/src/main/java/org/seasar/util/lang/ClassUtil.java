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
     * コンテキストクラスローダから指定された名前の{@link Class}を返します。
     * 
     * @param className
     *            クラス名
     * @return {@link Class}
     * @throws ClassNotFoundRuntimeException
     *             {@link ClassNotFoundException}がおきた場合
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class<?> forName(final String className)
            throws ClassNotFoundRuntimeException {
        return forName(className, Thread
            .currentThread()
            .getContextClassLoader());
    }

    /**
     * 指定のクラスローダから指定された名前の{@link Class}を返します。
     * 
     * @param className
     *            クラス名
     * @param loader
     *            クラスローダ
     * @return {@link Class}
     * @throws ClassNotFoundRuntimeException
     *             {@link ClassNotFoundException}がおきた場合
     * @see Class#forName(String, boolean, ClassLoader)
     */
    public static Class<?> forName(final String className,
            final ClassLoader loader) throws ClassNotFoundRuntimeException {
        try {
            return Class.forName(className, true, loader);
        } catch (final ClassNotFoundException ex) {
            throw new ClassNotFoundRuntimeException(className, ex);
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
     * 新しいインスタンスを作成します。
     * 
     * @param <T>
     *            インスタンスを作成する型
     * @param clazz
     *            クラス
     * @return 新しいインスタンス
     * @throws InstantiationRuntimeException
     *             {@link InstantiationException}が起きた場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}が起きた場合
     * @see Class#newInstance()
     */
    public static <T> T newInstance(final Class<T> clazz)
            throws InstantiationRuntimeException, IllegalAccessRuntimeException {
        try {
            return clazz.newInstance();
        } catch (final InstantiationException ex) {
            throw new InstantiationRuntimeException(clazz, ex);
        } catch (final IllegalAccessException ex) {
            throw new IllegalAccessRuntimeException(clazz, ex);
        }
    }

    /**
     * コンテキストクラスローダから取得したクラスの新しいインスタンスを作成します。
     * 
     * @param <T>
     *            生成するインスタンスの型
     * @param className
     *            クラス名
     * @return 新しいインスタンス
     * @throws ClassNotFoundRuntimeException
     *             {@link ClassNotFoundException}がおきた場合
     * @throws InstantiationRuntimeException
     *             {@link InstantiationException}がおきた場合
     * @throws IllegalAccessRuntimeException
     *             {@link IllegalAccessException}がおきた場合
     * @see #newInstance(Class)
     */
    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className)
            throws ClassNotFoundRuntimeException,
            InstantiationRuntimeException, IllegalAccessRuntimeException {
        return (T) newInstance(forName(className));
    }

    /**
     * 指定のトクラスローダから取得したクラスの新しいインスタンスを作成します。
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
     * {@link Constructor}を返します。
     * 
     * @param <T>
     *            クラスの型
     * @param clazz
     *            クラス
     * @param argTypes
     *            引数型の並び
     * @return {@link Constructor}
     * @throws NoSuchConstructorRuntimeException
     *             {@link NoSuchMethodException}がおきた場合
     * @see Class#getConstructor(Class...)
     */
    public static <T> Constructor<T> getConstructor(final Class<T> clazz,
            final Class<?>... argTypes)
            throws NoSuchConstructorRuntimeException {
        try {
            return clazz.getConstructor(argTypes);
        } catch (final NoSuchMethodException ex) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes)
                .initCause(ex);
        }
    }

    /**
     * そのクラスに宣言されている {@link Constructor}を返します。
     * 
     * @param <T>
     *            クラスの型
     * @param clazz
     *            クラス
     * @param argTypes
     *            引数型の並び
     * @return {@link Constructor}
     * @throws NoSuchConstructorRuntimeException
     *             {@link NoSuchMethodException}がおきた場合
     * @see Class#getDeclaredConstructor(Class...)
     */
    public static <T> Constructor<T> getDeclaredConstructor(
            final Class<T> clazz, final Class<?>... argTypes)
            throws NoSuchConstructorRuntimeException {
        try {
            return clazz.getDeclaredConstructor(argTypes);
        } catch (final NoSuchMethodException ex) {
            throw new NoSuchConstructorRuntimeException(clazz, argTypes)
                .initCause(ex);
        }
    }

    /**
     * {@link Method}を返します。
     * 
     * @param clazz
     *            クラス
     * @param methodName
     *            メソッド名
     * @param argTypes
     *            引数型の並び
     * @return {@link Method}
     * @throws NoSuchMethodRuntimeException
     *             {@link NoSuchMethodException}がおきた場合
     * @see Class#getMethod(String, Class...)
     */
    public static Method getMethod(final Class<?> clazz,
            final String methodName, final Class<?>... argTypes)
            throws NoSuchMethodRuntimeException {
        try {
            return clazz.getMethod(methodName, argTypes);
        } catch (final NoSuchMethodException ex) {
            throw new NoSuchMethodRuntimeException(clazz, methodName, argTypes)
                .initCause(ex);
        }
    }

    /**
     * そのクラスに宣言されている {@link Method}を返します。
     * 
     * @param clazz
     *            クラス
     * @param methodName
     *            メソッド名
     * @param argTypes
     *            引数型の並び
     * @return {@link Method}
     * @throws NoSuchMethodRuntimeException
     *             {@link NoSuchMethodException}がおきた場合
     * @see Class#getDeclaredMethod(String, Class...)
     */
    public static Method getDeclaredMethod(final Class<?> clazz,
            final String methodName, final Class<?>... argTypes)
            throws NoSuchMethodRuntimeException {
        try {
            return clazz.getDeclaredMethod(methodName, argTypes);
        } catch (final NoSuchMethodException ex) {
            throw new NoSuchMethodRuntimeException(clazz, methodName, argTypes)
                .initCause(ex);
        }
    }

    /**
     * {@link Field}を返します。
     * 
     * @param clazz
     *            クラス
     * @param fieldName
     *            フィールド名
     * @return {@link Field}
     * @throws NoSuchFieldRuntimeException
     *             {@link NoSuchFieldException}がおきた場合
     * @see Class#getField(String)
     */
    public static Field getField(final Class<?> clazz, final String fieldName)
            throws NoSuchFieldRuntimeException {
        try {
            return clazz.getField(fieldName);
        } catch (final NoSuchFieldException ex) {
            throw new NoSuchFieldRuntimeException(clazz, fieldName)
                .initCause(ex);
        }
    }

    /**
     * そのクラスに宣言されている {@link Field}を返します。
     * 
     * @param clazz
     *            クラス名
     * @param fieldName
     *            フィールド名
     * @return {@link Field}
     * @throws NoSuchFieldRuntimeException
     *             {@link NoSuchFieldException}がおきた場合
     * @see Class#getDeclaredField(String)
     */
    public static Field getDeclaredField(final Class<?> clazz,
            final String fieldName) throws NoSuchFieldRuntimeException {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (final NoSuchFieldException ex) {
            throw new NoSuchFieldRuntimeException(clazz, fieldName)
                .initCause(ex);
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
