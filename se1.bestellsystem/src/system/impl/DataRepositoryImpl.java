package system.impl;

import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import system.Repository;

public class DataRepositoryImpl {
    Repository<Customer> customerRepository = new RepositoryImpl<>();
    Repository<Article> articleRepository = new RepositoryImpl<>();
    Repository<Order> orderRepository = new RepositoryImpl<>();

    Repository<Customer> getCustomerRepository(){ 
        return customerRepository;
    } 

    Repository<Article> getArticleRepository(){ 
        return articleRepository;
    } 
    
    Repository<Order> getOrderRepository(){ 
        return orderRepository;
    } 
}
