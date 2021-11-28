package system.impl;

import system.RTE;


/**
 * Only public class in this package to provide access to the singleton
 * instance of the Runtime Environment (RTE) that implements the public
 * RTE interface.
 * 
 * The RTE interface offers public interfaces of all system components
 * through their public interfaces. Implementations are hidden as local
 * (none-public) classes in this package.
 * 
 * InstanceAccessor implements the singleton pattern for the RTE_Impl()
 * instance.
 *	@author Leander Wendt
 */

public class InstanceAccessor {

	/**
	 * private static singleton instance (lazy instantiation).
	 */
	private static RTE rte_instance = null;


	/**
	 * Static access method to RTE singleton instance (singleton pattern).
	 * 
	 * @return reference to RTE singleton instance.
	 */

	public static RTE getInstance() {
		/*
		 * lazy instance creation (only when getInstance() is called)
		 */
		if( rte_instance == null ) {
			rte_instance = new RTE_Impl();
		}
		return rte_instance;
	}


	/*
	 * Private constructor (singleton pattern).
	 */

	private InstanceAccessor() { }

}
