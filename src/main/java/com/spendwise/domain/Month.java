package com.spendwise.domain;

public class Month implements Entity {
    private Integer id;
    private int month;
    private Year year;

    public Month(Integer id, int month, Year year) {
        this.id = id;
        this.month = month;
        this.year = year;
    }

    public Month(int month, Year year) {
        this.month = month;
        this.year = year;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
