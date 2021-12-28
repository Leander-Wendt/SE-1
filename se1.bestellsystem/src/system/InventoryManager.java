package system;

import datamodel.Article;
import datamodel.Order;


/**
 * Public interface of the InventoryManager that manages the inventory
 * of articles. InventoryManager uses (and hides) the Article repository.
 * 
 * @author sgra64
 *
 */

public interface InventoryManager extends Repository<Article> {


	/**
	 * Return units in stock for given article.
	 * 
	 * @param id article identifier.
	 * @return units in stock of article.
	 * @throws IllegalArgumentException if id is null or id does not exist.
	 */
	int getUnitsInStock( String id );


	/**
	 * Update inventory for article.
	 * 
	 * @param id article identifier.
	 * @param updatedUnitsInStock update with number (must be {@code >= 0}).
	 * @throws IllegalArgumentException if id is null, id does not exist or unitsInStock is {@code < 0}).
	 */
	void update( String id, int updatedUnitsInStock );


	/**
	 * Test that order is fillable.
	 * 
	 * An order is fillable when all order items meet the condition:
	 * {@code orderItem.unitsOrdered <= inventory(article).unitsInStock}.
	 * 
	 * @param order to validate.
	 * @return true if order is fillable from current inventory.
	 * @throws IllegalArgumentException if order is null.
	 */
	boolean isFillable( Order order );


	/**
	 * Fills order by deducting all order items from the inventory, if the
	 * order is fillable. If the order is not fillable, inventory remains
	 * unchanged (transactional behavior: all or none order item is filled).
	 * 
	 * @param order to fill.
	 * @return true if order has been filled, false otherwise.
	 * @throws IllegalArgumentException if order is null.
	 */
	boolean fill( Order order );


	/**
	 * Print inventory as table.
	 * 
	 * @return printed inventory (as table).
	 */
	StringBuffer printInventory();


	/**
	 * Print inventory as table with sorting and limiting criteria.
	 * 
	 * @param sortedBy sorting criteria 1: byPrice; 2: byValue; 3: byUnits; 4: byDescription; 5: bySKU; else: unsorted
	 * @param decending true if in descending order
	 * @param limit upper boundary of articles printed after sorting
	 * @return printed inventory (as table).
	 */

	StringBuffer printInventory( int sortedBy, boolean decending, Integer... limit );

}
