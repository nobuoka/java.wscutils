package info.vividcode.util.oauth;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;

/**
 * An encoder for encoding character data using percent-encoding method
 * for OAuth 1.0 Protocol.
 * The method is specified in
 * <a href="http://tools.ietf.org/html/rfc5849">RFC 5849</a>.
 *
 * @author NOBUOKA Yu
 */
public class OAuthEncoder {

    /** Byte codes of ASCII-encoded characters "0123456789ABCDEF". */
    private static final byte[] BS = { 48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70 };
    /** Elements of this array represent whether the byte specified as index need to be encoded.
     * (Range of target bytes are just from 0x00 to 0x7F. All other bytes are need to be encoded.) */
    private static final boolean[] NEED_ENCODE = new boolean[0x80];
    static {
        for (int i = 0; i < NEED_ENCODE.length; i++) {
            // A(65)-Z(90), a(97)-z(122), 0(48)-9(57), -(45), .(46), _(95), ~(126)
            if ((65 <= i && i <= 90) || (97 <= i && i <= 122) ||
                    (48 <= i && i <= 57) || i == 45 || i == 46 || i == 95 || i == 126) {
                NEED_ENCODE[i] = false;
            } else {
                NEED_ENCODE[i] = true;
            }
        }
    }

    /**
     * can't instantiate this class
     */
    private OAuthEncoder() {}

    /**
     * Encodes a character string using percent-encoding method
     * for OAuth 1.0 Protocol.
     *
     * @param str The character string to encode. Must not be null.
     * @return The resulting encoded character string.
     */
    public static String encode(String str) {
        // Text values are first encoded as UTF-8 octets per [RFC3629] if
        // they are not already.
        // See: RFC 5849, 3.6. Percent Encoding
        return encode(str.getBytes(Charset.forName("UTF-8")));
    }

    /**
     * Encodes a byte array using percent-encoding method
     * for OAuth 1.0 Protocol.
     *
     * @param bytes The byte array to encode. Must not be null.
     * @return The resulting encoded character string.
     */
    public static String encode(byte[] bytes) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length);
        for (byte b : bytes) {
            if (b < 0 || NEED_ENCODE[b]) {
                os.write(37); // "%"
                os.write(BS[(b >> 4) & 0x0F]); // Upper four bits
                os.write(BS[b & 0x0F]); // Lower four bits
            } else {
                os.write(b);
            }
        }
        return new String(os.toByteArray(), Charset.forName("US-ASCII"));
        // ByteArrayOutputStream#close() method has no effect, so it need not be called.
    }

}
