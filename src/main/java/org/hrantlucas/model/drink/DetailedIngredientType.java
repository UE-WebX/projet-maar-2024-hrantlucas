package org.hrantlucas.model.drink;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detailedIngredientType")
class DetailedIngredientType {

    @XmlElement(name = "completText")
    private String completText;

    @XmlElement(name = "ingredientName")
    private String ingredientName;

    @XmlElement(name = "ingredientQuantity")
    private String ingredientQuantity;

    public String getCompletText() {
        return completText;
    }

    public void setCompletText(String completText) {
        this.completText = completText;
    }

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
}
