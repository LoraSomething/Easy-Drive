package com.example.easydriveapp;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class RetrofitClientInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.0.105/easydriveapi/api/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public interface GetDataService {

        @GET("getallfiles")
        Call<ArrayList<Userfile>> getAllFiles();

        @GET("/getfile/{file_id}")
        Call<Userfile> getSingleFile(@Path("file_id") String fileID);

        @Headers( "Content-Type: application/json; charset=utf-8")
        @POST("/updatefile/{file_id}")
        Call<Userfile> renameSingleFile(@Path("file_id") String fileID, @Body RequestBody newFilename);

    }
}
