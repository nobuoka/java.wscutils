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
		assertEquals( mJsonArray, mJsonArray.arrayValue() );
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnusableMethodObjectValue() {
		mJsonArray.objectValue();
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnusableMethodBooleanValue() {
		mJsonArray.booleanValue();
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnusableMethodNumberValue() {
		mJsonArray.numberValue();
	}

	@Test( expected = UnsupportedOperationException.class )
	public void testUnusableMethodStringValue() {
		mJsonArray.stringValue();
	}

}
