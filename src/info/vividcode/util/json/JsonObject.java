package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.HashMap;

public class JsonObject extends HashMap<String,JsonValue> implements JsonValue {

	private static final long serialVersionUID = -898362213984847287L;
	private static final String CLASS_NAME  = "JsonObject";
	private static final String METHOD_NAME = "objectValue()";
	final Object ID;
	
	public JsonObject() {
		super();
		ID = new Object();
	}

	@Override
	public ValueType valueType() {
		return ValueType.OBJECT_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}

	@Override
	public JsonObject objectValue() {
		return this;
	}

	@Override
	public BigDecimal numberValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}

	@Override
	public String stringValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}

	@Override
	public Boolean booleanValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}

}
