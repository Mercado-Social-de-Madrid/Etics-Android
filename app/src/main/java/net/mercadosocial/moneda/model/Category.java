package net.mercadosocial.moneda.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by julio on 1/02/18.
 */

public class Category {

    private String id;
    private String name;
    private String description;
    private String color;

    public static Map<String, String> categoriyNames = new HashMap<>();

    static {
        categoriyNames.put("uuid", "categoria X");
    }

}
