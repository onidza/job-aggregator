package com.onidza;

import com.onidza.model.Model;
import com.onidza.vo.SortType;

public class Controller {
    private final Model model;

    public Controller(Model model) {
        if (model == null) throw new IllegalArgumentException("Model не может быть null при создании Controller");
        this.model = model;
    }

    public void showVacancies(String vacancyName, SortType sortType) {
        model.getListOfVacancies(vacancyName, sortType);
    }
}
