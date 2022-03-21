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
 * Description: <it provides business logic to application;>
 ***********************************************************************************
 */
package com.hebars.nomnom.service;

import com.hebars.nomnom.entity.todo;
import com.hebars.nomnom.entity.fav;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.repository.RecipeRepository;
import com.hebars.nomnom.repository.UserRepository;
import com.hebars.nomnom.util.StringGenerator;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class RecipeService {

    private static final Path fileUploadDirectory = Paths.get("./src/main/uploads/images/recipe");


    Logger logger = LoggerFactory.getLogger(RecipeService.class);

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.hebars.nomnom.repository.todoRepository todoRepository;

    @Autowired
    private com.hebars.nomnom.repository.favRepository favRepository;

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString()));
    }

    public Set<Recipe> getAllRecipeByUser(Long id) {
        return recipeRepository.findByUser_Id(id);
    }

    public Page<Recipe> listAll(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        return recipeRepository.findAll(pageable);
    }

    public Recipe saveRecipe(Recipe recipe, User user, MultipartFile file) throws IOException {

        String sanitizedRecipeHTML = Jsoup.clean(recipe.getRecipeBody(), Whitelist.simpleText().addTags("h1", "h2", "h3", "h4", "li", "ul", "ol", "p"));
        recipe.setRecipeBody(sanitizedRecipeHTML);
        if (file == null || file.isEmpty()) {
            recipe.setFileName("default.png");
            recipe.setUser(user);
            recipeRepository.save(recipe);
            return recipe;
        } else {
            String fileName = StringGenerator.getRandomFilename(file);
            Path filePath = Paths.get(fileUploadDirectory + "/" + fileName);
            try {
                Files.write(filePath, file.getBytes());
                recipe.setFileName(fileName);
                recipe.setUser(user);
                recipeRepository.save(recipe);
                return recipe;
            } catch (Exception e) {

            }
        }
        return recipe;
    }


    public Recipe updateRecipeWithoutImages(Recipe recipe, User user) {
        recipe.setUser(user);
        recipe.setFileName(recipeRepository.findById(recipe.getId()).orElseThrow(EntityNotFoundException::new).getFileName());
        String sanitizedRecipeHTML = Jsoup.clean(recipe.getRecipeBody(), Whitelist.simpleText().addTags("h1", "h2", "h3", "h4", "li", "ul", "ol", "p"));
        recipe.setRecipeBody(sanitizedRecipeHTML);
        recipeRepository.save(recipe);
        return recipe;
    }

    public Recipe updateRecipePicture(Recipe recipe, MultipartFile file) {
        if (file.isEmpty()) {
            return recipe;
        } else {
            String toDelete = recipe.getFileName();
            String fileName = StringGenerator.getRandomFilename(file);
            Path filePath = Paths.get(fileUploadDirectory + "/" + fileName);
            try {
                Files.write(filePath, file.getBytes());
                recipe.setFileName(fileName);
                if (!("default.png".equals(toDelete))) {
                    Path pathToDelete = Paths.get(fileUploadDirectory + "/" + toDelete);
                    Files.delete(pathToDelete);
                }
            } catch (Exception e) {
            }

            return recipe;
        }
    }

    public Recipe deleteRecipePicture(Recipe recipe) throws IOException {
        if (!("default.png").equals(recipe.getFileName())) {
            Path deletePath = Paths.get(fileUploadDirectory + "/" + recipe.getFileName());
            Files.delete(deletePath);
            recipe.setFileName("default.png");
        }

        return recipe;
    }

    public void deleteRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        Set<fav> recipefavs = favRepository.findByRecipe_Id(recipe.getId());
        Set<todo> recipetodos = todoRepository.findByRecipe_Id(recipe.getId());
        for (fav fav : recipefavs) {
            favRepository.delete(fav);
        }
        for (todo todo : recipetodos) {
            todoRepository.delete(todo);
        }
        recipeRepository.delete(recipe);
    }

    public boolean hasRated(Long userId, Long recipeId) {
        Set<fav> favSet = favRepository.findByRecipe_Id(recipeId);
        for (fav fav : favSet) {
            if (fav.getUser().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    public Set<todo> getAlltodos(Long id) {
        return todoRepository.findByRecipe_Id(id);
    }

    public Set<fav> getAllfavs(Long id) {
        return favRepository.findByRecipe_Id(id);
    }

    public Page<Recipe> searchRecipe(String keyword, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber - 1, 5);
        return recipeRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public List<Recipe> previewRecipeList() {
        List<Recipe> recipeList = new ArrayList<>();
        List<Long> indexList = randomRecipeList();
        for (int i = 0; i <= 3; i++) {
            recipeList.add(getRecipeById(indexList.get(i)));
        }

        return recipeList;
    }

    private List<Long> randomRecipeList() {
        List<Long> idList = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            idList.add((long) i);
        }
        Collections.shuffle(idList);

        for (int i = 1; i <= 3; i++) {
            idList.remove(idList.size() - 1);
        }

        return idList;
    }

}
