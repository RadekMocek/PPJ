package cz.tul.ppj.controller;

import cz.tul.ppj.service.jpa.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebCityController {

    private CityService cityService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("/cities")
    public String getCitiesList(Model model) {
        var citiesList = cityService.getAll();
        model.addAttribute("citiesList", citiesList);
        return "cities :: citiesList";
    }

    @GetMapping("/cities/select")
    public String getCitiesSelect(Model model) {
        var citiesList = cityService.getAll();
        model.addAttribute("citiesList", citiesList);
        return "cities :: citiesSelect";
    }

}
