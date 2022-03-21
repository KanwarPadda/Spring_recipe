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
 * Description: <it handles web request/rest API for recipe page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.entity.todo;
import com.hebars.nomnom.entity.fav;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.service.todoService;
import com.hebars.nomnom.service.favService;
import com.hebars.nomnom.service.RecipeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/recipe")
public class RecipeController {

    Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private todoService todoService;

    @Autowired
    private favService favService;

    @GetMapping()
    public String getAllRecipes(Model model) {
        return "redirect:/recipe/page/1";
    }

    @GetMapping("/page/{pageNumber}")
    public String getRecipesOnPage(Model model, @PathVariable("pageNumber") int pageNumber, @AuthenticationPrincipal User user) {
        Page<Recipe> page = recipeService.listAll(pageNumber);
        List<Recipe> recipeList = page.getContent();
        int totalPages = page.getTotalPages();
        model.addAttribute("user", user);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("recipes", recipeList);
        return "recipes";
    }

    @GetMapping("/search/page/{pageNumber}")
    public String searchRecipes(Model model, @RequestParam("keyword") String keyword, @PathVariable("pageNumber") int pageNumber, @AuthenticationPrincipal User user) {
        Page<Recipe> page = recipeService.searchRecipe(keyword, pageNumber);
        List<Recipe> recipeList = page.getContent();
        int totalPages = page.getTotalPages();
        model.addAttribute("user", user);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("recipes", recipeList);
        model.addAttribute("keyword", keyword);

        return "search-results";
    }

    @GetMapping("/{recipeId}")
    public String recipePage(@PathVariable("recipeId") Long recipeId, Model model, @AuthenticationPrincipal User user) {

        Recipe recipe = recipeService.getRecipeById(recipeId);
        Set<todo> recipetodos = recipeService.getAlltodos(recipeId);
        Set<fav> recipefavs = recipeService.getAllfavs(recipeId);

        model.addAttribute("recipe", recipe);
        model.addAttribute("recipetodos", recipetodos);
        model.addAttribute("recipefavs", recipefavs);
        model.addAttribute("favs", favService.getRecipefavs(recipeId));
        model.addAttribute("user", user);
        return "recipe";
    }

    @GetMapping("/new")
    public String createRecipe(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "new-recipe";
    }

    @PostMapping()
    public String postRecipe(@ModelAttribute Recipe recipe, @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) throws IOException {
        recipeService.saveRecipe(recipe, user, file);
        return "redirect:/recipe/";
    }

    @GetMapping("/update/{recipeId}")
    public String editRecipe(Model model, @PathVariable("recipeId") Long recipeId, @AuthenticationPrincipal User user) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        model.addAttribute("recipe", recipe);
        model.addAttribute("user", user);
        return "edit-recipe";
    }

    @PostMapping("/update/{recipeId}")
    public String updateRecipe(@PathVariable("recipeId") Long recipeId, @ModelAttribute Recipe recipe, @AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file, @RequestParam("button") String command) throws IOException {
        recipe.setId(recipeId);
        recipeValidation(recipeId, user.getId());

        if (command.equals("update")) {
            if (!file.isEmpty()) {
                recipeService.saveRecipe(recipe, user, file);
            } else {
                recipeService.updateRecipeWithoutImages(recipe, user);
            }
        } else {
            Recipe rec = recipeService.getRecipeById(recipeId);
            recipeService.deleteRecipePicture(rec);
            recipeService.saveRecipe(rec, user, file);
        }

        return "redirect:/recipe/" + recipeId;
    }

    @GetMapping("/delete/{recipeId}")
    public String deleteRecipe(@PathVariable("recipeId") Long recipeId, @AuthenticationPrincipal User user) {
        recipeValidation(recipeId, user.getId());
        recipeService.deleteRecipeById(recipeId);
        return "redirect:/recipe/";
    }

    @PostMapping("/{recipeId}/todo")
    public String addtodo(@PathVariable("recipeId") Long recipeId, @ModelAttribute todo todo, @AuthenticationPrincipal User user) {
        if (user == null) {
            return "redirect:/login";
        }
        todo.setRecipe(recipeService.getRecipeById(recipeId));
        todo.setUser(user);
        todoService.savetodo(todo);
        return "redirect:/recipe/" + recipeId;
    }

    @GetMapping("/{recipeId}/todo/delete/{todoId}")
    public String deletetodo(@PathVariable("recipeId") Long recipeId, @PathVariable("todoId") Long todoId, @AuthenticationPrincipal User user) {
        todo todoValidation = todoService.gettodoById(todoId);
        if (!(user.getId()).equals(todoValidation.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        todoService.deletetodo(todoId);
        return "redirect:/recipe/" + recipeId;
    }

    @PostMapping("/{recipeId}/fav/")
    public String submitfav(@PathVariable("recipeId") Long recipeId, @RequestParam("button") String recipefav, @AuthenticationPrincipal User user) {
        fav fav;
        if (user == null) {
            return "redirect:/login";
        }

        if (!(recipeService.hasRated(user.getId(), recipeId))) {
            Recipe recipe = recipeService.getRecipeById(recipeId);
            fav = new fav();
            fav.setRecipe(recipe);
            fav.setUser(user);
            fav.setRecipefav(("fav").equals(recipefav));

            favService.savefav(fav);
        } else {
            fav = favService.getfavByIds(user.getId(), recipeId);
            fav.setRecipefav(("fav").equals(recipefav));

            favService.updatefav(fav);
        }

        return "redirect:/recipe/" + recipeId;
    }

    private void recipeValidation(Long recipeId, Long userId) {
        Recipe recipeTest = recipeService.getRecipeById(recipeId);
        if (!recipeTest.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
