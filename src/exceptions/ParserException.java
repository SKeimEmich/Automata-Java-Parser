package exceptions;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 1L; // unused, keeps compiler happy
	
	public ParserException(String message) {
		super(message);
	}

}