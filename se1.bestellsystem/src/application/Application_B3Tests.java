package application;

import java.util.function.Supplier;


/**
 * Class derived from Application_B3 to test the Customer implementation.
 * Customer objects are derived from super class.
 * 
 * @author sgra64
 *
 */

public class Application_B3Tests extends Application_B3 {

	/**
	 * Number of tests passed
	 */
	private int passed = 0;


	/**
	 * Public main function that creates instance of this class, runs
	 * the printCustomers() method and two test methods with more
	 * detailed tests for the implementation of the Customer class.
	 * 
	 * @param args standard argument vector passed from command line
	 */
	public static void main( String[] args ) {
		Application_B3Tests app = new Application_B3Tests();
		app.printCustomers();
		app.tests( 7, " tests passed", () -> app.customerTests() );
		app.tests( 5, " extended tests passed", () -> app.customerExtendedTests() );
	}


	private void tests( int passTarget, String msg, Supplier<Integer> test_func ) {
		try {
			if( test_func.get() == passTarget ) {
				System.out.println( passTarget + msg );
			} else {
				passed = -1;
				throw new AssertionError( "incomplete number of tests" );
			}
		//
		} catch( AssertionError ex ) {
			System.out.println( passed + " of " + passTarget + msg );
			System.err.println( passed >= 0? ("test " + (passed + 1) + " failed: ") : "" + ex.getMessage() );
		}
	}

	private int customerTests() throws AssertionError {
		passed = 0;
		//
		// test: 1
		anne.setId( 100000 );		// illegal, id already set (can only be set once)
		long id = anne.getId();
		if( id == 643270 ) {		// test id for original value
			passed++;
		} else throw new AssertionError( "id already set (can only be set once)" );
		//
		// test: 2
		anne.setName( "Anna", null );
		if( anne.getFirstName().equals( "Anna" ) ) {
			passed++;
		} else throw new AssertionError( "firstName not correctly changed" );
		//
		// test: 3
		anne.setName( null, "Beyer" );
		if( anne.getName().equals( "Beyer, Anna" ) ) {
			passed++;
		} else throw new AssertionError( "lastName not correctly changed" );
		//
		// test: 4
		if( anne.getContacts().length == 2 ) {
			passed++;
		} else throw new AssertionError( "invalid number of contacts recorded" );
		//
		// test: 5
		anne.addContact( "030 928-52345" );
		if( anne.getContacts().length == 3 && anne.getContacts()[2].equals( "030 928-52345" ) ) {
			passed++;
		} else throw new AssertionError( "contact not correctly recorded" );
		//
		// test: 6
		tim.addContact( "tim2346@gmx.de" );		// test duplicate entries
		tim.addContact( "tim2346@gmx.de" );
		tim.addContact( "tim2346@gmx.de" );
		tim.addContact( "030 42928424" );
		if( tim.contactsCount() == 2 && tim.getContacts()[0].equals( "tim2346@gmx.de" )
				&& tim.getContacts()[1].equals( "030 42928424" ) ) {
			passed++;
		} else throw new AssertionError( "contacts not correctly recorded" );
		//
		// test: 7
		tim.deleteAllContacts();		// delete all contacts
		if( tim.contactsCount() == 0 ) {
			passed++;
		} else throw new AssertionError( "contacts not correctly deleted" );
		//
		return passed;
	}

	private int customerExtendedTests() throws AssertionError {
		passed = 0;
		//
		// test: 1
		tim.setName( null );	// null is invalid; first- and lastName unchanged
		if( tim.getFirstName().equals( "Tim" ) ) {
			passed++;
		} else throw new AssertionError( "setFirstName(null) not ignored, changed value" );
		//
		// test: 2
		tim.setName( null, null );	// null is invalid; name unchanged
		if( tim.getLastName().equals( "Schulz-Mueller" ) ) {
			passed++;
		} else throw new AssertionError( "setLastName(null) not ignored, changed value" );
		//
		// test: 3
		tim.deleteAllContacts();
		tim.addContact( "030 42928424" );	// accepted contact
		tim.addContact( null );		// null is invalid contact, ignored
		tim.addContact( "" );		// "" is invalid as contact, ignored
		tim.addContact( "tim2346@gmx.de" );	// accepted contact
		if( tim.contactsCount() == 2 && tim.getContacts()[0].equals( "030 42928424" )
				&& tim.getContacts()[1].equals( "tim2346@gmx.de" ) ) {
			passed++;
		} else throw new AssertionError( "contacts not correctly recorded" );
		//
		// test: 4
		tim.deleteContact( -1 );	// illegal, invalid index, ignored
		if( tim.contactsCount() == 2 && tim.getContacts()[0].equals( "030 42928424" )
				&& tim.getContacts()[1].equals( "tim2346@gmx.de" ) ) {
			passed++;
		} else throw new AssertionError( "deleteContact( -1 ) changed contacts, not ignored" );
		//
		// test: 5
		tim.deleteContact( 1 );		// delete contact #1
		if( tim.contactsCount() == 1 && tim.getContacts()[0].equals( "030 42928424" ) ) {
			passed++;
		} else throw new AssertionError( "deleteContact( 1 ) contact not correctly deleted" );
		//
		return passed;
	}

}
