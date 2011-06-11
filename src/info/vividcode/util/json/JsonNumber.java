package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonNumber implements JsonValue {

	private static final String CLASS_NAME  = "JsonNumber";
	private static final String METHOD_NAME = "numberValue()";
	
	private BigDecimal val;
	
	public JsonNumber( BigDecimal val ) {
		this.val = val;
	}
	
	public JsonNumber( double val ) {
		this.val = new BigDecimal( val );
	}

	@Override
	public ValueType valueType() {
		return ValueType.NUMBER_VALUE;
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
		return val;
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
	
	@Override
	public String toString() {
		return "[JSON number : " + val.toString() + "]";
	}
	
	@Override
	public boolean equals( Object o ) {
		if( o instanceof JsonNumber ) {
			JsonNumber oo = (JsonNumber) o;
			return this.val.equals( oo.val );
		} else {
			return false;
		}
	}

}
