package com.onidza.model;

import com.onidza.view.View;
import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final View view;
    private final Provider[] providers;

    public Model(View view, Provider...providers) {
        if (view == null || providers == null || providers.length == 0) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }

    public void getListOfVacancies(String vacancyName, SortType sortType) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.providerRequest(vacancyName, sortType));
        }
        view.update(vacancies);
    }
}
