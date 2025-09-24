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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HHStrategy implements Strategy {
    private static final String URL_FORMAT  = "https://hh.ru/search/vacancy?text=%s&page=%d";

    @Override
    public List<Vacancy> getSortedBySalaryVacancies(String searchString) {
        return getWithoutSortedVacancies(searchString).stream()
                .filter((vacancy -> !(vacancy.getSalary().getLabel() == null)))
                .sorted(Comparator.comparingInt(vacancy -> {
                    Salary s = vacancy.getSalary();
                    if ("-".equals(s.getLabel())) {
                        return Integer.MAX_VALUE;
                    }
                    return s.getValue();
                }))
                .collect(Collectors.toList());
    }

    @Override
    public List<Vacancy> getWithoutSortedVacancies(String searchString) {
        List<Vacancy> vacancies = new ArrayList<>();

        int page = 0;
        while (true) {
            Document document = null;
            try {
                document = getDocument(searchString, page);
            } catch (IOException ignored) {

            }
            if (document == null) break;

            Elements vacanciesHtmlList = document.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
            if (vacanciesHtmlList.isEmpty()) break;

            for (Element element : vacanciesHtmlList) {
                String url = "";
                String title = "";
                Elements links = element.select("a[data-qa=serp-item__title]");
                if (!links.isEmpty()) {
                    url = links.attr("href");
                    title = links.get(0).text();
                }

                String address = "";
                Elements location = element.select("span[data-qa=vacancy-serp__vacancy-address]");
                if (!location.isEmpty()) {
                    address = location.get(0).text();
                }

                String name = "";
                Elements companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer");
                if (!companyName.isEmpty()) {
                    name = companyName.get(0).text();
                }

                String salaryStr = "";
                String experience = "";
                String format = "";
                Elements salaryBlocks = element.select("div[class^=compensation-labels]");
                if (!salaryBlocks.isEmpty()) {

                    Elements salarySpans = salaryBlocks.get(0).select("span[class*=magritte-text_style-primary]");
                    if (!salarySpans.isEmpty()) {
                        salaryStr = salarySpans.get(0).text();
                    }

                    Elements experienceSpan = salaryBlocks.get(0).select("span[data-qa^=vacancy-serp__vacancy-work-experience]");
                    if (!experienceSpan.isEmpty()) {
                        experience = experienceSpan.get(0).text();
                    }

                    Elements formateSpan = salaryBlocks.get(0).select("span[data-qa^=vacancy-label-work-schedule]");
                    if (!formateSpan.isEmpty()) {
                        format = formateSpan.get(0).text();
                    }
                }
                Salary salary = parseSalary(salaryStr);

                Vacancy vacancy = new Vacancy();
                vacancy.setSiteName("hh.ru");
                vacancy.setCity(address);
                vacancy.setTitle(title);
                vacancy.setUrl(url);
                if(salary != null) vacancy.setSalary(salary);
                vacancy.setCompanyName(name);
                vacancy.setExperience(experience);
                vacancy.setFormat(format);

                vacancies.add(vacancy);
            }
            page++;
        }
        return vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        return Jsoup.connect(String.format(URL_FORMAT, searchString, page))
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                .referrer("https://hh.ru/")
                .get();
    }

    public static Salary parseSalary(String salaryStr) {
        salaryStr = salaryStr.replaceAll("[\\s\u00A0\u202F\u2007,]", "");

        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(salaryStr);

        List<Integer> numbers = new ArrayList<>();
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }

        if (numbers.isEmpty()) return null;

        if (salaryStr.toLowerCase().contains("от") && numbers.size() == 1) {
            if (!salaryStr.contains("₽")) return null;
            return new Salary("от", numbers.get(0), "₽");
        } else if (salaryStr.toLowerCase().contains("до") && numbers.size() == 1) {
            if (!salaryStr.contains("₽")) return null;
            return new Salary("до", numbers.get(0), "₽");
        } else if (numbers.size() == 2) {
            if (!salaryStr.contains("₽")) return null;
            Salary salary = new Salary("от " + numbers.get(0) + " до " + numbers.get(1), numbers.get(0), "₽");
            salary.setRange(true);
            return salary;
        } else if (numbers.size() == 1){
            if (!salaryStr.contains("₽")) return null;
            return new Salary(numbers.get(0), "₽");
        } else {
            return null;
        }
    }
}
