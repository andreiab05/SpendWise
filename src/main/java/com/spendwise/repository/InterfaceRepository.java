package com.spendwise.repository;

import com.spendwise.domain.Entity;
import java.util.List;

public interface InterfaceRepository<T extends Entity> {
        void create(T entity);
        T read(int id);
        void update(T entity);
        void delete(int id);
        List<T> getAll();
}
