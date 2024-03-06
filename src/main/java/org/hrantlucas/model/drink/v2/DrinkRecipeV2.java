package org.hrantlucas.model.drink.v2;


import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;


public class DrinkRecipeV2 {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("name")
    private String name;

    @JsonProperty("type")
    private String type;

    @JsonProperty("alcoholic")
    private Boolean alcoholic;

    @JsonProperty("imageURL")
    private String imageURL;

    @JsonProperty("ingredients")
    private List<String> ingredients;

    @JsonProperty("detailedIngredients")
    private List<DetailedIngredientTypeV2> detailedIngredients;

    @JsonProperty("instructions")
    private String instructions;

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

    public Boolean getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(Boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<DetailedIngredientTypeV2> getDetailedIngredients() {
        return detailedIngredients;
    }

    public void setDetailedIngredients(List<DetailedIngredientTypeV2> detailedIngredients) {
        this.detailedIngredients = detailedIngredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}


