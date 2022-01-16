package system.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import datamodel.Article;
import datamodel.Currency;
import datamodel.Customer;
import datamodel.Order;
import datamodel.TAX;
import system.InventoryManager;
import system.Repository;


/**
 * Local implementation of DataSource interface.
 *
 */

class DataSourceImpl implements DataSource {


	@Override
	public long importCustomerJSON( String jsonFileName, Repository<Customer> collector, Integer... limit ) {
		long count = read( jsonFileName,
				jsonNode -> createCustomer( jsonNode ),
				e -> collector.save( e ),
				limit
		);
		return count;
	}

	@Override
	public long importArticleJSON( String jsonFileName, InventoryManager inventoryManager, Integer... limit ) {
		long count = read( jsonFileName,
				jsonNode -> {
					Optional<Article> aopt = createArticle( jsonNode );
					aopt.ifPresent( a -> {
						inventoryManager.save( a );	// add article to inventoryManager 
						JsonNode jn = jsonNode.get( "unitsInStock" );	// try to get value from JSON
						if( jn != null ) {
							inventoryManager.update( a.getId(), jn.asInt() );
						}
					});
					return aopt;
				},
//				e -> collector.save( e ),
				e -> {},	// article already collected with: inventoryManager.create( a )
				limit
		);
		return count;
	}

	@Override
	public long importOrderJSON( String jsonFileName, Repository<Order> collector, Integer... limit ) {
		// TODO Auto-generated method stub
		return 0;
	}


	/**
	 * Import objects from file with JSON Array [ {obj1}, {obj2}, ... ]
	 * 
	 * @param jsonFileName name of the JSON file
	 * @param limit maximum number of imported JSON objects (vararg)
	 * @return List of Customer objects imported from JSON file (up to limit)
	 */

	private <T> long read( String jsonFileName,
			Function<JsonNode,Optional<T>> creator,
			Consumer<T> collector,
			Integer... limit )
	{
		int lim = Math.max( limit.length > 0? limit[0].intValue() : Integer.MAX_VALUE, 0 );
		long count = 0;
		try (
				// auto-close on exception, InputStream implements the java.lang.AutoClosable interface
				InputStream fis = new FileInputStream( jsonFileName );
			) {
				//
				count = StreamSupport
					// stream source: read JSON array and split into stream of JsonNode's
					.stream( new ObjectMapper().readTree( fis ).spliterator(), false )
					//
					// cut stream to limited number of objects
					.limit( lim )
					//
					// map JsonNode to new Optional<T> Object
					.map( jsonNode -> {
						
						Optional<T> opt = creator.apply( jsonNode );
						if( opt.isEmpty() ) {
							System.out.println( "dropping: " + jsonNode.toString() );
						}
						return opt;
					})
					//
					// filter valid article objects (remove invalid ones from stream)
					.filter( a -> a.isPresent() )
					//
					// map remaining valid objects to T objects
					.map( aOpt -> {
						T entity = aOpt.get();
						collector.accept( entity );
						return entity;
					})
					//
					// collect and return valid article objects only
					.count();

			} catch( Exception e ) {
				e.printStackTrace();
			}

		return count;
	}


	/**
	 * Factory method that creates a new Customer object with attribute values from a
	 * JSON Node. Methods returns Optional<Customer>, which can be empty if no valid
	 * object could be created from JSON
	 * 
	 * @param jsonNode JSON Node with Customer attributes
	 * @return Optional<Customer> with object with valid attributes from JSON Node or empty
	 */

	private Optional<Customer> createCustomer( JsonNode jsonNode ) {
		//
		try {
			//
			// read attribute values from JsonNode
			String idStr = valueAsString( jsonNode, "id", true );
			String nameStr = valueAsString( jsonNode, "name", true );
			String contactStr = valueAsString( jsonNode, "contacts", false );
			//
			long id = idStr != null? Long.parseLong( idStr ) : -1;
			String name = nameStr != null? nameStr : "";
			String contacts = contactStr != null? contactStr : "";

			if( validCustomerAttrs( id, name, contacts ) ) {
				Customer customer = new Customer()		// create new Customer object
					.setId( id )					// and initialize attributes
					.setName( name );
				//
				for( String contact : contacts.split( "," ) ) {
					if( contact.length() > 0 ) {
						customer.addContact( contact );
					}
				}
				//
				return Optional.of( customer );
			}

		} catch( InvalidParameterException ipex ) {
			System.err.println( "AttributeNotFoundException, " + ipex.getMessage() + " in: " + jsonNode.toString() );

		} catch( NumberFormatException nex ) {
			System.err.println( "NumberFormatException in: " + jsonNode.toString() );

		} catch( NullPointerException npex ) {
			System.err.println( npex.getClass().getSimpleName() + " in: " + jsonNode.toString() );

		} catch( Exception ex ) {
			System.err.println( ex.getClass().getSimpleName() + " in: " + jsonNode.toString() );
		}

		return Optional.empty();
	}


