package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import datamodel.Article;
import datamodel.Currency;
import datamodel.Customer;
import datamodel.TAX;


/**
 * Class to read Customer and Order objects from JSON.
 * 
 * @author sgra64
 *
 */

public class Application_C3_jsondata {


	/**
	 * Public main function that creates instance of Application_B3 class
	 * and runs the printCustomers() method.
	 * 
	 * @param args standard argument vector passed from command line
	 */

	public static void main( String[] args ) {
		System.out.println( "SE1 Bestellsystem\n" );
		//
		final Application_C3_jsondata app = new Application_C3_jsondata();

		final String jsonFileName = "src/data/articles_871.json";
		final int limit = 40;	// limit number of objects imported from JSON
		//
		StringBuffer sb = new StringBuffer( "from: " + jsonFileName + ":\n\\\\" );
		//
//		sb.append( "\nJSON:\n" );
//		app.printJsonArrayfromFile( sb, jsonFileName, false, limit );

		sb.append( "\nList<Article>:\n" );
		List<Article> articles = app.readArticles( jsonFileName, limit );
		//
		articles.forEach( article -> app.print( sb,  article, " --> " ) );
		//
		sb.append( "\\\\\nimported: " ).append( articles.size() ).append( " Article objects." );
		//
		System.out.println( sb );

		final String jsonFileName1 = "src/data/customers_10.json";
		sb.append( "\nfrom: " + jsonFileName1 + ":\n\\\\" );
		//
		sb.append( "\nList<Customer>:\n" );
		List<Customer> customers = app.readCustomers( jsonFileName1 );

		customers = customers.stream()
			.sorted((n1, n2) -> n1.getLastName().compareTo(n2.getLastName()))
			.collect(Collectors.toList());

		//
		customers.forEach( customer -> app.print( sb,  customer, " ‐‐> " ) );
		//
		sb.append( "\\\\\nimported: " )
		.append( customers.size() ).append( " Customer objects." );
		//
		System.out.println( sb );
	}

	/**
	 * Import objects from file with JSON Array [ {obj1}, {obj2}, ... ]
	 * 
	 * @param jsonFileName name of the JSON file
	 * @param limit maximum number of imported JSON objects (vararg)
	 * @return List of Article objects imported from JSON file (up to limit)
	 */

	List<Article> readArticles( String jsonFileName, Integer... limit ) {
		int lim = Math.max( limit.length > 0? limit[0].intValue() : Integer.MAX_VALUE, 0 );
		List<Article> articles = null;
		try (
				// auto-close on exception, InputStream implements the java.lang.AutoClosable interface
				InputStream fis = new FileInputStream( jsonFileName );
			) {
				// 
				articles = StreamSupport
					// stream source: read JSON array and split into stream of JsonNode's
					// erstellt einen neuen (nicht! wegen param false) Parallelen Stream zum Supllier des fis inputstreams
					.stream( new ObjectMapper().readTree( fis ).spliterator(), false )
					//
					// cut stream to limited number of objects
					// limitiert die größe der Liste basierend auf dem gegebenem Parameter
					.limit( lim )
					//
					// map JsonNode to new Optional<Article> Object
					// schreibt die daten aus der .json in java Article Objekte um
					.map( jsonNode -> {
						Optional<Article> article = createArticle( jsonNode );
						if( article.isEmpty() ) {
							System.out.println( "dropping: " + jsonNode.toString() );
						}
						return article;
					})
					//
					// filter valid article objects (remove invalid ones from stream)
					// QA von den erstellten Artikel Objekten
					.filter( a -> a.isPresent() )
					//
					// map remaining valid objects to Article objects
					.map( aOpt -> aOpt.get() )
					//
					// collect and return valid article objects only
					.collect( Collectors.toList() );

			} catch( Exception e ) {
				articles = new ArrayList<Article>();	// return empty list
				e.printStackTrace();
			}

		return articles;
	}


	/**
	 * Factory method that creates a new Article object with attribute values from a
	 * JSON Node. Methods returns Optional<Article>, which can be empty if no valid
	 * object could be created from JSON
	 * 
	 * @param jsonNode JSON Node with Article attributes
	 * @return Optional<Article> with object with valid attributes from JSON Node or empty
	 */

