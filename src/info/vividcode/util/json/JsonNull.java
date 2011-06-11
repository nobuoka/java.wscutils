package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonNull implements JsonValue {
	
	public static final JsonNull VALUE = new JsonNull();
	
	private JsonNull() {}

	@Override
	public ValueType valueType() {
		return ValueType.NULL_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		return null;
	}

	@Override
	public JsonObject objectValue() {
		return null;
	}

	@Override
	public BigDecimal numberValue() {
		return null;
	}

	@Override
	public String stringValue() {
		return null;
	}

	@Override
	public Boolean booleanValue() {
		return null;
	}
	
	@Override
	public String toString() {
		return "[JSON null]";
	}

}
