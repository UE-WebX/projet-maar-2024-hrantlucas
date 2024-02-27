package org.hrantlucas.model.drink.v2;


import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "recipe")
public class DrinkRecipeV2 {

    @XmlElement(name = "cocktailName")
    private String cocktailName;

    @XmlElement(name = "detailedType")
    private DetailedTypeV2 detailedType;

    @XmlElement(name = "imageUrl")
    private String imageUrl;

    @XmlElement(name = "syntheticList")
    private String syntheticList;

    @XmlElementWrapper
    @XmlElement(name = "detailedIngredient")
    private List<DetailedIngredientTypeV2> detailedIngredients;

    @XmlElement(name = "instructions")
    private String instructions;

    public DrinkRecipeV2() {
    }

    public String getCocktailName() {
        return cocktailName;
    }

    public void setCocktailName(String cocktailName) {
        this.cocktailName = cocktailName;
    }

    public DetailedTypeV2 getDetailedType() {
        return detailedType;
    }

    public void setDetailedType(DetailedTypeV2 detailedType) {
        this.detailedType = detailedType;
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

    public List<DetailedIngredientTypeV2> getDetailedIngredient() {
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
}


