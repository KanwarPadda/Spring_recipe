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
 * Description: <it test case for recipe Page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.TestData;
import com.hebars.nomnom.entity.todo;
import com.hebars.nomnom.entity.fav;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.service.todoService;
import com.hebars.nomnom.service.favService;
import com.hebars.nomnom.service.RecipeService;
import com.hebars.nomnom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class RecipeControllerTest {

    @InjectMocks
    RecipeController recipeController;

    @Mock
    UserService userService;

    @Mock
    RecipeService recipeService;

    @Mock
    todoService todoService;

    @Mock
    favService favService;

    @Mock
    Model model;

    User user;
    MultipartFile multipartFile;
    Recipe recipe;
    todo todo;
    fav fav;

    @BeforeEach
    void setUp() {
        user = TestData.getUser();
        recipe = TestData.getRecipe();
        todo = TestData.gettodo();
        fav = TestData.getfav();
        multipartFile = TestData.getMockMultipartFile("test");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllRecipesTest() {
        assertEquals("redirect:/recipe/page/1", recipeController.getAllRecipes(model));
    }

    @Test
    public void getRecipesOnPageTest() {
        Page<Recipe> page = Page.empty();
        List<Recipe> recipeList = Collections.emptyList();
        when(recipeService.listAll(anyInt())).thenReturn(page);
        assertEquals("recipes", recipeController.getRecipesOnPage(model, 1, user));
    }

    @Test
    public void searchRecipesTest() {
        Page<Recipe> page = Page.empty();
        when(recipeService.searchRecipe(anyString(), anyInt())).thenReturn(page);
        assertEquals("search-results", recipeController.searchRecipes(model, "keyword", 1, user));
    }

    @Test
    public void recipePageTest() {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        when(recipeService.getAlltodos(anyLong())).thenReturn(Collections.singleton(todo));
        when(recipeService.getAllfavs(anyLong())).thenReturn(Collections.singleton(fav));
        when(favService.getRecipefavs(anyLong())).thenReturn(1L);

        assertEquals("recipe", recipeController.recipePage(TestData.TEST_ID, model, user));

    }

    @Test
    public void createRecipeTest() {
        assertEquals("new-recipe", recipeController.createRecipe(user, model));
    }

    @Test
    public void postRecipeTest() throws IOException {
        when(recipeService.saveRecipe(any(Recipe.class), any(User.class), any(MultipartFile.class))).thenReturn(recipe);
        assertEquals("redirect:/recipe/", recipeController.postRecipe(recipe, user, multipartFile));
    }

    @Test
    public void editRecipeTest() {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        assertEquals("edit-recipe", recipeController.editRecipe(model, TestData.TEST_ID, user));
    }

    @Test
    public void updateRecipeHasImage() throws IOException {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        when(recipeService.saveRecipe(any(Recipe.class), any(User.class), any(MultipartFile.class))).thenReturn(recipe);
        when(recipeService.updateRecipeWithoutImages(any(Recipe.class), any(User.class))).thenReturn(recipe);

        assertEquals("redirect:/recipe/" + TestData.TEST_ID, recipeController.updateRecipe(TestData.TEST_ID, recipe, user, multipartFile, "update"));
    }

    @Test
    public void updateRecipeDeleteImage() throws IOException {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        when(recipeService.saveRecipe(any(Recipe.class), any(User.class), any(MultipartFile.class))).thenReturn(recipe);
        when(recipeService.deleteRecipePicture(recipe)).thenReturn(recipe);

        assertEquals("redirect:/recipe/" + TestData.TEST_ID, recipeController.updateRecipe(TestData.TEST_ID, recipe, user, multipartFile, "noUpdate"));
    }

    @Test
    public void deleteRecipeTest() {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        assertEquals("redirect:/recipe/", recipeController.deleteRecipe(TestData.TEST_ID, user));
    }

    @Test
    public void addtodoUserNullTest() {
        user = null;
        assertEquals("redirect:/login", recipeController.addtodo(TestData.TEST_ID, todo, user));
    }

    @Test
    public void addtodoUserNotNullTest() {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        when(todoService.savetodo(todo)).thenReturn(todo);
        assertEquals("redirect:/recipe/" + TestData.TEST_ID, recipeController.addtodo(TestData.TEST_ID, todo, user));
    }

    @Test
    public void deletetodoTest() {
        when(todoService.gettodoById(anyLong())).thenReturn(todo);
        assertEquals("redirect:/recipe/" + TestData.TEST_ID, recipeController.deletetodo(TestData.TEST_ID, TestData.TEST_ID, user));
    }

    @Test
    public void addfavUserNullTest() {
        user = null;
        assertEquals("redirect:/login", recipeController.submitfav(TestData.TEST_ID, "fav", user));
    }

    @Test
    public void addfavUserIsNullTest() {
        when(recipeService.getRecipeById(anyLong())).thenReturn(recipe);
        when(recipeService.hasRated(anyLong(), anyLong())).thenReturn(false);
        assertEquals("redirect:/recipe/" + TestData.TEST_ID, recipeController.submitfav(TestData.TEST_ID, "fav", user));
    }
}
