package org.hrantlucas.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "recipe")
public class MealRecipe {
    private String label;

    private List<String> mealTypes;

    private List<String> dishTypes;

    private List<String> cuisineTypes;

    private String totalTime;

    private String image;

    private String url;

    private List<String> ingredientLines;

    private List<Ingredient> ingredients;

    private List<String> cautions;

    private String calories;

    public MealRecipe() {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getMealTypes() {
        return mealTypes;
    }

    @XmlElementWrapper
    @XmlElement(name = "mealType")
    public void setMealTypes(List<String> mealTypes) {
        this.mealTypes = mealTypes;
    }

    public List<String> getDishTypes() {
        return dishTypes;
    }

    @XmlElementWrapper
    @XmlElement(name = "dishType")
    public void setDishTypes(List<String> dishTypes) {
        this.dishTypes = dishTypes;
    }

    public List<String> getCuisineTypes() {
        return cuisineTypes;
    }

    @XmlElementWrapper
    @XmlElement(name = "cuisineType")
    public void setCuisineTypes(List<String> cuisineTypes) {
        this.cuisineTypes = cuisineTypes;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    @XmlElementWrapper
    @XmlElement(name = "ingredientLine")
    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @XmlElementWrapper
    @XmlElement(name = "ingredient")
    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getCautions() {
        return cautions;
    }

    @XmlElementWrapper
    @XmlElement(name = "caution")
    public void setCautions(List<String> cautions) {
        this.cautions = cautions;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "label='" + label + '\'' +
                ", mealTypes=" + mealTypes +
                ", dishTypes=" + dishTypes +
                ", cuisineTypes=" + cuisineTypes +
                ", totalTime=" + totalTime +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                ", ingredientLines=" + ingredientLines +
                ", ingredients=" + ingredients +
                ", cautions=" + cautions +
                ", calories=" + calories +
                '}';
    }
}
