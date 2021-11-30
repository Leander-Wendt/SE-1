package system.impl;

import datamodel.Currency;
import system.Formatter;


/**
 * Local implementation of Formatter interface.
 * @author Leander Wendt
 */

class FormatterImpl implements Formatter {

	/**
	 * private StringBuffer to hold formatted content.
	 */
	private final StringBuffer sb = new StringBuffer();


	/**
	 * Format price information as String (e.g. "19.99�") from long value
	 * (price in cent).
	 * 
	 * @param price price to format (in cent)
	 * @param currency defines included Currency symbol (empty "" for Currency.NONE)
	 * @return formatted price in a StringBuffer
	 */

	@Override
	public StringBuffer fmtPrice( long price, Currency... currency ) {
		StringBuffer sb2 = new StringBuffer();
		Currency cur = currency.length > 0? currency[0] : Currency.NONE;
		boolean fractional = cur != Currency.YEN;	// always fractional, including NONE
		String str;
		if( fractional ) {
			long cent = Math.abs( price % 100L );
			str = String.format( "%,d.%02d%s", price / 100, cent, cur.symbol( 1 ) );
		} else {
			str = String.format( "%,d%s", price, cur.symbol( 1 ) );
		}
		sb2.append( str );
		return sb2;
	}


	/**
	 * Format price information from long value padded to width (e.g. width 10 using
	 * fillChar '.' creates right aligned String "....19.99�".
	 * 
	 * @param price price to format (in cent)
	 * @param width padded width, e.g. 10 for "....19.99�"
	 * @param fillChar character to fill padded spaces
	 * @param currency defines included Currency symbol (empty "" for Currency.NONE)
	 * @return formatted price padded to width in a StringBuffer
	 */

	@Override
	public StringBuffer fmtPaddedPrice( long price, int width, char fillChar, Currency... currency ) {
		StringBuffer sb2 = fmtPrice( price, currency );
		if( sb2.length() > width ) {	// cut price to width
			sb2.delete( width - (width >= 1? 1 : 0), sb2.length() );
			sb2.append( width >= 1? "+" : " " );	// add '+' as cut-off String
		}
		if( sb2.length() < width ) {	// fill to width
			sb2.insert( 0, String.valueOf( fillChar ).repeat( width - sb2.length() ) );
		}
		return sb2;
	}


	/**
	 * Format text either left-'[' or right-']' aligned to given width. If text exceeds
	 * width, a cutoff String can be used to indicate, e.g. "...".
	 * 
	 * @param sb StringBuffer into which result is formatted
	 * @param text text to format
	 * @param width padded width, e.g. 10 for "....ABCDEF"
	 * @param direction '[' for left-, ']' for right-alignment
	 * @param cutoff String included to indicate that text was cut when text exceeds width
	 * @return
	 */

	@Override
	public StringBuffer fmtPaddedText( String text, int width, char direction, char fillChar, String... cutoff ) {
		String scut = cutoff.length > 0? (cutoff[0]==null? "" : cutoff[0]) : "";
		boolean dir = direction == '[';
		text = text != null? text : "";
		int len = text.length();
		int d = width - len;
		if( d >= 0 ) {	// insert fill spaces
			if( dir ) {
				sb.append( text ).append( String.valueOf( fillChar ).repeat( d ) );
			} else {
				sb.append( String.valueOf( fillChar ).repeat( d ) ).append( text );
			}
		//
		} else {	// cut str to width
			d = -d;
			boolean showCutOffStr = d > scut.length();
			if( showCutOffStr ) {
				width -= scut.length();		// adjust for cutoff string
				d += scut.length();
			}
			sb.append( ! dir && showCutOffStr? scut : "" );
			sb.append( dir? text.substring( 0, width ) : text.substring( d, len ) );
			//
			sb.append( dir && showCutOffStr? scut : "" );
		}
		return sb;
	}


	/**
	 * Return reference to internal StringBuffer.
	 * 
	 * @return StringBuffer with formatted result
	 */

	@Override
	public StringBuffer getBuffer() {
		return sb;
	}
}
