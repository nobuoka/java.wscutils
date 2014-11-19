/*
Copyright 2014 NOBUOKA Yu

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

package info.vividcode.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Test;

/**
 * @author NOBUOKA Yu
 */
public class UrlEncodedTest {

    @Test
    public void deserializeComponent() {
        { // Non-encoded characters.
            ByteArrayOutputStream testBytesOS = new ByteArrayOutputStream();
            for (int b = -128; b < 128; b++) {
                if (b != 0x25 && b != 0x2B) testBytesOS.write(b);
            }
            byte[] testBytes = testBytesOS.toByteArray();
            byte[] decoded = UrlEncoded.deserializeComponent(testBytes);
            assertArrayEquals("Any non-encoded byte is mapped to the same byte, if it should have been encoded.",
                    testBytes, decoded);
        }

        { // Plus sign.
            byte[] testBytes = { 0x2B }; /* '+' */
            byte[] expected = { 0x20 }; /* ' ' */
            byte[] decoded = UrlEncoded.deserializeComponent(testBytes);
            assertArrayEquals("Plus sign is mapped to space character.", expected, decoded);
        }

        { // Encoded characters.
            ByteArrayOutputStream expected = new ByteArrayOutputStream();
            StringBuilder testStrBuilder = new StringBuilder();
            for (int b = -128; b < 128; b++) {
                expected.write(b);
                testStrBuilder.append(String.format("%%%02X", (byte) b));
            }
            byte[] testBytes = testStrBuilder.toString().getBytes(Charset.forName("US-ASCII"));
            byte[] decoded = UrlEncoded.deserializeComponent(testBytes);
            assertArrayEquals("Encoded characters are decoded.", expected.toByteArray(), decoded);
        }

        { // Invalid percent encoding characters.
            String[][] testTargetAndExpectedPairs = {
                    { "Last percent sign is mapped to percent sign.", "a%", "a%" },
                    { "Percent sign not followed by two hex digit (just one hex digit)", "%0", "%0" },
                    { "Percent sign not followed by two hex digit (non hex digit)", "%z", "%z" },
                    { "Percent sign not followed by two hex digit (one hex digit and non hex digit)", "%0_", "%0_" },
                    { "Percent sign not followed by two hex digit (plus sign)", "%+02", "% 02" },
                    { "Percent sign not followed by two hex digit (percent sign)", "%%41", "%A" },
            };
            for (String[] pair : testTargetAndExpectedPairs) {
                String message = pair[0];
                byte[] testBytes = pair[1].getBytes(Charset.forName("US-ASCII"));
                byte[] expected = pair[2].getBytes(Charset.forName("US-ASCII"));
                byte[] decoded = UrlEncoded.deserializeComponent(testBytes);
                assertArrayEquals(message, expected, decoded);
            }
        }
    }

    @Test
    public void serializeComponent() {
        { // Characters not to be encoded.
            ByteArrayOutputStream testBytesOS = new ByteArrayOutputStream();
            for (char c = '0'; c <= '9'; c++) testBytesOS.write((byte) c);
            for (char c = 'A'; c <= 'Z'; c++) testBytesOS.write((byte) c);
            for (char c = 'a'; c <= 'z'; c++) testBytesOS.write((byte) c);
            for (char b : Arrays.asList('*', '-', '.', '_')) testBytesOS.write((byte) b);
            byte[] testBytes = testBytesOS.toByteArray();
            String serialized = UrlEncoded.serializeComponent(testBytes);
            assertEquals("ASCII alphanumeric, '*', '-', '.', and '_' characters are not encoded.",
                    new String(testBytes, Charset.forName("US-ASCII")), serialized);
        }

        { // Space character.
            byte[] testBytes = { 0x20 }; /* ' ' */
            String expected = "+";
            String serialized = UrlEncoded.serializeComponent(testBytes);
            assertEquals("Space character is mapped to plus sign.",
                    expected, serialized);
        }

        { // Characters to be encoded.
            ByteArrayOutputStream testBytesOS = new ByteArrayOutputStream();
            StringBuilder expected = new StringBuilder();
            for (int b = -128; b < 128; b++) {
                boolean isAlphaNumeric = (('0' <= b && b <= '9') || ('A' <= b && b <= 'Z') || (('a' <= b && b <= 'z')));
                boolean isSignNotToBeEncoded = (b == '*' || b == '-' || b == '.' || b == '_');
                boolean isSpaceChar = (b == ' ');
                if (!isAlphaNumeric && !isSignNotToBeEncoded && !isSpaceChar) {
                    testBytesOS.write(b);
                    expected.append(String.format("%%%02X", (byte) b));
                }
            }
            byte[] testBytes = testBytesOS.toByteArray();
            String serialized = UrlEncoded.serializeComponent(testBytes);
            assertEquals("Bytes except ASCII Alphanumeric, '*', '-', '.', '_', and ' ' are percent-encoded.",
                    expected.toString(), serialized);
        }
    }

}
