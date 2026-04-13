package com.spendwise.domain;

public class Year implements Entity {
    private final int id;
    private int year;
    private boolean readOnly;

    public Year(int id, int year, boolean readOnly) {
        this.id = id;
        this.year = year;
        this.readOnly = readOnly;
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

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
}
