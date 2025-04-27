package cz.tul.ppj.controller;

import cz.tul.ppj.service.jpa.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web")
public class WebWeatherController {

    private WeatherService weatherService;

    @Autowired
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weathers/all")
    public String getAllWeathers(Model model) {
        var weathers = weatherService.getAll();
        model.addAttribute("weathers", weathers);
        return "fragments/weathers :: weatherAll";
    }

    @GetMapping("/weathers/summary")
    public String getWeatherSummary(Model model) {
        var weatherSummary = weatherService.countWeathersByEachCity();
        model.addAttribute("weatherSummary", weatherSummary);
        return "fragments/weathers :: weatherSummary";
    }

    @GetMapping("/weathers/averages")
    public String getWeatherAverages(Model model, @RequestParam("citySelect") String citySelect) {
        model.addAttribute("test", citySelect); //TODO
        return "fragments/weathers :: weatherAverages";
    }

}
