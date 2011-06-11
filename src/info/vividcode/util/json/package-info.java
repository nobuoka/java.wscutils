/**
 * JSON を扱うためのパッケージ.
 * 
 * 各種 JSON オブジェクト (Array, Object, String, Number, String, Boolean, Null) 
 * を表すクラスと, JSON オブジェクトを表すインスタンスを JSON 文字列に変換する JsonSerializer, 
 * JSON 文字列を JSON オブジェクトに変換する JsonParser から成る.
 * 各種 JSON オブジェクトを表すためのクラスは JsonValue インターフェイスを実装している. 
 * 
 *<pre><code>String jsonStr = "[ 50, 200 ]";
 *JsonValue jsonObj = JsonParser( jsonStr );
 *BigDecimal num1 = jsonObj.arrayValue().get( 0 ).numberValue();
 *BigDecimal num2 = jsonObj.arrayValue().get( 1 ).numberValue();</code></pre>
 */
package info.vividcode.util.json;

