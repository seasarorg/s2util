/*
 * Copyright 2004-2012 the Seasar Foundation and the Others.
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
/**
 * ログ出力の機能を提供します。
 * <p>
 * ログ出力のフレームワークとしては、Commons Loggingおよびjava.util.loggingに対応しています。
 * Commons Logging用のブリッジ(jcl-over-slf4j)を使うことで、SLF4Jを使うこともできます。
 * </p>
 * <p>
 * クラスパスにCommons LoggingのAPIが含まれていればCommons Loggingを使用します。
 * 含まれていない場合はjava.util.loggingを使用します。
 * </p>
 */
package org.seasar.util.log;

