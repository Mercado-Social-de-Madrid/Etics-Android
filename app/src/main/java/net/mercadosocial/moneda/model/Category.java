package net.mercadosocial.moneda.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by julio on 1/02/18.
 */

public class Category {

    private String id;
    private String name;
    private String description;
    private String color;

    private transient boolean checked;

    public static List<Category> categoriyNames = new ArrayList<>();

    static {
        categoriyNames.add(new Category("Tecnología"));
        categoriyNames.add(new Category("Coworking"));
        categoriyNames.add(new Category("Librería"));
        categoriyNames.add(new Category("Alimentación"));
    }

    public Category(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
