/**
 * Module of a simple order processing application: <b>&rdquo;se1.bestellsystem&rdquo;</b>
 * <p>
 * The {@link datamodel} package exports the data model definitions.
 * </p>
 * 
 * @author Leander Wendt
 * @version 0.1.0
 */

module se1.bestellsystem {
	exports datamodel;
	exports system;
	
	requires transitive com.fasterxml.jackson.databind;
	requires transitive com.fasterxml.jackson.annotation;
	requires transitive com.fasterxml.jackson.core;
}
