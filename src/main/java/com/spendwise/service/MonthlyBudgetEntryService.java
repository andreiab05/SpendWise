package com.spendwise.service;

import com.spendwise.domain.MonthlyBudgetEntry;
import com.spendwise.exception.ValidationException;
import com.spendwise.repository.InterfaceRepository;
import java.time.Year;
import java.util.List;

public class MonthlyBudgetEntryService {
    private final InterfaceRepository<MonthlyBudgetEntry> repository;

    public MonthlyBudgetEntryService(InterfaceRepository<MonthlyBudgetEntry> repository) {
        this.repository = repository;
    }

    private void validateEditableYear(int yearValue) {
        int currentYear = Year.now().getValue();

        validateYear(yearValue);

        if (yearValue < currentYear) {
            throw new ValidationException("Past years are read-only.");
        }
    }

    private void validateYear(int year) {
        if (year <= 0) {
            throw new ValidationException("Year must be positive.");
        }
    }

    private void validateMonth(int month) {
        if (month < 1 || month > 12) {
            throw new ValidationException("Month must be between 1 and 12.");
        }
    }

    private void validateCategoryName(String categoryName) {
        if (categoryName == null || categoryName.trim().isBlank()) {
            throw new ValidationException("Category name cannot be empty.");
        }
    }

    private void validateAmount(Float amount, String fieldName){
        if (amount == null){
            throw new ValidationException(fieldName + " is required.");
        }
        if (amount < 0){
            throw new ValidationException(fieldName + " cannot be negative.");
        }
    }

    public void create(int yearValue, int monthValue, String categoryName, Float moneySpent, Float monthlyBudget) {
        validateEditableYear(yearValue);
        validateMonth(monthValue);
        validateCategoryName(categoryName);
        validateAmount(moneySpent, "Money spent");
        validateAmount(monthlyBudget, "Monthly budget");

        MonthlyBudgetEntry entry = new MonthlyBudgetEntry(yearValue,
                monthValue,
                categoryName.trim(),
                moneySpent,
                monthlyBudget);
        repository.create(entry);
    }

    public MonthlyBudgetEntry read(int id){
        return repository.read(id);
    }

    public void update(int id, String categoryName, float moneySpent, float monthlyBudget) {
        MonthlyBudgetEntry entry = repository.read(id);

        if (entry == null) {
            throw new ValidationException("Entry not found.");
        }

        validateEditableYear(entry.getYear());
        validateCategoryName(categoryName);
        validateAmount(moneySpent, "Money spent");
        validateAmount(monthlyBudget, "Monthly budget");

        entry.setCategoryName(categoryName.trim());
        entry.setMoneySpent(moneySpent);
        entry.setMonthlyBudget(monthlyBudget);

        repository.update(entry);
    }

    public void delete(int id){
        MonthlyBudgetEntry entry = repository.read(id);

        if (entry == null) {
            throw new ValidationException("Entry does not exist.");
        }

        validateEditableYear(entry.getYear());

        repository.delete(id);
    }

    public List<MonthlyBudgetEntry> getAll() {
        return repository.getAll();
    }

    public List<MonthlyBudgetEntry> getEntriesForMonth(int yearValue, int monthValue){
        validateYear(yearValue);
        validateMonth(monthValue);

        return repository.getAll()
                .stream()
                .filter(entry -> entry.getYear() == yearValue && entry.getMonth() == monthValue)
                .toList();
    }

    public void addCategoryToAllMonths(int yearValue, String categoryName, float monthlyBudget) {
        validateEditableYear(yearValue);
        validateCategoryName(categoryName);
        validateAmount(monthlyBudget, "Monthly budget");

        String trimmedCategoryName = categoryName.trim();
        List<MonthlyBudgetEntry> allEntries = repository.getAll();

        for (int month = 1; month <= 12; month++) {
            int currentMonth = month;

            boolean exists = allEntries.stream()
                    .anyMatch(entry ->
                            entry.getYear() == yearValue &&
                            entry.getMonth() == currentMonth &&
                            entry.getCategoryName().equalsIgnoreCase(trimmedCategoryName)
                    );

            if (!exists) {
                MonthlyBudgetEntry entry = new MonthlyBudgetEntry(
                        yearValue,
                        currentMonth,
                        trimmedCategoryName,
                        0f,
                        monthlyBudget
                );

                repository.create(entry);
            }
        }
    }
}
