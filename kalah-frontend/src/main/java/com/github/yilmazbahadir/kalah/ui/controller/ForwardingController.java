package com.github.yilmazbahadir.kalah.ui.controller;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * <h1>Http Request Forwarder</h1>
 * Since this application serves single page react app.
 * We have to forward any requests starting with kalah to root(/) - index.html.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@RestController
public class ForwardingController {

    @GetMapping("/kalah/**")
    public ModelAndView forward(ModelMap model) {
        return new ModelAndView("/", model);
    }
}
