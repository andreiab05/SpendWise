package com.spendwise.domain;

public class Year implements Entity {
    private final int id;
    private int year;

    public Year(int id, int year) {
        this.id = id;
        this.year = year;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
