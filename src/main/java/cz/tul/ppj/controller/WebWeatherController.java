package cz.tul.ppj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebWeatherController {

    @GetMapping("/weathers")
    public String getCitiesList(Model model) {
        return "weathers :: weathersList";
    }

}
