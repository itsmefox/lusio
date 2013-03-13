package ch.viascom.lusio.jersey.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import ch.viascom.lusio.base.jackson.ObjectMapperConfigurator;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
@Provider 
public class CustomJacksonJsonProvider extends JacksonJsonProvider {
	@Override 
	public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) { 
		ObjectMapper mapper = super.locateMapper(type, mediaType);

		ObjectMapperConfigurator.configure(mapper);
		
		return mapper; 
	} 
}