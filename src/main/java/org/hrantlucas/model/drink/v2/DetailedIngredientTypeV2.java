package org.hrantlucas.model.drink.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetailedIngredientTypeV2 {

    @JsonProperty("name")
    private String ingredientName;

    @JsonProperty("quantity")
    private String ingredientQuantity;

    @JsonProperty("image")
    private String image;

    // Getters et setters
    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(String ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}