	Optional<Article> createArticle( JsonNode jsonNode ) {
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

	List<Customer> readCustomers( String jsonFileName, Integer... limit ) {
		int lim = Math.max( limit.length > 0 ? limit[0].intValue() : Integer.MAX_VALUE, 0 );
		List<Customer> c = null;
		try (
				// auto-close on exception, InputStream implements the java.lang.AutoClosable interface
				InputStream fis = new FileInputStream( jsonFileName );
			) {
				// 
				c = StreamSupport
					// stream source: read JSON array and split into stream of JsonNode's
					// erstellt einen neuen (nicht! wegen param false) Parallelen Stream zum Supllier des fis inputstreams
					.stream( new ObjectMapper().readTree( fis ).spliterator(), false )
					//
					// cut stream to limited number of objects
					// limitiert die größe der Liste basierend auf dem gegebenem Parameter
					.limit( lim )
					//
					// map JsonNode to new Optional<Article> Object
					// schreibt die daten aus der .json in java Article Objekte um
					.map( jsonNode -> {
						Optional<Customer> customer = createCustomer( jsonNode );
						if( customer.isEmpty() ) {
							System.out.println( "dropping: " + jsonNode.toString() );
						}
						return customer;
					})
					//
					// filter valid article objects (remove invalid ones from stream)
					// QA von den erstellten Artikel Objekten
					.filter( a -> a.isPresent() )
					//
					// map remaining valid objects to Article objects
					.map( aOpt -> aOpt.get() )
					//
					// collect and return valid article objects only
					.collect( Collectors.toList() );

			} catch( Exception e ) {
				c = new ArrayList<Customer>();	// return empty list
				e.printStackTrace();
			}

		return c;
	}

	Optional<Customer> createCustomer( JsonNode jsonNode ) {
		//
		try {
			//
			// read attribute values from JsonNode
			Long id = Long.parseLong(valueAsString( jsonNode, "id", true));
			String name = valueAsString( jsonNode, "name", true);
			String[] contacts = valueAsString( jsonNode, "contacts", true).split(", ");

			if( validCustomerAttrs(id, name) ) {
				Customer customer = new Customer()		// create new Article object
					.setId( id )						// and initialize attributes
					.setName( name );
					for (String str : contacts){
						customer.addContact(str);
					}
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
	 * Validate Article attribute values passed as arguments
	 * 
	 * @param id Article's id attribute
	 * @param unitPrice Article's unitPrice attribute
	 * @param description Article's description attribute
	 * @param currency Article's currency attribute
	 * @return true if all arguments are valid Article attribute values
	 */

	boolean validArticleAttrs( String id, long unitPrice, String description, Currency currency ) {
		boolean valid = true;
		valid = valid && id != null && id.length() > 0 && ! id.equalsIgnoreCase( "null" );
		valid = valid && unitPrice >= 0;
		valid = valid && description != null && description.length() > 0;
		valid = valid && currency != Currency.NONE;
		return valid;
	}

	boolean validCustomerAttrs( Long id, String name) {
		boolean valid = true;
		valid = valid && id != null && id > 0;
		valid = valid && !name.trim().equals("") && name != null;
		return valid;
	}


	/**
	 * Print content of JSON Array file: [ {...}, {...}, ... ]
	 * 
	 * @param jsonFileName name of the JSON file
	 * @param pretty print with pretty format (indented multi-line) or flat line format
	 * @param limit maximum number of printed JSON objects (vararg)
	 */

	StringBuffer printJsonArrayfromFile( StringBuffer sb, String jsonFileName, boolean pretty, Integer... limit ) {
		StringBuffer sb_ = sb != null? sb : new StringBuffer();
		int lim = Math.max( limit.length > 0? limit[0].intValue() : Integer.MAX_VALUE, 0 );
		try (
				// auto-close on exception, InputStream implements the java.lang.AutoClosable interface
				InputStream fis = new FileInputStream( jsonFileName );
		) {
			//
			StreamSupport
				// read JSON array and split into stream of JsonNode's
				.stream( new ObjectMapper().readTree( fis ).spliterator(), false )
				//
				// limit printed JsonNode's
				.limit( lim )
				.forEach( jsonNode -> sb_.append( pretty? jsonNode.toPrettyString() : jsonNode.toString() ).append( "\n" ) );

		} catch( FileNotFoundException fex ) {
			System.err.println( "File not found: " + jsonFileName + "\n" );

		} catch( JsonParseException nex ) {
			System.err.println( "JsonParseException: " + jsonFileName + "\n" + nex.getMessage() );

		} catch( IOException ioex ) {
			System.err.println( ioex.getClass().getSimpleName() + ": " + jsonFileName + "\n" + ioex.getMessage() );

		} catch( Exception e ) {
			System.err.println( e.getClass().getSimpleName() + ": " + jsonFileName + "\n" + e.getMessage() );
			e.printStackTrace();
		}
		return sb_;
	}


	/**
	 * Print attribute values of Article objects into StringBuffer
	 * 
	 * @param article article to print
	 * @return StringBuffer with printed article attributes
	 */

	StringBuffer print( StringBuffer sb, Article article, String... prefix ) {
		return (sb != null? sb : new StringBuffer())
			.append( prefix.length > 0? prefix[0] : "" )
			.append( article.getId() ).append( ", " )
			
			//fmtPaddedText( StringBuffer sb, String text, int width, char direction, String... cutoff )
			.append( fmtPaddedText( new StringBuffer(), article.getDescription(), 70, '[', ".." ) )
			//.append( String.format( "%-36s", article.getDescription() + ":" ) )	// "%-16s" left aligned
			.append( fmtPaddedPrice( article.getUnitPrice(), 11, " ", article.getCurrency() ) )
			.append( " (" )
			.append( String.format( "%9s", article.getTax().description() + ")" ) )
			.append( "\n" );
	}

	StringBuffer print( StringBuffer sb, Customer c, String... prefix ) {
		String temp = "";
		String[] cl = c.getContacts();
		for (int i = 0; i < cl.length; i++){
			temp = (i == 0) ? cl[i] : temp + ", " + cl[i];
		}
		temp = "[ " + temp + " ]";
		return (sb != null? sb : new StringBuffer())
			.append( prefix.length > 0? prefix[0] : "" )
			.append( c.getId() ).append( ", " )
			
			//fmtPaddedText( StringBuffer sb, String text, int width, char direction, String... cutoff )
			.append( fmtPaddedText( new StringBuffer(), c.getName(), 36, '[', ".." ) )
			//.append( String.format( "%-36s", article.getDescription() + ":" ) )	// "%-16s" left aligned
			.append( fmtPaddedText( new StringBuffer(), temp, 50, '[', ".." ) )
			.append( "\n" );
	}


	/**
	 * Format price information as String (e.g. "19.99�") from long value
	 * (price in cent).
	 * 
	 * @param price price to format (in cent)
	 * @param currency defines included Currency symbol (empty "" for Currency.NONE)
	 * @return formatted price in a StringBuffer
	 */

	StringBuffer fmtPrice( long price, Currency... currency ) {
		StringBuffer sb = new StringBuffer();
		Currency cur = currency.length > 0? currency[0] : Currency.NONE;
		boolean fractional = cur != Currency.YEN;	// always fractional, including NONE
		String str;
		if( fractional ) {
			long cent = Math.abs( price % 100L );
			str = String.format( "%,d.%02d%s", price / 100, cent, cur.symbol( 1 ) );
		} else {
			str = String.format( "%,d%s", price, cur.symbol( 1 ) );
		}
		sb.append( str );
		return sb;
	}


	/**
	 * Format price information from long value padded to width (e.g. width 10 using
	 * fillChar '.' creates right aligned String "....19.99�".
	 * 
	 * @param price price to format (in cent)
	 * @param width padded width, e.g. 10 for "....19.99�"
	 * @param fillChar character to fill padded spaces
	 * @param currency defines included Currency symbol (empty "" for Currency.NONE)
	 * @return formatted price padded to width in a StringBuffer
	 */

	StringBuffer fmtPaddedPrice( long price, int width, String fillChar, Currency... currency ) {
		StringBuffer sb = fmtPrice( price, currency );
		if( sb.length() > width ) {	// cut price to width
			sb.delete( width - (width >= 1? 1 : 0), sb.length() );
			sb.append( width >= 1? "+" : " " );	// add '+' as cut-off String
		}
		if( sb.length() < width ) {	// fill to width
			sb.insert( 0, fillChar.repeat( width - sb.length() ) );
		}
		return sb;
	}


	/**
	 * Format text either left-'[' or right-']' aligned to given width. If text exceeds
	 * width, a cutoff String can be used to indicate, e.g. "...".
	 * 
	 * @param sb StringBuffer into which result is formatted
	 * @param text text to format
	 * @param width padded width, e.g. 10 for "....ABCDEF"
	 * @param direction '[' for left-, ']' for right-alignment
	 * @param cutoff String included to indicate that text was cut when text exceeds width
	 * @return
	 */

	private final char fillChar = ' ';		// character to fill padded spaces

	StringBuffer fmtPaddedText( StringBuffer sb, String text, int width, char direction, String... cutoff ) {
		String scut = cutoff.length > 0? (cutoff[0]==null? "" : cutoff[0]) : "";
		boolean dir = direction == '[';
		text = text != null? text : "";
		int len = text.length();
		int d = width - len;
		if( d >= 0 ) {	// insert fill spaces
			sb = dir?
				sb.append( text ).append( String.valueOf( fillChar ).repeat( d ) ) :
				sb.append( String.valueOf( fillChar ).repeat( d ) ).append( text );
		//
		} else {	// cut str to width
			d = -d;
			boolean showCutOffStr = d > scut.length();
			if( showCutOffStr ) {
				width -= scut.length();		// adjust for cutoff string
				d += scut.length();
			}
			sb.append( ! dir && showCutOffStr? scut : "" );
			sb = dir?
				sb.append( text.substring( 0, width ) ) :
				sb.append( text.substring( d, len ) );
			//
			sb.append( dir && showCutOffStr? scut : "" );
		}
		return sb;
	}

}
