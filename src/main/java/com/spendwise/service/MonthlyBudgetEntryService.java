package com.spendwise.service;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.repository.InterfaceRepository;
import java.util.List;

public class MonthlyBudgetEntryService {
    private final InterfaceRepository<MonthlyBudgetEntry> repository;

    public MonthlyBudgetEntryService(InterfaceRepository<MonthlyBudgetEntry> repository) {
        this.repository = repository;
    }

    public void create(int yearValue, int monthValue, String categoryName, Float moneySpent, Float monthlyBudget) {
        MonthlyBudgetEntry entry = new MonthlyBudgetEntry(yearValue, monthValue, categoryName, moneySpent, monthlyBudget);
        repository.create(entry);
    }

    public MonthlyBudgetEntry read(int id){
        return repository.read(id);
    }

    public void update(int id, int yearValue, int monthValue, String categoryName, Float moneySpent, Float monthlyBudget) {
        if(repository.read(id) == null){
            throw new IllegalArgumentException("Entry does not exist.");
        }
        MonthlyBudgetEntry entry = new MonthlyBudgetEntry(yearValue, monthValue, categoryName, moneySpent, monthlyBudget);
        repository.update(entry);
    }

    public void delete(int id){
        repository.delete(id);
    }

    public List<MonthlyBudgetEntry> getAll() {
        return repository.getAll();
    }
}
