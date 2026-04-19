package com.spendwise.domain;

public class MonthlyCategoryBudget implements Entity {
    private Integer id;
    private Category category;
    private Month month;
    private int moneySpent;
    private int monthlyBudget;

    public MonthlyCategoryBudget(Integer id, Category category, Month month, int moneySpent, int monthlyBudget) {
        this.id = id;
        this.category = category;
        this.month = month;
        this.moneySpent = moneySpent;
        this.monthlyBudget = monthlyBudget;
    }

    public MonthlyCategoryBudget(Category category, Month month, int moneySpent, int monthlyBudget) {
        this.category = category;
        this.month = month;
        this.moneySpent = moneySpent;
        this.monthlyBudget = monthlyBudget;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public int getMoneySpent() {
        return moneySpent;
    }

    public void setMoneySpent(int moneySpent) {
        this.moneySpent = moneySpent;
    }

    public int getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(int monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }
}
