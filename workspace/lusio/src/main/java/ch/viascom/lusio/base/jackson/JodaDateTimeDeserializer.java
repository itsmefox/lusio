package ch.viascom.lusio.base.jackson;

import java.io.IOException;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * @author      : Viascom GmbH
 * @version     : 1.0
 * @create:     : 13.03.2013
 * @product     : Viascom-Base
 *
 * @email       : info@viascom.ch
 * @website     : http://viascom.ch
 */
/**
 * Helper-Klasse, um ein JSON-Datum in ein Joda-DateTime-Objekt
 * zu konvertieren.
 */
public class JodaDateTimeDeserializer extends JsonDeserializer<DateTime> {
    public static final DateTimeFormatter DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    
	@Override
	public DateTime deserialize(JsonParser parser, DeserializationContext arg1) throws IOException, JsonProcessingException {
		return DateTimeFormatter.parseDateTime(parser.getText());
	}
}