package datamodel;

/**
 * Type to enumerate tax rates applicable for articles.
 * 
 * A selling business is obligated to collect taxes from sales based on a tax
 * rate. German sales tax (Umsatzsteuer) is collected as value-added tax (VAT,
 * Mehrwertsteuer, MwSt.) at different rates. A regular rate of 19% applies
 * to all articles, except for those with a reduced rate of 7% such as food,
 * health care items and books.
 * 
 * A VAT-system includes taxes in prices. Each business collects taxes and
 * reclaims paid taxes. End-customers cannot reclaim taxes. This creates a
 * system where taxes are collected from suppliers and distributors based on
 * their "added value" and by end-customers based on the final purchase price.
 * 
 * German MwSt. was ~243 Mrd, ~1/3 of total tax revenue of ~800 Mrd (2019).
 * See: <a href="https://de.wikipedia.org/wiki/Umsatzsteuer_(Deutschland)">Umsatzsteuer (Deutschland)</a>.
 * 
 * @since "0.1.0"
 * @version {@value package_info#Version}
 * @author Leander Wendt
 * 
 */

public enum TAX {

	/**
	 * no tax, 0.0%
	 */
	TAXFREE(  0.0, "Taxfree" ),

	/**
	 * German value-added tax, regular rate of 19%
	 */
	GER_VAT( 19.0, "19% MwSt" ),

	/**
	 * German value-added tax, reduced rate of 7%
	 */
	GER_VAT_REDUCED( 7.0, " 7% MwSt" );

	/**
	 * tax rate in percent, e.g. 19.0
	 */
	private final double rate;

	/**
	 * tax rate description
	 */
	private final String description;


	/**
	 * Constructor.
	 * 
	 * @param rate tax rate in percent
	 * @param description tax rate description
	 */
	TAX( double rate, String descr ) {
		this.rate = rate;
		this.description = descr;
	}

	/**
	 * rate getter.
	 * 
	 * @return tax rate associated with enumeration value
	 */
	public double rate() {
		return rate;
	}

	/**
	 * description getter.
	 * 
	 * @return brief description of tax rate
	 */
	public String description() {
		return description;
	}

}
