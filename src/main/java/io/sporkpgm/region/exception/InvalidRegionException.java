package io.sporkpgm.region.exception;

public class InvalidRegionException extends Exception {

	private static final long serialVersionUID = -8988962714866893205L;

	String message;

	public InvalidRegionException(String message) {
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

}
