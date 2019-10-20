package com.example.easydriveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DetailViewActivity extends AppCompatActivity {

    TextView tvFileName;
    Button btRename, btOpen, btDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        String userfileId = getIntent().getStringExtra("userfileIdIntent");

        tvFileName = findViewById(R.id.tvFileName);
        tvFileName.setText(userfileId);
        btRename = findViewById(R.id.btRename);
        btOpen = findViewById(R.id.btOpen);
        btDownload = findViewById(R.id.btDownload);



    }

}
