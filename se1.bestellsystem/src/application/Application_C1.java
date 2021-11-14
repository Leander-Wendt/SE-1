package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import datamodel.Article;
import datamodel.Currency;
import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;
import datamodel.TAX;


/**
 * Class with Customer and Order objects. Running the main() function prints
 * a list of orders using the OrderTableFormatter.
 * 
 * @author sgra64
 *
 */

public class Application_C1 {

	/**
	 * list with sample customers, List.of( eric, anne, tim, nadine, khaled )
	 */
	final List<Customer> customers;

	/**
	 * list with sample articles, List.of( aTasse, aBecher, aKanne, aTeller )
	 */
	final List<Article> articles;

	/**
	 * list with sample orders, List.of( o5234,  o8592,  o3563,  o6135 )
	 */
	final List<Order> orders;

	private Map<Customer, Integer> ordersPerCustomer = new HashMap<>();

	/**
	 * Default constructor to initialize Article and Order objects,
	 * Customer objects are inherited from base class.
	 */
	Application_C1() {
		// inherit Customers from base class
		Customer eric = new Customer( "Eric Meyer" )
				.setId( 892474 )	// set id, first time
				.setId( 482947 )	// ignored, since id can only be set once
				.addContact( "eric98@yahoo.com" ) 	// contact_1
				.addContact( "(030) 3945-642298" ) 	// contact_2
				.addContact( "(030) 3945-642298" ); 	// ignore duplicate

		Customer anne = new Customer( "Bayer, Anne" )
				.setId( 643270 )
				.addContact( "anne24@yahoo.de" )
				.addContact( "(030) 3481-23352" );

		Customer tim = new Customer( "Tim Schulz-Mueller" )
				.setId( 286516 )
				.addContact( "tim2346@gmx.de" );

		Customer nadine = new Customer( "Nadine-Ulla Blumenfeld" )
				.setId( 412396 )
				.addContact( "+49 152-92454" );

		Customer khaled = new Customer()
				.setName( "Khaled Saad Mohamed Abdelalim" )
				.setId( 456454 )
				.addContact( "+49 1524-12948210" );
		Customer lena = new Customer()
				.setName( "Lena Neumann" )
				.setId( 556849 )
				.addContact( "lena228@gmail.com" );
		//
		this.customers = new ArrayList<>( List.of( eric, anne, tim, nadine, khaled, lena ) );

		// sample articles to use in orders
		Article tasse  = new Article( "Tasse",  299 ).setId( "SKU-458362" );
		Article becher = new Article( "Becher", 149 ).setId( "SKU-693856" );
		Article kanne  = new Article( "Kanne", 1999 ).setId( "SKU-518957" );
		Article teller = new Article( "Teller", 649 ).setId( "SKU-638035" );
		Article pfanne = new Article( "Pfanne", 4999 ).setId( "SKU-300926" );
		Article fahrradhelm = new Article( "Fahrradhelm", 16900 ).setId( "SKU-663942" );
		Article fahrradkarte = new Article( "Fahrradkarte", 695 ).setId( "SKU-583978" )
			.setTax(TAX.GER_VAT_REDUCED);
		//
		Article buch_Java = new Article( "Buch \"Java\"", 4990 ).setId( "SKU-278530" )
				.setTax( TAX.GER_VAT_REDUCED );	// reduced tax rate for books
		//
		Article buch_OOP = new Article( "Buch \"OOP\"", 7995 ).setId( "SKU-425378" )
				.setTax( TAX.GER_VAT_REDUCED );	// reduced tax rate for books
		//
		this.articles = List.of( tasse, becher, kanne, teller, buch_Java, buch_OOP, pfanne, fahrradhelm, fahrradkarte );

		// Eric's 1st order
		Order o8592 = new Order( eric )	// new order for Eric
				.setId( "8592356245" )	// assign order-id: 8592356245
				// add items to order
				.addItem( teller, 4 )	// 4 Teller, 4x 6.49 €
				.addItem( becher, 8 )	// 8 Becher, 8x 1.49 €
				.addItem( buch_OOP, 1 )	// 1 Buch "OOP", 1x 79.95 €, 7% MwSt (5.23€)
				.addItem( tasse, 4 );	// 4 Tassen, 4x 2.99 €
		//
		// Anne's order
		Order o3563 = new Order( anne )
				.setId( "3563561357" )
				.addItem( teller, 2 )
				.addItem( tasse, 2 );
		//
		// Eric's 2nd order
		Order o5234 = new Order( eric )
				.setId( "5234968294" )
				.addItem( kanne, 1 );
		//
		// Nadine's order
		Order o6135 = new Order( nadine )
				.setId( "6135735635" )
				.addItem( teller, 12 )
				.addItem( buch_Java, 1 )
				.addItem( buch_OOP, 1 );
		//
		Order o6136 = new Order( eric )
				.setId( "7356613535" )
				.addItem( fahrradhelm, 1)
				.addItem( fahrradkarte, 1 );
		Order o6137 = new Order( eric )
				.setId( "4450735661" )
				.addItem( tasse, 3)
				.addItem( becher, 3)
				.addItem(kanne, 1);
		Order o6138 = new Order( lena )
				.setId( "6173535635" )
				.addItem(fahrradkarte, 1)
				.addItem(buch_Java, 1);
		this.orders = new ArrayList<>( List.of( o8592, o3563, o5234, o6135, o6136, o6137, o6138 ) );
	}


