package system;

import java.io.IOException;

import datamodel.Order;

/**
 * Component for printing orders
 *	@author Leander Wendt
 */

public interface Printer {
    /**
	 * Creates a StringBuffer for the given orders
	 *  
	 * @param orders objects you want a StringBuffer for
	 * @return StringBuffer for the orders param
	 */
    StringBuffer printOrders(Iterable<Order> orders);


    /**
	 * Creates a StringBuffer for the given order
	 *  
	 * @param order object you want a StringBuffer for
	 * @return StringBuffer for the order param
	 */
    StringBuffer printOrder(Order order, Formatter.OrderTableFormatter otfmt);

	/**
	 * Print the list of orders in a .txt file
	 *  
	 * @param orders object you want to be printed
	 * @throws IOException if a wrong filepath is used
	 */
	void printOrdersToFile( Iterable<Order> orders, String filepath ) throws IOException;
    /**
	 * Provide access to RTE singleton instance.
	 * 
	 * @return Formatter class for formatting prices
	 */
    Formatter createFormatter();
}
