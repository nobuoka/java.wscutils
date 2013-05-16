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
	
	@Test
	public void testArrayValue() {
		assertTrue( mJsonNull.arrayValue() == null );
	}

	@Test
	public void testUnusableMethodObjectValue() {
		assertTrue( mJsonNull.objectValue() == null );
	}

	@Test
	public void testUnusableMethodBooleanValue() {
		assertTrue( mJsonNull.booleanValue() == null );
	}

	@Test
	public void testUnusableMethodNumberValue() {
		assertTrue( mJsonNull.numberValue() == null );
	}

	@Test
	public void testUnusableMethodStringValue() {
		assertTrue( mJsonNull.stringValue() == null );
	}

}
