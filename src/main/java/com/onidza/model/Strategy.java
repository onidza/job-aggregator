package com.onidza.model;

import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;

import java.util.List;

public interface Strategy {
    List<Vacancy> strategyRequest(String searchString, SortType sortType);
}
