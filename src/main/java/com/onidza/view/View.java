package com.onidza.view;

import com.onidza.Controller;
import com.onidza.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies);
    void setController(Controller controller);
    void withoutSorting(String city);
    void sortBySalary(String city);
}
