package cz.tul.ppj.controller;

import cz.tul.ppj.service.jpa.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebWeatherController {

    private WeatherService weatherService;

    @Autowired
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weathers/summary")
    public String getCitiesList(Model model) {
        var weatherSummary = weatherService.countWeathersByEachCity();
        model.addAttribute("weatherSummary", weatherSummary);
        return "weathers :: weatherSummary";
    }

}
