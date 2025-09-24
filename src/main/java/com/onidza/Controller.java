package com.onidza;

import com.onidza.model.Model;

public class Controller {
    private Model model;

    public Controller(Model model) {
        if (model == null) throw new IllegalArgumentException("Model не может быть null при создании Controller");
        this.model = model;
    }

    public void showVacancies(String vacancyName, SortType sortType) {
        switch (sortType) {
            case NONE -> withoutSorting(vacancyName);
            case SALARY -> sortBySalary(vacancyName);
        }
    }

    public void withoutSorting(String cityName) {
        model.withoutSorting(cityName);
    }

    public void sortBySalary(String cityName) {
        model.sortBySalary(cityName);
    }

    public enum SortType {
        NONE, SALARY
    }
}
