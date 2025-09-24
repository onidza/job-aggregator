package com.onidza.model;

import com.onidza.view.View;
import com.onidza.vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private View view;
    private Provider[] providers;

    public Model(View view, Provider...providers) {
        if (view == null || providers == null || providers.length == 0) {
            throw new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }

    public void withoutSorting(String city) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.getWithoutSortedVacancies(city));
        }
        view.update(vacancies);
    }

    public void sortBySalary(String city) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.getSortedBySalaryVacancies(city));
        }
        view.update(vacancies);
    }
}
