package system.impl;

import datamodel.Order;
import datamodel.OrderItem;
import datamodel.TAX;
import system.Calculator;

class CalculatorImpl implements Calculator {

    CalculatorImpl(){
        // leerer Konstruktor
    }

    @Override
    public long calculateValue(Iterable<Order> orders) {
        long value = 0;
		for (Order o : orders){
			value += calculateValue(o);
		}
		return value;
    }

    @Override
    public long calculateValue(Order order) {
        long value = 0;
		OrderItem[] s = order.getItemsAsArray();
        for (OrderItem i : s) {
            value += calculateValue(i);
        }
		return value;
    }

    public long calculateValue (OrderItem i){
		return i.getArticle().getUnitPrice() * i.getUnitsOrdered();
	}

    @Override
    public long calculateIncludedVAT(Iterable<Order> orders) {
        long vat = 0;
		for (Order o : orders){
            vat += calculateIncludedVAT(o);
        }
		return vat;
    }

    @Override
    public long calculateIncludedVAT(Order order) {
        long sum = 0;
		for (OrderItem i : order.getItemsAsArray()){
			sum += calculateIncludedVAT(i);
		}
		return sum;
    }

    public long calculateIncludedVAT (OrderItem i) {
		return calculateIncludedVAT(i.getUnitsOrdered() * i.getArticle().getUnitPrice(), i.getArticle().getTax());
	}

    @Override
    public long calculateIncludedVAT(long price, TAX taxRate) {
        return  Math.round(price - (price / (1 + (taxRate.rate() / 100))));
    }
    
}
