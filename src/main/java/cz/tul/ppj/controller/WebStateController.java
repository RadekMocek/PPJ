package cz.tul.ppj.controller;

import cz.tul.ppj.service.jpa.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebStateController {

    private StateService stateService;

    @Autowired
    public void setStateService(StateService stateService) {
        this.stateService = stateService;
    }

    @GetMapping("/states")
    public String getStatesList(Model model) {
        var statesList = stateService.getAll();
        model.addAttribute("statesList", statesList);
        return "fragments/states :: statesList";
    }

}
