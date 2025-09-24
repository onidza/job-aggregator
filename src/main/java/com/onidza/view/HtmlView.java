package com.onidza.view;

import com.onidza.Controller;
import com.onidza.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.List;

public class HtmlView implements View {
    private Controller controller;
    private final String filePath = "C:\\Users\\user\\IdeaProjects\\job-aggregator\\src\\main\\java\\com\\onidza\\view\\vacancies.html";

    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String newContent = getUpdatedFileContent(vacancies);
            updateFile(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void getVacancies(String vacancyName) {
        controller.showVacancies(vacancyName, Controller.SortType.NONE);
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        Document document = null;
        try {
            document = getDocument();
            Elements hiddenTemplate = document.getElementsByClass("template");
            Element template = hiddenTemplate.clone().removeAttr("style").removeClass("template").get(0);

            Elements prevVacancies = document.getElementsByClass("vacancy");
            for (Element el : prevVacancies) {
                if (!el.hasClass("template")) {
                    el.remove();
                }
            }

            for (Vacancy vacancy : vacancies) {
                Element templateVacancy = template.clone();

                Element vacancyLink = templateVacancy.getElementsByAttribute("href").get(0);
                vacancyLink.appendText(vacancy.getTitle());
                vacancyLink.attr("href", vacancy.getUrl());

                Element city = templateVacancy.getElementsByClass("city").get(0);
                city.appendText(vacancy.getCity());

                Element companyName = templateVacancy.getElementsByClass("companyName").get(0);
                companyName.appendText(vacancy.getCompanyName());

                Element salary = templateVacancy.getElementsByClass("salary").get(0);
                salary.appendText(vacancy.getSalary().toString());

                Element experience = templateVacancy.getElementsByClass("experience").get(0);
                experience.appendText(vacancy.getExperience());

                Element format = templateVacancy.getElementsByClass("format").get(0);
                format.appendText(vacancy.getFormat());

                hiddenTemplate.before(templateVacancy.outerHtml());
            }
            return document.html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some exception occurred";
    }

    private void updateFile(String content) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath)))) {
            writer.write(content);
        } catch (IOException ignored) {

        }
    }

    protected Document getDocument() throws IOException {
        File file = new File(filePath);
        return Jsoup.parse(file, "UTF-8", "hh.ru");
    }
}
