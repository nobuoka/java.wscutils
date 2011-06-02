package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonNull implements JsonValue {
	
	static final public JsonNull VALUE = new JsonNull();
	
	private JsonNull() {}

	@Override
	public ValueType valueType() {
		return ValueType.NULL_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}

	@Override
	public JsonObject objectValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}

	@Override
	public BigDecimal numberValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}

	@Override
	public String stringValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}

	@Override
	public boolean booleanValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}
	
	@Override
	public String toString() {
		return "[JSON null]";
	}

}
