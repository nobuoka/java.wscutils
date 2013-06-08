package info.vividcode.util.json;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonArrayTest {

    private JsonArray mJsonArray;

    @Before
    public void setUp() {
        mJsonArray = new JsonArray();
    }

    @Test
    public void testJsonArray() {
        // nothing to test?
        //fail("Not yet implemented");
    }

    @Test
    public void testValueType() {
        assertEquals( JsonValue.ValueType.ARRAY_VALUE,
                new JsonArray().valueType() );
    }

    @Test
    public void testArrayValue() {
        assertEquals( mJsonArray, mJsonArray.asArray() );
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodObjectValue() {
        mJsonArray.asObject();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodBooleanValue() {
        mJsonArray.booleanValue();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodNumberValue() {
        mJsonArray.numberValue();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodStringValue() {
        mJsonArray.stringValue();
    }

}
