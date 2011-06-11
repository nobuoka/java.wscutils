package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonBoolean implements JsonValue {
	
	private static final String CLASS_NAME  = "JsonBoolean";
	private static final String METHOD_NAME = "booleanValue()";
	
	private Boolean val;
	
	private JsonBoolean( Boolean b ) {
		val = b;
	}

	static final public JsonBoolean TRUE  = new JsonBoolean( true );
	static final public JsonBoolean FALSE = new JsonBoolean( false );
	
	@Override
	public ValueType valueType() {
		return ValueType.BOOLEAN_VALUE;
	}

	@Override
	public JsonArray arrayValue() {
		throw new UnsupportedOperationException( 
				"This object is a " + CLASS_NAME + " object. " +
				"if you want to get the value, please use the " + METHOD_NAME + " method instead." );
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
		return val;
	}
	
	@Override
	public String toString() {
		return "[JSON boolean : " + ( val ? "true" : "false" ) + "]";
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o instanceof JsonBoolean ) {
			JsonBoolean oo = (JsonBoolean) o;
			return this.val.equals( oo.val );
		} else {
			return false;
		}
	}

}
