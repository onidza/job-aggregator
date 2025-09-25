package com.onidza.model;

import com.onidza.vo.Salary;
import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HabrCareerStrategy implements Strategy {
    private static final String RUB_SYMBOL = "₽";
    private static final String URL_FORMAT  = "https://career.habr.com/vacancies?page=%d&q=%s";


    @Override
    public List<Vacancy> strategyRequest(String searchString, SortType sortType) {
        List<Vacancy> notSortedVacancies = getVacancyList(searchString);
        return switch (sortType) {
            case NONE -> notSortedVacancies;
            case SALARY -> getSortedBySalary(notSortedVacancies);
        };
    }

    private List<Vacancy> getVacancyList(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 0;
        while (true) {
            Document document = null;
            try {
                document = getDocument(searchString, page);
            } catch (IOException e) {
                System.out.println("Ошибка загрузки страницы " + page + ": " + e.getMessage());
            }

            if (document == null) {
                System.out.println("На странице " + page + " вакансий не найдено, заканчиваем цикл.");
                break;
            }

            Elements vacanciesHtmlList = document.select("div.vacancy-card");
            if (vacanciesHtmlList.isEmpty()) break;

            for (Element element : vacanciesHtmlList) {
                String url = parseUrl(element);
                String title = parseTitle(element);
                String address = parseAddress(element);
                String name = parseName(element);
                String salary = parseSalary(element);
                String experience = parseExperience(element);
                String format = parseFormat(element);
                vacancies.add(fillVacancy(url, title, address, name, salary, experience, format));
            }
            page++;
        }
        return vacancies;
    }

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

    private Vacancy fillVacancy(String url, String title, String city, String companyName,
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
    private Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, page, searchString))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                .referrer("https://career.habr.com/")
                .timeout(10_000)
                .get();
    }

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

    private boolean isRuble(String string) {
        return string == null || !string.contains(RUB_SYMBOL);
    }

    private String parseUrl(Element element) {
        Elements links = element.select("a.vacancy-card__title-link");
        String url = "https://career.habr.com";
        if (!links.isEmpty()) {
            url += links.attr("href");
            return url;
        }
        return "";
    }

    private String parseTitle(Element element) {
        Elements links = element.select("a.vacancy-card__title-link");
        String title;
        if (!links.isEmpty()) {
            title = links.get(0).text();
            return title;
        }
        return "";
    }

    private String parseAddress(Element element) {
        String address;
        Elements location = element.select("div.vacancy-card__meta a");
        if (!location.isEmpty()) {
            address = location.get(0).text();
            return address;
        }
        return "";
    }

    private String parseName(Element element) {
        String name;
        Elements companyName = element.select("div.vacancy-card__company-title > a");
        if (!companyName.isEmpty()) {
            name = companyName.get(0).text();
            return name;
        }
        return "";
    }

    private String parseSalary(Element element) {
        Element salaryBlock = element.selectFirst("div.vacancy-card__salary .basic-salary");
        if (!(salaryBlock == null)) {
            return salaryBlock.text();
        }
        return "";
    }

    private String parseExperience(Element element) {
        String experience;
        Elements experienceLinks = element.select("div.vacancy-card__skills a[href*='qid=']");
        if (!experienceLinks.isEmpty()) {
            experience = Objects.requireNonNull(experienceLinks.first()).text();
            return experience;
        }
        return "";
    }

    private String parseFormat(Element element) {
        String format;
        Elements formatBlocks = element.select("div.vacancy-card__meta span.preserve-line:last-of-type");
        if (!formatBlocks.isEmpty()) {
            format = formatBlocks.get(0).text();
            return format;
        }
        return "";
    }

}

