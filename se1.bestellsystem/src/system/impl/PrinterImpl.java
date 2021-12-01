package system.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import datamodel.Currency;
import datamodel.Customer;
import datamodel.Order;
import datamodel.OrderItem;
import system.Calculator;
import system.Formatter;
import system.Printer;

class PrinterImpl implements Printer {
    private Calculator calc;
    private Formatter formatter;
    private Map<Customer, Integer> orderCount = new HashMap<>();

    PrinterImpl(Calculator calculator) { // im Konstruktor injizierte Abhängigkeit
        this.calc = calculator;       // zur Calculator Komponente
        this.formatter = createFormatter();
    }

    @Override
    public StringBuffer printOrders(Iterable<Order> orders) {
        Formatter.OrderTableFormatter otfmt = new OrderTableFormatterImpl(formatter, new Object[][] {
            // five column table with column specs: width and alignment ('[' left, ']' right)
			{ 12, '[' }, { 25, '[' }, { 36, '[' }, { 10, ']' }, { 10, ']' }
        });
        otfmt
            .liner( "+-+-+-+-+-+" )		// print table header
			.hdr( "||", "Order-Id", "Customer", "Ordered Items", "Order", "MwSt." )
			.hdr( "||", "", "", "", "Value", "incl." )
			.liner( "+-+-+-+-+-+" )
			.liner( "||" );

        long totalAllOrders = 0;
        long totalOrderVAT = 0;

        for (Order o : orders){             
            printOrder(o, otfmt);	// print first order in table
			totalAllOrders += calc.calculateValue(o);
			totalOrderVAT += calc.calculateIncludedVAT(o);
		}	

		otfmt			// finalize table with compound value and VAT (MwSt.) of all orders
			.lineTotal( totalAllOrders, totalOrderVAT, Currency.EUR );
        return otfmt.getFormatter().getBuffer();
    }

    @Override
    public StringBuffer printOrder(Order order, Formatter.OrderTableFormatter otfmt) {
        OrderItem[] a = order.getItemsAsArray();
		
		otfmt.line( "#" + order.getId(), parseOrderAmount(order.getCustomer()), a[0].getUnitsOrdered() + " " + a[0].getArticle().getDescription()+ " (" + a[0].getArticle().getId() + "), " + parseAmount(a[0].getUnitsOrdered()) + parsePrice(a[0].getArticle().getUnitPrice(), a[0].getArticle().getCurrency()), calc.calculateValue(a[0]), calc.calculateIncludedVAT(a[0]) );
			for (int i = 1; i < a.length; i++){
			otfmt
				.line( "", "", a[i].getUnitsOrdered() + " " +  a[i].getArticle().getDescription() + " (" + a[i].getArticle().getId() + "), " + parseAmount(a[i].getUnitsOrdered()) + parsePrice(a[i].getArticle().getUnitPrice(), a[i].getArticle().getCurrency()), calc.calculateValue(a[i]), calc.calculateIncludedVAT(a[i]));
			}
			otfmt
				.liner( "| | |-|-|-|" )
				.line( "", "", "total:", calc.calculateValue(order), calc.calculateIncludedVAT(order) )
				.liner( "| | | |=|=|" )
				.liner( "| | | | | |" );
            return otfmt.getFormatter().getBuffer();
    }

    @Override
    public Formatter createFormatter() {
        return new FormatterImpl();
    }

    private String parseOrderAmount (Customer x) {
		int num = 0;
		if (orderCount.containsKey(x)) {
			num = orderCount.get(x) + 1;
			orderCount.put(x, num);
		} else {
			orderCount.put(x, 1);
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

    private String parseAmount (int amount) {
		if (amount == 1) {
			return "";
		}
		return amount + "x ";
	}

    private String parsePrice (long p, Currency c) {
		double t = p;
		if (c != Currency.YEN) {
			t = t / 100;
		}		
		return "" + t + c.getSymbol();
	}

	@Override
	public void printOrdersToFile( Iterable<Order> orders, String filepath ) throws IOException {
		// TODO implement method
		throw new IOException( "not implemented." );
	}
    
}
