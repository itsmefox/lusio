package ch.viascom.lusio.base.jackson;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.joda.time.DateTime;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
public class ObjectMapperConfigurator {

	public static void configure(ObjectMapper mapper) {
	    // Avoid 'unrecognized field in JSON' error message
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		// Joda-DateTime deserializer
		SimpleModule jodaDeserializerModule = new SimpleModule("JodaDateTimeDeserializer", new Version(1, 0, 0, null));
		jodaDeserializerModule.addDeserializer(DateTime.class, new JodaDateTimeDeserializer());
		mapper.registerModule(jodaDeserializerModule);
	    
		// Joda-DateTime serializer
		SimpleModule jodaSerializerModule = new SimpleModule("JodaDateTimeSerializer", new Version(1, 0, 0, null));
		jodaSerializerModule.addSerializer(DateTime.class, new JodaDateTimeSerializer());
	    mapper.registerModule(jodaSerializerModule);
	}
}
