package info.vividcode.util.json;


import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class JsonSerializerTest {
	
	@Test
	public void testJsonArray() {
		JsonArray sorc = new JsonArray(); 
		sorc.add( new JsonNumber( new BigDecimal( 100 ) ) );
		JsonValue dist = JsonParser.parse( JsonSerializer.serialize( sorc ) );
		assertEquals( sorc, dist );
	}
	
	@Test
	public void testJsonObject() {
		JsonObject sorc = new JsonObject(); 
		sorc.put( "test", new JsonNumber( new BigDecimal( 100 ) ) );
		JsonValue dist = JsonParser.parse( JsonSerializer.serialize( sorc ) );
		assertEquals( sorc, dist );
	}
	
	/**
	 * 再帰的な構造を持つ際にシリアライズ不可能であることを確認する.
	 */
	@Test( expected = InvalidJsonException.class )
	public void testRecursiveJsonArray() {
		JsonArray sorc = new JsonArray(); 
		sorc.add( new JsonNumber( new BigDecimal( 100 ) ) );
		sorc.add( sorc );
		JsonParser.parse( JsonSerializer.serialize( sorc ) );
	}
	
}
