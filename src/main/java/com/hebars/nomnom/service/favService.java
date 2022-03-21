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

import com.hebars.nomnom.entity.fav;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class favService {

    @Autowired
    private com.hebars.nomnom.repository.favRepository favRepository;

    public fav savefav(fav fav) {
        favRepository.save(fav);
        return fav;
    }


    public fav updatefav(fav fav) {
        favRepository.save(fav);
        return fav;
    }

    public long getRecipefavs(Long recipeId) {
        long count = 0;
        Set<fav> favSet = favRepository.findByRecipe_Id(recipeId);
        for (fav fav : favSet) {
            if (fav.getRecipefav()) {
                count++;
            }
        }

        return count;
    }

    public fav getfavByIds(Long userId, Long recipeId) {
        Set<fav> favSet = favRepository.findByRecipe_Id(recipeId);
        for (fav fav : favSet) {
            if (fav.getUser().getId().equals(userId)) {
                return fav;
            }
        }

        return null;
    }

}
