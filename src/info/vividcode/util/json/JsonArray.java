package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonValue> implements JsonValue {
	
	private static final long serialVersionUID = -8694573603186652059L;

	public JsonArray() {
		super();
	}

	@Override
	public ValueType valueType() {
		return ValueType.ARRAY_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		return this;
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

}
