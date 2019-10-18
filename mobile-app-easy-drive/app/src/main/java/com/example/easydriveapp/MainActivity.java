package com.example.easydriveapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements UserfileAdapter.ItemClicked {

    ArrayList<Userfile> userfiles;
    RecyclerView tvUserFileList;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView textViewTests;
    ProgressDialog progDial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTests = findViewById(R.id.textViewTests);
        tvUserFileList = findViewById(R.id.tvUserFileList);
        tvUserFileList.setHasFixedSize(true);

        userfiles = new ArrayList<>();

        progDial = new ProgressDialog(MainActivity.this);
        progDial.setMessage("Loading....");
        progDial.show();

        RetrofitClientInstance.GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitClientInstance.GetDataService.class);
        Call<ArrayList<Userfile>> call = service.userfilesResponse();
        call.enqueue(new Callback<ArrayList<Userfile>> () {
            @Override
            public void onResponse(Call<ArrayList<Userfile>> call, retrofit2.Response<ArrayList<Userfile>> response) {
                progDial.dismiss();
                userfiles = response.body();
                populateRecyclerView(userfiles);
            }

            @Override
            public void onFailure(Call<ArrayList<Userfile>> call, Throwable t) {
                progDial.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                Log.e("mes","------------------------------ERROR---------------------------------",t);
            }
        });
    }

    private void populateRecyclerView(ArrayList<Userfile> userfiles) {
        myAdapter = new UserfileAdapter(this,userfiles);
        tvUserFileList.setAdapter(myAdapter);
        layoutManager = new GridLayoutManager(this,2);
        tvUserFileList.setLayoutManager(layoutManager);
    }

    @Override
    public void onItemClicked(int index) {
        Toast.makeText(this, "Surname "+userfiles.get(index).getName(), Toast.LENGTH_SHORT).show();
    }
}
