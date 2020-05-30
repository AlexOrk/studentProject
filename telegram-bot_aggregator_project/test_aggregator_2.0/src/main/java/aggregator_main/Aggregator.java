package aggregator_main;

import model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.*;

public class Aggregator {
    private final static Logger log = LoggerFactory.getLogger(Aggregator.class);

    public synchronized boolean runAggregator(String jobName, String city) {
        log.info("Aggregator started.");
        HtmlView view = new HtmlView();
        Provider hhProvider = new Provider(new HHStrategy());
//        Provider moikrugProvider = new Provider(new MoikrugStrategy());
        // Other strategies are under development.
        Model model = new Model(view, hhProvider);
        Controller controller = new Controller(model);
        view.setController(controller);
        view.userCitySelectMethod(jobName, city);
        log.info("Aggregator finished the job and returned the result.");
        return !view.isVacancyListEmpty();
    }
}
