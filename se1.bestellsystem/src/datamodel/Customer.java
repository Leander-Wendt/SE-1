package datamodel;


import java.util.ArrayList;
import java.util.List;

/**
 * Class for entity type Customer. Customer is an individual (not a business) who creates and owns orders in the system.
 * 
 * @since 0.1.0
 * @version {@value package_info#Version}
 * @author Leander Wendt
 */

public class Customer {

	/**
	 * id attribute, {@code < 0} invalid, can be set only once
	 */
	private long id = -1;

	/**
	 * none-surname name parts, never null, mapped to ""
	 */
	private String firstName = "";

	/**
	 * surname, never null, mapped to ""
	 */
	private String lastName = "";

	/**
	 * contact information, multiple contacts are possible
	 */
	private final List<String> contacts = new ArrayList<String>();

	/**
	 * Public constructor with name argument.
	 * @param name single-String Customer name, e.g. "Eric Meyer"
	 */
	public Customer( String name ) {
		if (name != null) {
			setName(name);
		}
	}
	
	/**
	 * Default constructor
	 */
	public Customer() {
	}

	/**
	 * Id getter.
	 * @return customer id, may be invalid {@code < 0} if unassigned
	 */
	public long getId() {
		return id;
	}

	/**
	 * Id setter. Id can only be set once, id is immutable after assignment.
	 * @param id assignment if id is valid {@code >= 0} and id attribute is still unassigned {@code < 0}
	 * @return chainable self-reference
	 */
	public Customer setId( long id ) {
		if (this.id == -1)
			this.id = id;
		return this;
	}

	/**
	 * Getter that returns single-String name from lastName and firstName attributes in format: {@code "lastName, firstName"} or {@code "lastName"} if {@code firstName} is empty.
	 * @return single-String name
	 */
	public String getName() {
		return lastName + ", " + firstName;
	}

	/**
	 * firstName getter.
	 * @return value of firstName attribute, never null, mapped to ""
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * lastName getter.
	 * @return value of lastName attribute, never null, mapped to ""
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Setter that splits single-String name (e.g. "Eric Meyer") into first- and lastName parts and assigns parts to corresponding attributes.
	 * @param name single-String name to split into first- and lastName parts
	 * @return chainable self-reference
	 */
	public Customer setName( String name ) {
		if (name != null) {
			splitName(name);
		}
		return this;
	}

	/**
	 * firstName, lastName setter. Method maintains that both attributes are never null; null-arguments are ignored and don't change attributes.
	 * @param first assigned to firstName attribute
	 * @param last assigned to lastName attribute
	 * @return chainable self-reference
	 */
	public Customer setName( String first, String last ) {
		if (first != null && last != null) {
			firstName = first;
			lastName = last;
		}
		if (first != null && last == null) {
			firstName = first;
		}
		if (first == null && last != null) {
			lastName = last;
		}
		return this;
	}

	/**
	 * Return number of contacts.
	 * @return number of contactsContacts getter (as {@code String[]}).
	 */
	public int contactsCount() {
		return contacts.size();
	}

	/**
	 * Contacts getter (as {@code String[]}).
	 * @return contacts (as {@code String[]})
	 */
	public String[] getContacts() {
		String[] temp = new String[contacts.size()];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = contacts.get(i);
		}
		return temp;
	}

	/**
	 * Add new contact for Customer. Duplicate entries are ignored.
	 * @param contact added when not already present
	 * @return chainable self-reference
	 */
	public Customer addContact( String contact ) {
		if (contact != null && !contacts.contains(contact)) {
			if (!contact.equals("")) {
				contacts.add(contact);
			}
		}
		return this;
	}

	/**
	 * Delete i-th contact with constraint: {@code i >= 0} and {@code i < contacts.size()}, otherwise method has no effect.
	 * @param i index in contacts
	 */
	public void deleteContact( int i ) {
		if (i >= 0 && i < contacts.size()) {
			contacts.remove(i);
		}
	}

	/**
	 * Delete all contacts.
	 */
	public void deleteAllContacts() {
		contacts.clear();
	}

	/**
	 * Split single-String name into first- and last name.
	 * @param name single-String name split into first- and last name
	 */
	private void splitName( String name ) {
		String f = "", l = "";
		if (name != null) {
			String[] temp = name.split("[\s*]");
			for (int i = 0; i < temp.length; i++) {
				if (temp.length == 1) {
					f = temp[i];
				} else {
					if (i + 1 == temp.length) {
						l = temp[i];
					} else {
						if (i == 0) {
							f = temp[i];
						} else {
							f = f + " " + temp[i];
						}
					}
				}
			}
			if (f.contains(",")) {
				String swapHelper = f.split(",")[0];
				f = l;
				l = swapHelper;
			}
			if (f.contains(".")) {
				String swapHelper = f.split(".")[0];
				f = l;
				l = swapHelper;
			}
			setName(f, l);
		}
	}

}