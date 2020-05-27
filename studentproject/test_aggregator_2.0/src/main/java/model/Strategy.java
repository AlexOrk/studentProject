package model;

import vo.Vacancy;
import java.util.List;

public interface Strategy {
    List<Vacancy> getVacancies(String jobName, String city);
}
