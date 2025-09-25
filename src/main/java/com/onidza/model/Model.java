package com.onidza.model;

import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private final Provider[] providers;

    public Model(Provider...providers) {
        if (providers == null || providers.length == 0) throw new IllegalArgumentException();
        this.providers = providers;
    }

    public List<Vacancy> getListOfVacancies(String vacancyName, SortType sortType) {
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers) {
            vacancies.addAll(provider.providerRequest(vacancyName, sortType));
        }
        return vacancies;
    }
}
