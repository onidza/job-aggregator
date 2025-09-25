package com.onidza.model;

import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Provider {

    @Setter
    private Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<Vacancy> providerRequest(String vacancyName, SortType sortType) {
        List<Vacancy> vacancies = strategy.strategyRequest(vacancyName, sortType);
        return Objects.requireNonNullElse(vacancies, Collections.emptyList());
    }
}
