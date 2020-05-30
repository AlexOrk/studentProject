package aggregator_main;

import model.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller {
    private final static Logger log = LoggerFactory.getLogger(Controller.class);
    private Model model;

    public Controller(Model model) {
        try {
            if (model == null) throw new IllegalArgumentException();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            log.error("The model is null.");
            log.error(e.getMessage());
        }
        this.model = model;
    }

    public void onJobNameAndCitySelect(String jobName, String city) {
        model.selectJobNameAndCity(jobName, city);
    }
}
