package ch.viascom.lusio.business.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.base.utilities.IOUtils;
import ch.viascom.base.utilities.StringUtil;

/**
 * This client simplifies the access to a Http server.
 * A request can be made via POST or GET.
 * 
 * @author Rino
 */
public class SimpleHttpClient {
	public interface Server500ErrorHandler {
		void handleServer500Error(HttpResponse response) throws ServiceException;
	}
	
	private final String LOG_TAG = SimpleHttpClient.class.getName();

	private HttpClient _httpClient;
	private String _requestEncoding;
	private String _responseEncoding;
	private String _requestUri;
	private Server500ErrorHandler _server500ErrorHandler;
	
	
	public Server500ErrorHandler getServer500ErrorHandler() {
		return _server500ErrorHandler;
	}

	public void setServer500ErrorHandler(Server500ErrorHandler server500ErrorHandler) {
		_server500ErrorHandler = server500ErrorHandler;
	}

	
	public String getRequestUri() {
		return _requestUri;
	}

	public void setRequestUri(String requestUri) {
		_requestUri = requestUri;
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
	
	
    public HttpClient getHttpClient() {
		return _httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		_httpClient = httpClient;
	}
	
	
	
	/**
	 * Constructor.
	 */
	public SimpleHttpClient() {
		setHttpClient(HttpUtils.getThreadSafeClient());
	}
	
	/**
	 * Creates a new instance of a HttpGet object with
	 * the specified request URL.
	 * 
	 * @return HttpGet
	 */
	public HttpGet getHttpGet() {
		return new HttpGet(getRequestUri());
	}
	
	/**
	 * Creates a new instance of a HttpPost object with
	 * the specified request URL.
	 * 
	 * @return HttpPost
	 */
	public HttpPost getHttpPost() {
		return new HttpPost(getRequestUri());
	}
	
	/**
	 * Creates a new instance of a HttpPut object with
	 * the specified request URL.
	 * 
	 * @return HttpPut
	 */
	public HttpPut getHttpPut() {
		return new HttpPut(getRequestUri());
	}
	
	/**
	 * Adds the given entitiy parameters to the given request object.
	 * The request object should be a HttpPost or HttpPut object.
	 * 
	 * @param request		Request object (HttpPost, HttpPut).
	 * @param parameters	Entity parameters
	 */
	public void addEntityParameters(HttpEntityEnclosingRequestBase request, List<NameValuePair> parameters) {
		try {
			request.setEntity(new UrlEncodedFormEntity(parameters, getRequestEncoding()));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Executes a HTTP request and verifies the server response code.
	 * 
	 * @param request	HttpUriRequest to execute
	 * @return HttpResponse of server (status code 200)
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpResponseException Thrown if server response code is unequal to 200
	 * @throws ServiceException 
	 */
	public HttpResponse getHttpResponse(HttpUriRequest request) throws IOException, HttpResponseException, ServiceException {
		HttpResponse response = getHttpResponseUnsafe(request);
		
		verifyHttpResponseStatusCode(response);
		
		return response;
	}
	
	/**
	 * Executes a HTTP request.
	 * 
	 * @param request	HttpUriRequest to execute
	 * @return HttpResponse of server (status code 200)
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpResponseException Thrown if server response code is unequal to 200
	 */
	public HttpResponse getHttpResponseUnsafe(HttpUriRequest request) throws ClientProtocolException, IOException, HttpResponseException {
		HttpResponse response = getHttpClient().execute(request);
		return response;
	}

	/**
	 * Executes a HTTP request and verifies the server response code.
	 * The server response is returned.
	 * 
	 * @param request	HttpUriRequest to execute
	 * @return String	Response body string
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpResponseException Thrown if server response code is unequal to 200
	 */
	public String getHttpResponseString(HttpUriRequest request) throws Exception {
		try {
			HttpResponse response = getHttpResponse(request);
			return getResponseBodyString(response);
		}
		catch (Exception ex) {

			if (request instanceof HttpEntityEnclosingRequestBase) {
				HttpEntityEnclosingRequestBase entityRequest = (HttpEntityEnclosingRequestBase)request;

				HttpEntity entity = entityRequest.getEntity();
				
				if (entity != null) {
					String content = IOUtils.toString(entity.getContent(), "utf-8", true);
				}
			}
			
			throw ex;
		}
	}
	
	/**
	 * Verifies that the response code of the given HttpResponse
	 * equals 200.
	 * 
	 * @param response HttpResponse object
	 * 
	 * @throws HttpResponseException
	 * @throws ServiceException 
	 */
	public void verifyHttpResponseStatusCode(HttpResponse response) throws HttpResponseException, ServiceException {
		StatusLine status = response.getStatusLine();
		
		if (status.getStatusCode() == 500) {
			if (_server500ErrorHandler != null) {
				_server500ErrorHandler.handleServer500Error(response);
			}
			else {
				String msg = String.format("Server returned invalid status code (%s): %s", status.getStatusCode(), status);
				throw new HttpResponseException(status.getStatusCode(), msg);
			}
		}
	}
	
	/**
	 * Gets the body string of the given HttpResponse object.
	 * 
	 * @param response HttpResponse object
	 * @return Body string
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	public String getResponseBodyString(HttpResponse response) {
		try {
			HttpEntity body = response.getEntity();
	
			String encoding = getResponseEncoding();
			
			if (body.getContentEncoding() != null && !StringUtil.isEmpty(body.getContentEncoding().getValue()))
				encoding = body.getContentEncoding().getValue();
	
			String bodyText = IOUtils.toString(body.getContent(), encoding, true);

			//// Debugging
			//android.util.Log.d("ch.viascom.vision", bodyText);
			
			return bodyText;
		} catch (Exception ex) {
			throw new RuntimeException("Http-server response parsing error: " + ex.getMessage(), ex);
		}
	}
}
