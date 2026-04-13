package com.spendwise.repository;

import com.spendwise.domain.Entity;

import java.util.*;

public class InMemoryRepository<T extends Entity> implements InterfaceRepository<T> {
    private final Map<Integer, T> data = new HashMap<>();

    @Override
    public void create(T entity){
        if (read(entity.getId()) != null) {
            throw new RuntimeException("Entity already exists");
        }
        data.put(entity.getId(), entity);
    }

    @Override
    public T read(int id){
        return data.get(id);
    }

    @Override
    public void update(T entity){
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getId() == entity.getId()) {
                data.put(entity.getId(), entity);
                return;
            }
        }
        throw new RuntimeException("Entity does not exist");
    }

    @Override
    public void delete(int id){
        data.remove(id);
    }

    public List<T> getAll() {return new ArrayList<>(data.values());}

    public Iterator<T> iterator() {
        return new ArrayList<>(this.data.values()).iterator();
    }
}

