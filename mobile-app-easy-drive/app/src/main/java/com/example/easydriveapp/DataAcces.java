package com.example.easydriveapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;

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

    public static void getSingleFile(Callback<Userfile> callback, String fileID) {
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<Userfile> call = service.getSingleFile(fileID);
        call.enqueue(callback);
    }

    public static void renameSingleFile(Callback<Userfile> callback, String fileID, String newFilename) {
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);

        JSONObject objName = new JSONObject();
        try {
            objName.put("name",newFilename);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody myreqbody = null;
        myreqbody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),objName.toString());
        Call<Userfile> call = service.renameSingleFile(fileID, myreqbody);
        call.enqueue(callback);
    }
}
