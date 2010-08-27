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
package org.seasar.util.beans.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.seasar.util.beans.BeanDesc;
import org.seasar.util.beans.ParameterizedClassDesc;
import org.seasar.util.beans.PropertyDesc;
import org.seasar.util.collection.ArrayMap;
import org.seasar.util.collection.CaseInsensitiveMap;
import org.seasar.util.convert.ByteConversionUtil;
import org.seasar.util.convert.DoubleConversionUtil;
import org.seasar.util.convert.FloatConversionUtil;
import org.seasar.util.convert.IntegerConversionUtil;
import org.seasar.util.convert.LongConversionUtil;
import org.seasar.util.convert.ShortConversionUtil;
import org.seasar.util.exception.ConstructorNotFoundRuntimeException;
import org.seasar.util.exception.EmptyRuntimeException;
import org.seasar.util.exception.FieldNotFoundRuntimeException;
import org.seasar.util.exception.MethodNotFoundRuntimeException;
import org.seasar.util.exception.PropertyNotFoundRuntimeException;
import org.seasar.util.lang.ClassUtil;
import org.seasar.util.lang.ConstructorUtil;
import org.seasar.util.lang.FieldUtil;
import org.seasar.util.lang.MethodUtil;
import org.seasar.util.lang.StringUtil;

import static org.seasar.util.collection.CollectionsUtil.*;
import static org.seasar.util.lang.GenericsUtil.*;

/**
 * {@link BeanDesc}の実装クラスです。
 * 
 * @author higa
 */
public class BeanDescImpl implements BeanDesc {

    /** 空のオブジェクト配列 */
    protected static final Object[] EMPTY_ARGS = new Object[0];

    /** 空のクラス配列 */
    protected static final Class<?>[] EMPTY_PARAM_TYPES = new Class<?>[0];

    /** Beanのクラス */
    protected final Class<?> beanClass;

    /** 型引数と型変数のマップ */
    protected final Map<TypeVariable<?>, Type> typeVariables;

    /** コンストラクタの配列 */
    protected final Constructor<?>[] constructors;

    /** プロパティ名から{@link PropertyDesc}へのマップ */
    protected final CaseInsensitiveMap<PropertyDesc> propertyDescCache =
        new CaseInsensitiveMap<PropertyDesc>();

    /** メソッド名からメソッド配列へのマップ */
    protected final Map<String, Method[]> methodsCache = newHashMap();

    /** フィールド名からフィールドへのマップ */
    protected final ArrayMap<String, Field> fieldCache =
        new ArrayMap<String, Field>();

    /** 不正なプロパティ名のセット */
    protected final Set<String> invalidPropertyNames = newHashSet();

