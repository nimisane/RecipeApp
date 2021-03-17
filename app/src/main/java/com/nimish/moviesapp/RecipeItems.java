package com.nimish.moviesapp;

import java.util.List;

public class RecipeItems {

    String recipeName;
    String recipeDescription;
    String recipeType;
    String ingredients;
    String imgUrl;
    String eatenAt;
    Boolean trending;
    List<String> preparation;


    public RecipeItems() {
    }

    public RecipeItems(String recipeName, String recipeDescription, String recipeType, String ingredients, String imgUrl, String eatenAt, Boolean trending, List<String> recipeSteps) {
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeType = recipeType;
        this.ingredients = ingredients;
        this.imgUrl = imgUrl;
        this.eatenAt = eatenAt;
        this.trending = trending;
        this.preparation = recipeSteps;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public String getRecipeType() {
        return recipeType;
    }

    public void setRecipeType(String recipeType) {
        this.recipeType = recipeType;
    }

    public String getIngredients() {
        return ingredients;
    }

    public List<String> getPreparation() {
        return preparation;
    }

    public void setPreparation(List<String> preparation) {
        this.preparation = preparation;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getEatenAt() {
        return eatenAt;
    }

    public void setEatenAt(String eatenAt) {
        this.eatenAt = eatenAt;
    }

    public Boolean getTrending() {
        return trending;
    }

    public void setTrending(Boolean trending) {
        this.trending = trending;
    }
}
