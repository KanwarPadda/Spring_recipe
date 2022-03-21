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
 * Description: <it handles web request/rest API for home page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.repository.UserRepository;
import com.hebars.nomnom.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String homePage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("recipes", recipeService.previewRecipeList());
        model.addAttribute("user", user);
        return "home";
    }
}