	/**
	 * Public main function that creates instance of Application_B3 class
	 * and runs the printCustomers() method.
	 * 
	 * @param args standard argument vector passed from command line
	 */
	public static void main( String[] args ) {
		System.out.println( "SE1 Bestellsystem\n" );
		//
		final Application_C1 app = new Application_C1();
		//
		app.printOrders( app.orders );
	}


	/**
	 * Print list of orders using OrderTableFormatter.
	 * 
	 * @param orders list of orders to print
	 */
	void printOrders( List<Order> orders ) {
		long totalAllOrders = 0;		// calculate value of all orders
		long totalVAT = 0;		// calculate compound VAT (MwSt.) for all orders

		OrderTableFormatter otfmt = new OrderTableFormatter( new Object[][] {
			// five column table with column specs: width and alignment ('[' left, ']' right)
			{ 12, '[' }, { 25, '[' }, { 36, '[' }, { 10, ']' }, { 10, ']' }
		})
			.liner( "+-+-+-+-+-+" )		// print table header
			.hdr( "||", "Order-Id", "Customer", "Ordered Items", "Order", "MwSt." )
			.hdr( "||", "", "", "", "Value", "incl." )
			.liner( "+-+-+-+-+-+" )
			.liner( "||" );
		for (int i = 0; i < orders.size(); i++) {
			printOrder( otfmt, orders.get(i) );	// print first order in table
			totalAllOrders += calculateValue(orders.get(i));
			totalVAT += calculateIncludedVAT(orders.get(i));
		}	

		otfmt			// finalize table with compound value and VAT (MwSt.) of all orders
			.lineTotal( totalAllOrders, totalVAT, Currency.EUR )
			.print();	// output table
	}


	/**
	 * Append content of order to OrderTableFormatter table.
	 * <p>Example of lines appended to table:<pre>
	 * |8592356245 |Eric's order: |4 Teller (SKU-638035), 4x 6.49€     |  25.96€|  4.14€|
	 * |           |              |8 Becher (SKU-693856), 8x 1.49€     |  11.92€|  1.90€|
	 * |           |              |1 Buch "OOP" (SKU-425378), 79.95€   |  79.95€|  5.23€|
	 * |           |              |4 Tasse (SKU-458362), 4x 2.99€      |  11.96€|  1.91€|
	 * |           |              |------------------------------------|--------|-------|
	 * |           |              |total:                              | 129.79€| 13.18€|
	 * |           |              |                                    |========|=======|
	 * |           |              |                                    |        |       |</pre>
	 * 
	 * @param otfmt OrderTableFormatter to collect content from order
	 * @param order order to print
	 */
	void printOrder( OrderTableFormatter otfmt, Order order ) {
		OrderItem[] a = order.getItemsAsArray();
		
			// print Eric's order with hard-coded content
		otfmt.line( "#" + order.getId(), parseOrderAmount(order.getCustomer()), a[0].getUnitsOrdered() + " " + a[0].getArticle().getDescription()+ " (" + a[0].getArticle().getId() + "), " + parseAmount(a[0].getUnitsOrdered()) + parsePrice(a[0].getArticle().getUnitPrice(), a[0].getArticle().getCurrency()), calculateValue(a[0]), calculateIncludedVAT(a[0]) );
			for (int i = 1; i < a.length; i++){
			otfmt
				.line( "", "", a[i].getUnitsOrdered() + " " +  a[i].getArticle().getDescription() + " (" + a[i].getArticle().getId() + "), " + parseAmount(a[i].getUnitsOrdered()) + parsePrice(a[i].getArticle().getUnitPrice(), a[i].getArticle().getCurrency()), calculateValue(a[i]), calculateIncludedVAT(a[i]));
			}
			otfmt
				.liner( "| | |-|-|-|" )
				.line( "", "", "total:", calculateValue(order), calculateIncludedVAT(order) )
				.liner( "| | | |=|=|" )
				.liner( "| | | | | |" );
	}


