package com.yw.exception;

public class InvalidDateException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3983215559946502405L;

	public InvalidDateException(){
		super();
	}
	
	public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateException(String message) {
        super(message);
    }

    public InvalidDateException(Throwable cause) {
        super(cause);
    }
}
