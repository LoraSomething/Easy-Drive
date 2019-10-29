package com.example.easydriveapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DataAcces {

    ProgressDialog progDial;
    ArrayList<Userfile> userfiles;

    public static void getAllFiles(Callback<ArrayList<Userfile>> callback) {
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<ArrayList<Userfile>> call = service.getAllFiles();
        call.enqueue(callback);
    }

    public static void getSingleFile(Callback<Userfile> callback, int fileID) {
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<Userfile> call = service.getSingleFile(fileID);
        call.enqueue(callback);
    }

    public static void renameSingleFile(Callback<Userfile> callback, Userfile u) {
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

        JsonObject j = new JsonObject();

        j.addProperty("ID",Integer.toString(u.get_id()));
        j.addProperty("filename",u.getName());
        j.addProperty("extension",u.getExtension());

        Call<Userfile> call = service.renameSingleFile(j);
        call.enqueue(callback);
    }
}
