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
 * Description: <it test case for service test;>
 ***********************************************************************************
 */
package com.hebars.nomnom.it;

import com.hebars.nomnom.TestData;
import com.hebars.nomnom.entity.todo;
import com.hebars.nomnom.entity.fav;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.repository.UserRepository;
import com.hebars.nomnom.service.todoService;
import com.hebars.nomnom.service.favService;
import com.hebars.nomnom.service.RecipeService;
import com.hebars.nomnom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

// Here we create some simple integration tests testing random things because we promised Valdis.

@SpringBootTest
@ActiveProfiles("test")
public class ServiceTestIt {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RecipeService recipeService;

    @Autowired
    todoService todoService;

    @Autowired
    favService favService;

    User user;
    Recipe recipe;
    fav fav;
    todo todo;
    MultipartFile multipartFile;

    private final String TEST_FILENAME_2 = "testFilename_2.png";
    private final String ABSOLUTE_PATH = "./src/main/resources/static/images/recipe/";
    private final String TEST_CONTENT = "This is test content";

    @BeforeEach
    public void setUp() {
        user = TestData.getUser();
        recipe = TestData.getRecipe();
        fav = TestData.getfav();
        todo = TestData.gettodo();
    }

    @Test
    public void retrieveUserTest() {
        UserDetails userTest = userService.loadUserByUsername(TestData.TEST_USERNAME);

        assertEquals(TestData.TEST_USERNAME, userTest.getUsername());
        assertNotEquals(TestData.TEST_PASSWORD, userTest.getPassword());
    }

    @Test
    public void retrieveUserRecipesTest() {
        Set<Recipe> retrievedRecipes = recipeService.getAllRecipeByUser(1L);
        Recipe testRecipe = null;

        for (Recipe recipe : retrievedRecipes) {
            testRecipe = recipe;
        }

        assertEquals(TestData.TEST_ID, testRecipe.getId());
        assertEquals(TestData.TEST_RECIPE_BODY, testRecipe.getRecipeBody());
        assertEquals(TestData.TEST_TITLE, testRecipe.getTitle());
    }

    @Test
    public void retrieveRecipeTest() throws IOException {
        Recipe retrievedRecipe = recipeService.getRecipeById(TestData.TEST_ID);

        assertEquals(TestData.TEST_FILENAME, retrievedRecipe.getFileName());
        assertEquals(TestData.TEST_RECIPE_BODY, retrievedRecipe.getRecipeBody());
        assertEquals(TestData.TEST_TITLE, retrievedRecipe.getTitle());
    }

    @Test
    public void retrieveRecipetodoTest() {
        Recipe retrievedRecipe = recipeService.getRecipeById(TestData.TEST_ID);
        Set<todo> retrievedtodos = recipeService.getAlltodos(retrievedRecipe.getId());
        todo retrievedtodo = null;
        for (todo todo : retrievedtodos) {
            retrievedtodo = todo;
        }

        assertEquals(TestData.gettodo().getId(), retrievedtodo.getId());
        assertEquals(TestData.gettodo().getRecipetodo(), retrievedtodo.getRecipetodo());
    }

    @Test
    public void saveAndDeleteRecipetodoTest() {
        Recipe retrievedRecipe = recipeService.getRecipeById(TestData.TEST_ID);
        todo todo = new todo();
        todo.setRecipe(retrievedRecipe);

        todoService.savetodo(todo);
        Set<todo> allComents = recipeService.getAlltodos(retrievedRecipe.getId());
        assertEquals(2, allComents.size());

        for (todo todoTest : allComents) {
            if (todoTest.getId() != 1) {
                todoService.deletetodo(todo.getId());
            }
        }
        assertEquals(1, recipeService.getAlltodos(1L).size());
    }

    @Test
    public void retrieveRecipefavTest() {
        Recipe retrievedRecipe = recipeService.getRecipeById(TestData.TEST_ID);
        Set<fav> retrievedfavs = recipeService.getAllfavs(retrievedRecipe.getId());
        fav retrievedfav = null;
        for (fav fav : retrievedfavs) {
            retrievedfav = fav;
        }

        assertEquals(TestData.gettodo().getId(), retrievedfav.getId());
        assertEquals(TestData.getfav().getRecipefav(), retrievedfav.getRecipefav());
    }
}
