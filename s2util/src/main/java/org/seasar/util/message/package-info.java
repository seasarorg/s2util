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
 * プロパティファイルに定義されたパターンを使用してメッセージを組み立てるためのクラスを提供します。
 * <h3>メッセージコード</h3>
 * <p>
 * プロパティファイルのキーはメッセージコードと呼ばれます。
 * メッセージコードは以下の形式の文字列です。
 * </p>
 * 
 * <pre>
 * [Level][SystemName][Number]
 * </pre>
 * <dl>
 * <dt>Level</dt>
 * <dd>メッセージの出力レベルです。
 * <table border="1">
 * <tr>
 * <th>出力レベル</th>
 * <th>意味</th>
 * </tr>
 * <tr>
 * <td>F</td>
 * <td>Fatal (致命的)</td>
 * </tr>
 * <tr>
 * <td>E</td>
 * <td>Error (エラー)</td>
 * </tr>
 * <tr>
 * <td>W</td>
 * <td>Warning (警告)</td>
 * </tr>
 * <tr>
 * <td>I</td>
 * <td>Info (情報)</td>
 * </tr>
 * <tr>
 * <td>D</td>
 * <td>Debug (デバッグ)</td>
 * </tr>
 * </table>
 * </dd>
 * <dt>SystemName</dt>
 * <dd>メッセージコードの集まりを識別する1文字以上の識別名です。<br />
 * </dd>
 * <dt>Number</dt>
 * <dd>4桁の数字で表される通番です。</dd>
 * </dl>
 * <p>
 * システム名が<code>Log</code>の場合、メッセージコードは次のようになります。
 * 
 * <pre>
 * ELog0001
 * DLog1000
 * </pre>
 * 
 * <h3>プロパティファイル</h3>
 * <p>
 * システム名に<code>Messages</code>を付加した名前がメッセージのパターンを定義したプロパティファイルのベース名になります。<br />
 * 例えばシステム名が<code>Log</code>の場合、プロパティファイルのベース名は<code>LogMessages</code>となります。
 * 実際のファイル名は、ベース名に拡張子<code>.properties</code>を付加して<code>LogMessages.properties</code>
 * のようになります。
 * </p>
 * <p>
 * {@link java.util.ResourceBundle.Control}のプロパティ形式と同様に言語コードや国コードを持つプロパティファイルを用意することにより、メッセージを国際化することができます。
 * 例えば日本語メッセージのパターンを定義したプロパティファイルは<code>LogMessages_ja.properties</code>や<code>LogMessages_ja_JP.properties</code>のようになります。
 * </p>
 * <p>
 * プロパティファイルの内容は<code>メッセージコード=パターン</code>の形式となります。
 * </p>
 * <p>
 * メッセージコードはそのままキーとして記述することも、システム名を省略して記述することもできます。 次の二つの例は等価です。
 * </p>
 * <h4>メッセージコードをそのままキーとする場合</h4>
 * 
 * <pre>
 * ELog0001=エラーが発生しました。理由:{0}
 * DLog1000=これはやばい。変数の値:{0}
 * </pre>
 * 
 * <h4>システム名を省略する場合</h4>
 * 
 * <pre>
 * E0001=エラーが発生しました。理由:{0}
 * D1000=これはやばい。変数の値:{0}
 * </pre>
 * <p>
 * パターンは{@link java.text.MessageFormat}の形式に従います。
 * </p>
 * 
 * <h3>プロパティファイルのキャッシュ</h3>
 * <p>
 * デフォルトではプロパティファイルは最初に必要になった時点で読み込まれ、そのままキャッシュされます。
 * アプリケーションの開発中など、プロパティファイルの変更をリアルタイムに反映させたい場合は、
 * {@link java.util.ResourceBundle#clearCache()}を呼び出すことでキャッシュがクリアされます。
 * </p>
 */
package org.seasar.util.message;

