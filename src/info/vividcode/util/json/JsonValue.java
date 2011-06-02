package info.vividcode.util.json;

import java.math.BigDecimal;

public interface JsonValue {
	enum ValueType {
		OBJECT_VALUE,
		ARRAY_VALUE,
		NUMBER_VALUE,
		STRING_VALUE,
		BOOLEAN_VALUE,
		NULL_VALUE
	}
	public ValueType valueType();
	public JsonArray arrayValue();
	public JsonObject objectValue();
	public BigDecimal numberValue();
	public String  stringValue();
	public boolean booleanValue();
}
