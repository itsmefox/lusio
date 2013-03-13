package ch.viascom.lusio.base.utilities;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class FailableResult<ET extends Throwable, RT> {

	private ET _error;
	private RT _result;
	
	
	public ET getError() {
		return _error;
	}
	
	protected void setError(ET error) {
		_error = error;
	}
	
	
	public RT getResult() {
		return _result;
	}
	
	protected void setResult(RT result) {
		_result = result;
	}
	
	public boolean hasError() {
		return getError() != null;
	}
	
	public boolean hasResult() {
		return getResult() != null;
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param error    Exception. Null if no error given.
	 * @param result   Result.
	 */
	public FailableResult(ET error, RT result) {
		setError(error);
		setResult(result);
	}
}
