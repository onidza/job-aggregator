package com.onidza.vo;

public class Salary {
    private String label;
    private final int value;
    private final String typeCurrency;
    private boolean range = false;


    public Salary(int value, String typeCurrency) {
        this.value = value;
        this.typeCurrency = typeCurrency;
    }

    public Salary() {
        this.label = "-";
        this.value = 0;
        this.typeCurrency = "";
    }

    public Salary(String label, int salary, String typeCurrency) {
        this.label = label;
        this.value = salary;
        this.typeCurrency = typeCurrency;
    }

    public String getLabel() {
        return label;
    }

    public int getValue() {
        return value;
    }

    public String getTypeCurrency() {
        return typeCurrency;
    }

    public boolean isRange() {
        return range;
    }

    public void setRange(boolean range) {
        this.range = range;
    }

    @Override
    public String toString() {
        if (value == 0) {
            return label + " " + typeCurrency;
        }

        if (range) {
            return label + " " + typeCurrency;
        }

        return label + " " + value + typeCurrency;
    }


}
