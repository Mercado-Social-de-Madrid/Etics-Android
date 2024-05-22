package net.mercadosocial.moneda.api.response;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by julio on 15/03/18.
 */

public class ApiError {

    private String message;
    private String reason;
    private String error;


    public static ApiError parse(Response response) {

        try {
            String errorBody = response.errorBody().string();
            if (errorBody == null || errorBody.isEmpty()) {
                return new ApiError("Error", "Error");
            }
            return new Gson().fromJson(errorBody, ApiError.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiError("Error", "Error");
        }
    }

    public ApiError(String message, String error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message != null ? message : reason != null ? reason : error != null ? error : "Error";
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public String getError() {
//        return error;
//    }
//
//    public void setError(String error) {
//        this.error = error;
//    }
}
