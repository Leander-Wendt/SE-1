package system.impl;

import datamodel.Currency;
import system.Formatter;
import system.Formatter.OrderTableFormatter;


/**
 * Local implementation of OrderTableFormatter interface.
 * @author Leander Wendt
 * 
 */

class OrderTableFormatterImpl extends TableFormatterImpl implements OrderTableFormatter {

	OrderTableFormatterImpl( Formatter formatter, Object[][] descr ) {
		super( formatter, descr );
	}

	@Override
	public OrderTableFormatter line( String orderId, String customerName, String orderedItems, long value, long vat ) {
		String valueStr = formatter.fmtPaddedPrice( value, width[ 3 ], fillChar, Currency.EUR ).toString();
		String vatStr = formatter.fmtPaddedPrice( vat, width[ 4 ], fillChar, Currency.EUR ).toString();
		hdr( new String[] {		// use hdr() to output line
			"||", orderId, customerName, orderedItems, valueStr, vatStr
		});
		return this;
	}

	@Override
	public OrderTableFormatter lineTotal( long value, long vat, Currency currency ) {
		String valueStr = formatter.fmtPrice( value, currency ).toString();
		String vatStr = formatter.fmtPrice( vat, currency ).toString();
		return liner( "+-+-+-+-+-+" )
				.hdr( "", "", "total value (all orders):", "|", valueStr, "|", vatStr, "|" )
				.liner( "      +-+-+" );
	}

	@Override
	public OrderTableFormatter hdr( String... headerColumns ) {
		return (OrderTableFormatter) super.hdr( headerColumns );
	}

	@Override
	public OrderTableFormatter line( String... rowColumns ) {
		return (OrderTableFormatter) super.line( rowColumns );
	}

	@Override
	public OrderTableFormatter liner( String... spec ) {
		return (OrderTableFormatter) super.liner( spec );
	}
}