	/**
	 * Calculates the compound value of all orders
	 * 
	 * @param orders list of orders
	 * @return compound value of all orders
	 */
	long calculateValue( Iterable<Order> orders ) {
		long value = 0;
		for (Order o : orders){
			value += calculateValue(o);
		}
		return value;
	}

	private String parseAmount (int amount) {
		if (amount == 1) {
			return "";
		}
		return amount + "x ";
	}

	private String parseOrderAmount (Customer x) {
		int num = 0;
		if (ordersPerCustomer.containsKey(x)) {
			num = ordersPerCustomer.get(x) + 1;
			ordersPerCustomer.put(x, num);
		} else {
			ordersPerCustomer.put(x, 1);
			num = 1;
		}

		switch (num) {
			case 1:
				return x.getFirstName() + "'s " + num + "st order:";
			case 2:
				return x.getFirstName() + "'s " + num + "nd order:";
			case 3:
				return x.getFirstName() + "'s " + num + "rd order:";
			default:
				return x.getFirstName() + "'s " + num + "th order:";
		}
	}

	/**
	 * Calculates the compound VAT of all orders
	 * 
	 * @param orders list of orders
	 * @return compound VAT of all orders
	 */
	long calculateIncludedVAT( Iterable<Order> orders ) {
		long vat = 0;
		for (Order o : orders){
            vat += calculateIncludedVAT(o);
        }
		return vat;
	}

	/**
	 * Calculates the value of one order
	 * 
	 * @param order order for which the value is calculated
	 * @return value of the order
	 */
	long calculateValue( Order order ) {
		long value = 0;
		OrderItem[] s = order.getItemsAsArray();
        for (OrderItem i : s) {
            value += calculateValue(i);
        }
		return value;
	}

	private long calculateValue (OrderItem i){
		return i.getArticle().getUnitPrice() * i.getUnitsOrdered();
	}

	/**
	 * Calculates the VAT of one order. VAT is based on the rate that
	 * applies to the articles of each line item.
	 * 
	 * @param order order for which the included VAT is calculated
	 * @return compound VAT of all ordered items 
	 */
	long calculateIncludedVAT( Order order ) {
		long sum = 0;
		for (OrderItem i : order.getItemsAsArray()){
			sum += calculateIncludedVAT(i);
		}
		return sum;
	}

	private long calculateIncludedVAT (OrderItem i) {
		return calculateIncludedVAT(i.getUnitsOrdered() * i.getArticle().getUnitPrice(), i.getArticle().getTax());
	}

	/*
	 * private helper to calculate included VAT for a price based on
	 * a TAX rate (as defined in Enum TAX).
	 */
	private long calculateIncludedVAT( long price, TAX taxRate ) {
		return  Math.round(price - (price / (1 + (taxRate.rate() / 100))));
	}

	private String parsePrice (long p, Currency c) {
		double t = p;
		if (c != Currency.YEN) {
			t = t / 100;
		}		
		return "" + t + c.getSymbol();
	}



	/*
	 * no changes needed in code below
	 */


	/**
	 * TableFormatter to format output for orders in form of a 5-column table.
	 * 
	 * @author sgra64
	 *
	 */
	class OrderTableFormatter extends TableFormatter {

		OrderTableFormatter( Object[][] descr ) {
			super( descr );
		}

		OrderTableFormatter line( String orderId, String customerName, String orderedItems, long value, long vat ) {
			String valueStr = fmtPaddedPrice( value, width[ 3 ], " ", Currency.EUR ).toString();
			String vatStr = fmtPaddedPrice( vat, width[ 4 ], " ", Currency.EUR ).toString();
			hdr( new String[] {		// use hdr() to output line
				"||", orderId, customerName, orderedItems, valueStr, vatStr
			});
			return this;
		}

		OrderTableFormatter lineTotal( long value, long vat, Currency currency ) {
			String valueStr = fmtPrice( value, currency ).toString();
			String vatStr = fmtPrice( vat, currency ).toString();
			return liner( "+-+-+-+-+-+" )
					.hdr( "", "", "total value (all orders):", "|", valueStr, "|", vatStr, "|" )
					.liner( "      +-+-+" );
		}

