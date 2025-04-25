package cz.tul.ppj.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/reports")
    public String reportsPage() {
        return "reports";
    }

    @GetMapping("/averages")
    public String averagesPage() {
        return "averages";
    }

}
