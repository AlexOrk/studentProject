package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.View;
import vo.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final static Logger log = LoggerFactory.getLogger(Model.class);
    private View view;
    private Provider[] providers;

    public Model(View view, Provider... providers) {
        if (view == null || providers == null || providers.length == 0) {
            throw  new IllegalArgumentException();
        }
        this.view = view;
        this.providers = providers;
    }

    public void selectJobNameAndCity(String jobName, String city) {
        log.info("selectJobNameAndCity method selected.");
        List<Vacancy> vacancies = new ArrayList<>();
        for (Provider provider : providers)
            vacancies.addAll(provider.getJavaVacancies(jobName, city));
        view.update(vacancies);
        log.info("View has been updated.");
    }
}
