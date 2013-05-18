package info.vividcode.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.NoSuchElementException;

/**
 * Base64 エンコードを行う処理を提供するクラス.
 * @author nobuoka
 *
 */
public class Base64Encoder {

    static final private byte[] CONVERT_TABLE =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
        .getBytes( Charset.forName("US-ASCII") );

    static private class Bits6Iterator {
        private byte[] bytes;
        private int bytePos;
        private int bitPos;
        public Bits6Iterator(byte[] bytes) {
            bytePos = 0;
            bitPos = 0;
            this.bytes = bytes;
        }
        public boolean hasNext() {
            return bytePos < bytes.length;
        }
        public byte next() {
            int b = 0;
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (bitPos + 6 <= 8) {
                b = ( bytes[bytePos] >> ( 2 - bitPos ) ) & 0x3F;
                if ((bitPos += 6) == 8) {
                    bytePos++;
                    bitPos = 0;
                }
            } else {
                b += ( bytes[bytePos] << ( bitPos - 2 ) ) & 0x3F;
                bytePos++;
                bitPos -= 2;
                if (hasNext()) {
                    b += ( (int)( bytes[bytePos] ) & 0xFF ) >> ( 8 - bitPos );
                }
            }
            return (byte)b;
        }
    }

    // cant instantiate from this class
    private Base64Encoder() {}

    /**
     * バイト列を Base64 エンコードする.
     * @param bytes Base64 エンコードを行う対象のバイト列
     * @return bytes を Base64 エンコードした結果の文字列
     */
    static public String encode(byte[] bytes) {
        ByteArrayOutputStream os = null;
        byte[] res = null;
        try {
            os = new ByteArrayOutputStream();
            int count = 0;
            Bits6Iterator b6r = new Bits6Iterator(bytes);
            while (b6r.hasNext()) {
                os.write( CONVERT_TABLE[ b6r.next() ] );
                count++;
            }
            for (int i = ( 4 - (count % 4) ) % 4; 0 < i; i--) {
                os.write((byte)61);
            }
            res = os.toByteArray();
        } finally {
            try {
                // close する意味はないが, 一応
                if (os != null) os.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        }
        return new String(res, Charset.forName("US-ASCII"));
    }

}
