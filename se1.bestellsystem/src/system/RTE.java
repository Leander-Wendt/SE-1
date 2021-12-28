package system;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import system.impl.InstanceAccessor;


/**
 * The Run-Time Environment (RTE) allows to configure and launch Runtime
 * instances that offer public interfaces for all system components
 * through their public interfaces.
 * 
 * Implementations are hidden as local (none-public) classes in the .impl
 * package.
 *	@author Leander Wendt
 */

public interface RTE {


	/**
	 * Provide access to RTE singleton instance.
	 * 
	 * @return reference to RTE singleton instance.
	 */
	public static RTE getInstance() {
		return InstanceAccessor.getInstance();
	}


	/**
	 * Create and configure a new Runtime instance before it is launched.
	 *  
	 * @param config functional interface to provide configuration information.
	 * @return instance of Configuration that can be launched.
	 */
	Configuration create( Consumer<Configuration> config );


	/**
	 * Interface to configure a Runtime instance before launch. A Runtime instance
	 * represents a running instance of the system.
	 *
	 */

	interface Configuration {
		//
		public static final String KEY_DATASOURCE = "rte.datasource";
		public static final String KEY_DATASOURCE_CUSTOMER = "rte.datasource.customers";
		public static final String KEY_DATASOURCE_ARTICLE = "rte.datasource.articles";
		public static final String KEY_DATASOURCE_ORDER = "rte.datasource.orders";
		//
		public static final String JSON_DATASOURCE = "JSON";

		/**
		 * Store configuration Property as String key-value pair.
		 * 
		 * @param key key to store value
		 * @param value value stored
		 * @return chainable self-reference
		 */
		Configuration put( String key, String value );

		/**
		 * Retrieve value stored for key.
		 * 
		 * @param key key to access value
		 * @return value stored for key or empty
		 */
		Optional<String> get( String key );

		/**
		 * Create and launch new Runtime from configuration.
		 * 
		 * @param runtime functional interface with Configuration and Runtime to launch.
		 * @return instance of Runtime.
		 * @throws RuntimeException thrown with errors during launch
		 */
		Runtime launch( BiConsumer<Configuration, Runtime> runtime ) throws RuntimeException;
	}


	/**
	 * A Runtime instance represents a running instance of the system.
	 * It provides access to system components.
	 *
	 */

	interface Runtime {

		/**
		 * Configuration getter.
		 * 
		 * @return Configuration.
		 */
		Configuration getConfiguration();

	
		/**
		 * Shutting down Runtime instance.
		 * 
		 * @param runtime functional interface invoked during shutdown.
		 * @return Runtime Environment from which Runtime instance was launched.
		 * 
		 * @throws RuntimeException thrown with errors during shutdown
		 */
		RTE shutdown( Consumer<Runtime> runtime ) throws RuntimeException;

	
		/**
		 * Return singleton calculator instance.
		 * 
		 * @return singleton calculator instance.
		 */
		Calculator getCalculator();


		/**
		 * Return singleton printer instance.
		 * 
		 * @return singleton printer instance.
		 */
		Printer getPrinter();


		/**
		 * Return singleton instance of CustomerRepository.
		 * 
		 * @return singleton instance of CustomerRepository
		 */
		Repository<Customer> getCustomerRepository();


		/**
		 * Return singleton instance of InventoryManager.
		 * 
		 * @return singleton instance of InventoryManager
		 */
		InventoryManager getInventoryManager();


		/**
		 * Return singleton instance of OrderRepository.
		 * 
		 * @return singleton instance of OrderRepository
		 */
		Repository<Order> getOrderRepository();


		/**
		 * Load data into repositories during Runtime launch,
		 * {@code Runtime.launch( (config, rt) -> { rt.loadData(); } );}
		 * 
		 * @return chainable self reference.
		 */
		Runtime loadData();

	}

}
