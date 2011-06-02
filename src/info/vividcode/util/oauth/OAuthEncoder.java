package info.vividcode.util.oauth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * The OAuth 1.0 Protocol の仕様に合う形で文字列をパーセントエンコードする
 * 機能を提供するクラス
 * @author nobuoka
 */
public class OAuthEncoder {
	
	/** "0123456789ABCDEF" の ASCII バイト列 */
	static final private byte[] BS = { 48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70 };
	/** 指定のバイトをパーセントエンコードする必要があるかどうかの真理値を格納した配列 
	 * (インデックスがバイト値に対応. ただし最上位ビットが 1 のものは含まない) */
	static final private boolean[] NEED_ENCODE = new boolean[ 0x7F ];
	
	// NEED_ENCODING の初期化
	static {
		for( int i = 0; i < NEED_ENCODE.length; i++ ) {
			// a(97)-z(122), A(65)-Z(90), 0(48)-9(57), -(45), .(46), _(95), ~(126)
			if( ( 65 <= i && i <= 90 ) || ( 97 <= i && i <= 122) || 
					( 48 <= i && i <= 57 ) || i == 45 || i == 46 || i == 95 || i == 126 ) {
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
	 * The OAuth 1.0 Protocol の仕様に合う形で文字列をパーセントエンコードする.
	 * パーセントエンコードの対象になるのは 'A'-'Z', 'a'-'z', '0'-'9', '-', '.', '_', '~' を除く全ての文字である. 
	 * @param str パーセントエンコードの対象文字列
	 * @return str をパーセントエンコードした文字列
	 */
	static public String encode( String str ) {
		String encodedStr = null;
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			for( byte b : str.getBytes( Charset.forName( "UTF-8" ) ) ) {
				if( b < 0 || NEED_ENCODE[b] ) {
					// "%"
					os.write( 37 );
					// 上の 4 ビット
					os.write( BS[( b >> 4 ) & 0x0F] );
					// 下の 4 ビット
					os.write( BS[b & 0x0F] );
				} else {
					os.write( b );
				}
			}
			encodedStr = os.toString();
		} finally {
			try {
				// close する意味はないが, 一応
				if( os != null ) os.close();
			} catch( IOException err ) {
				err.printStackTrace();
			}
		}
		return encodedStr;
	}
	
}
