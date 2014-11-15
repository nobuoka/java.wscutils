/*
Copyright 2011-2014 NOBUOKA Yu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package info.vividcode.util.oauth;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Locale;

import org.junit.Test;

public class OAuthEncoderTest {

    final String CAPITAL_ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final String SMALL_ALPHA = CAPITAL_ALPHA.toLowerCase(Locale.US);
    final String DIGITS = "0123456789";
    final String UNRESERVED_SIGN_CHARS = "-._~";

    @Test
    public void encode_unreservedCharacterSetMustNotBeEncoded() {
        // Characters in the unreserved character set as defined by
        // [RFC3986], Section 2.3 (ALPHA, DIGIT, "-", ".", "_", "~") MUST
        // NOT be encoded.
        // See: https://tools.ietf.org/html/rfc5849#section-3.6

        assertEquals("Capital alphabet letters are not encoded. (string)",
                CAPITAL_ALPHA, OAuthEncoder.encode(CAPITAL_ALPHA));
        assertEquals("Capital alphabet letters are not encoded. (byte array)",
                CAPITAL_ALPHA, OAuthEncoder.encode(CAPITAL_ALPHA.getBytes(Charset.forName("US-ASCII"))));

        assertEquals("Lower alphabet letters are not encoded. (string)",
                SMALL_ALPHA, OAuthEncoder.encode(SMALL_ALPHA));
        assertEquals("Lower alphabet letters are not encoded. (byte array)",
                SMALL_ALPHA, OAuthEncoder.encode(SMALL_ALPHA.getBytes(Charset.forName("US-ASCII"))));

        assertEquals("Digit letters are not encoded. (string)",
                DIGITS, OAuthEncoder.encode(DIGITS));
        assertEquals("Digit letters are not encoded. (byte array)",
                DIGITS, OAuthEncoder.encode(DIGITS.getBytes(Charset.forName("US-ASCII"))));

        assertEquals("Unreserved sign characters are not encoded. (string)",
                UNRESERVED_SIGN_CHARS, OAuthEncoder.encode(UNRESERVED_SIGN_CHARS));
        assertEquals("Unreserved sign characters are not encoded. (byte array)",
                UNRESERVED_SIGN_CHARS, OAuthEncoder.encode(UNRESERVED_SIGN_CHARS.getBytes(Charset.forName("US-ASCII"))));
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

        // Test all other bytes.
        HashSet<Byte> unescapedBytes = new HashSet<Byte>();
        String unescaped = CAPITAL_ALPHA + SMALL_ALPHA + DIGITS + UNRESERVED_SIGN_CHARS;
        for (byte b : unescaped.getBytes(Charset.forName("US-ASCII"))) {
            unescapedBytes.add(b);
        }
        StringBuilder expectedBuilder = new StringBuilder();
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        for (byte b = -128; b < 127; b++) {
            if (!unescapedBytes.contains(b)) {
                bb.write(b);
                expectedBuilder.append('%').append(String.format(Locale.US, "%02X", b));
            }
        }
        assertEquals("All other bytes are encoded.",
                expectedBuilder.toString(), OAuthEncoder.encode(bb.toByteArray()));
        // ByteArrayOutputStream object need not be closed.
    }

}
