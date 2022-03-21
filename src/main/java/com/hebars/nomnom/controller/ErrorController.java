/*
 *********************************************************************************

 * Project: < TEAM_HEBARS>
 * Assignment: < assignment 1 >
 * Authors & Student Number: <
    Kanwartejbir Singh Padda(101280244);
    Amandeep Hazra (101286346);
    Sanjana Sharmin (101277205);
 >
 * Date: November 7, 2021,
 * Description: <it handles web request/rest API; >
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.entity.User;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "sad-cupcake-error";
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
