package info.vividcode.util.json;

import java.math.BigDecimal;

public class JsonString implements JsonValue {
	private String str;
	public JsonString( String str ) {
		this.str = str;
	}
	@Override
	public ValueType valueType() {
		return ValueType.STRING_VALUE;
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
		return str;
	}
	@Override
	public boolean booleanValue() {
		// TODO Auto-generated method stub
		throw new RuntimeException();
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		JsonSerializer.serializeJString( sb, str );
		return "[Json string : " + sb.toString() + "]";
	}

}
