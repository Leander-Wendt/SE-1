package system;

import java.util.Optional;


/**
 * Public interface of data repositories that store objects (entities)
 * of data model classes.
 *	@author Leander Wendt
 */

public interface Repository<T> {
    Optional<T> findById( long id );    // find object of id
    Optional<T> findById( String id );    // find object of id
    Iterable<T> findAll();      // return all objects stored in repository
    long count();        // return count of objects in repository
    T save( T entity );  // save entity to repository
}