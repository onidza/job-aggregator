package com.onidza;

import com.onidza.model.Model;
import com.onidza.view.View;
import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Controller {
    private final Model model;
    private final View view;

    public void showVacancies(String vacancyName, SortType sortType) {
        List<Vacancy> vacancies = model.getListOfVacancies(vacancyName, sortType);
        view.update(vacancies);
    }
}
