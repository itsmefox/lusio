package ch.viascom.lusio.business;

import javax.enterprise.context.SessionScoped;

import ch.viascom.base.utilities.StringUtil;

public class ServiceProviderConfig {
	private String _sessionId;

	
	
	// HTTP-Client config
	private String _requestEncoding;
	private String _responseEncoding;
	private String _requestRootURL;

	
	public String getSessionId() {
        return _sessionId;
    }

	public void setSessionId(String sessionId) {
        _sessionId = sessionId;
    }

    public String getRequestRootURL() {
		return _requestRootURL;
	}

	public void setRequestRootURL(String requestRootURL) {
		_requestRootURL = requestRootURL;
	}
	
	
	public String getResponseEncoding() {
		return _responseEncoding;
	}

	public void setResponseEncoding(String responseEncoding) {
		_responseEncoding = responseEncoding;
	}

	
	public String getRequestEncoding() {
		return _requestEncoding;
	}

	public void setRequestEncoding(String requestEncoding) {
		_requestEncoding = requestEncoding;
	}

}
