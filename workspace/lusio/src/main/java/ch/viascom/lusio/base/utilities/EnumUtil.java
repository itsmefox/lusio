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
public class EnumUtil {
	/**
	 * Returns a type of Enum  
	 * 
	 * @param enm The type which will returned if the input isn't in the Enum.
	 * @param input The String whichh will search in the Enum.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Enum<T>> T stringToEnum(Enum<T> enm, String input) {
		try {
			return (T) Enum.valueOf(enm.getClass(), input.toUpperCase());
		} catch (IllegalArgumentException e) {
			return (T) enm;
		}
	}
}
