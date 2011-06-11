package info.vividcode.util.json;

import java.math.BigDecimal;

/**
 * JSON の各種値 (object, array, string, number, boolean, null) 
 * に対応する Java のオブジェクトによって実装されるインターフェイス.
 * @author nobuoka
 *
 */
public interface JsonValue {
	
	/**
	 * JSON の各種値の型を表す列挙型
	 * @author nobuoka
	 *
	 */
	enum ValueType {
		OBJECT_VALUE,
		ARRAY_VALUE,
		NUMBER_VALUE,
		STRING_VALUE,
		BOOLEAN_VALUE,
		NULL_VALUE
	}
	
	/**
	 * このオブジェクトが表す JSON の値のタイプを返す. 
	 */
	public ValueType valueType();
	
	/**
	 * JSON の array を表すオブジェクトの場合に, 
	 * 対応する Java のオブジェクト ({@link JsonArray} オブジェクト) を返す. 
	 * @return 対応する JsonArray オブジェクト
	 */
	public JsonArray arrayValue();
	
	/**
	 * JSON の object を表すオブジェクトの場合に, 
	 * 対応する Java のオブジェクト ({@link JsonObject} オブジェクト) を返す. 
	 * @return 対応する JsonObject オブジェクト
	 */
	public JsonObject objectValue();
	
	/**
	 * JSON の number を表すオブジェクトの場合に, 
	 * 対応する Java のオブジェクト ({@link BigDecimal} オブジェクト) を返す. 
	 * @return 対応する BigDecimal オブジェクト
	 */
	public BigDecimal numberValue();
	
	/**
	 * JSON の object を表すオブジェクトの場合に, 
	 * 対応する Java のオブジェクト ({@link String} オブジェクト) を返す. 
	 * @return 対応する String オブジェクト
	 */
	public String  stringValue();
	
	/**
	 * JSON の object を表すオブジェクトの場合に, 
	 * 対応する Java のオブジェクト ({@link Boolean} オブジェクト) を返す. 
	 * @return 対応する Boolean オブジェクト
	 */
	public Boolean booleanValue();
	
}
