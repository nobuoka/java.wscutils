/**
 *JSON を扱うためのパッケージ. 
 *各種 JSON オブジェクト (Array, Object, String, Number, String, Boolean, Null) 
 *を表すクラスと, パーサー, シリアライザーから成る.
 *<p> 
 *各種 JSON オブジェクトを表すためのクラスは JsonValue インターフェイスを実装しており, 
 *実際の値を取得するためには xxxxValue メソッド (xxxx は "array" や "string" など) 
 *を使用する. 
 *例えば, JSON の string を表すためのクラスは JsonString であり, 
 *実際の値 (String オブジェクト) を取得するには stringValue メソッドを使用する.
 *<pre><code>  // インスタンスはコンストラクタに String オブジェクトを渡して生成する 
 *  JsonString js = new JsonString( "テスト" );
 *  // 実際の値は stringValue メソッドを使用して取得できる
 *  String s = js.stringValue();</code></pre>
 *JSON 文字列をパージングするためには JsonParser を使用する.
 *<pre><code>  // パース対象の JSON 文字列
 *  String jsonStr = "[ 50, 200 ]";
 *  // パースする
 *  JsonValue jsonObj = JsonParser.parse( jsonStr );
 *  // 現在は JsonArray であることがわかっているので, JsonArray として扱う
 *  BigDecimal num1 = jsonObj.arrayValue().get( 0 ).numberValue();
 *  BigDecimal num2 = jsonObj.arrayValue().get( 1 ).numberValue();</code></pre>
 *JSON オブジェクトを JSON 文字列に変換するためには JsonSerializer を使用する.
 *<pre><code>  // シリアライズ対象の JSON オブジェクト
 *  JsonArray jsonArray = new JsonArray();
 *  jsonArray.add( new JsonString( "テスト\t改行も\n" ) );
 *  // シリアライズして出力
 *  System.out.println( JsonSerializer.serialize( jsonArray ) );
 *      // ["テスト\t改行も\n"]</code></pre>
 */
package info.vividcode.util.json;