		OrderTableFormatter hdr( String... headers ) {
			return (OrderTableFormatter)super.hdr( headers );
		}

		OrderTableFormatter liner( String... pattern ) {
			return (OrderTableFormatter)super.liner( pattern );
		}

		OrderTableFormatter print() {
			return (OrderTableFormatter)super.print();
		}
	}


	/**
	 * Format price information as String (e.g. "19.99€") from long value
	 * (price in cent).
	 * 
	 * @param price price to format (in cent)
	 * @param currency defines included Currency symbol (empty "" for Currency.NONE)
	 * @return formatted price in a StringBuffer
	 */

	StringBuffer fmtPrice( long price, Currency... currency ) {
		StringBuffer sb = new StringBuffer();
		Currency cur = currency.length > 0? currency[0] : Currency.NONE;
		boolean fractional = cur != Currency.YEN;	// always fractional, including NONE
		String str;
		if( fractional ) {
			long cent = Math.abs( price % 100L );
			str = String.format( "%,d.%02d%s", price / 100, cent, cur.symbol( 1 ) );
		} else {
			str = String.format( "%,d%s", price, cur.symbol( 1 ) );
		}
		sb.append( str );
		return sb;
	}


	/**
	 * Format price information from long value padded to width (e.g. width 10 using
	 * fillChar '.' creates right aligned String "....19.99€".
	 * 
	 * @param price price to format (in cent)
	 * @param width padded width, e.g. 10 for "....19.99€"
	 * @param fillChar character to fill padded spaces
	 * @param currency defines included Currency symbol (empty "" for Currency.NONE)
	 * @return formatted price padded to width in a StringBuffer
	 */

	StringBuffer fmtPaddedPrice( long price, int width, String fillChar, Currency... currency ) {
		StringBuffer sb = fmtPrice( price, currency );
		if( sb.length() > width ) {	// cut price to width
			sb.delete( width - (width >= 1? 1 : 0), sb.length() );
			sb.append( width >= 1? "+" : " " );	// add '+' as cut-off String
		}
		if( sb.length() < width ) {	// fill to width
			sb.insert( 0, fillChar.repeat( width - sb.length() ) );
		}
		return sb;
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

	private final char fillChar = ' ';		// character to fill padded spaces

	StringBuffer fmtPaddedText( StringBuffer sb, String text, int width, char direction, String... cutoff ) {
		String scut = cutoff.length > 0? (cutoff[0]==null? "" : cutoff[0]) : "";
		boolean dir = direction == '[';
		text = text != null? text : "";
		int len = text.length();
		int d = width - len;
		if( d >= 0 ) {	// insert fill spaces
			sb = dir?
				sb.append( text ).append( String.valueOf( fillChar ).repeat( d ) ) :
				sb.append( String.valueOf( fillChar ).repeat( d ) ).append( text );
		//
		} else {	// cut str to width
			d = -d;
			boolean showCutOffStr = d > scut.length();
			if( showCutOffStr ) {
				width -= scut.length();		// adjust for cutoff string
				d += scut.length();
			}
			sb.append( ! dir && showCutOffStr? scut : "" );
			sb = dir?
				sb.append( text.substring( 0, width ) ) :
				sb.append( text.substring( d, len ) );
			//
			sb.append( dir && showCutOffStr? scut : "" );
		}
		return sb;
	}


	/**
	 * Formatter to format output as a table. A table is defined by colums
	 * with:
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
	 * 
	 * @author sgra64
	 *
	 */
	class TableFormatter {
		final StringBuffer sb = new StringBuffer();
		final int cols;
		final int width[];
		final char align[];

		TableFormatter( Object[][] descr ) {
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
		TableFormatter hdr( String... headers ) {
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
				fmtPaddedText( sb, text, width[col], align[col] );
				sep1 = sep_;
				col++;
			}
			if( ! sep1.equals(" ") ) {
				sb.append( sep1 );
			}
			sb.append( "\n" );
			return this;
		}

		TableFormatter liner( String... pattern ) {
			String pat = pattern.length > 0? pattern[0] : null;
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

		TableFormatter print() {
			System.out.println( sb.toString() );
			return this;
		}

		private String pat( String pat, int i, String c12 ) {
			String c = pat == null? i % 2 == 0? String.valueOf( c12.charAt(0) ) : String.valueOf( c12.charAt(1) ) :
				i < pat.length()? String.valueOf( pat.charAt( i ) ) : "";
			return c;
		}

	}
}

