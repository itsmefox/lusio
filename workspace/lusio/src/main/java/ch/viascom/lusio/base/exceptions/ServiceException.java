package ch.viascom.lusio.base.exceptions;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class ServiceException extends Exception {
	private static final long serialVersionUID = -339330063981442247L;

	protected ServiceFault _fault;
	

	public void setFault(ServiceFault fault) {
		_fault = fault;
	}
	
	/**
	 * Gets the underlying service fault.
	 * This exception acts as container for the fault.
	 * The data is stored in the fault and not in this exception
	 * because of serialization.
	 */
	public ServiceFault getFault() {
		return _fault;
	}

	/**
	 * Sets the exception which caused this service exception.
	 * It's important to set this exception to understand
	 * and reproduce the error!
	 * 
	 * @param innerException	Underlying exception
	 */
	public ServiceException setException(Throwable innerException) {
		ByteArrayOutputStream messageOutputStream = new ByteArrayOutputStream();
		innerException.printStackTrace(new PrintStream(messageOutputStream));

		String exceptionMessage = messageOutputStream.toString();

		_fault.setException(exceptionMessage);

		return this;
	}

	public ServiceException setRequestedType(Class<?> type) {
		_fault.setRequestedType(type);
		return this;
	}

	public ServiceException setRequestUrl(String url) {
		_fault.setRequestUrl(url);
		return this;
	}
	
	public ServiceException setResponseStatusCode(int status) {
		_fault.setResponseStatusCode(status);
		return this;
	}


	
	/**
	 * Constructor.
	 */
	public ServiceException() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param exceptionCode		Technical exception code.
	 * @param message			Display text of exception.
	 */
	public ServiceException(String code, String message) {
		super(message);

		_fault = new ServiceFault(code, message);
	}
	
	/**
	 * Constructor.
	 * 
	 * @param fault		Error information.
	 */
	public ServiceException(ServiceFault fault) {
		super(fault.getMessage());
		
		_fault = fault;
	}

	/**
	 * Constructor.
	 */
	public ServiceException(String code, String message, int responseStatusCode) {
		super(message);

		_fault = new ServiceFault(code, message);
		_fault.setResponseStatusCode(responseStatusCode);
	}
	
	/**
	 * Adds a name-value pair to the request parameter list.
	 * 
	 * @return The exception which is used.
	 */
	public ServiceException addRequestParameter(String key, String value) {
		_fault.addRequestParam(key, value);
		return this;
	}
}