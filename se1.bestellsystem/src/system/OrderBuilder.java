package system;

import datamodel.Order;

public interface OrderBuilder {
    /**
       * Validate and save order to OrderRepository, if order is accepted.
       *
       * @param order order to accept.
       * @return true if order was validated and saved to OrderRepository.
       */
    public boolean accept( Order order );
    /**
       * Build orders in OrderRepository.
       *
       * @return chainable self reference.
       */
    public OrderBuilder build();
    }
