package com.onidza.model;

import com.onidza.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HabrCareerStrategy extends AbstractStrategy {
    private static final String URL_FORMAT  = "https://career.habr.com/vacancies?page=%d&q=%s";

    protected List<Vacancy> getVacancyList(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 0;
        while (true) {
            Document document = null;
            try {
                document = getDocument(searchString, page);
            } catch (IOException e) {
                System.out.println("Ошибка загрузки страницы " + page + ": " + e.getMessage() + " в " + this.getClass().getSimpleName());
            }

            if (document == null) {
                System.out.println("На странице " + page + " вакансий не найдено, заканчиваем цикл в " + this.getClass().getSimpleName());
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

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, page, searchString))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                .referrer("https://career.habr.com/")
                .timeout(10_000)
                .get();
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

