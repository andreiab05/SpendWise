package com.spendwise.service;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.repository.InterfaceRepository;
import java.time.Year;
import java.util.List;

public class MonthlyBudgetEntryService {
    private final InterfaceRepository<MonthlyBudgetEntry> repository;

    public MonthlyBudgetEntryService(InterfaceRepository<MonthlyBudgetEntry> repository) {
        this.repository = repository;
    }

    private void validateEditableYear(int year) {
        int currentYear = Year.now().getValue();

        if (year < currentYear) {
            throw new IllegalArgumentException("Past years are read-only.");
        }
    }

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }
    }

    public void create(int yearValue, int monthValue, String categoryName, Float moneySpent, Float monthlyBudget) {
        validateEditableYear(yearValue);
        validateMonth(monthValue);

        MonthlyBudgetEntry entry = new MonthlyBudgetEntry(yearValue, monthValue, categoryName, moneySpent, monthlyBudget);
        repository.create(entry);
    }

    public MonthlyBudgetEntry read(int id){
        return repository.read(id);
    }

    public void update(int id, int yearValue, int monthValue, String categoryName, Float moneySpent, Float monthlyBudget) {
        validateEditableYear(yearValue);
        validateMonth(monthValue);

        if(repository.read(id) == null){
            throw new IllegalArgumentException("Entry does not exist.");
        }
        MonthlyBudgetEntry entry = new MonthlyBudgetEntry(
                id,
                yearValue,
                monthValue,
                categoryName,
                moneySpent,
                monthlyBudget
        );

        repository.update(entry);
    }

    public void update(int id, String categoryName, float moneySpent, float monthlyBudget) {
        MonthlyBudgetEntry entry = repository.read(id);

        if (entry == null) {
            throw new IllegalArgumentException("Entry not found.");
        }

        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        if (moneySpent < 0) {
            throw new IllegalArgumentException("Money spent cannot be negative.");
        }

        if (monthlyBudget < 0) {
            throw new IllegalArgumentException("Monthly budget cannot be negative.");
        }

        entry.setCategoryName(categoryName);
        entry.setMoneySpent(moneySpent);
        entry.setMonthlyBudget(monthlyBudget);

        repository.update(entry);
    }

    public void delete(int id){
        MonthlyBudgetEntry entry = repository.read(id);

        if (entry == null) {
            throw new IllegalArgumentException("Entry does not exist.");
        }

        validateEditableYear(entry.getYear());

        repository.delete(id);
    }

    public List<MonthlyBudgetEntry> getAll() {
        return repository.getAll();
    }

    public List<MonthlyBudgetEntry> getEntriesForMonth(int yearValue, int monthValue){
        validateMonth(monthValue);

        return repository.getAll()
                .stream()
                .filter(entry -> entry.getYear() == yearValue && entry.getMonth() == monthValue)
                .toList();
    }

    public void addCategoryToAllMonths(int yearValue, String categoryName, float monthlyBudget) {
        validateEditableYear(yearValue);

        if (categoryName == null || categoryName.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty.");
        }

        if (monthlyBudget < 0) {
            throw new IllegalArgumentException("Monthly budget cannot be negative.");
        }

        for (int month = 1; month <= 12; month++) {
            MonthlyBudgetEntry entry = new MonthlyBudgetEntry(
                    yearValue,
                    month,
                    categoryName,
                    0f,
                    monthlyBudget
            );

            repository.create(entry);
        }
    }
}
