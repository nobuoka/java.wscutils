package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonNumber implements JsonValue {
	
	
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
		return val;
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
		return "[JSON number : " + val.toString() + "]";
	}

}
