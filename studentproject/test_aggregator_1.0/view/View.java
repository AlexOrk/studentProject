package studentproject.view;

import studentproject.Controller;
import studentproject.vo.Vacancy;

import java.util.List;

public interface View {
    void update(List<Vacancy> vacancies);
    void setController(Controller controller);
}
