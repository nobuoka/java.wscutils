package info.vividcode.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author nobuoka
 */
public class Base64EncoderTest {

    @Test
    public void encodeBytes() {
        byte[] target;
        String b64encodedStr;

        target = new byte[]{ 0 };
        b64encodedStr = Base64Encoder.encode(target);
        assertEquals("AA==", b64encodedStr);

        target = new byte[]{ 0, 0 };
        b64encodedStr = Base64Encoder.encode(target);
        assertEquals("AAA=", b64encodedStr);

        target = new byte[]{ 77, 97, 110 };
        b64encodedStr = Base64Encoder.encode(target);
        assertEquals("TWFu", b64encodedStr);

        target = new byte[]{ -1, -128, 127, 0 };
        b64encodedStr = Base64Encoder.encode(target);
        assertEquals("/4B/AA==", b64encodedStr);
    }

    @Test
    public void encodeEmptyBytes() {
        String b64encodedStr = Base64Encoder.encode(new byte[0]);
        assertEquals("", b64encodedStr);
    }

    @Test( expected = IllegalArgumentException.class )
    public void encodeNull() {
        Base64Encoder.encode(null);
    }

    @Test
    public void encodeLongBytes() {
        byte[] bytesUnit = new byte[]{ 77, 97, 110 }; // length must be 3
        String expectedStrUnit = "TWFu";
        int loopCount = 200;

        byte[] target = new byte[3*loopCount];
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < loopCount; ++i) {
            System.arraycopy(bytesUnit, 0, target, i*3, 3);
            sb.append(expectedStrUnit);
        }
        String expectedStr = sb.toString();

        assertEquals(expectedStr, Base64Encoder.encode(target));
    }

}
