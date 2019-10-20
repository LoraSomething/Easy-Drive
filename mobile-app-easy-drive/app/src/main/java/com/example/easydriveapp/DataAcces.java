package com.example.easydriveapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import android.app.ProgressDialog;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class DataAcces {

    ProgressDialog progDial;
    ArrayList<Userfile> userfiles;

    public static void getAllFiles(Callback<ArrayList<Userfile>> callback) {
        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<ArrayList<Userfile>> call = service.userfilesResponse();
        call.enqueue(callback);
    }
}
