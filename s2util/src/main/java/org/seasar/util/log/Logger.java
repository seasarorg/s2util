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
package org.seasar.util.log;

import java.util.Map;

import org.seasar.util.message.MessageFormatter;
import org.seasar.util.misc.Disposable;
import org.seasar.util.misc.DisposableUtil;

import static org.seasar.util.collection.CollectionsUtil.*;

/**
 * ログ出力を提供するクラスです。
 * 
 * @author higa
 */
public class Logger {

    /**
     * ログの出力レベルです。
     */
    public enum LogLevel {
        /** デバッグ */
        DEBUG,
        /** 情報 */
        INFO,
        /** 警告 */
        WARN,
        /** エラー */
        ERROR,
        /** 致命的 */
        FATAL,
    }

    /** ロガーアダプタのファクトリ */
    protected static final LoggerAdapterFactory factory =
        getLoggerAdapterFactory();

    /** クラスをキーとするロガー のマップ */
    protected static final Map<Class<?>, Logger> loggers = newHashMap();

    /** 初期化済みを示すフラグ */
    private static boolean initialized;

    /** ロガーアダプタ */
    private final LoggerAdapter log;

    /**
     * {@link Logger}を返します。
     * 
     * @param clazz
     * @return {@link Logger}
     */
    public static synchronized Logger getLogger(final Class<?> clazz) {
        if (!initialized) {
            initialize();
        }
        Logger logger = loggers.get(clazz);
        if (logger == null) {
            logger = new Logger(clazz);
            loggers.put(clazz, logger);
        }
        return logger;
    }

    /**
     * フォーマットされたメッセージ文字列を返します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数
     * @return フォーマットされたメッセージ文字列
     */
    public static LogMessage format(final String messageCode,
            final Object... args) {
        final char messageType = messageCode.charAt(0);
        final String message =
            MessageFormatter.getSimpleMessage(messageCode, args);
        switch (messageType) {
        case 'D':
            return new LogMessage(LogLevel.DEBUG, message);
        case 'I':
            return new LogMessage(LogLevel.INFO, message);
        case 'W':
            return new LogMessage(LogLevel.WARN, message);
        case 'E':
            return new LogMessage(LogLevel.ERROR, message);
        case 'F':
            return new LogMessage(LogLevel.FATAL, message);
        default:
            throw new IllegalArgumentException("messageCode : " + messageCode);
        }
    }

    /**
     * {@link Logger}を初期化します。
     */
    protected static synchronized void initialize() {
        DisposableUtil.addFirst(new Disposable() {
            @Override
            public void dispose() {
                initialized = false;
                loggers.clear();
                factory.releaseAll();
            }
        });
        initialized = true;
    }

    /**
     * ログアダプタのファクトリを返します。
     * <p>
     * Commons Loggingが使える場合はCommons Loggingを利用するためのファクトリを返します。
     * 使えない場合はjava.util.loggingロガーを利用するためのファクトリを返します。
     * </p>
     * 
     * @return ログアダプタのファクトリ
     */
    protected static LoggerAdapterFactory getLoggerAdapterFactory() {
        try {
            Class.forName("org.apache.commons.logging.LogFactory");
            return new JclLoggerAdapterFactory();
        } catch (final Throwable ignore) {
            return new JulLoggerAdapterFactory();
        }
    }

    /**
     * インスタンスを構築します。
     * 
     * @param clazz
     *            ログ出力のカテゴリとなるクラス
     */
    protected Logger(final Class<?> clazz) {
        log = factory.getLoggerAdapter(clazz);
    }

    /**
     * DEBUG情報が出力されるかどうかを返します。
     * 
     * @return DEBUG情報が出力されるかどうか
     */
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    /**
     * DEBUG情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public void debug(final Object message, final Throwable throwable) {
        if (isDebugEnabled()) {
            log.debug(toString(message), throwable);
        }
    }

    /**
     * DEBUG情報を出力します。
     * 
     * @param message
     */
    public void debug(final Object message) {
        if (isDebugEnabled()) {
            log.debug(toString(message));
        }
    }

    /**
     * INFO情報が出力されるかどうかを返します。
     * 
     * @return INFO情報が出力されるかどうか
     */
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    /**
     * INFO情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public void info(final Object message, final Throwable throwable) {
        if (isInfoEnabled()) {
            log.info(toString(message), throwable);
        }
    }

    /**
     * INFO情報を出力します。
     * 
     * @param message
     */
    public void info(final Object message) {
        if (isInfoEnabled()) {
            log.info(toString(message));
        }
    }

