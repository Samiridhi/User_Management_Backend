package org.example.userapi.model;

import javax.persistence.Embeddable;

@Embeddable
public class Hair {
    private String color;
    private String type;

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
