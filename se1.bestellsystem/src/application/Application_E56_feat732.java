package application;

import static system.RTE.Configuration.JSON_DATASOURCE;
import static system.RTE.Configuration.KEY_DATASOURCE;
import static system.RTE.Configuration.KEY_DATASOURCE_ARTICLE;
import static system.RTE.Configuration.KEY_DATASOURCE_CUSTOMER;

import datamodel.Currency;
import system.InventoryManager;
import system.RTE;
import system.RTE.Runtime;


/**
 * Class with main() function.
 * 
 * @since "0.1.0"
 * @version "0.1.1 - feature.732 InventoryManager" 
 * @author sgra64
 *
 */

public class Application_E56_feat732 {


	/**
	 * main() function.
	 * 
	 * @param args arguments passed to main() function
	 */
	public static void main( String[] args ) {
		//
		System.out.println( "SE1 Bestellsystem (feature.732 InventoryManager)" );

		Runtime runtime = RTE.getInstance()
			//
			.create( config -> {
				/*
				 * configure customer and article data imported from JSON
				 */
				config.put( KEY_DATASOURCE, JSON_DATASOURCE );
				config.put( KEY_DATASOURCE_CUSTOMER, "src/data/customers_10.json" );
				config.put( KEY_DATASOURCE_ARTICLE, "src/data/articles_9.json" );
			})
			//
			.launch( (config, rt ) -> {
				/*
				 * launch runtime system, load JSON data
				 */
				System.out.println( "launching..." );
				rt.loadData();
				System.out.println( "system is running..." );
			});
		//
		InventoryManager im = runtime.getInventoryManager();

		// adjust inventory to test order acceptance in OrderBuilder.build()
		//
//		im.update( "SKU-693856", 8 + 3 );	// SKU-693856 Becher (Eric's 1st: 8x, 4th order: 3x)
//		im.update( "SKU-425378", 0 );	// update inventory for: SKU-425378 Buch 'OOP'
//		im.update( "SKU-583978", 0 );	// update inventory for: SKU-583978 Fahrradkarte
//		im.update( "SKU-300926", 0 );	// update inventory for: SKU-300926 Pfanne
		//
		StringBuffer inventory = im.printInventory();
		System.out.print( "\ninventory before orders:\n" + inventory.toString() );

		System.out.println( "accepting orders: " );
		//
		OrderBuilder ob = OrderBuilder.getInstance( runtime );
		ob.build();		// build and save filled orders to OrderRepository
		//
		long orderValue = runtime.getCalculator().calculateValue( runtime.getOrderRepository().findAll() );
		StringBuffer orderValueStr = runtime.getPrinter().createFormatter().fmtPaddedPrice( orderValue, 12, ' ', Currency.NONE );
		System.out.println( "***                          --------\n" +
				"value of accepted orders:" + orderValueStr.toString() );

		// sortedBy 1: byPrice; 2: byValue; 3: byUnits; 4: byDescription; 5: bySKU; else: unsorted
//		inventory = im.printInventory( 2, true );
		inventory = im.printInventory();
		System.out.println( "\ninventory after orders:\n" + inventory.toString() );

		runtime.shutdown( rt -> { System.out.println( "...shutting down." ); } );
	}

}
