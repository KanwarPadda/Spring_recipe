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
 * Description: <it provides data for test;>
 ***********************************************************************************
 */
package com.hebars.nomnom;

import com.hebars.nomnom.entity.todo;
import com.hebars.nomnom.entity.fav;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import org.springframework.mock.web.MockMultipartFile;

import java.util.HashSet;
import java.util.Set;

public class TestData {

    public static final long TEST_ID = 1L;
    public static final String TEST_USERNAME = "TestUsername";
    public static final String TEST_PASSWORD = "TestPassword";
    public static final String TEST_FILE_NAME = "TestFileName";
    public static final String TEST_TITLE = "TestTitle";
    public static final String TEST_RECIPE_todo = "Test recipe todo!";
    public static final String TEST_FILENAME = "testFilename.png";
    public static final String TEST_RECIPE_BODY = "TestRecipeBody";
    public static final String TEST_CONTENT = "This is test content";
    public static final String RECIPE_ABSOLUTE_PATH = "./src/main/uploads/images/recipe/";
    public static final String USER_ABSOLUTE_PATH = "./src/main/uploads/images/user/";

    public static User getUser() {
        User user = new User();
        user.setId(TEST_ID);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        user.setFileName(TEST_FILENAME);
        return user;
    }

    public static Recipe getRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId(TEST_ID);
        recipe.setFileName(TEST_FILE_NAME);
        recipe.setTitle(TEST_TITLE);
        recipe.setRecipeBody(TEST_RECIPE_BODY);
        recipe.setUser(getUser());
        return recipe;
    }

    public static fav getfav() {
        return getfav(true);
    }

    public static fav getfav(boolean isRecipefav) {
        fav fav = new fav();
        fav.setId(TEST_ID);
        fav.setRecipe(getRecipe());
        fav.setUser(getUser());
        fav.setRecipefav(isRecipefav);
        return fav;
    }

    public static Set<fav> getSetOffavs(int totalNumberOffavs, int numberOfDisfavs) {
        Set<fav> favs = new HashSet<>();
        for (int i = 0; i < totalNumberOffavs; i++) {
            if (numberOfDisfavs > 0) {
                favs.add(getfav(false));
                numberOfDisfavs--;
            } else {
                favs.add(getfav());
            }
        }
        return favs;
    }

    public static todo gettodo() {
        todo todo = new todo();
        todo.setId(TEST_ID);
        todo.setRecipetodo(TEST_RECIPE_todo);
        todo.setUser(getUser());
        return todo;
    }

    public static MockMultipartFile getMockMultipartFile(String content) {
        return new MockMultipartFile("fileName", TEST_FILENAME, null, content.getBytes());
    }
}
