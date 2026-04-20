package com.spendwise.domain;

public class Year implements Entity {
    private Integer id;
    private int yearValue;
    private boolean readOnly;

    public Year(Integer id, int year, boolean readOnly) {
        this.id = id;
        this.yearValue = year;
        this.readOnly = readOnly;
    }

    public Year(int year, boolean readOnly) {
        this.yearValue = year;
        this.readOnly = readOnly;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(int anInt) {
    }

    public int getYear() {
        return yearValue;
    }

    public void setYear(int year) {
        this.yearValue = year;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public String toString() {
        return String.valueOf(yearValue);
    }
}
