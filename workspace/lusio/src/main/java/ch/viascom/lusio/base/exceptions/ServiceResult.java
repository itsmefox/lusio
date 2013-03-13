package ch.viascom.lusio.base.exceptions;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class ServiceResult<T> {
	private ServiceResultStatus _status;
	private T _content;
	private Object _tag;
	
	
	public ServiceResultStatus getStatus() {
		return _status;
	}
	
	public ServiceResult<T> setStatus(ServiceResultStatus status) {
		_status = status;
		return this;
	}
	
	
	public T getContent() {
		return _content;
	}
	
	public ServiceResult<T> setContent(T content) {
		_content = content;
		return this;
	}

	
	public Object getTag() {
		return _tag;
	}

	public void setTag(Object tag) {
		_tag = tag;
	}
}
