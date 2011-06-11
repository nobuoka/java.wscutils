package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonString implements JsonValue {
	
	private static final String CLASS_NAME  = "JsonString";
	private static final String METHOD_NAME = "stringValue()";
	
	private String val;
	public JsonString( String str ) {
		this.val = str;
	}
	
	@Override
	public ValueType valueType() {
		return ValueType.STRING_VALUE;
	}
	@Override
	public JsonArray arrayValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"If you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	@Override
	public JsonObject objectValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"If you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	@Override
	public BigDecimal numberValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"If you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	@Override
	public String stringValue() {
		return val;
	}
	@Override
	public Boolean booleanValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"If you want to get the value, please use the " + METHOD_NAME + " method instead." );
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		JsonSerializer.serializeJString( sb, val );
		return "[Json string : " + sb.toString() + "]";
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o instanceof JsonString ) {
			JsonString oo = (JsonString) o;
			return this.val.equals( oo.val );
		} else {
			return false;
		}
	}

}
