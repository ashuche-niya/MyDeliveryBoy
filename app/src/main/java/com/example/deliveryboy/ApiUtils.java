package com.example.deliveryboy;

public class ApiUtils {

    private ApiUtils() {}

    private static final String BASE_URL = "https://shrouded-ocean-13795.herokuapp.com/";

    public static ApiInterface getAPIService() {
        return ApiManager.getClient(BASE_URL).create(ApiInterface.class);
    }

}
