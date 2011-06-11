package info.vividcode.util.json;

/**
 *無効な JSON 文字列をパースしようとしたときや, 
 *無効な JSON オブジェクト (再帰的な構造など) をシリアライズしようとしたときなどに発生する例外.
 *
 *@author nobuoka
 */
public class InvalidJsonException extends RuntimeException {

	private static final long serialVersionUID = 7931499555717836599L;
	
	InvalidJsonException( String message ) {
		super( message );
	}

}
