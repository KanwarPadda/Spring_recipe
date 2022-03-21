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
 * Description: <it define the recipe table as object;>
 ***********************************************************************************
 */

package com.hebars.nomnom.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private String fileName;

    @Lob
    private String recipeBody;

    @OneToMany(mappedBy = "recipe")
    private Set<fav> favs;

    public Recipe() {
    }

    @OneToMany(mappedBy = "recipe")
    private Set<todo> todos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRecipeBody() {
        return recipeBody;
    }

    public void setRecipeBody(String recipeBody) {
        this.recipeBody = recipeBody;
    }

    public Set<fav> getfavs() {
        return favs;
    }

    public void setfavs(Set<fav> favs) {
        this.favs = favs;
    }

    public Set<todo> gettodos() {
        return todos;
    }

    public void settodos(Set<todo> todos) {
        this.todos = todos;
    }
}