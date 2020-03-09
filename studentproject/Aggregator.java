package studentproject;

import studentproject.model.HHStrategy;
import studentproject.model.Model;
import studentproject.model.MoikrugStrategy;
import studentproject.model.Provider;
import studentproject.view.HtmlView;
import studentproject.view.View;

public class Aggregator {
    public static void main(String[] args) {
        HtmlView view = new HtmlView();
        Provider hhProvider = new Provider(new HHStrategy());
        Provider moikrugProvider = new Provider(new MoikrugStrategy());
        Model model = new Model(view, hhProvider, moikrugProvider);
        Controller controller = new Controller(model);
        view.setController(controller);
        view.userCitySelectEmulationMethod();
    }
}