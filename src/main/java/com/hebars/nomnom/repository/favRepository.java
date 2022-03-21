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
 * Description: <it provides basic crud operation over the table;>
 ***********************************************************************************
 */

package com.hebars.nomnom.repository;

import com.hebars.nomnom.entity.fav;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface favRepository extends JpaRepository<fav, Long> {

    Set<fav> findByRecipe_Id(Long recipeId);

}
