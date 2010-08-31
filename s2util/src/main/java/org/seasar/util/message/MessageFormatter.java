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

import java.text.MessageFormat;

/**
 * メッセージコードと引数からメッセージを組み立てるクラスです。
 * 
 * @author higa
 */

public abstract class MessageFormatter {

    private static final String MESSAGES = "Messages";

    /**
     * メッセージを返します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数
     * @return メッセージ
     */
    public static String getMessage(final String messageCode,
            final Object... args) {
        return getFormattedMessage(
            messageCode == null ? "" : messageCode,
            getSimpleMessage(messageCode, args));
    }

    /**
     * メッセージコードつきのメッセージを返します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param simpleMessage
     *            引数が展開された単純なメッセージ
     * @return メッセージコードつきのメッセージ
     */
    public static String getFormattedMessage(final String messageCode,
            final String simpleMessage) {
        return "[" + messageCode + "]" + simpleMessage;
    }

    /**
     * 引数を展開してメッセージコードなしの単純なメッセージを返します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数
     * @return メッセージコードなしの単純なメッセージ
     */
    public static String getSimpleMessage(final String messageCode,
            final Object... args) {
        try {
            final String pattern = getPattern(messageCode);
            if (pattern != null) {
                return MessageFormat.format(pattern, args);
            }
            return getNoPatternMessage(args);
        } catch (final Throwable ignore) {
            return getNoPatternMessage(args);
        }
    }

    /**
     * メッセージコードに対応するパターン文字列を返します。
     * 
     * @param messageCode
     *            メッセージコード
     * @return パターン文字列
     */
    protected static String getPattern(final String messageCode) {
        final MessageResourceBundle resourceBundle =
            getMessages(getSystemName(messageCode));
        if (resourceBundle == null) {
            return null;
        }

        final int length = messageCode.length();
        if (length > 4) {
            final String key =
                messageCode.charAt(0) + messageCode.substring(length - 4);
            final String pattern = resourceBundle.get(key);
            if (pattern != null) {
                return pattern;
            }
        }
        return resourceBundle.get(messageCode);
    }

    /**
     * システム名を返します。
     * 
     * @param messageCode
     *            メッセージコード
     * @return システム名
     */
    protected static String getSystemName(final String messageCode) {
        return messageCode.substring(1, Math.max(1, messageCode.length() - 4));
    }

    /**
     * メッセージリソースバンドルを返します。
     * 
     * @param systemName
     *            システム名
     * @return メッセージリソースバンドル
     */
    protected static MessageResourceBundle getMessages(final String systemName) {
        return MessageResourceBundleFactory.getBundle(systemName + MESSAGES);
    }

    /**
     * パターンを使用しないで引数を並べたメッセージを返します。
     * 
     * @param args
     *            引数
     * @return 引数を並べたメッセージ
     */
    protected static String getNoPatternMessage(final Object... args) {
        if (args == null || args.length == 0) {
            return "";
        }
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            buffer.append(args[i] + ", ");
        }
        buffer.setLength(buffer.length() - 2);
        return new String(buffer);
    }

}
