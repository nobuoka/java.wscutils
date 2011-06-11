package info.vividcode.util.json;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import info.vividcode.util.json.JsonValue.ValueType;

public class JsonSerializer {
	
	static private void serializeJObject( StringBuilder sb, JsonObject jobject, Set<Object> ancestorIdSet ) {
		// 先祖に自分自身が存在するかどうかチェック
		if( ancestorIdSet.contains( jobject.ID ) ) {
			throw new InvalidJsonException( "Can't serialize a recursive JSON structure" );
		}
		ancestorIdSet.add( jobject.ID );
		sb.append( "{" );
		String[] keys = jobject.keySet().toArray( new String[0] );
		if( keys.length != 0 ) {
			String key = keys[0];
			serializeJString( sb, key );
			sb.append( ":" );
			serializeAnyJValue( sb, jobject.get( key ), ancestorIdSet );
		}
		for( int i = 1; i < keys.length; i++ ) {
			sb.append( "," );
			String key = keys[i];
			serializeJString( sb, key );
			sb.append( ":" );
			serializeAnyJValue( sb, jobject.get( key ), ancestorIdSet );
		}
		sb.append( "}" );
		ancestorIdSet.remove( jobject.ID );
	}
	
	static private void serializeJArray( StringBuilder sb, JsonArray jarray, Set<Object> ancestorIdSet ) {
		// 先祖に自分自身が存在するかどうかチェック
		if( ancestorIdSet.contains( jarray.ID ) ) {
			throw new InvalidJsonException( "Can't serialize a recursive JSON structure" );
		}
		ancestorIdSet.add( jarray.ID );
		sb.append( "[" );
		JsonValue[] vs = jarray.toArray( new JsonValue[0] );
		if( vs.length != 0 ) {
			serializeAnyJValue( sb, vs[0], ancestorIdSet );
		}
		for( int i = 1; i < vs.length; i++ ) {
			sb.append( "," );
			serializeAnyJValue( sb, vs[i], ancestorIdSet );
		}
		sb.append( "]" );
		ancestorIdSet.remove( jarray.ID );
	}
	
	static void serializeJString( StringBuilder sb, String str ) {
		sb.append( "\"" );
		// unescaped = %x20-21 / %x23-5B / %x5D-10FFFF
		int numCodePoints = str.codePointCount( 0, str.length() );
		// TODO code points の扱いが正しくない
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
	
	static void serializeAnyJValue( StringBuilder sb, JsonValue jvalue, Set<Object> ancestorIdSet ) {
		//if( jvalue == null ) {
		//	sb.append( "null" );
		//} else {
			switch( jvalue.valueType() ) {
				case OBJECT_VALUE:
					serializeJObject( sb, jvalue.objectValue(), ancestorIdSet );
					break;
				case ARRAY_VALUE:
					serializeJArray( sb, jvalue.arrayValue(), ancestorIdSet );
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
					throw new InvalidJsonException( "Unknown Value Type [" + jvalue.valueType() + "]" );
			}
		//}
	}
	
	/** JsonObject でも JsonArray でもない値をシリアライズしようとしたときのエラーメッセージ */
	private static final String ERRMSG_NOT_OBJ_OR_ARR_SERIALIZATION = 
		"The JSON Value which is serialization target must be JsonObject or JsonArray.";
	
	static public String serialize( JsonValue jvalue ) {
		// array か object でなければいけない
		if( jvalue.valueType() != ValueType.ARRAY_VALUE && 
				jvalue.valueType() != ValueType.OBJECT_VALUE ) {
			// TODO
			throw new InvalidJsonException( ERRMSG_NOT_OBJ_OR_ARR_SERIALIZATION );
		}
		StringBuilder sb = new StringBuilder();
		serializeAnyJValue( sb, jvalue, new HashSet<Object>() );
		return sb.toString();
	}
	
}
