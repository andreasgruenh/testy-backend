package testy.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.FORBIDDEN)
public class NotEnoughPermissionsException extends RuntimeException{
	
	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;

	public NotEnoughPermissionsException(String message) {
		super(message);
	}
	
}
