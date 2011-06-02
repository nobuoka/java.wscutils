package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonBoolean implements JsonValue {
	
	private boolean val;
	
	private JsonBoolean( boolean b ) {
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
		return val;
	}
	
	@Override
	public String toString() {
		return "[JSON boolean : " + ( val ? "true" : "false" ) + "]";
	}

}
