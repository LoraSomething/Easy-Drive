package com.example.easydriveapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewActivity extends AppCompatActivity {

    TextView tvFileName;
    Button btRename, btOpen, btDownload, btDelete;
    int userfileId;
    Userfile userfile;
    ImageView ivDetailFiletype;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        userfileId = getIntent().getIntExtra("userfileIdIntent",0);

        tvFileName = findViewById(R.id.tvFileName);
        btRename = findViewById(R.id.btRename);
        btOpen = findViewById(R.id.btOpen);
        btDownload = findViewById(R.id.btDownload);
        btDelete= findViewById(R.id.btDelete);
        ivDetailFiletype = findViewById(R.id.ivDetailFiletype);

        DataAcces.getSingleFile(new Callback<Userfile>() {
            @Override
            public void onResponse(Call<Userfile> call, Response<Userfile> response) {
                userfile = response.body();
                tvFileName.setText(userfile.getName()+userfile.getExtension());

                if ( userfile.getFileType() == Userfile.FileType.IMAGE){
                    ivDetailFiletype.setImageResource(R.drawable.png);
                }
                if ( userfile.getFileType() == Userfile.FileType.PDF){
                    ivDetailFiletype.setImageResource(R.drawable.pdf);
                }
            }

            @Override
            public void onFailure(Call<Userfile> call, Throwable t) {
                Toast.makeText(DetailViewActivity.this, "Get failed", Toast.LENGTH_SHORT).show();
            }
        }, userfileId);

        btRename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailViewActivity.this);
                builder.setTitle("Rename");

                final EditText input = new EditText(DetailViewActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                input.setText(userfile.getName());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String m_Text = input.getText().toString();
                        userfile.setName(m_Text);
                        DataAcces.renameSingleFile(new Callback<Userfile>() {
                            @Override
                            public void onResponse(Call<Userfile> call, Response<Userfile> response) {
                                Toast.makeText(DetailViewActivity.this, "Rename Success", Toast.LENGTH_SHORT).show();
                                tvFileName.setText(response.body().getName());
                            }

                            @Override
                            public void onFailure(Call<Userfile> call, Throwable t) {
                                Toast.makeText(DetailViewActivity.this, "Rename failed", Toast.LENGTH_SHORT).show();

                            }
                        }, userfile);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Toast.makeText(DetailViewActivity.this, "Rename cancelled", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });
/*
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataAcces.deleteFile(new Callback<Userfile>() {
                    @Override
                    public void onResponse(Call<Userfile> call, Response<Userfile> response) {
                        //finish();
                    }

                    @Override
                    public void onFailure(Call<Userfile> call, Throwable t) {
                        Toast.makeText(DetailViewActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                    }
                },userfile);
            }
        });
*/
    }

}
