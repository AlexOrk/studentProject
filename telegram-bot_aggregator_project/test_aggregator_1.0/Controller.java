package studentproject;

import studentproject.model.Model;

public class Controller {
    private Model model;

    public Controller(Model model) throws IllegalArgumentException {
        if (model == null) throw new IllegalArgumentException();
        this.model = model;
    }

    public void onCitySelect(String cityName) {
        model.selectCity(cityName);
    }
}
