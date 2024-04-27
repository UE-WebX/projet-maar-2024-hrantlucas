package org.hrantlucas.model.menu;

import java.util.List;

public class MenuFilter {
    private String cuisineType;
    private Boolean alcoholic;
    private String presentIngredient;
    private String maxPreparationTime;
    private List<String> constraints;

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public Boolean getAlcoholic() {
        return alcoholic;
    }

    public void setAlcoholic(Boolean alcoholic) {
        this.alcoholic = alcoholic;
    }

    public String getPresentIngredient() {
        return presentIngredient;
    }

    public void setPresentIngredient(String presentIngredient) {
        this.presentIngredient = presentIngredient;
    }

    public String getMaxPreparationTime() {
        return maxPreparationTime;
    }

    public void setMaxPreparationTime(String maxPreparationTime) {
        this.maxPreparationTime = maxPreparationTime;
    }

    public List<String> getConstraints() {
        return constraints;
    }

    public void setConstraints(List<String> constraints) {
        this.constraints = constraints;
    }
}
