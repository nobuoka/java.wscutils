package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonNull implements JsonValue {
	
	public static final JsonNull VALUE = new JsonNull();
	private static final String CLASS_NAME  = "JsonNull";
	
	private JsonNull() {}

	@Override
	public ValueType valueType() {
		return ValueType.NULL_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object, so " +
				"this method is not supported." );
	}

	@Override
	public JsonObject objectValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object, so " +
				"this method is not supported." );
	}

	@Override
	public BigDecimal numberValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object, so " +
				"this method is not supported." );
	}

	@Override
	public String stringValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object, so " +
				"this method is not supported." );
	}

	@Override
	public boolean booleanValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object, so " +
				"this method is not supported." );
	}
	
	@Override
	public String toString() {
		return "[JSON null]";
	}

}
