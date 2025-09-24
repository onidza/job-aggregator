package com.onidza.vo;

import java.util.Objects;

public class Vacancy {
    private String siteName;

    private String title;
    private Salary salary;
    private String city;
    private String companyName;
    private String url;
    private String experience;
    private String format;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Salary getSalary() {
        return salary;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vacancy vacancy)) return false;
        return Objects.equals(siteName, vacancy.siteName) && Objects.equals(title, vacancy.title) && Objects.equals(salary, vacancy.salary) && Objects.equals(city, vacancy.city) && Objects.equals(companyName, vacancy.companyName) && Objects.equals(url, vacancy.url) && Objects.equals(experience, vacancy.experience) && Objects.equals(format, vacancy.format);
    }

    @Override
    public int hashCode() {
        return Objects.hash(siteName, title, salary, city, companyName, url, experience, format);
    }
}
