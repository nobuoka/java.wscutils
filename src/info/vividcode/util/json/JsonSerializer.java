package info.vividcode.util.json;

import java.math.BigDecimal;

import info.vividcode.util.json.JsonValue.ValueType;

public class JsonSerializer {
	
	static private void serializeJObject( StringBuilder sb, JsonObject jobject ) {
		sb.append( "{" );
		String[] keys = jobject.keySet().toArray( new String[0] );
		if( keys.length != 0 ) {
			String key = keys[0];
			serializeJString( sb, key );
			sb.append( ":" );
			serializeAnyJValue( sb, jobject.get( key ) );
		}
		for( int i = 1; i < keys.length; i++ ) {
			sb.append( "," );
			String key = keys[i];
			serializeJString( sb, key );
			sb.append( ":" );
			serializeAnyJValue( sb, jobject.get( key ) );
		}
		sb.append( "}" );
	}
	
	static private void serializeJArray( StringBuilder sb, JsonArray jarray ) {
		sb.append( "[" );
		JsonValue[] vs = jarray.toArray( new JsonValue[0] );
		if( vs.length != 0 ) {
			serializeAnyJValue( sb, vs[0] );
		}
		for( int i = 1; i < vs.length; i++ ) {
			sb.append( "," );
			serializeAnyJValue( sb, vs[i] );
		}
		sb.append( "]" );
	}
	
	static void serializeJString( StringBuilder sb, String str ) {
		sb.append( "\"" );
		// unescaped = %x20-21 / %x23-5B / %x5D-10FFFF
		int numCodePoints = str.codePointCount( 0, str.length() );
		for( int i = 0; i < numCodePoints; i++ ) {
			int cp = str.codePointAt( i );
			if( cp == 0x20 || cp == 0x21 || ( 0x23 <= cp && cp <= 0x5B ) ||
					( 0x5D <= cp && cp <= 0x10FFFF ) ) {
				sb.appendCodePoint( cp );
			} else {
				if( 0x10FFFF < cp ) {
					// TODO
					throw new RuntimeException();
				}
				sb.append( "\\" );
				switch( cp ) {
					case 0x22: // "\""
					case 0x5C: // "\\"
					case 0x2F: // "/" // ここには到達しない
						sb.appendCodePoint( cp );
						break;
					case 0x08: sb.append( "b" ); break;
					case 0x0C: sb.append( "f" ); break;
					case 0x0A: sb.append( "n" ); break;
					case 0x0D: sb.append( "r" ); break;
					case 0x09: sb.append( "t" ); break;
					default:
						sb.append( String.format( "u%04X", cp ) );
						break;
				}
			}
		}
		sb.append( "\"" );
	}
	
	static private void serializeJNumber( StringBuilder sb, BigDecimal dec ) {
		sb.append( dec.toString() );
	}
	
	static void serializeAnyJValue( StringBuilder sb, JsonValue jvalue ) {
		//if( jvalue == null ) {
		//	sb.append( "null" );
		//} else {
			switch( jvalue.valueType() ) {
				case OBJECT_VALUE:
					serializeJObject( sb, jvalue.objectValue() );
					break;
				case ARRAY_VALUE:
					serializeJArray( sb, jvalue.arrayValue() );
					break;
				case STRING_VALUE:
					serializeJString( sb, jvalue.stringValue() );
					break;
				case NUMBER_VALUE:
					serializeJNumber( sb, jvalue.numberValue() );
					break;
				case BOOLEAN_VALUE:
					sb.append( jvalue.booleanValue() ? "true" : "false" );
					break;
				case NULL_VALUE:
					sb.append( "null" );
					break;
				default:
					// TODO
					throw new RuntimeException( "Unknown Value Type" );
			}
		//}
	}
	
	static public String serialize( JsonValue jvalue ) {
		// array か object でなければいけない
		if( jvalue.valueType() != ValueType.ARRAY_VALUE && 
				jvalue.valueType() != ValueType.OBJECT_VALUE ) {
			// TODO
			throw new RuntimeException();
		}
		StringBuilder sb = new StringBuilder();
		serializeAnyJValue( sb, jvalue );
		return sb.toString();
	}
	
}
