package system;

import datamodel.Order;
import datamodel.OrderItem;
import datamodel.TAX;

/**
 * Component for calculating taxes and prices of orders
 *	@author Leander Wendt
 */

public interface Calculator {
    /**
	 * Calculates the Value of the given orders
	 *  
	 * @param orders of which the value is to be calculated
	 * @return the value of the orders
	 */
    long calculateValue(Iterable<Order> orders);

    /**
	 * Calculates the Value of the given order
	 *  
	 * @param order of which the value is to be calculated
	 * @return the value of the order
	 */
    long calculateValue(Order order);

	/**
	 * Calculates the value of OrderItem
	 *  
	 * @param i of which OrderItem the value is to be calculated
	 * @return the value of the OrderItem
	 */
	long calculateValue (OrderItem i);

    /**
	 * Calculates the included tax of the given orders
	 *  
	 * @param orders of which the inclueded tax is to be calculated
	 * @return the inclueded tax of the orders
	 */
    long calculateIncludedVAT(Iterable<Order> orders);

    /**
	 * Calculates the included tax of the given order
	 *  
	 * @param order of which the inclueded tax is to be calculated
	 * @return the inclueded tax of the order
	 */
    long calculateIncludedVAT(Order order);

    /**
	 * Calculates the included tax of the given price and tax
	 *  
	 * @param price of which the inclueded tax is to be calculated
     * @param taxRate which is used to calculate the included given tax
	 * @return the inclueded tax of the price
	 */
    long calculateIncludedVAT(long price, TAX taxRate);

	/**
	 * Calculates the included tax of OrderItem
	 *  
	 * @param i of which OrderItem the inclueded tax is to be calculated
	 * @return the inclueded tax of the price
	 */
	long calculateIncludedVAT (OrderItem i);
}
