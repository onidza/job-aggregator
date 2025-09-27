package com.onidza.model;

import com.onidza.vo.Vacancy;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HHStrategy extends AbstractStrategy {
    private static final String URL_FORMAT  = "https://hh.ru/search/vacancy?text=%s&page=%d";

    protected List<Vacancy> getVacancyList(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();
        int page = 0;
        while (true) {
            Document document = getDocument(searchString, page);
            if (document == null) {
                log.info("На странице {} вакансий не найдено, заканчиваем цикл в {}", page, this.getClass().getSimpleName());
                break;
            }

            Elements vacanciesHtmlList = document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
            if (vacanciesHtmlList.isEmpty()) {
                log.info("На странице {} не найдено элементов вакансий в {}.", page, this.getClass().getSimpleName());
                break;
            }

            for (Element element : vacanciesHtmlList) {
                String url = parseUrl(element);
                log.debug("Найдена вакансия: {}", url);

                String title = parseTitle(element);
                String address = parseAddress(element);
                String name = parseName(element);

                Elements salaryBlocks = element.select("div[class^=compensation-labels]");
                String salaryStr = parseCompensationBlocks(salaryBlocks, "span[class*=magritte-text_style-primary]");
                String experience = parseCompensationBlocks(salaryBlocks, "span[data-qa^=vacancy-serp__vacancy-work-experience]");
                String format = parseCompensationBlocks(salaryBlocks, "span[data-qa^=vacancy-label-work-schedule]");

                vacancies.add(fillVacancy(url, title, address, name, salaryStr, experience, format));
            }
            page++;
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) {
        try {
            return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                    .referrer("https://hh.ru/")
                    .timeout(10_000)
                    .get();
        } catch (IOException e) {
            log.error("Ошибка загрузки страницы {} для {}: {} ", page, this.getClass().getSimpleName(), e.getMessage());
        }
        return null;
    }

    private String parseUrl(Element element) {
        Elements links = element.select("a[data-qa=serp-item__title]");
        String url;
        if (!links.isEmpty()) {
            url = links.attr("href");
            return url;
        }
        return "";
    }

    private String parseTitle(Element element) {
        Elements links = element.select("a[data-qa=serp-item__title]");
        String title;
        if (!links.isEmpty()) {
            title = links.get(0).text();
            return title;
        }
        return "";
    }

    private String parseAddress(Element element) {
        String address;
        Elements location = element.select("span[data-qa=vacancy-serp__vacancy-address]");
        if (!location.isEmpty()) {
            address = location.get(0).text();
            return address;
        }
        return "";
    }

    private String parseName(Element element) {
        String name;
        Elements companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer");
        if (!companyName.isEmpty()) {
            name = companyName.get(0).text();
            return name;
        }
        return "";
    }

    private String parseCompensationBlocks(Elements salaryBlocks, String cssQuery) {
        if (salaryBlocks == null || salaryBlocks.isEmpty()) return "";
        Elements elements = salaryBlocks.get(0).select(cssQuery);
        return elements.isEmpty() ? "" : elements.get(0).text();
    }
}
