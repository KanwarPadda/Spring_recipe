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
 * Description: <it handles web request/rest API for user profile page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.service.RecipeService;
import com.hebars.nomnom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/profile")
    public String showProfilePage(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("recipes", recipeService.getAllRecipeByUser(user.getId()));
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/{username}")
    public String findUserProfile(Model model, @PathVariable("username") String username, @AuthenticationPrincipal User user) {
        if (user != null && username.equals(user.getUsername())) {
            return "redirect:/user/profile";
        }
        User u = userService.getUserByUsername(username);
        model.addAttribute("user", u);
        model.addAttribute("recipes", recipeService.getAllRecipeByUser(u.getId()));
        return "user-page";
    }


    @PostMapping("/update/image")
    public String postImage(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal User user, @RequestParam("button") String command) throws IOException {
        if (command.equals("update")) {
            userService.saveProfilePhoto(user, file);
        } else {
            userService.saveProfilePhoto(user, null);
        }

        return "redirect:/user/profile";
    }


}
