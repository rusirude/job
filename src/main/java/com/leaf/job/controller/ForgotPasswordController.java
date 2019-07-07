package com.leaf.job.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author : rusiru on 7/6/19.
 */

@Controller
public class ForgotPasswordController {

    @RequestMapping(path="/forgotPassword", method = RequestMethod.GET)
    public ModelAndView forgotPassword() {
        ModelAndView  mv = new  ModelAndView();
        mv.setViewName("forgotPassword");
        return mv;
    }
}
