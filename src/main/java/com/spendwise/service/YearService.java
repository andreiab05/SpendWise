package com.spendwise.service;

import com.spendwise.domain.Year;
import com.spendwise.repository.InterfaceRepository;
import java.util.List;

public class YearService {
    private final InterfaceRepository<Year> repository;

    public YearService(InterfaceRepository<Year> repository) {
        this.repository = repository;
    }

    public void create(int yearValue, boolean readOnly) {
        Year year = new Year(yearValue, readOnly);
        repository.create(year);
    }

    public Year read(int id){
        return repository.read(id);
    }

    public void update(int id, int yearValue, boolean readOnly) {
        if(repository.read(id) == null){
            throw new IllegalArgumentException("Anul nu exista.");
        }
        Year year = new Year(id, yearValue, readOnly);
        repository.update(year);
    }

    public void delete(int id){
        repository.delete(id);
    }

    public List<Year> getAll() {
        return repository.getAll();
    }
}
