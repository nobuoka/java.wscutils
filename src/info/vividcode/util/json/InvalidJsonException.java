package info.vividcode.util.json;

public class InvalidJsonException extends RuntimeException {

	private static final long serialVersionUID = 7931499555717836599L;
	
	InvalidJsonException( String message ) {
		super( message );
	}

}
