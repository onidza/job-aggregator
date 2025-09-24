package com.onidza.model;

import com.onidza.vo.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> getWithoutSortedVacancies(String searchString);

    List<Vacancy> getSortedBySalaryVacancies(String searchString);
}
