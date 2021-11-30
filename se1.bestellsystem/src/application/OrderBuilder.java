package application;

import system.Repository;
import system.RTE.Runtime;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;


/**
 * Singleton component that builds orders and stores them in the
 * OrderRepository.
 * 
 * @author Leander Wendt
 *
 */

public class OrderBuilder {

	/**
	 * static singleton reference to OrderBuilder instance (singleton pattern).
	 */
	private static OrderBuilder orderBuilder_instance = null;

	/**
	 * Repository dependencies.
	 */
	private final Repository<Customer> customerRepository;
	//
	private final Repository<Article> articleRepository;
	//
	private final Repository<Order> orderRepository;


	/**
	 * Provide access to RTE OrderBuilder singleton instance (singleton pattern).
	 * 
	 * @param runtime dependency to resolve Repository dependencies.
	 * @return
	 */
	public static OrderBuilder getInstance( Runtime runtime ) {
		if( orderBuilder_instance == null ) {
			orderBuilder_instance = new OrderBuilder( runtime );
		}
		return orderBuilder_instance;
	}


	/**
	 * Private constructor to prevent external instantiation (singleton pattern).
	 * 
	 * @param runtime dependency injected from where repository
	 * dependencies are resolved.
	 */
	private OrderBuilder( Runtime runtime ) {
		this.customerRepository = runtime.getCustomerRepository();
		this.articleRepository = runtime.getArticleRepository();
		this.orderRepository = runtime.getOrderRepository();
	}


	/**
	 * Save order to OrderRepository.
	 * 
	 * @param order saved to OrderRepository
	 * @return chainable self-reference
	 */
	public boolean accept( Order order ) {
		// TODO: validate order
		boolean valid = true;
		orderRepository.save( order );
		return valid;
	}


	/**
	 * Build and save orders to OrderRepository.
	 * 
	 * @param order saved to OrderRepository
	 * @return chainable self-reference
	 */
	public OrderBuilder build() {

		Repository<Customer> crep = customerRepository;
		/*
		 * Look up customers from CustomerRepository.
		 */
		Customer eric = crep.findById( 892474 ).get();
		Customer anne = crep.findById( 643270 ).get();
		Customer tim = crep.findById( 286516 ).get();
		Customer nadine = crep.findById( 412396 ).get();
		Customer khaled = crep.findById( 456454 ).get();
		Customer lena = crep.findById( 556849 ).get();
		Customer max = crep.findById( 482596 ).get();
		Customer brigitte = crep.findById( 660380 ).get();
		Customer joel = crep.findById( 582596 ).get();

		Repository<Article> arep = articleRepository;
		/*
		 * Look up articles from ArticleRepository.
		 */
		Article tasse = arep.findById( "SKU-458362" ).get();
		Article becher = arep.findById( "SKU-693856" ).get();
		Article kanne = arep.findById( "SKU-518957" ).get();
		Article teller = arep.findById( "SKU-638035" ).get();
		Article buch_Java = arep.findById( "SKU-278530" ).get();
		Article buch_OOP = arep.findById( "SKU-425378" ).get();
		Article pfanne = arep.findById( "SKU-300926" ).get();
		Article helm = arep.findById( "SKU-663942" ).get();
		Article karte = arep.findById( "SKU-583978" ).get();

		/*
		 * Build orders.
		 */

		// Eric's 1st order
		Order o8592 = new Order( eric )	// new order for Eric
				.setId( "8592356245" )	// assign order-id: 8592356245
				// add items to order
				.addItem( teller, 4 )	// 4 Teller, 4x 6.49 �
				.addItem( becher, 8 )	// 8 Becher, 8x 1.49 �
				.addItem( buch_OOP, 1 )	// 1 Buch "OOP", 1x 79.95 �, 7% MwSt (5.23�)
				.addItem( tasse, 4 );	// 4 Tassen, 4x 2.99 �
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
		// Eric's 3rd order
		Order o7356 = new Order( eric )
				.setId( "7356613535" )
				.addItem( helm, 1 )
				.addItem( karte, 1 );
		//
		// Eric's 4th order
		Order o4450 = new Order( eric )
				.setId( "4450735661" )
				.addItem( tasse, 3 )
				.addItem( becher, 3 )
				.addItem( kanne, 1 );
		//
		// Lena's 1sr order
		Order o6173 = new Order( lena )
				.setId( "6173535635" )
				.addItem( buch_Java, 1 )
				.addItem( karte, 1 );
		//
		// Tim's 1sr order
		Order o6174 = new Order( tim )
				.setId( "6356351735" )
				.addItem( buch_Java, 1 )
				.addItem( buch_OOP, 1 );
		//
		// Khaled's 1sr order
		Order o6175 = new Order( khaled )
				.setId( "3563617355" )
				.addItem( teller, 4 )
				.addItem( becher, 4 )
				.addItem( pfanne, 1 );
		//
		// Brigitte's 1sr order
		Order o6176 = new Order( brigitte )
				.setId( "4434573683" )
				.addItem( teller, 6 )
				.addItem( tasse, 6 );
		//
		// Joel's 1sr order
		Order o6177 = new Order( joel )
				.setId( "5356173635" )
				.addItem( buch_Java, 1 )
				.addItem( karte, 1);
		//
		// Max's 1sr order
		Order o6178 = new Order( max )
				.setId( "9356736867" )
				.addItem( helm, 1 )
				.addItem( karte, 2 )
				.addItem( buch_Java, 1 );
		//
		// Lena's 2nd order
		Order o6179 = new Order( lena )
				.setId( "5356617335" )
				.addItem( teller, 12 )
				.addItem( tasse, 12 )
				.addItem( pfanne, 2 );

		/*
		 * Save orders to OrderRepository.
		 */
		accept( o8592 );
		accept( o3563 );
		accept( o5234 );
		accept( o6135 );
		accept( o7356 );
		accept( o4450 );
		accept( o6173 );	// total value (all orders):  |   642.70�|   76.78�|
		//
//		accept( o6174 );
//		accept( o6175 );
//		accept( o6176 );
//		accept( o6177 );
//		accept( o6178 );
//		accept( o6179 );	// total value (all orders):  | 1,414.73�|  176.40�|

		return this;
	}

}
