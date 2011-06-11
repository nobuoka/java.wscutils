package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.ArrayList;

public class JsonArray extends ArrayList<JsonValue> implements JsonValue {
	
	private static final long serialVersionUID = -8694573603186652059L;
	private static final String CLASS_NAME  = "JsonArray";
	private static final String METHOD_NAME = "arrayValue()";
	final Object ID;
	
	public JsonArray() {
		super();
		ID = new Object();
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
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
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
	public boolean booleanValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	
}
