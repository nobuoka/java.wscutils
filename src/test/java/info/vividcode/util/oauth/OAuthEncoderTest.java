package info.vividcode.util.oauth;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

public class OAuthEncoderTest {

    @Test
    public void encode_unreservedCharacterSetMustNotBeEncoded() {
        // Characters in the unreserved character set as defined by
        // [RFC3986], Section 2.3 (ALPHA, DIGIT, "-", ".", "_", "~") MUST
        // NOT be encoded.
        // See: https://tools.ietf.org/html/rfc5849#section-3.6

        String capitalAlpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        assertEquals("Capital alphabet letters are not encoded.",
                capitalAlpha, OAuthEncoder.encode(capitalAlpha));

        String lowerAlpha = capitalAlpha.toLowerCase(Locale.US);
        assertEquals("Lower alphabet letters are not encoded.",
                lowerAlpha, OAuthEncoder.encode(lowerAlpha));

        String digits = "0123456789";
        assertEquals("Digit letters are not encoded.",
                digits, OAuthEncoder.encode(digits));

        String unreservedSignChars = "-._~";
        assertEquals("Unreserved sign characters are not encoded.",
                unreservedSignChars, OAuthEncoder.encode(unreservedSignChars));
    }

    @Test
    public void encode_otherCharactersMustBeEncoded() {
        // *  All other characters MUST be encoded.
        // *  The two hexadecimal characters used to represent encoded
        //   characters MUST be uppercase.
        // See: https://tools.ietf.org/html/rfc5849#section-3.6

        String str = "(あ) good!\u007F\u0080";
        assertEquals("All other characters are encoded.",
                "%28%E3%81%82%29%20good%21%7F%C2%80", OAuthEncoder.encode(str));
    }

}
