package com.onidza.vo;

import lombok.Data;

@Data
public class Vacancy {
    private String siteName;
    private String title;
    private Salary salary;
    private String city;
    private String companyName;
    private String url;
    private String experience;
    private String format;
}
