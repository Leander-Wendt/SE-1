package system.impl;

import datamodel.Order;
import system.InventoryManager;

class InventoryManagerMOCK implements InventoryManager{

    public InventoryManagerMOCK() {
    }

    @Override
    public boolean isFillable(Order order) {
        return false;
    }
    
}