    /**
     * {@link BeanDescImpl}を作成します。
     * 
     * @param beanClass
     *            ビーンのクラス
     */
    public BeanDescImpl(final Class<?> beanClass) {
        if (beanClass == null) {
            throw new EmptyRuntimeException("beanClass");
        }
        this.beanClass = beanClass;
        typeVariables = getTypeVariableMap(beanClass);
        constructors = beanClass.getConstructors();
        setupPropertyDescs();
        setupMethods();
        setupFields();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<T> getBeanClass() {
        return (Class<T>) beanClass;
    }

    @Override
    public Map<TypeVariable<?>, Type> getTypeVariables() {
        return typeVariables;
    }

    @Override
    public boolean hasPropertyDesc(final String propertyName) {
        return propertyDescCache.get(propertyName) != null;
    }

    @Override
    public PropertyDesc getPropertyDesc(final String propertyName)
            throws PropertyNotFoundRuntimeException {
        final PropertyDesc pd = propertyDescCache.get(propertyName);
        if (pd == null) {
            throw new PropertyNotFoundRuntimeException(beanClass, propertyName);
        }
        return pd;
    }

    @Override
    public PropertyDesc getPropertyDesc(final int index) {
        return propertyDescCache.getAt(index);
    }

    @Override
    public int getPropertyDescSize() {
        return propertyDescCache.size();
    }

    @Override
    public boolean hasField(final String fieldName) {
        return fieldCache.get(fieldName) != null;
    }

    @Override
    public Field getField(final String fieldName) {
        final Field field = fieldCache.get(fieldName);
        if (field == null) {
            throw new FieldNotFoundRuntimeException(beanClass, fieldName);
        }
        return field;
    }

    @Override
    public Field getField(final int index) {
        return fieldCache.getAt(index);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getFieldValue(final String fieldName, final Object target) {
        final Field field = getField(fieldName);
        return (T) FieldUtil.get(field, target);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getStaticFieldValue(String fieldName) {
        final Field field = getField(fieldName);
        return (T) FieldUtil.get(field);
    }

    @Override
    public void setFieldValue(String fieldName, Object target, Object value) {
        final Field field = getField(fieldName);
        FieldUtil.set(field, target, value);
    }

    @Override
    public void setStaticFieldValue(String fieldName, Object value) {
        final Field field = getField(fieldName);
        FieldUtil.set(field, value);
    }

    @Override
    public int getFieldSize() {
        return fieldCache.size();
    }

    @Override
    public <T> T newInstance(final Object... args) {
        final Constructor<T> constructor = getSuitableConstructor(args);
        return ConstructorUtil.newInstance(constructor, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T invoke(final Object target, final String methodName,
            final Object... args) {
        final Method method = getSuitableMethod(methodName, args);
        return (T) MethodUtil.invoke(method, target, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T invokeStatic(final String methodName, final Object... args) {
        final Method method = getSuitableMethod(methodName, args);
        return (T) MethodUtil.invokeStatic(method, args);
    }

    @Override
    public <T> Constructor<T> getSuitableConstructor(Object... args) {
        Constructor<T> constructor = findSuitableConstructor(args);
        if (constructor != null) {
            return constructor;
        }
        constructor = findSuitableConstructorAdjustNumber(args);
        if (constructor != null) {
            return constructor;
        }
        throw new ConstructorNotFoundRuntimeException(beanClass, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Constructor<T> getConstructor(final Class<?>... paramTypes) {
        for (final Constructor<?> constructor : constructors) {
            if (Arrays.equals(paramTypes, constructor.getParameterTypes())) {
                return (Constructor<T>) constructor;
            }
        }
        throw new ConstructorNotFoundRuntimeException(beanClass, paramTypes);
    }

    @Override
    public Method getSuitableMethod(final String methodName, Object... args) {
        final Method[] methods = getMethods(methodName);
        Method method = findSuitableMethod(methods, args);
        if (method != null) {
            return method;
        }
        method = findSuitableMethodAdjustNumber(methods, args);
        if (method != null) {
            return method;
        }
        throw new MethodNotFoundRuntimeException(beanClass, methodName, args);
    }

    @Override
    public Method getMethod(final String methodName,
            final Class<?>... paramTypes) {
        final Method method = getMethodNoException(methodName, paramTypes);
        if (method != null) {
            return method;
        }
        throw new MethodNotFoundRuntimeException(
            beanClass,
            methodName,
            paramTypes);
    }

    @Override
    public Method getMethodNoException(final String methodName,
            final Class<?>... paramTypes) {
        final Method[] methods = methodsCache.get(methodName);
        if (methods == null) {
            return null;
        }
        for (final Method method : methods) {
            if (Arrays.equals(paramTypes, method.getParameterTypes())) {
                return method;
            }
        }
        return null;
    }

    /**
     * @see org.seasar.util.beans.BeanDesc#getMethods(java.lang.String)
     */
    @Override
    public Method[] getMethods(final String methodName) {
        final Method[] methods = methodsCache.get(methodName);
        if (methods == null) {
            throw new MethodNotFoundRuntimeException(
                beanClass,
                methodName,
                null);
        }
        return methods;
    }

    @Override
    public boolean hasMethod(final String methodName) {
        return methodsCache.get(methodName) != null;
    }

    @Override
    public String[] getMethodNames() {
        return methodsCache.keySet().toArray(new String[methodsCache.size()]);
    }

    /**
     * {@link PropertyDesc}を返します。
     * 
     * @param propertyName
     *            プロパティ名
     * @return {@link PropertyDesc}。プロパティが存在しない場合は{@literal null}
     */
    protected PropertyDesc getPropertyDescNoException(final String propertyName) {
        return propertyDescCache.get(propertyName);
    }

    /**
     * 引数に適合するコンストラクタを返します。
     * 
     * @param <T>
     *            ビーンのクラス
     * @param args
     *            コンストラクタ引数の並び
     * @return 引数に適合するコンストラクタ。存在しない場合は{@literal null}
     */
    @SuppressWarnings("unchecked")
    protected <T> Constructor<T> findSuitableConstructor(final Object... args) {
        for (final Constructor<?> constructor : constructors) {
            if (isSuitable(constructor.getParameterTypes(), args, false)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    /**
     * 引数に適合するコンストラクタを返します。
     * <p>
     * 引数の型が数値の場合、引数を数値に変換することが出来れば適合するとみなします。
     * </p>
     * 
     * @param <T>
     *            ビーンのクラス
     * @param args
     *            コンストラクタ引数の並び
     * @return 引数に適合するコンストラクタ。存在しない場合は{@literal null}
     */
    @SuppressWarnings("unchecked")
    protected <T> Constructor<T> findSuitableConstructorAdjustNumber(
            final Object... args) {
        for (final Constructor<?> constructor : constructors) {
            if (isSuitable(constructor.getParameterTypes(), args, true)) {
                return (Constructor<T>) constructor;
            }
        }
        return null;
    }

    /**
     * 引数に適合するメソッドを返します。
     * 
     * @param methods
     *            メソッドの配列
     * @param args
     *            メソッド引数の並び
     * @return 引数に適合するメソッド。存在しない場合は{@literal null}
     */
    protected Method findSuitableMethod(final Method[] methods,
            final Object[] args) {
        for (final Method method : methods) {
            if (isSuitable(method.getParameterTypes(), args, false)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 引数に適合するメソッドを返します。
     * <p>
     * 引数の型が数値の場合、引数を数値に変換することが出来れば適合するとみなします。
     * </p>
     * 
     * @param methods
     *            メソッドの配列
     * @param args
     *            メソッド引数の並び
     * @return 引数に適合するメソッド。存在しない場合は{@literal null}
     */
    protected Method findSuitableMethodAdjustNumber(final Method[] methods,
            final Object[] args) {
        for (final Method method : methods) {
            if (isSuitable(method.getParameterTypes(), args, true)) {
                return method;
            }
        }
        return null;
    }

    /**
     * 引数が引数型に適合するかチェックします。
     * 
     * @param paramTypes
     *            引数型の並び
     * @param args
     *            引数の並び
     * @param adjustNumber
     *            引数型が数値型の場合に引数を適合するように変換する場合は{@literal true}
     * @return 引数が引数型に適合する場合は{@literal true}
     */
    protected boolean isSuitable(final Class<?>[] paramTypes,
            final Object[] args, boolean adjustNumber) {
        if (args == null) {
            return paramTypes.length == 0;
        }
        if (paramTypes.length != args.length) {
            return false;
        }
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) {
                continue;
            }
            if (ClassUtil.isAssignableFrom(paramTypes[i], args[i].getClass())) {
                continue;
            }
            if (adjustNumber && adjustNumber(paramTypes, args, i)) {
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * 指定された位置の引数型が数値の場合、引数を適合するように変換します。
     * 
     * @param paramTypes
     *            引数型の並び
     * @param args
     *            引数の並び
     * @param index
     *            操作対象となる引数のインデックス
     * @return 引数を適合するように変換した場合は{@literal true}
     */
    protected static boolean adjustNumber(final Class<?>[] paramTypes,
            final Object[] args, final int index) {
        if (paramTypes[index].isPrimitive()) {
            if (paramTypes[index] == byte.class) {
                args[index] = ByteConversionUtil.toByte(args[index]);
                return true;
            } else if (paramTypes[index] == short.class) {
                args[index] = ShortConversionUtil.toShort(args[index]);
                return true;
            } else if (paramTypes[index] == int.class) {
                args[index] = IntegerConversionUtil.toInteger(args[index]);
                return true;
            } else if (paramTypes[index] == long.class) {
                args[index] = LongConversionUtil.toLong(args[index]);
                return true;
            } else if (paramTypes[index] == float.class) {
                args[index] = FloatConversionUtil.toFloat(args[index]);
                return true;
            } else if (paramTypes[index] == double.class) {
                args[index] = DoubleConversionUtil.toDouble(args[index]);
                return true;
            }
        } else {
            if (paramTypes[index] == Byte.class) {
                args[index] = ByteConversionUtil.toByte(args[index]);
                return true;
            } else if (paramTypes[index] == Short.class) {
                args[index] = ShortConversionUtil.toShort(args[index]);
                return true;
            } else if (paramTypes[index] == Integer.class) {
                args[index] = IntegerConversionUtil.toInteger(args[index]);
                return true;
            } else if (paramTypes[index] == Long.class) {
                args[index] = LongConversionUtil.toLong(args[index]);
                return true;
            } else if (paramTypes[index] == Float.class) {
                args[index] = FloatConversionUtil.toFloat(args[index]);
                return true;
            } else if (paramTypes[index] == Double.class) {
                args[index] = DoubleConversionUtil.toDouble(args[index]);
                return true;
            }
        }
        return false;
    }

    /**
     * {@link PropertyDesc}を準備します。
     */
    protected void setupPropertyDescs() {
        for (final Method m : beanClass.getMethods()) {
            if (m.isBridge() || m.isSynthetic()) {
                continue;
            }
            final String methodName = m.getName();
            if (methodName.startsWith("get")) {
                if (m.getParameterTypes().length != 0
                    || methodName.equals("getClass")
                    || m.getReturnType() == void.class) {
                    continue;
                }
                final String propertyName =
                    StringUtil.decapitalize(methodName.substring(3));
                setupReadMethod(m, propertyName);
            } else if (methodName.startsWith("is")) {
                if (m.getParameterTypes().length != 0
                    || !m.getReturnType().equals(Boolean.TYPE)
                    && !m.getReturnType().equals(Boolean.class)) {
                    continue;
                }
                final String propertyName =
                    StringUtil.decapitalize(methodName.substring(2));
                setupReadMethod(m, propertyName);
            } else if (methodName.startsWith("set")) {
                if (m.getParameterTypes().length != 1
                    || methodName.equals("setClass")
                    || m.getReturnType() != void.class) {
                    continue;
                }
                final String propertyName =
                    StringUtil.decapitalize(methodName.substring(3));
                setupWriteMethod(m, propertyName);
            }
        }
        for (final String name : invalidPropertyNames) {
            propertyDescCache.remove(name);
        }
        invalidPropertyNames.clear();
    }

    /**
     * getterメソッドを準備します。
     * 
     * @param readMethod
     *            getterメソッド
     * @param propertyName
     *            プロパティ名
     */
    protected void setupReadMethod(final Method readMethod,
            final String propertyName) {
        final Class<?> propertyType = readMethod.getReturnType();
        PropertyDesc propDesc = getPropertyDescNoException(propertyName);
        if (propDesc == null) {
            propDesc =
                new PropertyDescImpl(
                    propertyName,
                    propertyType,
                    readMethod,
                    null,
                    null,
                    this);
            addPropertyDesc(propDesc);
        } else if (propDesc.getPropertyType() != propertyType) {
            invalidPropertyNames.add(propertyName);
        } else {
            propDesc.setReadMethod(readMethod);
        }
    }

    /**
     * setterメソッドを準備します。
     * 
     * @param writeMethod
     *            setterメソッド
     * @param propertyName
     *            プロパティ名
     */
    protected void setupWriteMethod(final Method writeMethod,
            final String propertyName) {
        final Class<?> propertyType = writeMethod.getParameterTypes()[0];
        PropertyDesc propDesc = getPropertyDescNoException(propertyName);
        if (propDesc == null) {
            propDesc =
                new PropertyDescImpl(
                    propertyName,
                    propertyType,
                    null,
                    writeMethod,
                    null,
                    this);
            addPropertyDesc(propDesc);
        } else if (propDesc.getPropertyType() != propertyType) {
            invalidPropertyNames.add(propertyName);
        } else {
            propDesc.setWriteMethod(writeMethod);
        }
    }

    /**
     * {@link PropertyDesc}を追加します．
     * 
     * @param propertyDesc
     *            {@link PropertyDesc}
     */
    protected void addPropertyDesc(final PropertyDesc propertyDesc) {
        if (propertyDesc == null) {
            throw new EmptyRuntimeException("propertyDesc");
        }
        propertyDescCache.put(propertyDesc.getPropertyName(), propertyDesc);
    }

    /**
     * メソッドを準備します。
     */
    protected void setupMethods() {
        final ArrayMap<String, List<Method>> methodListMap =
            new ArrayMap<String, List<Method>>();
        for (final Method method : beanClass.getMethods()) {
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final String methodName = method.getName();
            List<Method> list = methodListMap.get(methodName);
            if (list == null) {
                list = new ArrayList<Method>();
                methodListMap.put(methodName, list);
            }
            list.add(method);
        }
        for (int i = 0; i < methodListMap.size(); ++i) {
            final List<Method> methodList = methodListMap.getAt(i);
            methodsCache.put(
                methodListMap.getKeyAt(i),
                methodList.toArray(new Method[methodList.size()]));
        }
    }

    /**
     * フィールドを準備します。
     */
    protected void setupFields() {
        if (beanClass.isInterface()) {
            setupFieldsByInterface(beanClass);
        } else {
            setupFieldsByClass(beanClass);
        }
    }

    /**
     * インターフェースに定義されたフィールドを準備します。
     * 
     * @param interfaceClass
     *            対象のインターフェース
     */
    protected void setupFieldsByInterface(final Class<?> interfaceClass) {
        addFields(interfaceClass);
        final Class<?>[] interfaces = interfaceClass.getInterfaces();
        for (int i = 0; i < interfaces.length; ++i) {
            setupFieldsByInterface(interfaces[i]);
        }
    }

    /**
     * クラスに定義されたフィールドを準備します。
     * 
     * @param targetClass
     *            対象のクラス
     */
    private void setupFieldsByClass(final Class<?> targetClass) {
        addFields(targetClass);
        for (final Class<?> intf : targetClass.getInterfaces()) {
            setupFieldsByInterface(intf);
        }
        final Class<?> superClass = targetClass.getSuperclass();
        if (superClass != Object.class && superClass != null) {
            setupFieldsByClass(superClass);
        }
    }

    /**
     * クラスまたはインターフェースに定義されたフィールドを追加します。
     * 
     * @param clazz
     *            対象のクラスまたはインターフェース
     */
    protected void addFields(final Class<?> clazz) {
        for (final Field field : clazz.getDeclaredFields()) {
            final String fname = field.getName();
            if (fieldCache.containsKey(fname)) {
                continue;
            }
            field.setAccessible(true);
            fieldCache.put(fname, field);
            if (!FieldUtil.isInstanceField(field)) {
                continue;
            }
            if (hasPropertyDesc(fname)) {
                final PropertyDesc pd = getPropertyDesc(field.getName());
                pd.setField(field);
                continue;
            }
            if (FieldUtil.isPublicField(field)) {
                final PropertyDesc pd =
                    new PropertyDescImpl(
                        field.getName(),
                        field.getType(),
                        null,
                        null,
                        field,
                        this);
                propertyDescCache.put(fname, pd);
            }
        }
    }

    /**
     * {@link Type}を表現する{@link ParameterizedClassDesc}を作成して返します。
     * 
     * @param type
     *            型
     * @param map
     *            パラメータ化された型が持つ型変数をキー、型引数を値とする{@link Map}
     * @return 型を表現する{@link ParameterizedClassDesc}
     */
    protected static ParameterizedClassDesc createParameterizedClassDesc(
            final Type type, final Map<TypeVariable<?>, Type> map) {
        final Class<?> rowClass = getActualClass(type, map);
        if (rowClass == null) {
            return null;
        }
        final ParameterizedClassDescImpl desc =
            new ParameterizedClassDescImpl(rowClass);
        final Type[] parameterTypes = getGenericParameter(type);
        if (parameterTypes == null) {
            return desc;
        }
        final ParameterizedClassDesc[] parameterDescs =
            new ParameterizedClassDesc[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; ++i) {
            parameterDescs[i] =
                createParameterizedClassDesc(parameterTypes[i], map);
        }
        desc.setArguments(parameterDescs);
        return desc;
    }

}
