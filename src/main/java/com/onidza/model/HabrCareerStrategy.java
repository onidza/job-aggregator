package com.onidza.model;

import com.onidza.vo.Salary;
import com.onidza.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//public class HabrCareerStrategy implements Strategy {
//    private static final String URL_FORMAT = "https://career.habr.com/vacancies?page=%d&q=%s";
//
//    @Override
//    public List<Vacancy> getSortedBySalaryVacancies(String searchString) {
//        return strategyRequest(searchString).stream()
//                .filter((vacancy -> !(vacancy.getSalary().getLabel() == null)))
//                .sorted(Comparator.comparingInt(vacancy -> {
//                    Salary s = vacancy.getSalary();
//                    if ("-".equals(s.getLabel())) {
//                        return Integer.MAX_VALUE;
//                    }
//                    return s.getValue();
//                }))
//                .collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Vacancy> strategyRequest(String searchString) {
//        List<Vacancy> vacancies = new ArrayList<>();
//
//        int page = 0;
//        while (true) {
//            Document document = null;
//            try {
//                document = getDocument(searchString, page);
//            } catch (IOException ignored) {
//
//            }
//            if (document == null) break;
//
//            Elements vacanciesHtmlList = document.select("div.vacancy-card");
//            if (vacanciesHtmlList.isEmpty()) break;
//
//            for (Element element : vacanciesHtmlList) {
//                String url = "https://career.habr.com";
//                String title = "";
//                Elements links = element.select("a.vacancy-card__title-link");
//                if (!links.isEmpty()) {
//                    url += links.attr("href");
//                    title = links.get(0).text();
//                }
//
//                String address = "";
//                Elements location = element.select("div.vacancy-card__meta a");
//                if (!location.isEmpty()) {
//                    address = location.get(0).text();
//                }
//
//                String name = "";
//                Elements companyName = element.select("div.vacancy-card__company-title > a");
//                if (!companyName.isEmpty()) {
//                    name = companyName.get(0).text();
//                }
//
//                Element salaryBlock = element.selectFirst("div.vacancy-card__salary .basic-salary");
//                String salaryStr = salaryBlock != null ? salaryBlock.text() : null;
//
//                Elements experienceLinks = element.select("div.vacancy-card__skills a[href*='qid=']");
//                String experience = experienceLinks.isEmpty() ? "" : Objects.requireNonNull(experienceLinks.first()).text();
//
//                String format = "";
//                Elements formatBlocks = element.select("div.vacancy-card__meta span.preserve-line:last-of-type");
//                if (!formatBlocks.isEmpty()) {
//                    format = formatBlocks.get(0).text();
//                }
//
//                Salary salary = parseSalary(salaryStr);
//
//                Vacancy vacancy = new Vacancy();
//                vacancy.setSiteName("habr.com");
//                vacancy.setCity(address);
//                vacancy.setTitle(title);
//                vacancy.setUrl(url);
//                if(salary != null) vacancy.setSalary(salary);
//                vacancy.setCompanyName(name);
//                vacancy.setExperience(experience);
//                vacancy.setFormat(format);
//
//                vacancies.add(vacancy);
//            }
//            page++;
//        }
//        return vacancies;
//    }
//
//    public static Salary parseSalary(String salaryStr) {
//        return HHStrategy.parseSalary(salaryStr);
//    }
//
//    protected Document getDocument(String searchString, int page) throws IOException {
//        return Jsoup.connect(String.format(URL_FORMAT, page, searchString))
//                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
//                .referrer("https://career.habr.com/")
//                .get();
//    }
//}

