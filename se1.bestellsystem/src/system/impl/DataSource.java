package system.impl;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import system.Repository;

/**
 * Public interface of a data provider that reads or writes data from/to a data source.
 *	@author Leander Wendt
 */

interface DataSource {


	/**
	 * Import objects from file containing a JSON Array [ {obj1}, {obj2}, ... ].
	 * Collector is called for each JSON object to convert JSON object to Java Object
	 * and collect in a repository or a container.
	 * 
	 * @param jsonFileName name of JSON file to read
	 * @param collector called to convert JSON object to Java Object and collect in a repository or container
	 * @param limit maximum number of JSON objects
	 * @return number of objects imported
	 */

	long importCustomerJSON( String jsonFileName, Repository<Customer> collector, Integer... limit );
	
	long importArticleJSON( String jsonFileName, Repository<Article> collector, Integer... limit );

	long importOrderJSON( String jsonFileName, Repository<Order> collector, Integer... limit );


//	<T, ID> void importJSON( String jsonFileName, DataRepository.Repository<T, ID> collector, Integer... limit );
//	dp.<Customer, Long>importJSON( "data/customers_10.json", crep );
//	dp.<Article, String>importJSON( "data/articles_9.json", arep );
}
