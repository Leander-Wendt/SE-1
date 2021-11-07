package datamodel;


/**
 * Type to enumerate currencies.
 * 
 * Currency is the unit in which price information is quoted.
 * 
 * @since "0.1.0"
 * @version {@value package_info#Version}
 * @author Leander Wendt
 * 
 */

public enum Currency {

	/**
	 * Currency units used in the system: EUR, USD, GBP, YEN
	 */

	/**
	 * Euro, legal tender in many countries in the European Union
	 */
	EUR("\u20AC"),

	/**
	 * US Dollar, legal tender in the United States of America
	 */
	USD("$"),

	/**
	 * Great Britain Pound (or Pound Sterling), legal tender in the United Kingdom
	 */
	GBP("\u00A3"),

	/**
	 * Japanese Yen, legal tender in Japan
	 */
	YEN("\u00A5"),

	/**
	 * represents an undefined currency
	 */
	NONE(" ");

	/**
	 * not used: PLN("z\u0142"), CZK("K\u010D")
	 * source: https://www.ip2currency.com/currency-symbol
	 */


	/**
	 * UNICODE symbol for currency, e.g. "\u20AC" for EURO
	 */
	private final String symbol;


	/**
	 * Constructor taking UNICODE symbol as argument.
	 * 
	 * @param s UNICODE symbol for currency
	 */
	Currency( String s ) {
		symbol = s;
	}

	/**
	 * Method to return currency symbol padded to width.
	 * 
	 * @param width variable argument (vararg) length/width of padded symbol
	 * @return width-padded String with currency UNICODE
	 */
	public String symbol( Integer... width ) {
		int w = width.length > 0? width[0] : 1;	// 1 is default width
		String padded = this==NONE || w==0? "" : symbol;
		if( w > 1 ) {
			padded = padded + " ".repeat( w );	// pad left-aligned, "�  "
		}
		return padded;
	}

}
