package system;

import datamodel.Currency;


/**
 * Public interface of a Formatter component that provides
 * methods and interfaces to format objects to text in an
 * internal StringBuffer.
 *	@author Leander Wendt
 */

public interface Formatter {

	/**
	 * Format price from long value to text. Prices from fractional currencies
	 * such as EUR, USD are assumed in cent and converted (e.g. 1999 to "19.99�").
	 * None-fractional currencies such as the YEN are not converted (e.g. 1999
	 * formats to "1999�").
	 * 
	 * @param price price to format (in cent)
	 * @param currency vararg that specifies Currency and symbol (EUR is default,
	 * 			no symbol is shown if currency is not given or is Currency.NONE)
	 * @return formatted price in a StringBuffer
	 */
	StringBuffer fmtPrice( long price, Currency... currency );


	/**
	 * Format price as in fmtPrice() padded to width (e.g. width 10 using
	 * fillChar '.' creates right aligned String "....19.99�".
	 * 
	 * @param price price to format (in cent)
	 * @param width padded width, e.g. 10 for "....19.99�"
	 * @param fillChar char to fill padding, e.g. '.' in "....19.99�"
	 * @param currency specifies Currency and symbol (EUR is default,
	 * 			no symbol is shown if currency is not given or is Currency.NONE)
	 * @return formatted price padded to width in a StringBuffer
	 */
	StringBuffer fmtPaddedPrice( long price, int width, char fillChar, Currency... currency );


	/**
	 * Format text padded left-'[' or right-']' to a given width. If text
	 * exceeds width, an cutoff String can be specified, e.g. "...".
	 * 
	 * @param text text to format
	 * @param width padded width, e.g. 10 for "....ABCDEF"
	 * @param direction char {@code '['} for left-, {@code ']'} for right-alignment
	 * @param fillChar char to fill padding, e.g. '.' in "....ABCDEF"
	 * @param cutoff String included when text was cut to width, vararg
	 * @return StringBuffer with formatted result
	 */
	StringBuffer fmtPaddedText( String text, int width, char direction, char fillChar, String... cutoff );


	/**
	 * Return reference to internal StringBuffer.
	 * 
	 * @return StringBuffer with formatted result
	 */
	StringBuffer getBuffer();


	/**
	 * Interface of a formatter instance for table output.
	 * 
	 * A table is defined by colums of fixed width and alignment: '[' left-aligned, ']' right-aligned.
	 * 
	 * Rows can be added with methods:
	 *  - hdr( hdrColumns ) - header line with header content
	 *  - row( rowColumns ) - line item with column content for one row
	 *  - liner( specs ) - specification that defines horizontal lines with
	 *                     separators "|", "+" and fillers "-" for each column
	 *  - getResult() - returns formatted result as a the StringBuffer
	 */
	interface TableFormatter {

		/**
		 * Include a table header with text[]-vargs for header columns. 
		 * 
		 * @param headerColumns text[] varargs for header columns
		 * @return chainable self reference
		 */
		TableFormatter hdr( String... headerColumns );

		/**
		 * Include a table row with text[]-vargs for columns. 
		 * 
		 * @param rowColumns text[] varargs for row columns
		 * @return chainable self reference
		 */
		TableFormatter line( String... rowColumns );

		/**
		 * Include a horizontal line with a pattern of limiters and fillers.
		 * 
		 * @param spec vararg to define vertical limiters: {@code ('|', '+', '<space>')}
		 * and horizontal fillers: {@code ('-', '<space>')} to render horizontal
		 * lines in a table. Example: "+-|-|-+".
		 * @return chainable self reference
		 */
		TableFormatter liner( String... spec );

		/**
		 * 
		 * @return Formatter used to format, also contains formatting result.
		 */
		Formatter getFormatter();
	}


	/**
	 * Interface of a formatter for Order table output.
	 */
	interface OrderTableFormatter extends TableFormatter {
		OrderTableFormatter line( String orderId, String customerName, String orderedItems, long value, long vat );
		OrderTableFormatter lineTotal( long value, long vat, Currency currency );

		OrderTableFormatter hdr( String... headerColumns );
		OrderTableFormatter line( String... rowColumns );
		OrderTableFormatter liner( String... spec );
	}

}
