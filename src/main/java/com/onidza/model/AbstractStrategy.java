package com.onidza.model;

import com.onidza.vo.Salary;
import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractStrategy implements Strategy {
    private static final String RUB_SYMBOL = "₽";

    public List<Vacancy> strategyRequest(String searchString, SortType sortType) {
        List<Vacancy> notSortedVacancies = getVacancyList(searchString);
        return switch (sortType) {
            case NONE -> notSortedVacancies;
            case SALARY -> getSortedBySalary(notSortedVacancies);
        };
    }

    abstract List<Vacancy> getVacancyList(String searchString);

    public List<Vacancy> getSortedBySalary(List<Vacancy> vacancies) {
        return vacancies.stream()
                .filter((vacancy -> !(vacancy.getSalary().getLabel() == null)))
                .sorted(Comparator.comparingInt(vacancy -> {
                    Salary s = vacancy.getSalary();
                    if (s.getLabel().isEmpty()) {
                        return Integer.MAX_VALUE;
                    }
                    return s.getValue();
                }))
                .collect(Collectors.toList());
    }

    protected Vacancy fillVacancy(String url, String title, String city, String companyName,
                                String salaryStr, String experience, String format) {
        Vacancy vacancy = new Vacancy();
        vacancy.setSiteName("hh.ru");
        vacancy.setCity(city);
        vacancy.setTitle(title);
        vacancy.setUrl(url);
        vacancy.setSalary(parseSalary(salaryStr)
                .orElseGet(() -> new Salary("", 0, "", false)));
        vacancy.setCompanyName(companyName);
        vacancy.setExperience(experience);
        vacancy.setFormat(format);
        return vacancy;
    }

    abstract Document getDocument(String searchString, int page) throws IOException;

    public Optional<Salary> parseSalary(String salaryStr) {
        salaryStr = salaryStr.replaceAll("[\\s\u00A0\u202F\u2007,]", "");

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(salaryStr);

        List<Integer> numbers = new ArrayList<>();
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }
        if (numbers.isEmpty()) return Optional.empty();

        Salary salary;
        if (salaryStr.toLowerCase().contains("от") && numbers.size() == 1) {
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new Salary("от", numbers.get(0), RUB_SYMBOL, false);
        } else if (salaryStr.toLowerCase().contains("до") && numbers.size() == 1) {
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new Salary("до", numbers.get(0), RUB_SYMBOL, false);
        } else if (numbers.size() == 2) {
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new Salary("от " + numbers.get(0) + " до " + numbers.get(1), numbers.get(0), RUB_SYMBOL, true);
        } else if (numbers.size() == 1){
            if (isRuble(salaryStr)) return Optional.empty();
            salary = new Salary(numbers.get(0), RUB_SYMBOL);
        } else {
            return Optional.empty();
        }
        return Optional.of(salary);
    }

    protected boolean isRuble(String string) {
        return string == null || !string.contains(RUB_SYMBOL);
    }
}
