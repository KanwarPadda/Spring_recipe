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
 * Description: <it test case for user Page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.TestData;
import com.hebars.nomnom.entity.Recipe;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.service.RecipeService;
import com.hebars.nomnom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    @InjectMocks
    UserController userController;

    private User user;
    private MultipartFile multipartFile;


    @BeforeEach
    void setUp() {
        user = TestData.getUser();
        multipartFile = TestData.getMockMultipartFile("test");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void showProfilePageTest() {
        Set<Recipe> emptySet = Collections.emptySet();
        when(recipeService.getAllRecipeByUser(anyLong())).thenReturn(emptySet);
        assertEquals("profile", userController.showProfilePage(model, user));
    }

    @Test
    public void findUserProfileSameUserTest() {
        when(userService.getUserByUsername(anyString())).thenReturn(user);
        assertEquals("redirect:/user/profile", userController.findUserProfile(model, user.getUsername(), user));
    }

    @Test
    public void findUserProfileDifferentUserTest() {
        Set<Recipe> emptySet = Collections.emptySet();
        when(recipeService.getAllRecipeByUser(anyLong())).thenReturn(emptySet);
        when(userService.getUserByUsername(anyString())).thenReturn(user);
        assertEquals("user-page", userController.findUserProfile(model, "differentName", user));
    }

    @Test
    public void postImageTest() throws IOException {
        assertEquals("redirect:/user/profile", userController.postImage(multipartFile, user, "update"));
        verify(userService, atLeastOnce()).saveProfilePhoto(any(User.class), any(MultipartFile.class));
    }

    @Test
    public void postImageDeleteTest() throws IOException {
        assertEquals("redirect:/user/profile", userController.postImage(multipartFile, user, "nope"));
    }



}
