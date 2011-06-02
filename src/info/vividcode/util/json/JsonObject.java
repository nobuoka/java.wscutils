package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.HashMap;

public class JsonObject extends HashMap<String,JsonValue> implements JsonValue {

	private static final long serialVersionUID = -898362213984847287L;
	
	public JsonObject() {
		super();
	}

	@Override
	public ValueType valueType() {
		return ValueType.OBJECT_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}

	@Override
	public JsonObject objectValue() {
		// TODO Auto-generated method stub
		return this;
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

}
