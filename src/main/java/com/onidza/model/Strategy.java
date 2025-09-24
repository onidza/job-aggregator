package com.onidza.model;

import com.onidza.vo.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> getWithoutSortedVacancies(String searchString);
    List<Vacancy> getSortedBySalaryVacancies(String searchString);

//    List<Vacancy> getVacancies(String searchString, SortType sortType);
//
//    enum SortType {
//        NONE, SALARY
//    }
}
