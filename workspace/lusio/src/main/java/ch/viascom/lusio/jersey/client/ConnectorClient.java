package ch.viascom.lusio.jersey.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;

import ch.viascom.lusio.base.exceptions.ServiceException;
import ch.viascom.lusio.base.exceptions.ServiceFault;
import ch.viascom.lusio.base.exceptions.ServiceResult;
import ch.viascom.lusio.jersey.provider.CustomJacksonJsonProvider;
import ch.viascom.lusio.base.utilities.NameValuePair;
import ch.viascom.lusio.base.utilities.StringUtil;
import ch.viascom.lusio.base.utilities.URIUtil;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class ConnectorClient {
	private final Logger _logger = Logger.getLogger(ConnectorClient.class.getName());
	
	
	/**
	 * Thread safe Jersey client (static).
	 */
	private static final Client _client;

	
	private String _baseURL;
	
	
	public String getBaseURL() {
		return _baseURL;
	}
	
	public void setBaseURL(String baseURL) {
		_baseURL = baseURL;
	}
	
	
	/**
	 * Static constructor.
	 */
	static {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        config.getClasses().add(CustomJacksonJsonProvider.class);
        
        _client = Client.create(config); // TODO ApacheHttpClient
	}
	
	
	/**
	 * Constructor.
	 */
	public ConnectorClient() {
	}
	
	/**
	 * Constructor.
	 * 
	 * @param baseURL	Base URL for all requests.
	 */
	public ConnectorClient(String baseURL) {
		setBaseURL(baseURL);
	}
	
	/**
	 * Gets a Jersey client with Jackson as POJO mapper.
	 */
	public Client getClient() {
		return _client;
	}
	
	
	/**
	 * <p>
	 * Creates the web resource object needed for a service request
	 * with the given parameters.<br />
	 * Use {@link #get(String, Class, NameValuePair...)} for requests
	 * if possible.
	 * </p>
	 * <p>
	 * The URL and its parameters must not be encoded.<br />
	 * 
	 * Achtung: Slashes im Pfad d�rfen keine Backslashes (\) sein,
	 * 		    sondern nur uncodierte Slashes (/).
	 * 
	 * Falsch: D:\Daten\test.txt
	 * Falsch: D:%5CDaten%5Ctest.txt
	 * Richtig: D:/Daten/test.txt
	 * 
	 * </p>
	 * <p>
	 * Example:<br />
	 * <dl>
	 * <dt>url</dt>
	 * <dd>http://localhost:8080/service/{paramX}/foo/{paramY}</dd>
	 * <dt>params</dt>
	 * <dd>
	 * paramX -> "Value for paramX"<br />
	 * paramY -> "Value for paramY"
	 * </dd>
	 * </dl>
	 * <br />
	 * </p>
	 * 
	 * @param url		Unencoded URL ({@link #encodeURL})
	 * @param params	Unencoded URL parameters (see method description)
	 * @throws UnsupportedEncodingException 
	 */
	public WebResource getWebResource(String url, NameValuePair... params) {
		
		// Concat base URL with given URL
		if (!StringUtil.isEmpty(_baseURL))
			url = URIUtil.concat(_baseURL, url);
		
		// Encode all parameters and add it to the given url
		if (params != null) {
			for (NameValuePair param : params) {
				String encodedValue;
				try {
					String placeholder = "{" + param.getName() + "}";
					
					if (!url.contains(placeholder))
						throw new RuntimeException("Placeholder " + placeholder + " does not exists in given url: " + url);
					
					encodedValue = URLEncoder.encode(param.getValue(), "UTF-8").replace("+",  "%20");
					url = url.replace(placeholder, encodedValue);
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		}

		_logger.finest("Encoded URL: " + url);
		
		WebResource resource = getClient().resource(url);
		resource.accept(MediaType.APPLICATION_JSON);
		
		return resource;
	}
	

	/**
	 * <p>
	 * Execute the request with the given parameters.
	 * </p>
	 * <p>
	 * The URL and its parameters must not be encoded.<br />
	 * 
	 * Achtung: Slashes im Pfad d�rfen keine Backslashes (\) sein,
	 * 		    sondern nur uncodierte Slashes (/).
	 * 
	 * Falsch: D:\Daten\test.txt
	 * Falsch: D:%5CDaten%5Ctest.txt
	 * Richtig: D:/Daten/test.txt
	 * 
	 * </p>
	 * <p>
	 * Example:<br />
	 * <dl>
	 * <dt>url</dt>
	 * <dd>http://localhost:8080/service/{paramX}/foo/{paramY}</dd>
	 * <dt>params</dt>
	 * <dd>
	 * paramX -> "Value for paramX"<br />
	 * paramY -> "Value for paramY"
	 * </dd>
	 * </dl>
	 * <br />
	 * </p>
	 * 
	 * @param url		Unencoded URL ({@link #encodeURL})
	 * @param type		Entity type
	 * @param params	Unencoded URL parameters (see method description)
	 * @throws ServiceException	Request error 
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String url, Class<T> type, NameValuePair... params) throws ServiceException {
		WebResource resource = getWebResource(url, params);

		ClientResponse response = resource.get(ClientResponse.class);

		if (response.getStatus() == 200) {
			if (type.isInstance(response))
				return (T)response;
			
			return response.getEntity(type);
		}
		
		if (response.getStatus() == 404)
			throw new ServiceException("NOT_FOUND", "Resource " + resource.getURI().getPath() + " not found.")
				.setResponseStatusCode(404)
				.setRequestedType(type)
				.setRequestUrl(url);
		
		ServiceResult<ServiceFault> result = response.getEntity(new GenericType<ServiceResult<ServiceFault>>() { });
		
		ServiceException ex = new ServiceException(result.getContent());
		
		_logger.log(Level.SEVERE, "Error occured during execution of ConnectorClient#get(url: " + url + ", type: " + type.getName() + ", params: ...)", ex);
		
		throw ex;
	}
}
