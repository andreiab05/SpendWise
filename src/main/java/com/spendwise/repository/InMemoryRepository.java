package com.spendwise.repository;

import com.spendwise.domain.Entity;
import com.spendwise.exception.RepositoryException;

import java.util.*;

public class InMemoryRepository<T extends Entity> implements InterfaceRepository<T> {
    private final Map<Integer, T> data = new HashMap<>();

    @Override
    public void create(T entity){
        if (data.containsKey(entity.getId())) {
            throw new RepositoryException("Entity already exists");
        }
        data.put(entity.getId(), entity);
    }

    @Override
    public T read(int id){
        return data.get(id);
    }

    @Override
    public void update(T entity){
        if(!data.containsKey(entity.getId())) {
            throw new RepositoryException("Entity does not exist");
        }

        data.put(entity.getId(), entity);
    }

    @Override
    public void delete(int id){
        if(!data.containsKey(id)) {
            throw new RepositoryException("Entity does not exist");
        }

        data.remove(id);
    }

    public List<T> getAll() {return new ArrayList<>(data.values());}

    public Iterator<T> iterator() {
        return new ArrayList<>(this.data.values()).iterator();
    }
}

