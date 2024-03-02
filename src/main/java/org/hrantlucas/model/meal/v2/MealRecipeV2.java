package org.hrantlucas.model.meal.v2;

import java.util.List;

public class MealRecipeV2 {
    private boolean success;
    private String name;
    private String type;
    private String country;
    private String preparationTime;
    private String imageURL;
    private String source;
    private String ingredients;
    private List<IngredientV2> detailedIngredients;
    private List<String> instructions;

    public MealRecipeV2() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientV2> getDetailedIngredients() {
        return detailedIngredients;
    }

    public void setDetailedIngredients(List<IngredientV2> detailedIngredients) {
        this.detailedIngredients = detailedIngredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }
}
