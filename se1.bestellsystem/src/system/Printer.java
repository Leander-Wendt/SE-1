package system;

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
    StringBuffer printOrder(Order order);

    /**
	 * Provide access to RTE singleton instance.
	 * 
	 * @return Formatter class for formatting prices
	 */
    Formatter createFormatter();
}
