package com.spendwise.domain;

public class MonthlyBudgetEntry implements Entity {
    private Integer id;
    private int year;
    private int month;
    private String categoryName;
    private Float moneySpent;
    private Float monthlyBudget;


    public MonthlyBudgetEntry(Integer id, int year, int month, String categoryName, Float moneySpent, Float monthlyBudget) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.categoryName = categoryName;
        this.moneySpent = moneySpent;
        this.monthlyBudget = monthlyBudget;
    }

    public MonthlyBudgetEntry(int year, int month, String categoryName, Float moneySpent, Float monthlyBudget) {
        this.year = year;
        this.month = month;
        this.categoryName = categoryName;
        this.moneySpent = moneySpent;
        this.monthlyBudget = monthlyBudget;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {}

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Float getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(Float moneySpent) {
        this.moneySpent = moneySpent;
    }

    public Float getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(Float monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
