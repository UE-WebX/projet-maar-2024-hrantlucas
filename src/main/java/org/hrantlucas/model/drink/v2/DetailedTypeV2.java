package org.hrantlucas.model.drink.v2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "detailedType")
public class DetailedTypeV2 {

    @XmlElement(name = "isAlcoholic")
    private String isAlcoholic;

    @XmlElement(name = "category")
    private String category;

    @XmlElement(name = "glassType")
    private String glassType;

    public String getIsAlcoholic() {
        return isAlcoholic;
    }

    public void setIsAlcoholic(String isAlcoholic) {
        this.isAlcoholic = isAlcoholic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getGlassType() {
        return glassType;
    }

    public void setGlassType(String glassType) {
        this.glassType = glassType;
    }
}
