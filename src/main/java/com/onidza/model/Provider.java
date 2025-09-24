package com.onidza.model;

import com.onidza.vo.Vacancy;

import java.util.Collections;
import java.util.List;

public class Provider {
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<Vacancy> getWithoutSortedVacancies(String searchString) {
        List<Vacancy> vacancies = strategy.getWithoutSortedVacancies(searchString);
        if (vacancies == null) {
            return Collections.emptyList();
        } else {
            return vacancies;
        }
    }

    public List<Vacancy> getSortedBySalaryVacancies(String searchString) {
        List<Vacancy> vacancies = strategy.getSortedBySalaryVacancies(searchString);
        if (vacancies == null) {
            return Collections.emptyList();
        } else {
            return vacancies;
        }
    }
}
