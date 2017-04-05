package com.memory.manager.prototype;

/**
 * Unchecked exception for flexible code change to get specific application exception.
 * @author syambrij
 *
 */
public class CustomMemoryManagerException extends RuntimeException {

	/**
	 * To get the most of implementing serializable.
	 */
	private static final long serialVersionUID = 3658809074449130932L;
	
	public CustomMemoryManagerException(Throwable cause) {
		super(cause);
	}
	
	public CustomMemoryManagerException(String message) {
		super(message);
	}
	
	public CustomMemoryManagerException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
