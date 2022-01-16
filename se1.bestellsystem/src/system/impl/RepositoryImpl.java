package system.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import datamodel.Article;
import datamodel.Customer;
import datamodel.Order;
import system.Repository;

public class RepositoryImpl<T> implements Repository<T> {
    Map<Object, T> map = new HashMap<>();

    @Override
    public Optional<T> findById(long id) {
        return Optional.of(map.get(id));
    }

    @Override
    public Optional<T> findById(String id) {
        return Optional.of(map.get(id));
    }

    @Override
    public Iterable<T> findAll() {
        return map.values();
    }

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public T save(T entity) {
        if (entity instanceof Article) {
            map.put(((Article) entity).getId(), entity);
        }
        if (entity instanceof Order) {
            map.put(((Order) entity).getId(), entity);
        }
        if (entity instanceof Customer) {
            map.put(((Customer) entity).getId(), entity);
        }
        return entity;
    }
    
}
