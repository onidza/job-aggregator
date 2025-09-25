package com.onidza.view;

import com.onidza.Controller;
import com.onidza.vo.SortType;
import com.onidza.vo.Vacancy;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class HtmlView implements View {

    @Setter
    private Controller controller;
    private static final String TEMPLATE_PATH = "/view/vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String newContent = getUpdatedContent(vacancies);
            updateFile(newContent);
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении списка вакансий.");
        }
    }

    public void requestVacancies(String vacancyName, SortType sortType) {
        controller.showVacancies(vacancyName, sortType);
    }

    private Elements clearOldVacancies(Document document) {
        Elements hiddenTemplate = document.getElementsByClass("template");
        document.getElementsByClass("vacancy")
                .stream()
                .filter(el -> !el.hasClass("template"))
                .forEach(Element::remove);
        return hiddenTemplate;
    }

    private Element createVacancy(Vacancy vacancy, Element template) {
        Element templateVacancy = template.clone();

        Objects.requireNonNull(templateVacancy.select("[href]").first())
                        .text(vacancy.getTitle())
                        .attr("href", vacancy.getUrl());

        Objects.requireNonNull(templateVacancy.getElementsByClass("city").first()).text(vacancy.getCity());
        Objects.requireNonNull(templateVacancy.getElementsByClass("companyName").first()).text(vacancy.getCompanyName());
        Objects.requireNonNull(templateVacancy.getElementsByClass("salary").first()).text(vacancy.getSalary().toString());
        Objects.requireNonNull(templateVacancy.getElementsByClass("experience").first()).text(vacancy.getExperience());
        Objects.requireNonNull(templateVacancy.getElementsByClass("format").first()).text(vacancy.getFormat());
        return templateVacancy;
    }

    private void insertVacancies(Elements hiddenTemplate, Element vacancyElement) {
        hiddenTemplate.before(vacancyElement.outerHtml());
    }

    private String getUpdatedContent(List<Vacancy> vacancies) {
            Document document = getDocument();
            if (document == null) return "Null error при получении контента";

            Elements hiddenTemplate = clearOldVacancies(document);
            Element template = hiddenTemplate.clone().removeAttr("style").removeClass("template").get(0);

            for (Vacancy vacancy : vacancies) {
                Element vanancyElement = createVacancy(vacancy, template);
                insertVacancies(hiddenTemplate, vanancyElement);
            }
            return document.html();
    }

    private void updateFile(String content) throws IOException {
        Path outputPath = Path.of("vacancies-filled.html");
        Files.writeString(outputPath, content, StandardCharsets.UTF_8);
    }

    protected Document getDocument(){
        try (InputStream inputStream = getClass().getResourceAsStream(TEMPLATE_PATH)) {
            if (inputStream == null) throw new FileNotFoundException("Template not found:" + TEMPLATE_PATH);
            return Jsoup.parse(inputStream, "UTF-8", "");
        } catch (IOException e) {
            System.out.println("Ошибка при создании вакансий.");
        }
        return null;
    }
}
