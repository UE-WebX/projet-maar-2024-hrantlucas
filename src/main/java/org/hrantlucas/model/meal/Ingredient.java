package org.hrantlucas.model.meal;

public class Ingredient {
    private String text;
    private String food;
    private String quantity;
    private String image;

    public Ingredient() {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "text='" + text + '\'' +
                ", food='" + food + '\'' +
                ", quantity='" + quantity + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
