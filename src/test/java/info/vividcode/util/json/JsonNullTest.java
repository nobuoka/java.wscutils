package info.vividcode.util.json;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonNullTest {

    private JsonNull mJsonNull;

    @Before
    public void setUp() {
        mJsonNull = JsonNull.VALUE;
    }

    @Test
    public void testJsonNull() {
        assertTrue( mJsonNull != null );
    }

    @Test
    public void testValueType() {
        assertEquals( JsonValue.ValueType.NULL_VALUE,
                mJsonNull.valueType() );
    }

    @Test( expected = JsonTypeException.class )
    public void testArrayValue() {
        mJsonNull.asArray();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodObjectValue() {
        mJsonNull.asObject();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodBooleanValue() {
        mJsonNull.booleanValue();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodNumberValue() {
        mJsonNull.numberValue();
    }

    @Test( expected = JsonTypeException.class )
    public void testUnusableMethodStringValue() {
        mJsonNull.stringValue();
    }

}
