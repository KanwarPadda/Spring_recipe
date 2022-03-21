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
 * Description: <it test case for registration Page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.controller;

import com.hebars.nomnom.TestData;
import com.hebars.nomnom.entity.User;
import com.hebars.nomnom.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RegistrationControllerTest {

    @InjectMocks
    RegistrationController registrationController;

    @Mock
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestData.getUser();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void registerPageTest() {
        assertEquals("register", registrationController.registerPage());
    }

    @Test
    public void addUserThatDoesNotExistTest() {
        when(userService.userExists(anyString())).thenReturn(false);
        when(userService.saveUserRegister(user)).thenReturn(user);

        assertEquals("redirect:/login", registrationController.addUser(user));
    }

    @Test
    public void addUserThatAlreadyExistsTest() {
        when(userService.userExists(anyString())).thenReturn(true);

        assertEquals("redirect:/register", registrationController.addUser(user));
    }
}