    /**
     * WARN情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public void warn(final Object message, final Throwable throwable) {
        log.warn(toString(message), throwable);
    }

    /**
     * WARN情報を出力します。
     * 
     * @param message
     */
    public void warn(final Object message) {
        log.warn(message.toString());
    }

    /**
     * ERROR情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public void error(final Object message, final Throwable throwable) {
        log.error(message.toString(), throwable);
    }

    /**
     * ERROR情報を出力します。
     * 
     * @param message
     */
    public void error(final Object message) {
        log.error(message.toString());
    }

    /**
     * FATAL情報を出力します。
     * 
     * @param message
     * @param throwable
     */
    public void fatal(final Object message, final Throwable throwable) {
        log.fatal(message.toString(), throwable);
    }

    /**
     * FATAL情報を出力します。
     * 
     * @param message
     */
    public void fatal(final Object message) {
        log.fatal(message.toString());
    }

    /**
     * ログを出力します。
     * 
     * @param throwable
     */
    public void log(final Throwable throwable) {
        error(throwable.getMessage(), throwable);
    }

    /**
     * ログを出力します。
     * 
     * @param messageCode
     *            メッセージコード
     * @param args
     *            引数
     */
    public void log(final String messageCode, final Object... args) {
        log(format(messageCode, args));
    }

    /**
     * ログを出力します。
     * <p>
     * ログメッセージは{@link #format(String, Object...)}メソッドで作成します。
     * {@link #format(String, Object...)}を{@literal static import}しておくと便利です。
     * </p>
     * 
     * <pre>
     * import static org.seasar.util.log.Logger.format;
     * 
     * Logger logger = Logger.getLogger(Xxx.class);
     * logger.log(format("DXXX0000", arg1, arg2, arg3));
     * </pre>
     * 
     * @param logMessage
     *            ログメッセージ
     */
    public void log(final LogMessage logMessage) {
        log(logMessage, null);
    }

    /**
     * ログを出力します。
     * <p>
     * ログメッセージは{@link #format(String, Object...)}メソッドで作成します。
     * {@link #format(String, Object...)}を{@literal static import}しておくと便利です。
     * </p>
     * 
     * <pre>
     * import static org.seasar.util.log.Logger.format;
     * 
     * Logger logger = Logger.getLogger(Xxx.class);
     * logger.log(format("DXXX0000", arg1, arg2, arg3), t);
     * </pre>
     * 
     * @param logMessage
     *            ログメッセージ
     * @param throwable
     *            例外
     */
    public void log(final LogMessage logMessage, final Throwable throwable) {
        final LogLevel level = logMessage.getLevel();
        if (isEnabledFor(level)) {
            final String message = logMessage.getMessage();
            switch (level) {
            case DEBUG:
                log.debug(message, throwable);
                break;
            case INFO:
                log.info(message, throwable);
                break;
            case WARN:
                log.warn(message, throwable);
                break;
            case ERROR:
                log.error(message, throwable);
                break;
            case FATAL:
                log.fatal(message, throwable);
                break;
            }
        }
    }

    /**
     * 指定のログレベルが有効なら{@literal true}を返します．
     * 
     * @param logLevel
     *            ログレベル
     * @return 指定のログレベルが有効なら{@literal true}
     */
    protected boolean isEnabledFor(final LogLevel logLevel) {
        switch (logLevel) {
        case DEBUG:
            return log.isDebugEnabled();
        case INFO:
            return log.isInfoEnabled();
        case WARN:
            return log.isWarnEnabled();
        case ERROR:
            return log.isErrorEnabled();
        case FATAL:
            return log.isFatalEnabled();
        default:
            throw new IllegalArgumentException("logLevel : " + logLevel);
        }
    }

    /**
     * メッセージオブジェクトの文字列表現を返します。
     * 
     * @param message
     *            メッセージオブジェクト
     * @return メッセージオブジェクトの文字列表現
     */
    protected static String toString(final Object message) {
        if (message == null) {
            return "null";
        }
        if (message instanceof String) {
            return (String) message;
        }
        return message.toString();
    }

    /**
     * ログ出力するメッセージです。
     * 
     * @author koichik
     */
    public static class LogMessage {
        /** ログレベル */
        protected final LogLevel level;

        /** ログメッセージ */
        protected final String message;

        /**
         * インスタンスを構築します。
         * 
         * @param level
         *            ログレベル
         * @param message
         *            ログメッセージ
         */
        public LogMessage(final LogLevel level, final String message) {
            this.level = level;
            this.message = message;
        }

        /**
         * @return the level
         */
        public LogLevel getLevel() {
            return level;
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }

    }

}
