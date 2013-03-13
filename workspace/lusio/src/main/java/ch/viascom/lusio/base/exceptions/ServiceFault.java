package ch.viascom.lusio.base.exceptions;

import java.util.ArrayList;
import java.util.List;

import ch.viascom.lusio.base.utilities.NameValuePair;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class ServiceFault {
	protected String _code;
	protected String _message;
	protected String _requestUrl;
	protected Class<?> _requestedType;

	protected int _responseStatusCode = 500;
	protected List<NameValuePair> _requestParams;
	protected String _exception;

	

	public Class<?> getRequestedType() {
		return _requestedType;
	}

	public void setRequestedType(Class<?> requestedType) {
		_requestedType = requestedType;
	}
	
	public int getResponseStatusCode() {
		return _responseStatusCode;
	}

	public void setResponseStatusCode(int status) {
		_responseStatusCode = status;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}

	public String getMessage() {
		return _message;
	}

	public void setMessage(String message) {
		_message = message;
	}

	public String getException() {
		return _exception;
	}

	public ServiceFault setException(String exception) {
		_exception = exception;
		return this;
	}

	public String getRequestUrl() {
		return _requestUrl;
	}

	public ServiceFault setRequestUrl(String url) {
		_requestUrl = url;
		return this;
	}

	public List<NameValuePair> getRequestParams() {
		return _requestParams;
	}
	
	public void setRequestParams(List<NameValuePair> requestParams) {
		_requestParams = requestParams;
	}
	
	

	/**
	 * Constructor.
	 */
	public ServiceFault() {
		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param code		Technical error code.
	 * @param message	Display error message.
	 */
	public ServiceFault(String code, String message) {
		_code = code;
		_message = message;
	}

	/**
	 * Adds a name-value pair to the request parameter list.
	 * 
	 * @return The fault which is used.
	 */
	public ServiceFault addRequestParam(String key, String value) {
		if (_requestParams == null)
			_requestParams = new ArrayList<NameValuePair>();

		_requestParams.add(new NameValuePair(key, value));

		return this;
	}
}
