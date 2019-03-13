package net.mercadosocial.moneda.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class MES {

    public static String cityCode;
    private String name;
    private String code;

    public static List<MES> mesList = new ArrayList<>();

    static {
        mesList.add(new MES("Madrid", "mad"));
        mesList.add(new MES("Aragón", "ara"));
    }

    public MES(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public static int getMESPositionByCode(String codeMES) {
        for (int i = 0; i < mesList.size(); i++) {
            if (TextUtils.equals(mesList.get(i).getCode(), codeMES)) {
                return i;
            }
        }

        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
