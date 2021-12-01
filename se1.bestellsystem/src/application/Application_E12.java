package application;

import datamodel.Order;
import system.RTE;
import system.RTE.Runtime;
//
import static system.RTE.Configuration.KEY_DATASOURCE;
import static system.RTE.Configuration.JSON_DATASOURCE;
import static system.RTE.Configuration.KEY_DATASOURCE_CUSTOMER;

import java.io.IOException;

import static system.RTE.Configuration.KEY_DATASOURCE_ARTICLE;


/**
 * Class with main() function.
 * 
 * @author Leander Wendt
 *
 */
public class Application_E12 {


	/**
	 * main() function.
	 * 
	 * @param args arguments passed to main() function
	 */
	public static void main( String[] args ) {
		//
		System.out.println( "SE1 Bestellsystem" );

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
			.launch( (config, rt) -> {
				/*
				 * launch runtime system, load JSON data
				 */
				System.out.println( "launching..." );
				rt.loadData();
				System.out.println( "system is running..." );
			});

		OrderBuilder ob = OrderBuilder.getInstance( runtime );
		//
		ob.build();		// build and save orders to OrderRepository

		Iterable<Order> orders = runtime.getOrderRepository().findAll();
		StringBuffer sb = runtime.getPrinter().printOrders( orders );

		System.out.println( sb.toString() );

		runtime.shutdown( rt -> { System.out.println( "...shutting down." ); } );

        long count = runtime.getOrderRepository().count();
        Iterable<Order> orders2 = runtime.getOrderRepository().findAll();
        String filepath = "output/orders.txt"; // path to save orders as table
        try {
           runtime.getPrinter().printOrdersToFile( orders2, filepath );
           System.out.println( count + " orders printed to: " + filepath );
        } catch( IOException e ) {
           System.err.println( "Error printing orders to: " + filepath +
        ", reason: " + e.getMessage() );
        }
		/**1. Variablennamen in der Main anpassen um Dopplungen zu vermeiden
		 * 2. Printer Interface um die Methode void printOrderToFile(Iterable<Order> orders, String filepath) erweitern
		 * 3. Die neue Methode in der Klasse PrinterImpl implementieren und testen
		 */
	}

}
