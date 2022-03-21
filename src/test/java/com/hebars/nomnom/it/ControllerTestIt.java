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
 * Description: <it test case for controller;>
 ***********************************************************************************
 */
package com.hebars.nomnom.it;

import com.hebars.nomnom.entity.todo;
import com.hebars.nomnom.entity.fav;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.service.todoService;
import com.hebars.nomnom.service.favService;
import com.hebars.nomnom.service.RecipeService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTestIt {


    private final String BASE_PATH = "/recipe";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;
    @MockBean
    private todoService todoService;
    @MockBean
    private favService favService;

    //Look at other Controller tests for a reference.
    //We are making very simple ones, only checking if right service methods are invoked and right template is returned.
    //More proper way would be to use MockMVC but we are low on time
    //Also Service tests are more important to have so focus on those! \o/
    @Test
    @DisplayName("GET /recipe")
    public void getAllRecipesTest() throws Exception {
        mockMvc.perform(get(BASE_PATH))
                .andExpect(redirectedUrl("/recipe/page/1"))
                .andExpect(status().isFound());
    }

    @Test
    public void getRecipesOnPageTest() throws Exception {
        List<Recipe> rl = Collections.singletonList(new Recipe());
        doReturn(new PageImpl<>(rl)).when(recipeService).listAll(5);

        mockMvc.perform(get(BASE_PATH + "/page/{pageNumber}", 5))
                .andExpect(status().isOk())
                .andExpect(model().attribute("totalPages", 1))
                .andExpect(model().attribute("currentPage", 5))
                .andExpect(model().attribute("recipes", rl));
    }

    @Test
    @Disabled
    public void recipePageTest() throws Exception {
        User u = new User();
        u.setId(1L);
        u.setUsername("User_1");

        List<Recipe> rl = Collections.singletonList(new Recipe());
        Recipe r = new Recipe();
        r.setUser(u);
        Set<todo> c = new HashSet<>(Collections.singletonList(new todo()));
        Set<fav> l = new HashSet<>(Collections.singletonList(new fav()));

        doReturn(r).when(recipeService).getRecipeById(1L);
        doReturn(c).when(recipeService).getAlltodos(1L);
        doReturn(l).when(recipeService).getAllfavs(1L);

        mockMvc.perform(get(BASE_PATH + "/{recipeId}", 1L, u))
                .andExpect(status().isOk())
                .andExpect(model().attribute("recipe", r))
                .andExpect(model().attribute("recipetodos", c))
                .andExpect(model().attribute("recipefavs", l))
                .andExpect(model().attribute("user", u));
    }

    @Test
    public void createRecipeTest() throws Exception {
        mockMvc.perform(get(BASE_PATH + "/new"))
                .andExpect(status().isOk());
    }

    @Test
    @Disabled
    public void postRecipeTest() throws Exception {
        doReturn(new Recipe()).when(recipeService).saveRecipe(any(Recipe.class), any(User.class), any(MultipartFile.class));
        mockMvc.perform(post(BASE_PATH))
                .andExpect(redirectedUrl(BASE_PATH))
                .andExpect(status().isOk());
    }
}


