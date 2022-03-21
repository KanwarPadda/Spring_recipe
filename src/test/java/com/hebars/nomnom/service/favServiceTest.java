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
 * Description: <it test case for favorite Page;>
 ***********************************************************************************
 */
package com.hebars.nomnom.service;

import com.hebars.nomnom.TestData;
import com.hebars.nomnom.entity.fav;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class favServiceTest {

    @Mock
    com.hebars.nomnom.repository.favRepository favRepository;

    @InjectMocks
    favService favService;

    private fav fav;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fav = TestData.getfav();

        when(favRepository.save(any(fav.class))).thenReturn(fav);
    }

    @Test
    void savefavTest() {
        assertEquals(fav, favService.savefav(fav));
    }


    @Test
    void updatefavTest() {
        assertEquals(fav, favService.updatefav(fav));
    }

    @Test
    void getRecipefavs() {
        Set<fav> favs = TestData.getSetOffavs(10, 4);
        when(favRepository.findByRecipe_Id(anyLong())).thenReturn(favs);

        assertEquals(6, favService.getRecipefavs(TestData.TEST_ID));
    }


    @Test
    void getfavByIdsTest() {
        when(favRepository.findByRecipe_Id(anyLong())).thenReturn(Collections.singleton(fav));

        assertEquals(fav, favService.getfavByIds(TestData.TEST_ID, 10L));
    }

    @Test
    void getfavByIdsTestReturnsNull() {
        when(favRepository.findByRecipe_Id(anyLong())).thenReturn(Collections.singleton(fav));

        assertNull(favService.getfavByIds(999L, 10L));
    }
}
