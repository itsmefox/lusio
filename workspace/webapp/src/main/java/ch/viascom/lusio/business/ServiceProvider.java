package ch.viascom.lusio.business;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


import ch.viascom.base.exceptions.ServiceException;
import ch.viascom.base.exceptions.ServiceFault;
import ch.viascom.base.exceptions.ServiceResult;
import ch.viascom.base.jackson.ObjectMapperConfigurator;
import ch.viascom.base.utilities.IOUtils;
import ch.viascom.base.utilities.StringUtil;
import ch.viascom.base.utilities.URIUtil;
import ch.viascom.lusio.DataStore;
import ch.viascom.lusio.business.network.HttpUtils;
import ch.viascom.lusio.business.network.SimpleHttpClient;
import ch.viascom.lusio.business.network.SimpleHttpClient.Server500ErrorHandler;


/**
 * ServiceProvider base class.
 * 
 * This class provides the base functionality for accessing a data service.
 */
public abstract class ServiceProvider implements Server500ErrorHandler {

	private final ObjectMapper _mapper;

	@Inject
	private DataStore store;
	
	public ServiceProviderConfig getConfiguration() {
		return store.getConfig();
	}

	protected void setConfiguration(ServiceProviderConfig config) {
	    store.setConfig(config);
	}

	public ObjectMapper getMapper() {
		return _mapper;
	}

	/**
	 * Constructor.
	 */
	public ServiceProvider() {
		_mapper = new ObjectMapper();
		ObjectMapperConfigurator.configure(_mapper);
	}

	/**
	 * <p>
	 * Creates a new SimpleHttpClient instance with the given path appended to
	 * RequestRootURL.
	 * </p>
	 * <p>
	 * Placeholders in path get replaced with their value from pathParameters.
	 * </p>
	 * <p>
	 * <b>Example:</b><br />
	 * /segment/all/{location}/more
	 * </p>
	 */
	public SimpleHttpClient getHttpClient(String path,
			NameValuePair... pathParameters) {
		String requestRootURL = getConfiguration().getRequestRootURL();
		String url = URIUtil.concat(requestRootURL, path);
		

		if (pathParameters != null) {
			for (NameValuePair parameter : pathParameters) {
				String value = "";
				
				if (!StringUtil.isEmpty(parameter.getValue()))
                    try {
                        value = URLEncoder.encode(parameter.getValue(), "UTF-8").replace("+",  "%20");
                    } catch (UnsupportedEncodingException e) {

                        throw new RuntimeException(e);
                    }
				
				url = url.replace("{" + parameter.getName() + "}", value);
			}
		}
		
		// Check for unmatched path parameters
		if (url.contains("{") && url.contains("}")) {
			throw new IllegalArgumentException(url + " contains unmatched path parameters.");
		}
		
		SimpleHttpClient client = new SimpleHttpClient();
		client.setHttpClient(HttpUtils.getThreadSafeTrustAllCertsClient());
		client.setServer500ErrorHandler(this);
		client.setRequestEncoding(getConfiguration().getRequestEncoding());
		client.setResponseEncoding(getConfiguration().getResponseEncoding());
		client.setRequestUri(url);
		
		return client;
	}

	
	/**
	 * Initialization logic for this service provider.
	 */
	public void initialize(ServiceProviderConfig config) {
		setConfiguration(config);
	}
	
	
	/**
	 * Helper function to build a name-value-pair list for the request parameters.
	 *
	 * @param nameValuePairs    Additional entries.
	 * @return					List of NameValuePair
	 */
	protected List<NameValuePair> buildNameValuePairList(NameValuePair... nameValuePairs) {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>(10);
		
		for (NameValuePair nameValuePair : nameValuePairs)
			nameValuePairList.add(nameValuePair);
		
		return nameValuePairList;
	}
	

	public void handleServer500Error(HttpResponse response) throws ServiceException {
		String encoding = "utf-8";
		Header encodingHeader = response.getEntity().getContentEncoding();
		
		if (encodingHeader != null)
			encoding = encodingHeader.getValue();
		
		String json;
		try {
			json = IOUtils.toString(response.getEntity().getContent(), encoding, true);
		} catch (Exception e) {
			// TODO: RuntimeExecption sollte nicht geworfen werden. Sollte durch
			// eine passendere ersetzt werden.
			throw new RuntimeException(e);
		}
		
		throw mapServiceException(json);
	}
	
	public ServiceException mapServiceException(String json) {
		try {
			ServiceResult<ServiceFault> fault = _mapper.readValue(json,
					new TypeReference<ServiceResult<ServiceFault>>() {
					});

			return new ServiceException(fault.getContent());
		} catch (Exception e) {
			// TODO: RuntimeExecption sollte nicht geworfen werden. Sollte durch
			// eine Passendere ersetzt werden.
			throw new RuntimeException(
					"Failed to map JSON Exception to ServiceException. JSON: "
							+ json, e);
		}
	}
}