	/**
	 * Method to extract the value for an attribute name from a JsonNode.
	 * 
	 * @param jsonNode JSON Node to extract attribute
	 * @param attributeName name of attribute (key)
	 * @param mandatory if attributeName could not be found, throws InvalidParameterException
	 * for mandatory attributes, method returns "" otherwise
	 * @return String value for attributeName if found, "" if not for non-mandatory attributes
	 * @throws InvalidParameterException if attributeName cannot be found and mandatory is true
	 */

	private String valueAsString( JsonNode jsonNode, String attributeName, boolean mandatory ) throws InvalidParameterException {
		if( jsonNode.has( attributeName ) ) {
			return jsonNode.get( attributeName ).asText();
		} else {
			if( mandatory )
				throw new InvalidParameterException( "\"" + attributeName + "\" not found" );
			return "";
		}
	}


	/**
	 * Validate Customer attribute values passed as arguments
	 * 
	 * @param id Customer id attribute
	 * @param name Customer's unitPrice attribute
	 * @param contacts Customer's contacts attribute
	 * @return true if all arguments are valid Customer attribute values
	 */

	private boolean validCustomerAttrs( long id, String name, String contacts ) {
		boolean valid = true;
		valid = valid && id >= 0;
//		valid = valid && name != null && name.length() > 0;
		return valid;
	}


	/**
	 * Factory method that creates a new Article object with attribute values from a
	 * JSON Node. Methods returns Optional<Article>, which can be empty if no valid
	 * object could be created from JSON
	 * 
	 * @param jsonNode JSON Node with Article attributes
	 * @return Optional<Article> with object with valid attributes from JSON Node or empty
	 */

	private Optional<Article> createArticle( JsonNode jsonNode ) {
		//
		try {
			//
			// read attribute values from JsonNode
			String id = valueAsString( jsonNode, "id", true );
			String priceStr = valueAsString( jsonNode, "unitPrice", true );
			String currencyStr = valueAsString( jsonNode, "currency", true );
			String vatStr = valueAsString( jsonNode, "vat", false );
			//String unitsInStockStr = jsonValueAsString( jsonNode, "unitsInStock", true );
			String description = valueAsString( jsonNode, "description", true );

			Double priceDbl = Double.parseDouble( priceStr );
			//
			Currency currency = currencyStr.equalsIgnoreCase( "EUR" ) ||
				currencyStr.equalsIgnoreCase( "EURO" )? Currency.EUR :
				currencyStr.equalsIgnoreCase( "USD" )? Currency.USD :
				currencyStr.equalsIgnoreCase( "GBP" )? Currency.GBP :
				currencyStr.equalsIgnoreCase( "YEN" )? Currency.YEN : Currency.NONE;
			//
			if( currency == Currency.EUR || currency == Currency.USD || currency == Currency.GBP ) {
				priceDbl = priceDbl * 100.0;
			}
			//
			long price = Math.round( priceDbl );

			if( validArticleAttrs( id, price, description, currency ) ) {
				Article article = new Article()		// create new Article object
					.setId( id )					// and initialize attributes
					.setCurrency( currency )
					.setDescription(description)
					.setUnitPrice( price );
				//
				if( vatStr.equalsIgnoreCase( "VAT_REDUCED" ) ) {
					article.setTax( TAX.GER_VAT_REDUCED );
				}
				//
				return Optional.of( article );
			}

		} catch( InvalidParameterException ipex ) {
			System.err.println( "AttributeNotFoundException, " + ipex.getMessage() + " in: " + jsonNode.toString() );

		} catch( NumberFormatException nex ) {
			System.err.println( "NumberFormatException in: " + jsonNode.toString() );

		} catch( NullPointerException npex ) {
			System.err.println( npex.getClass().getSimpleName() + " in: " + jsonNode.toString() );

		} catch( Exception ex ) {
			System.err.println( ex.getClass().getSimpleName() + " in: " + jsonNode.toString() );
		}

		return Optional.empty();
	}


	/**
	 * Validate Article attribute values passed as arguments
	 * 
	 * @param id Article's id attribute
	 * @param unitPrice Article's unitPrice attribute
	 * @param description Article's description attribute
	 * @param currency Article's currency attribute
	 * @return true if all arguments are valid Article attribute values
	 */

	private boolean validArticleAttrs( String id, long unitPrice, String description, Currency currency ) {
		boolean valid = true;
		valid = valid && id != null && id.length() > 0 && ! id.equalsIgnoreCase( "null" );
		valid = valid && unitPrice >= 0;
		valid = valid && description != null && description.length() > 0;
		valid = valid && currency != Currency.NONE;
		return valid;
	}

}
