package application;

import java.util.List;
import datamodel.Customer;


/**
 * Class with Customer objects and main() function.
 * 
 * @author sgra64
 *
 */

public class Application_B3 {
	final Customer eric;
	final Customer anne;
	final Customer tim;
	final Customer nadine;
	final Customer khaled;
	//
	final List<Customer> customers;


	/**
	 * Default constructor to initialize Customer objects
	 */
	Application_B3() {
		eric = new Customer( "Eric Meyer" )
				.setId( 892474 )	// set id, first time
				.setId( 482947 )	// ignored, since id can only be set once
				.addContact( "eric98@yahoo.com" ) 	// contact_1
				.addContact( "(030) 3945-642298" ) 	// contact_2
				.addContact( "(030) 3945-642298" ); 	// ignore duplicate

		anne = new Customer( "Bayer, Anne" )
				.setId( 643270 )
				.addContact( "anne24@yahoo.de" )
				.addContact( "(030) 3481-23352" );

		tim = new Customer( "Tim Schulz-Mueller" )
				.setId( 286516 )
				.addContact( "tim2346@gmx.de" );

		nadine = new Customer( "Nadine-Ulla Blumenfeld" )
				.setId( 412396 )
				.addContact( "+49 152-92454" );

		khaled = new Customer()
				.setName( "Khaled Saad Mohamed Abdelalim" )
				.setId( 456454 )
				.addContact( "+49 1524-12948210" );

		customers = List.of( eric, anne, tim, nadine, khaled );
	}


	/**
	 * Public main function that creates instance of Application_B3 class
	 * and runs the printCustomers() method.
	 * 
	 * @param args standard argument vector passed from command line
	 */
	public static void main( String[] args ) {
		Application_B3 app = new Application_B3();
		app.printCustomers();
	}


	/*
	 * Formatting and print methods
	 */

	void printCustomers() {
		System.out.print( fmtCustomer( eric ) );
		System.out.print( fmtCustomer( anne ) );
		System.out.print( fmtCustomer( tim ) );
		System.out.print( fmtCustomer( nadine ) );
		System.out.print( fmtCustomer( khaled ) );
	}


	String fmtCustomer( Customer customer ) {
		return fmtCustomer( new StringBuffer(), customer ).toString();
	}

	StringBuffer fmtCustomer( StringBuffer sb, Customer customer ) {
		sb = sb != null? sb : new StringBuffer();
		sb.append( customer.getId() );
		int l1 = sb.length();
		sb.append( " " )
			.append( customer.getLastName() )
			.append( ", " )
			.append( customer.getFirstName() );
		int l2 = sb.length();
		int tabs = Math.max( 1, 3 - ( (l2 - l1) / 10 ) );
		sb.append( "\t".repeat( tabs ) ).append( "contacts: " );
		for( int i=0; i < customer.contactsCount(); i++ ) {
			sb.append( i > 0? ", " : "" )
				.append( customer.getContacts()[i] );
		}
		sb.append( customer.contactsCount()==0? "--\n" : "\n" );
		return sb;
	}

}
