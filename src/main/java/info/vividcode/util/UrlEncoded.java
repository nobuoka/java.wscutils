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

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;

import static java.util.Arrays.asList;

/**
 * This class treats the <code>application/x-www-form-urlencoded</code> format.
 *
 * See: <a href="https://url.spec.whatwg.org/#application/x-www-form-urlencoded"
 * >URL Living Standard, 6. <code>application/x-www-form-urlencoded</code></a>
 *
 * @author NOBUOKA Yu
 */
class UrlEncoded {

    /* Byte codes of ASCII-encoded characters "0123456789ABCDEF". */
    private static final char[] HD = { 48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70 };

    private static final BigInteger NON_ENCODING_BYTE_FLAGS;
    static {
        final int len = 17; // BigInteger has sign, so most significant bit must be set to zero.
        byte[] bb = new byte[len];
        for (int i : asList(0x2A, 0x2D, 0x2E, 0x5F)) bb[len - 1 - (i/8)] |= 1L << (i%8);
        for (int i = 0x30; i <= 0x39; i++) bb[len - 1 - (i/8)] |= 1L << (i%8);
        for (int i = 0x41; i <= 0x5A; i++) bb[len - 1 - (i/8)] |= 1L << (i%8);
        for (int i = 0x61; i <= 0x7A; i++) bb[len - 1 - (i/8)] |= 1L << (i%8);
        NON_ENCODING_BYTE_FLAGS = new BigInteger(bb);
    }

    private static byte parseHex(char c) throws NumberFormatException {
        if ('0' <= c && c <= '9') {
            return (byte) (c - '0');
        } else if ('A' <= c && c <= 'F') {
            return (byte) (c - 'A' + 10);
        } else if ('a' <= c && c <= 'f') {
            return (byte) (c - 'a' + 10);
        } else {
            throw new NumberFormatException("Character `" + c + "` is not hexadecimal digit.");
        }
    }

    /**
     * The <code>application/x-www-form-urlencoded</code> byte deserialization.
     * ASCII plus sign (0x2B) is mapped to ASCII space character (0x20).
     * Percent-encoded bytes are decoded.
     * If there is percent sign not followed by two hex digit,
     * then that percent sign is treated as row percent sign. (Error doesn't occur.)
     *
     * @param bb Byte sequence to be deserialized.
     * @return Result of deserialization.
     */
    public static byte[] deserializeComponent(byte[] bb) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int pos = 0;
        while (pos < bb.length) {
            byte c = bb[pos];
            switch (c) {
            case 0x2B /* '+' */:
                out.write(0x20);
                break;
            case 0x25 /* '%' */ :
                byte d;
                try {
                    byte upperHex = parseHex((char) bb[pos + 1]);
                    byte lowerHex = parseHex((char) bb[pos + 2]);
                    d = (byte) (upperHex * 0x10 + lowerHex);
                    pos += 2;
                } catch (IndexOutOfBoundsException ex) {
                    d = (byte) '%';
                } catch (NumberFormatException ex) {
                    d = (byte) '%';
                }
                out.write(d);
                break;
            default:
                out.write(c);
                break;
            }
            pos++;
        }
        return out.toByteArray();
    }

    /**
     * The <code>application/x-www-form-urlencoded</code> byte serializer.
     * ASCII space character (0x20) is mapped to ASCII plus sign ('+').
     * A byte of ASCII alphanumeric (0x30 to 0x39, 0x41 to 0x5A, and 0x61 to 0x7A),
     * 0x2A, 0x2D, 0x2E, or 0x5F is mapped to a character which has a code point
     * whose value is byte. All other bytes are percent-encoded.
     *
     * See: <a href="https://url.spec.whatwg.org/#concept-urlencoded-byte-serializer"
     * ><code>application/x-www-form-urlencoded</code> byte serializer</a>
     *
     * @param bb Byte sequence to be serialized.
     * @return Result of serialization, which contains only alphanumeric characters, '%', '+', '*', '-', '.', and '_'.
     */
    public static String serializeComponent(byte[] bb) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bb) {
            if (b == 0x20) {
                sb.append('+');
            } else if (NON_ENCODING_BYTE_FLAGS.testBit(b & 0xFF)) {
                sb.append((char) b);
            } else {
                sb.append('%');
                sb.append(HD[(b >> 4) & 0x0F]); // Upper four bits
                sb.append(HD[(b >> 0) & 0x0F]); // Lower four bits
            }
        }
        return sb.toString();
    }

}
