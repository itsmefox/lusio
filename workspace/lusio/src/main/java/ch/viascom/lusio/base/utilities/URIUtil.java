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
public class URIUtil {
	public static String concat(String segment1, String segment2) {
		if (StringUtil.isEmpty(segment1))
			return segment2;
		
		if (StringUtil.isEmpty(segment2))
			return segment1;
		
		if (segment1.endsWith("/")) {
			if (segment2.startsWith("/"))
				return segment1 + segment2.substring(1);
			
			return segment1 + segment2;			
		}
		
		if (segment2.startsWith("/"))
			return segment1 + segment2;
		
		return segment1 + "/" + segment2;
	}
}
