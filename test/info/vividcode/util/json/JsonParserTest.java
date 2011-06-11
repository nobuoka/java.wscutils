package info.vividcode.util.json;


import static org.junit.Assert.*;

import org.junit.Test;

public class JsonParserTest {
	
	/**
	 * 無効な JSON 文字列のパージングの際の例外発生をテストする.
	 * 空文字列の場合.
	 */
	@Test( expected = InvalidJsonException.class )
	public void testInvalidStringEmpty() {
		String invalidJsonString = "";
		JsonParser.parse( invalidJsonString );
	}
	
	/**
	 * 無効な JSON 文字列のパージングの際の例外発生をテストする.
	 * 配列にもオブジェクトにも入っていない素の数字.
	 */
	@Test( expected = InvalidJsonException.class )
	public void testInvalidStringOnlyNumber() {
		String invalidJsonString = "200";
		JsonParser.parse( invalidJsonString );
	}
	
	/**
	 * 空の JSON Array のパージングをテストする.
	 */
	@Test
	public void testEmptyArrayParsing() {
		String jsonString = "[]";
		JsonArray jarray = JsonParser.parse( jsonString ).arrayValue();
		assertEquals( 0, jarray.size() );
	}
	
	/**
	 * 空の JSON Object のパージングをテストする.
	 */
	@Test
	public void testEmptyObjectParsing() {
		String jsonString = "{}";
		JsonObject jobject = JsonParser.parse( jsonString ).objectValue();
		assertEquals( 0, jobject.size() );
	}
	
}
