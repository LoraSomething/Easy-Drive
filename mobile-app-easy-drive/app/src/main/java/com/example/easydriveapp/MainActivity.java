package com.example.easydriveapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements UserfileAdapter.ItemClicked {

    ArrayList<Userfile> userfiles;
    RecyclerView tvUserFileList;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvError;
    RecyclerViewFiles rvFiles;
    Boolean finishedLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvError = findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);
        tvUserFileList = findViewById(R.id.tvUserFileList);
        tvUserFileList.setHasFixedSize(true);
        tvUserFileList.setVisibility(View.GONE);

        finishedLoading = false;

        userfiles = new ArrayList<>();

        getAllFiles(null);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllFiles(null);
    }

    private void populateRecyclerView(ArrayList<Userfile> userfiles) {
        myAdapter = new UserfileAdapter(this, userfiles);
        tvUserFileList.setAdapter(myAdapter);
        layoutManager = new GridLayoutManager(this, 2);
        tvUserFileList.setLayoutManager(layoutManager);
    }


    public void getAllFiles(final Action<Boolean> succes) {

        DataAcces.getAllFiles(new Callback<ArrayList<Userfile>>() {
            @Override
            public void onResponse(Call<ArrayList<Userfile>> call, retrofit2.Response<ArrayList<Userfile>> response) {
                userfiles = response.body();
                populateRecyclerView(userfiles);
                tvError.setVisibility(View.GONE);
                tvUserFileList.setVisibility(View.VISIBLE);
                if(succes!=null)
                {
                    succes.execute(true);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Userfile>> call, Throwable t) {
                tvError.setText("Something went wrong.\n\nSwipe down to try again.");
                tvError.setVisibility(View.VISIBLE);
                tvUserFileList.setVisibility(View.GONE);
                if(succes!=null)
                {
                    succes.execute(false);
                }
            }
        });
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof RecyclerViewFiles) {
            rvFiles = (RecyclerViewFiles)getSupportFragmentManager().findFragmentById(R.id.frAllFiles);
            rvFiles.setMainActivity(this);
        }
    }

    @Override
    public void onItemClicked(int index) {
        Intent myIntent = new Intent(MainActivity.this, DetailViewActivity.class);
        myIntent.putExtra("userfileIdIntent",userfiles.get(index).get_id());
        startActivity(myIntent);
    }
}
