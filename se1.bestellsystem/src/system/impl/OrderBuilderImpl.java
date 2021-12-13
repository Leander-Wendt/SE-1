package system.impl;

import datamodel.Order;
import system.OrderBuilder;

class OrderBuilderImpl implements OrderBuilder {
    // InventoryManager inventoryManager = RTE_Impl.get
    @Override
    public boolean accept(Order order) {
        boolean validOrder = inventoryManager.isFillable( order );
        if( validOrder ) {
            orderRepository.save( order );
        }
        return validOrder;    
    }

    @Override
    public OrderBuilder build() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
