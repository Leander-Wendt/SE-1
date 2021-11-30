package system.impl;

import system.Formatter;
import system.Formatter.TableFormatter;


/**
 * Local implementation of TableFormatter interface, which formats output as a table.
 * A table is defined by colums with:
 *  - fixed width and alinment: '[' left-aligned, ']' right-aligned
 * 
 * Rows are added to a table with
 *  - hdr()		- header line with header content
 *  - line()	- line item with column content for one row
 *  - liner()	- horizontal line with specs for separators "|", "+"
 *  			  and fillers "-" for each column
 *  
 *  Table content is collected in a StringBuffer.
 *   - print()	- outputs the StringBuffer
 *	@author Leander Wendt
 */

class TableFormatterImpl implements TableFormatter {
	final Formatter formatter;
	final int cols;
	final int width[];
	final char align[];
	final char fillChar = ' ';


	TableFormatterImpl( Formatter formatter, Object[][] descr ) {
		this.formatter = formatter;
		this.cols = descr.length;
		this.width = new int[cols];
		this.align = new char[cols];
		for( int i=0; i < cols; i++ ) {
			this.width[i] = (int)descr[i][0];
			this.align[i] = (char)descr[i][1];
		}
	}


	/**
	 * Append header content to OrderTableFormatter.
	 * <p>Example of lines appended to table:<pre>
	 * 
	 * .hdr( "Order-Id", "Customer", "Ordered Items", "Order" )
	 * .hdr( "||", "Order-Id", "Customer", "Ordered Items", "Order" )
	 * .hdr( "|", "Order-Id", "|", "Customer", "|", "Ordered Items", "|", "Order", "|" )
	 * 
	 *  Order-Id     Customer           Ordered Items                   Order
	 * |Order-Id    |Customer          |Ordered Items             |     Order|
	 * |Order-Id    |Customer          |Ordered Items             |     Order|</pre>
	 * 
	 * @param headers
	 * @return chainable self reference
	 */

	@Override
	public TableFormatter hdr( String... headers ) {
		
		StringBuffer sb = formatter.getBuffer();
		
		int len = headers.length;
		int col = 0;
		boolean allSeps = len > 0 && headers[0] != null && headers[0].equals( "||" );
		String sep_ = allSeps? "|" : " ";
		String sep1 = sep_;
		for( int ih = allSeps? 1 : 0; ih < len && ( col < cols || headers[ih].length() == 1 ); ih++ ) {
			String text = headers[ih];
			if( ! allSeps && text.length() == 1 ) {
				sep1 = text;
				continue;
			}
			sb.append( sep1 );
			formatter.fmtPaddedText( text, width[col], align[col], fillChar );
			sep1 = sep_;
			col++;
		}
		if( ! sep1.equals(" ") ) {
			sb.append( sep1 );
		}
		sb.append( "\n" );
		return this;
	}


	/**
	 * Include a table row with text[]-vargs for columns. 
	 * 
	 * @param rowColumns text[] varargs for row columns
	 * @return chainable self reference
	 */

	@Override
	public TableFormatter line( String... rowColumns ) {
		return hdr( rowColumns );
	}


	/**
	 * Include a horizontal line with a pattern of limiters and fillers.
	 * 
	 * @param spec vararg to define vertical limiters: {@code ('|', '+', '<space>')}
	 * and horizontal fillers: {@code ('-', '<space>')} to render horizontal
	 * lines in a table. Example: "+-|-|-+".
	 * @return chainable self reference
	 */

	@Override
	public TableFormatter liner( String... spec ) {
		
		StringBuffer sb = formatter.getBuffer();
		
		String pat = spec.length > 0? spec[0] : null;
		int pi = 0;
		boolean compressedPattern = pat != null && pat.equals( "||" );
		String c12 = compressedPattern? "| " : "+-";
		pat = compressedPattern? null : pat;
		for( int i=0; i < cols; i++ ) {
			sb.append( pat( pat, pi++, c12 ) ).append( pat( pat, pi++, c12 ).repeat( width[i] ) );
		}
		sb.append( pat( pat, pi++, c12) );
		sb.append( "\n" );
		return this;
	}


	@Override
	public Formatter getFormatter() {
		return formatter;
	}


	private String pat( String pat, int i, String c12 ) {
		String c = pat == null? i % 2 == 0? String.valueOf( c12.charAt(0) ) : String.valueOf( c12.charAt(1) ) :
			i < pat.length()? String.valueOf( pat.charAt( i ) ) : "";
		return c;
	}

}
