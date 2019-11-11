package com.example.easydriveapp;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.EventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFiles extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    Context activity;
    SwipeRefreshLayout swipe;

    public RecyclerViewFiles() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view_files, container, false);
        swipe = view.findViewById(R.id.swiperefresh);
        swipe.setOnRefreshListener(this);
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            swipe.setRefreshing(true);
            ((MainActivity) activity).getAllFiles(new Action<Boolean>(){
                @Override
                public void execute(Boolean param) {
                    swipe.setRefreshing(false);
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) activity).getAllFiles(new Action<Boolean>(){
            @Override
            public void execute(Boolean param) {
                swipe.setRefreshing(false);
            }
        });
    }

    public void setMainActivity(Context context){
        activity = context;
    }

    @Override
    public void onRefresh() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            swipe.setRefreshing(true);
            if (activity instanceof MainActivity) {
                ((MainActivity) activity).getAllFiles(new Action<Boolean>() {
                    @Override
                    public void execute(Boolean param) {
                        swipe.setRefreshing(false);
                    }
                });
            }
        }
        else{
            swipe.setRefreshing(false);
        }
    }
}
