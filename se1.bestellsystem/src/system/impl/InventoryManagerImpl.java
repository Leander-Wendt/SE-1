package system.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import datamodel.Article;
import datamodel.Order;
import datamodel.OrderItem;
import system.InventoryManager;
import system.Repository;

public class InventoryManagerImpl implements system.InventoryManager {
    private static InventoryManagerImpl instance = null;
	private static Repository<Article> repo = null;
    private final Map<String, Integer> inventory;


    private InventoryManagerImpl(Repository<Article> repo){
        this.repo = repo;
        inventory = new HashMap<>();
    }

    public static InventoryManager getInstance(Repository<Article> repo) {
		if( instance == null ) {
			instance = new InventoryManagerImpl( repo );
		}
		return instance;
	}

    
    @Override
    public int getUnitsInStock(String id) {
        if (id == null || id.equals("")) {
            throw new IllegalArgumentException("id cannot be empty");
        }
		return inventory.get(id);
    }

    @Override
    public void update(String id, int updatedUnitsInStock) {
        if (id == null || id.equals("")) {
            throw new IllegalArgumentException("id cannot be empty");
        }
		if (updatedUnitsInStock < 0) {
            throw new IllegalArgumentException("amount of updatedUnitsInStock must be positive or 0");
        }
		inventory.put(id, updatedUnitsInStock);        
    }

    @Override
    public boolean isFillable(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("order cannot be null");
        }

		for (OrderItem item : order.getItems()){
			if (item.getUnitsOrdered() > getUnitsInStock(item.getArticle().getId())) {
                return false;
            }
		}
		return true;
    }

    @Override
    public boolean fill(Order order) {
        if (isFillable(order)) {
			for(OrderItem item : order.getItems()){
                String id = item.getArticle().getId();
				update(id, getUnitsInStock(id) - item.getUnitsOrdered());
			}
			return true;
		}
		return false;
    }

    @Override
    public StringBuffer printInventory() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringBuffer printInventory(int sortedBy, boolean decending, Integer... limit) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Optional<Article> findById(long id) {
        return repo.findById(id);
    }


    @Override
    public Optional<Article> findById(String id) {
        return repo.findById(id);
    }


    @Override
    public Iterable<Article> findAll() {
        return repo.findAll();
    }


    @Override
    public long count() {
        return repo.count();
    }


    @Override
    public Article save(Article entity) {
        if (entity == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }
		String id = entity.getId();
		if (id == null) {
            throw new IllegalArgumentException("entity cannot be null");
        }

		repo.save(entity);
		if (!inventory.containsKey(id)) {
			this.inventory.put(id, Integer.valueOf(0));
		}
		return entity;
    }

}
