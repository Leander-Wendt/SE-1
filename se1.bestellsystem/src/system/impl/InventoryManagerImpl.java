package system.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import datamodel.Article;
import datamodel.Currency;
import datamodel.Order;
import datamodel.OrderItem;
import system.Formatter;
import system.InventoryManager;
import system.Repository;
import system.Formatter.TableFormatter;

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

    /**
    * Print inventory as table.
    * 
    * @return printed inventory (as table).
    */
    @Override
    public StringBuffer printInventory() {
	    return printInventory(
		    StreamSupport.stream( repo.findAll().spliterator(), false )
	    );
    }

    private StringBuffer printInventory( Stream<Article> articleStream ) {
	    //
	    Formatter formatter = new FormatterImpl();
	    TableFormatter tfmt = new TableFormatterImpl( formatter, new Object[][] {
		    // five column table with column specs: width and alignment ('[' left, ']' right)
		    { 12, '[' }, { 32, '[' }, { 12, ']' }, { 10, ']' }, { 14, ']' }
	    })
		    .liner( "+-+-+-+-+-+" )		// print table header
		    .hdr( "||", "Inv.-Id", "Article / Unit", "Unit", "Units", "Value" )
		    .hdr( "||", "", "", "Price", "in-Stock", "(in €)" )
		    .liner( "+-+-+-+-+-+" );
	    //
	    long totalValue = articleStream
		    .map( a -> {
			    long unitsInStock = this.inventory.get( a.getId() ).intValue();
			    long value = a.getUnitPrice() * unitsInStock;
			    tfmt.hdr( "||",
			    	a.getId(),
				    a.getDescription(),
				    formatter.fmtPrice( a.getUnitPrice(), a.getCurrency()).toString(),
			    	Long.toString( unitsInStock ),
				    formatter.fmtPrice( value, a.getCurrency() ).toString()
			    );
			    return value;
		    })
		    .reduce( 0L, (a, b) -> a + b );
	    //
	    String inventoryValue = formatter.fmtPrice( totalValue, Currency.EUR ).toString();
	    tfmt
		    .liner( "+-+-+-+-+-+" )
		    .hdr( "", "", "Invent", "ory Value:", inventoryValue );
	    //
	    return tfmt.getFormatter().getBuffer();
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

	 /**
   * Print inventory as table with sorting and limiting criteria.
   * @param sortedBy sorting criteria 1: byPrice; 2: byValue; 3: byUnits;
   *          4: byDescription; 5: bySKU; else: unsorted
   * @param decending true if in descending order
   * @param limit upper boundary of articles printed after sorting
   * @return printed inventory (as table).
   */
    @Override
	public StringBuffer printInventory(int sortedBy, boolean descending, Integer... limit) {		
		List<Article> articles = new ArrayList<>();
		for (Article a : repo.findAll()){
			articles.add(a);
		}
		int min = articles.size();
		for (Integer x : limit){
			min = x < min ? x : min;
		}

		switch (sortedBy){
			case 1:
				articles.sort(Comparator.comparing(a -> a.getUnitPrice()));
				break;
			case 2:
				articles.sort(Comparator.comparingLong(a -> a.getUnitPrice() * getUnitsInStock(a.getId())));
				break;
			case 3:
				articles.sort(Comparator.comparing(a -> getUnitsInStock(a.getId())));
				break;
			case 4:
				articles.sort(Comparator.comparing(a -> a.getDescription()));
				break;
			case 5:
				articles.sort(Comparator.comparing(a -> a.getId()));
				break;
			default:
				break;
		}

		if(descending) {
			Collections.reverse(articles);
		}

		for (int i = articles.size() - 1; i > min - 1; i--){
			articles.remove(i);
		}
		return printInventory(articles.stream());
	}

}
