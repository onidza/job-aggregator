package com.onidza;

import com.onidza.model.Model;

public class Controller {
    private Model model;

    public Controller(Model model) {
        if (model == null) throw new IllegalArgumentException("Не найдены провайдеры!");
        this.model = model;
    }

    public void withoutSorting(String cityName) {
        model.withoutSorting(cityName);
    }

    public void sortBySalary(String cityName) {
        model.sortBySalary(cityName);
    }
}
