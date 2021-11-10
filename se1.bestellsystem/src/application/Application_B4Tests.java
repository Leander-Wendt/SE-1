package application;

import java.util.stream.Stream;

import datamodel.Article;
import datamodel.Currency;
import datamodel.Order;
import datamodel.OrderItem;
import datamodel.TAX;


/**
 * Class to test remaining classes of the datamodel package: Article, Order,
 * OrderItem and Customer. Customer objects are derived from super class.
 * 
 * Run test methods: articleTests(), orderTests() and customerTests()
 * in the main() function;
 * 
 * @author Leander Wendt
 *
 */

public class Application_B4Tests extends Application_B3 {


	/**
	 * Public main function that creates instance of Application_B3 class
	 * and runs the printCustomers() method.
	 * 
	 * @param args standard argument vector passed from command line
	 */
	public static void main( String[] args ) {
		System.out.println( "SE1 Bestellsystem\n" );
		//
		Application_B4Tests app = new Application_B4Tests();
		//
		int passed = 0;
		int expectedPassed = 15;
		passed += app.articleTests( 15 );		// run tests for class: Article
		//
		expectedPassed += 31;
		passed += app.orderTests( 31 );			// run tests for classes: Order and OrderItem
		//		//
		expectedPassed += 12;
		passed += app.customerTests( 12 );		// run tests for class: Customer
		//
		app.assertThat( String.format( "total: %d/%d tests", passed == expectedPassed? passed :
			(expectedPassed - passed), expectedPassed ), passed == expectedPassed, "error" );
	}


	/*
	 * Simple test driver
	 * 
	 * @param testMsg message with test number
	 * @param assertionCondition result of comparison between expected and actual result
	 * @param errMsg message shown when assertionCondition == FALSE
	 * @return 1 if assertionCondition is TRUE, 0 otherwise (used to count passed tests)
	 */
	private int assertThat( String testMsg, boolean assertionCondition, String errMsg ) {
		if( assertionCondition ) {
			System.out.println( testMsg + " passed" );
		} else {
			System.err.println( testMsg + " failed: " + errMsg );
		}
		return assertionCondition? 1 : 0;
	}


