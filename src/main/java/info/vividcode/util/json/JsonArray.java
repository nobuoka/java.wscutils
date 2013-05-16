package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * JSON の array を表すためのクラスであり, JsonValue オブジェクトを格納するための 
 * {@link java.util.List} でもある.
 * 
 * @author nobuoka
 *
 */
public class JsonArray extends ArrayList<JsonValue> implements JsonValue {
	
	private static final long serialVersionUID = -8694573603186652059L;
	private static final String CLASS_NAME  = "JsonArray";
	private static final String METHOD_NAME = "arrayValue()";
	final Object ID;
	
	/**
	 * 何も含まない状態の JsonArray オブジェクトを生成する.
	 */
	public JsonArray() {
		super();
		ID = new Object();
	}
	
	/**
	 * 対応する JSON の値のタイプを返す. 
	 * このクラスは JSON の array に対応するため, 常に {@link JsonValue.ValueType}.ARRAY_VALUE を返す.
	 */
	@Override
	public ValueType valueType() {
		return ValueType.ARRAY_VALUE;
	}
	
	/**
	 * 自分自身を返す. 
	 * このオブジェクトが JsonValue 型として扱われている場合に, キャストの代わりとして使用することができる.
	 */
	@Override
	public JsonArray arrayValue() {
		return this;
	}
	
	/**
	 * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
	 */
	@Override
	public JsonObject objectValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	
	/**
	 * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
	 */
	@Override
	public BigDecimal numberValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	
	/**
	 * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
	 */
	@Override
	public String stringValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	
	/**
	 * 常に, 例外 {@link UnsupportedOperationException} が投げられる.
	 */
	@Override
	public Boolean booleanValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	
}
