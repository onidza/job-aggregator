package com.onidza.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class Salary {
    private String label;
    private final int value;
    private final String typeCurrency;
    private boolean range = false;

    @Override
    public String toString() {
        if (label == null && value != 0) return value + typeCurrency;
        if (value == 0 || range) return label + " " + typeCurrency;
        return label + " " + value + typeCurrency;
    }
}
