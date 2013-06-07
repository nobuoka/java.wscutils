package info.vividcode.util.json;


import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Test;

public class JsonParserTest {

    /**
     * 無効な JSON 文字列のパージングの際の例外発生をテストする.
     * 空文字列の場合.
     */
    @Test( expected = InvalidJsonException.class )
    public void testInvalidStringEmpty() {
        String invalidJsonString = "";
        JsonParser.parse(invalidJsonString);
    }

    /**
     * 無効な JSON 文字列のパージングの際の例外発生をテストする.
     * 配列にもオブジェクトにも入っていない素の数字.
     */
    @Test( expected = InvalidJsonException.class )
    public void testInvalidStringOnlyNumber() {
        String invalidJsonString = "200";
        JsonParser.parse(invalidJsonString);
    }

    /**
     * 空の JSON Array のパージングをテストする.
     */
    @Test
    public void testEmptyArrayParsing() {
        String jsonString = "[]";
        JsonArray jarray = JsonParser.parse(jsonString).arrayValue();
        assertEquals( 0, jarray.size() );
    }

    /**
     * 空の JSON Object のパージングをテストする.
     */
    @Test
    public void testEmptyObjectParsing() {
        String jsonString = "{}";
        JsonObject jobject = JsonParser.parse(jsonString).objectValue();
        assertEquals( 0, jobject.size() );
    }

    /**
     * JSON Number パージングをテストする.
     */
    @Test
    public void testNumberParsing() {
        String jsonString = null;
        String[] targetNumStrs = new String[]{
            "200", "-0", "5.60", "8888.8888E-10"
        };
        for( String s : targetNumStrs ) {
            jsonString = "[ " + s + " ]";
            JsonValue v = JsonParser.parse(jsonString).arrayValue().get(0);
            assertEquals( JsonValue.ValueType.NUMBER_VALUE, v.valueType() );
            assertEquals( new BigDecimal(s), v.numberValue() );
        }
    }

    @Test
    public void testNormalStringParsing() {
        String jsonString = "[ \"abcd0123あいうえお\" ]";
        JsonValue v = JsonParser.parse(jsonString).arrayValue().get(0);
        assertEquals( JsonValue.ValueType.STRING_VALUE, v.valueType() );
        assertEquals( "abcd0123あいうえお", v.stringValue() );
    }

    @Test
    public void testEscapedStringParsing() {
        String jsonString = "[ \"\\u0020\" ]";
        JsonValue v = JsonParser.parse(jsonString).arrayValue().get(0);
        assertEquals( JsonValue.ValueType.STRING_VALUE, v.valueType() );
        assertEquals( " ", v.stringValue() );
    }

    @Test( expected = InvalidJsonException.class )
    public void testInvalidEscapedStringParsing() {
        String jsonString = "[ \"\\u20\" ]";
        JsonParser.parse(jsonString);
    }

}
