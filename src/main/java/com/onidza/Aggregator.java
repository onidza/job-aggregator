package com.onidza;

import com.onidza.model.HHStrategy;
import com.onidza.model.HabrCareerStrategy;
import com.onidza.model.Model;
import com.onidza.model.Provider;
import com.onidza.view.HtmlView;
import com.onidza.view.View;
import com.onidza.vo.SortType;

public class Aggregator {
    public static void main(String[] args) {
        View view = new HtmlView();
        Model model = new Model(view, new Provider(new HHStrategy()),new Provider(new HabrCareerStrategy()));
        Controller controller = new Controller(model);
        view.setController(controller);
        view.requestVacancies("java developer", SortType.SALARY);
    }
}
