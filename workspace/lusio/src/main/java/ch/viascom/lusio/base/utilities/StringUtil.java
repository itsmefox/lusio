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
public class StringUtil {

	/**
	 * Indicates if the string is null or empty (length = 0).
	 * The string is not considered as empty if it only consists of whitespace.
	 * 
	 * @param 	value	String to check
	 * @return 			True if empty, otherwise false
	 */
	public static boolean isEmpty(String value) {
		return value == null || value.length() == 0;
	}
	
	/**
	 * Performs a safe String.trim(String) operation
	 * on the given String.
	 * 
	 * @param value		String (can be null)
	 * @return	Trimmed string or null if given value is null
	 */
	public static String trim(String value) {
		if (value == null)
			return value;
		
		return value.trim();
	}
}
