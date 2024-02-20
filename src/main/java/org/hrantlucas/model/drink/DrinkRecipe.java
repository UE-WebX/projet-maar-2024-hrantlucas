package org.hrantlucas.model.drink;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recipe")
public class DrinkRecipe {

    @XmlElement(name = "cocktailName")
    private String cocktailName;

    @XmlElement(name = "detailedType")
    private DetailedType detailedType;

    @XmlElement(name = "preparationTime")
    private String preparationTime;

    @XmlElement(name = "imageUrl")
    private String imageUrl;

    @XmlElement(name = "syntheticList")
    private String syntheticList;

    @XmlElement(name = "detailedIngredient")
    private DetailedIngredientType detailedIngredient;

    @XmlElement(name = "instructions")
    private String instructions;

    public DrinkRecipe() {
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public DetailedType getDetailedType() {
        return detailedType;
    }

    public void setDetailedType(DetailedType detailedType) {
        this.detailedType = detailedType;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSyntheticList() {
        return syntheticList;
    }

    public void setSyntheticList(String syntheticList) {
        this.syntheticList = syntheticList;
    }

    public DetailedIngredientType getDetailedIngredient() {
        return detailedIngredient;
    }

    public void setDetailedIngredient(DetailedIngredientType detailedIngredient) {
        this.detailedIngredient = detailedIngredient;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}