	/*
	 * Tests for class: Article
	 */
	private int articleTests( int expectedTestsPassed ) {
		System.out.println( "Article tests:\n-----------------" );
		int passed = 0;
		Article a1 = new Article( "test article", -100 );		// -100 is illegal price, change to 0
		passed += assertThat( "Test A01:", a1.getId() == null, String.format( "expected null as initial id, found: %s", a1.getId() ) );
		passed += assertThat( "Test A02:", a1.getUnitPrice() == 0, String.format( "expected 0 as initial price, found: %d", a1.getUnitPrice() ) );
		//
		a1.setId( "" );					// "" is illegal id
		a1.setUnitPrice( 100 );			// 100 is valid price
		passed += assertThat( "Test A03:", a1.getId() == null, String.format( "expected null as id, found: \"%s\"", a1.getId() ) );
		passed += assertThat( "Test A04:", a1.getUnitPrice() == 100, String.format( "expected 100 as price, found: %d", a1.getUnitPrice() ) );
		//
		String vid = "ABCDEF";			// valid id
		String id = a1.setId( vid )		// valid id set for the first time
			.setUnitPrice( -100 )		// -100 is invalid price, prior price 100 remains unchanged
			.getId();
		passed += assertThat( "Test A05:", id != null && id.equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		passed += assertThat( "Test A06:", a1.getUnitPrice() == 100, String.format( "expected 100 as price, found: %d", a1.getUnitPrice() ) );
		//
		id = a1.setId( null ).getId();	// attempt to set invalid id with valid id already set
		passed += assertThat( "Test A07:", id != null && id.equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		//
		id = a1.setId( "" ).getId();	// attempt to set invalid id with valid id already set
		passed += assertThat( "Test A08:", id != null && id.equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		//
		id = a1.setId( "XYZ" ).getId();	// attempt to set another valid id with valid id already set
		passed += assertThat( "Test A09:", id != null && id.equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		//
		Currency cy = a1.getCurrency();
		passed += assertThat( "Test A10:", cy == Currency.EUR, String.format( "expected EUR as currency, found: \"%s\"", cy.toString() ) );
		//
		cy = a1.setCurrency( Currency.USD ).getCurrency();
		passed += assertThat( "Test A11:", cy == Currency.USD, String.format( "expected USD as currency, found: \"%s\"", cy.toString() ) );
		//
		cy = a1.setCurrency( null ).getCurrency();	// null is illegal, attribute remains unchanged
		passed += assertThat( "Test A12:", cy == Currency.USD, String.format( "expected USD as currency, found: \"%s\"", cy.toString() ) );
		//
		TAX tax = a1.getTax();
		passed += assertThat( "Test A20:", tax == TAX.GER_VAT, String.format( "expected TAX.GER_VAT as tax rate, found: \"%s\"", tax.toString() ) );
		//
		tax = a1.setTax( TAX.GER_VAT_REDUCED ).getTax();
		passed += assertThat( "Test A21:", tax == TAX.GER_VAT_REDUCED, String.format( "expected GER_VAT_REDUCED as tax rate, found: \"%s\"", tax.toString() ) );
		//
		tax = a1.setTax( null ).getTax();			// null is illegal, attribute remains unchanged
		passed += assertThat( "Test A22:", tax == TAX.GER_VAT_REDUCED, String.format( "expected GER_VAT_REDUCED as currency, found: \"%s\"", tax.toString() ) );
		//
		System.out.println( String.format( "-----------------: %d/%d tests passed.\n", passed, expectedTestsPassed ) );
		return passed;
	}


	/*
	 * Tests for classes: Order and OrderItem
	 */
	private int orderTests( int expectedTestsPassed ) {
		System.out.println( "Order tests:\n-----------------" );
		int passed = 0;
		Article a1 = new Article( "test article", 100 );
		try {
			new Order( null );		// attempt to create invalid order with Customer reference null, must throw IllegalArgumentException
			assertThat( "Test O01:", false, "expected IllegalArgumentException that was not thrown" );

		} catch( IllegalArgumentException e ) {
			passed += assertThat( "Test O01:", true, "passed" );
		} catch( Exception e2 ) {
			assertThat( "Test O01:", false, String.format( "expected IllegalArgumentException, but %s was thrown", e2.getClass().getSimpleName() ) );
		}
		Order o1 = new Order( eric );
		//
		passed += assertThat( "Test O02:", o1.getId() == null, String.format( "expected null as initial id, found: %s", o1.getId() ) );
		passed += assertThat( "Test O03:", o1.setId( null ).getId() == null, String.format( "expected null as id, found: %s", o1.getId() ) );
		passed += assertThat( "Test O04:", o1.setId( "" ).getId() == null, String.format( "expected null as id, found: %s", o1.getId() ) );
		//
		String vid = "ABCDEF";					// valid id
		String id = o1.setId( vid ).getId();	// valid id set for the first time
		passed += assertThat( "Test O05:", id != null && id.equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		passed += assertThat( "Test O06:", o1.setId( null ).getId().equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		passed += assertThat( "Test O07:", o1.setId( "" ).getId().equals( vid ), String.format( "expected \"%s\" as id, found: \"%s\"", vid, id ) );
		//
		passed += assertThat( "Test O10:", o1.getCustomer() == eric, String.format( "expected \"%s\" as customer, found: \"%s\"", eric.getName(), o1.getCustomer() ) );
		//
		// TODO: getCreationDate(), setCreationDate() tests
		//
		passed += assertThat( "Test O30:", o1.itemsCount() == 0, String.format( "expected %d as itemsCount, found: %d", 0, o1.itemsCount() ) );
		passed += assertThat( "Test O31:", o1.getItemsAsArray() != null && o1.getItemsAsArray().getClass().isArray(),
				String.format( "invalid return from getItemsAsArray: []" ) );
		passed += assertThat( "Test O32:", o1.getItemsAsArray() != null && o1.getItemsAsArray().length == 0,
				String.format( "expected %d as itemsCount, found: %d", 0, o1.getItemsAsArray().length ) );
		//
		//
		// Order: addItem() tests
		//
		try {
			o1.addItem( null, 1 );		// attempt to add invalid order item with Article reference null, must throw IllegalArgumentException
			assertThat( "Test O40:", false, "expected IllegalArgumentException that was not thrown" );

		} catch( IllegalArgumentException e ) {
			passed += assertThat( "Test O40:", true, "passed" );
		} catch( Exception e2 ) {
			assertThat( "Test O40:", false, String.format( "expected IllegalArgumentException, but %s was thrown", e2.getClass().getSimpleName() ) );
		}
		//
//		try {
//			o1.addItem( a1, 0 );		// attempt to add invalid number of units ordered, must throw IllegalArgumentException
//			assertThat( "Test O41:", false, "expected IllegalArgumentException that was not thrown" );
//
//		} catch( IllegalArgumentException e ) {
//			passed += assertThat( "Test O41:", true, "passed" );
//		} catch( Exception e2 ) {
//			assertThat( "Test O41:", false, String.format( "expected IllegalArgumentException, but %s was thrown", e2.getClass().getSimpleName() ) );
//		}
//		//
//		try {
//			o1.addItem( a1, -1 );		// attempt to add invalid number of units ordered, must throw IllegalArgumentException
//			assertThat( "Test O42:", false, "expected IllegalArgumentException that was not thrown" );
//
//		} catch( IllegalArgumentException e ) {
//			passed += assertThat( "Test O42:", true, "passed" );
//		} catch( Exception e2 ) {
//			assertThat( "Test O42:", false, String.format( "expected IllegalArgumentException, but %s was thrown", e2.getClass().getSimpleName() ) );
//		}
		//
		//
		o1.addItem( a1, 1 );	// add valid order item: 1 unit of article a1
		//
		passed += assertThat( "Test O43:", o1.itemsCount() == 1, String.format( "expected %d as itemsCount, found: %d", 1, o1.itemsCount() ) );
		passed += assertThat( "Test O44:", o1.getItemsAsArray() != null && o1.getItemsAsArray().length == 1,
				String.format( "expected %d as itemsCount, found: %d", 1, o1.getItemsAsArray().length ) );
		passed += assertThat( "Test O45:", o1.getItems() != null && o1.getItems() instanceof Iterable,
				String.format( "expected \"%s\" from getItems(), found: \"%s\"", "Iterable", o1.getItems() ) );
		//
		//
		o1.addItem( a1, 10 );	// add second valid order item: 10 units of article a1
		//
		passed += assertThat( "Test O46:", o1.itemsCount() == 2, String.format( "expected %d as itemsCount, found: %d", 2, o1.itemsCount() ) );
		passed += assertThat( "Test O47:", o1.getItemsAsArray() != null && o1.getItemsAsArray().length == 2,
				String.format( "expected %d as itemsCount, found: %d", 2, o1.getItemsAsArray().length ) );
		//
		int count = 0;
		Article a0 = null;
		for( OrderItem item : o1.getItems() ) {
			if( count++ == 0 ) {
				a0 = item.getArticle();
			} else {
				count = (a0 == item.getArticle())? count : -1000;	// compare with article of second item
			}
		}
		passed += assertThat( "Test O50:", count == 2, String.format( "expected same article in ordered items, found: different" ) );
		OrderItem[] items = o1.getItemsAsArray();
		passed += assertThat( "Test O51:", items != null && items.length == 2, String.format( "expected OrderItem[2] items, found: %s", (Object)items ) );
		passed += assertThat( "Test O52:", items[0].getArticle() == items[1].getArticle(), String.format( "expected same Article, found different" ) );
		passed += assertThat( "Test O53:", items[0].getUnitsOrdered() == 1,
				String.format( "expected %d units in item[0], found %d", 1, items[0].getUnitsOrdered() ) );
		passed += assertThat( "Test O54:", items[1].getUnitsOrdered() == 10,
				String.format( "expected %d units in item[1], found %d", 10, items[1].getUnitsOrdered() ) );
		//
		//
		// Order: Stream<OrderItem> getItemsAsStream() tests
		//
		Stream<OrderItem> ois = o1.getItemsAsStream();
		passed += assertThat( "Test O60:", ois != null, String.format( "expected Stream<OrderItem> as itemsCount, found: null" ) );
		//
		count = (int)ois.count();
		passed += assertThat( "Test O61:", count == 2, String.format( "expected %d as stream count, found: %d", 2, count ) );
		//
		ois = o1.getItemsAsStream();
		count = ois.reduce( 0, (sum, item) -> sum + item.getUnitsOrdered(), Integer::sum );		// sum units ordered over all items
		passed += assertThat( "Test O62:", count == (1 + 10), String.format( "expected %d as sum units ordered over all items, found: %d", 11, count ) );
		//
		//
		// Order: deleteItem(), deleteAllItems() tests
		//
		o1.deleteItem( -1 );	// illegal index, nothing is deleted
		passed += assertThat( "Test O70:", o1.itemsCount() == 2, String.format( "expected %d as itemsCount, found: %d", 2, o1.itemsCount() ) );
		//
		o1.deleteItem( 2 );		// illegal index, nothing is deleted
		passed += assertThat( "Test O71:", o1.itemsCount() == 2, String.format( "expected %d as itemsCount, found: %d", 2, o1.itemsCount() ) );
		//
		o1.deleteItem( 0 );		// delete first item
		passed += assertThat( "Test O72:", o1.itemsCount() == 1, String.format( "expected %d as itemsCount, found: %d", 1, o1.itemsCount() ) );
		passed += assertThat( "Test O73:", o1.getItemsAsArray().length == 1, String.format( "expected %d as itemsCount, found: %d", 1, o1.itemsCount() ) );
		//
		count = o1.getItemsAsArray()[0].getUnitsOrdered();		// ordered units of remaining item
		passed += assertThat( "Test O74:", count == 10, String.format( "expected %d as ordered units of remaining item, found: %d", 10, count ) );
		//
		o1.deleteAllItems();
		passed += assertThat( "Test O75:", o1.itemsCount() == 0, String.format( "expected %d as itemsCount, found: %d", 0, o1.itemsCount() ) );
		//
		System.out.println( String.format( "-----------------: %d/%d tests passed.\n", passed, expectedTestsPassed ) );
		return passed;
	}


	/*
	 * Tests for class: Customer
	 */
	private int customerTests( int expectedTestsPassed ) {
		System.out.println( "Customer tests:\n-----------------" );
		int passed = 0;
		//
		long expect_long = anne.getId();
		anne.setId( 100000 );		// illegal, id already set (can only be set once)
		passed += assertThat( "Test C01:", anne.getId() == 643270, String.format( "expected %d as initial id, found: %d", expect_long, anne.getId() ) );
		//
		String expect = "Anna";
		anne.setName( expect, null );
		passed += assertThat( "Test C10:", anne.getFirstName().equals( expect ), String.format( "expected %s as first name, found: %s", expect, anne.getId() ) );
		//
		anne.setName( null, "Beyer" );
		expect = "Beyer, Anna";
		passed += assertThat( "Test C11:", anne.getName().equals( expect ), String.format( "expected %s as name, found: %s", expect, anne.getName() ) );
		//
		int expect_int = 2;
		passed += assertThat( "Test C20:", anne.getContacts().length == expect_int,
				String.format( "expected %d contacts, found: %d", expect_int, anne.getContacts().length ) );
		//
		expect = "030 928-52345";
		expect_int = 3;
		anne.addContact( expect );
		passed += assertThat( "Test C21:", anne.getContacts().length == expect_int && anne.getContacts()[2].equals( expect ),
				String.format( "expected %d contacts, found: %d", expect_int, anne.getContacts().length ) );
		//
		expect = "tim2346@gmx.de";
		expect_int = 2;
		tim.addContact( expect );		// test duplicate entries
		tim.addContact( expect );
		tim.addContact( expect );
		tim.addContact( "030 42928424" );
		passed += assertThat( "Test C22:", tim.contactsCount() == expect_int && tim.getContacts()[0].equals( expect )
				&& tim.getContacts()[1].equals( "030 42928424" ),
				String.format( "expected %d contacts, found: %d", expect_int, tim.getContacts().length ) );
		//
		expect_int = 0;
		tim.deleteAllContacts();		// delete all contacts
		passed += assertThat( "Test C23:", tim.contactsCount() == expect_int,
				String.format( "expected %d contacts, found: %d", expect_int, tim.getContacts().length ) );
		//
		//
		// extended tests
		//
		tim.setName( null );	// null is invalid; first- and lastName unchanged
		expect = "Tim";
		passed += assertThat( "Test C12:", tim.getFirstName().equals( expect ),
				String.format( "expected %s as initial id, found: %s, setFirstName(null) not ignored, changed value", expect, tim.getFirstName() ) );
		//
		tim.setName( null, null );	// null is invalid; name unchanged
		expect = "Schulz-Mueller";
		passed += assertThat( "Test C13:", tim.getLastName().equals( expect ),
				String.format( "expected %s as initial id, found: %s, setLastName(null) not ignored, changed value", expect, tim.getLastName() ) );
		//
		tim.deleteAllContacts();
		tim.addContact( "030 42928424" );	// accepted contact
		tim.addContact( null );		// null is invalid contact, ignored
		tim.addContact( "" );		// "" is invalid as contact, ignored
		tim.addContact( "tim2346@gmx.de" );	// accepted contact
		expect = "030 42928424";
		String expect_2 = "tim2346@gmx.de";
		expect_int = 2;
		passed += assertThat( "Test C24:", tim.contactsCount() == expect_int && tim.getContacts()[0].equals( expect )
				&& tim.getContacts()[1].equals( expect_2 ),
				String.format( "expected %d contacts, found: %d", expect_int, tim.getContacts().length ) );
		//
		tim.deleteContact( -1 );	// illegal, invalid index, ignored
		passed += assertThat( "Test C25:", tim.contactsCount() == expect_int && tim.getContacts()[0].equals( expect )
				&& tim.getContacts()[1].equals( expect_2 ),
				String.format( "expected %d contacts, found: %d", expect_int, tim.getContacts().length ) );
		//
		expect_int = 1;
		tim.deleteContact( 1 );		// delete contact #1
		passed += assertThat( "Test C26:", tim.contactsCount() == expect_int && tim.getContacts()[0].equals( expect ),
				String.format( "expected %d contacts, found: %d", expect_int, tim.getContacts().length ) );
		//
		System.out.println( String.format( "-----------------: %d/%d tests passed.\n", passed, expectedTestsPassed ) );
		return passed;
	}

}
