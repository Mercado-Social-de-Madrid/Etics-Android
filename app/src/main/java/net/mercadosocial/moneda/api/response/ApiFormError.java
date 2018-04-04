package net.mercadosocial.moneda.api.response;

import android.content.Context;

import com.google.gson.Gson;

import net.mercadosocial.moneda.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Response;

/**
 * Created by julio on 15/03/18.
 */

public class ApiFormError {

    private Map<String, List<String>> register;

    public static ApiFormError parse(Response response) {

        try {
            String errorBody = response.errorBody().string();
            return new Gson().fromJson(errorBody, ApiFormError.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


//    public String getMessage() {
//        return "Error";
//    }

    public Map<String, List<String>> getRegisterMap() {
        return register;
    }

    public String getFormErrorMessage(Context context) {

        String message = "";

        Set<Map.Entry<String, List<String>>> entries = getRegisterMap().entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            String field = entry.getKey();
            int fieldNameId = context.getResources().getIdentifier(field, "string", context.getPackageName());
            String fieldName = field;
            if (fieldNameId > 0) {
                fieldName = context.getString(fieldNameId);
            }

            message += String.format(context.getString(R.string.error_in), fieldName) + "\n\n";
            List<String> errors = entry.getValue();
            for (String error : errors) {
                message += "- " + error + "\n";
            }
        }

        return message;
    }
}
