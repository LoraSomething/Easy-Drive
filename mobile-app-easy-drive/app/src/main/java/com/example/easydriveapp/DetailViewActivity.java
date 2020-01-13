package com.example.easydriveapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewActivity extends AppCompatActivity {

    TextView tvFileName, tvFileExt, tvLastModified;
    Button btRename, btOpen, btDownload, btDelete;
    String userfileId;
    Userfile userfile;
    ImageView ivDetailFiletype;

    File localFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);
        userfileId = getIntent().getStringExtra("userfileIdIntent");

        tvFileName = findViewById(R.id.tvFileName);
        tvFileExt = findViewById(R.id.tvFileExt);
        tvLastModified = findViewById(R.id.tvLastModified);
        btRename = findViewById(R.id.btRename);
        btOpen = findViewById(R.id.btOpen);
        btDownload = findViewById(R.id.btDownload);
        btDelete = findViewById(R.id.btDelete);
        ivDetailFiletype = findViewById(R.id.ivDetailFiletype);

        btDownload.setEnabled(true);
        btRename.setEnabled(true);
        btOpen.setEnabled(true);
        btDelete.setEnabled(true);

        final ProgressBar spinner;
        spinner = (ProgressBar) findViewById(R.id.progress_bar);
        spinner.setVisibility(View.GONE);

        DataAccesFirestore.GetSingleFile(new Action<Userfile>() {
            @Override
            public void execute(Userfile param) {
                userfile = param;

                java.util.Date time = new java.util.Date((long)userfile.getLastModified()*1000);

                long date1 =  userfile.getLastModified()*1000;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date dt = new Date(date1);



                if (userfile.getFileExtension() != null) {
                    tvFileName.setText("Name: "+userfile.getName());
                    tvFileExt.setText("Extension: "+userfile.getFileExtension());
                    tvLastModified.setText("Last Modified: "+dateFormat.format(dt));
                } else {
                    tvFileName.setText("Name: "+userfile.getName());
                    tvFileExt.setText("Extension: "+"unknown");
                    tvLastModified.setText("Last Modified: "+userfile.getLastModified()+"");
                }

                if (userfile.decideFileType() == Userfile.FileType.IMAGE) {
                    ivDetailFiletype.setImageResource(R.drawable.png);
                }
                if (userfile.decideFileType() == Userfile.FileType.PDF) {
                    ivDetailFiletype.setImageResource(R.drawable.pdf);
                }
                if (userfile.decideFileType() == Userfile.FileType.TEXT) {
                    ivDetailFiletype.setImageResource(R.drawable.text);
                }
                if (userfile.decideFileType() == Userfile.FileType.AUDIO) {
                    ivDetailFiletype.setImageResource(R.drawable.audio);
                }
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

                        final String m_Text = input.getText().toString().trim();
                        userfile.setName(m_Text);
                        DataAccesFirestore.UpdateOneFile(new Action<Boolean>() {
                            @Override
                            public void execute(Boolean param) {
                                tvFileName.setText("Name: "+m_Text );
                            }
                        }, userfileId, userfile);
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

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.setVisibility(View.VISIBLE);
                btDownload.setEnabled(false);
                btRename.setEnabled(false);
                btOpen.setEnabled(false);
                btDelete.setEnabled(false);
                DataAccesFirestore.DeleteOneFile(new Action<Boolean>() {
                    @Override
                    public void execute(Boolean param) {
                        spinner.setVisibility(View.GONE);
                        finish();
                    }
                }, userfileId);
            }
        });

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    //do your check here
                    spinner.setVisibility(View.VISIBLE);
                    btDownload.setEnabled(false);
                    btRename.setEnabled(false);
                    btOpen.setEnabled(false);
                    btDelete.setEnabled(false);
                    DataAccesFirestore.DownloadOneFile(new Action<Boolean>() {
                        @Override
                        public void execute(Boolean param) {
                            Toast.makeText(DetailViewActivity.this, "Opening file", Toast.LENGTH_SHORT).show();
                            File fileNameOnDevice = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "");
                            String file_path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + userfile.getName();
                            File localFile = new File(fileNameOnDevice, userfile.getName() + "." + userfile.getFileExtension());
                            Uri uri = FileProvider.getUriForFile(DetailViewActivity.this,
                                    "com.mypackage.myprovider", localFile);

                            ContentResolver cR = getContentResolver();
                            MimeTypeMap mime = MimeTypeMap.getSingleton();
                            String type = mime.getExtensionFromMimeType(cR.getType(uri));

                            Intent intent = new Intent();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    | Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setDataAndType(uri, type);
                            startActivityForResult(intent,123);

                            spinner.setVisibility(View.GONE);
                            btDownload.setEnabled(true);
                            btRename.setEnabled(true);
                            btOpen.setEnabled(true);
                            btDelete.setEnabled(true);

                        }
                    }, userfile);
                } else {
                    ActivityCompat.requestPermissions(DetailViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });


        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    //do your check here
                    spinner.setVisibility(View.VISIBLE);
                    btDownload.setEnabled(false);
                    btRename.setEnabled(false);
                    btOpen.setEnabled(false);
                    btDelete.setEnabled(false);
                    DataAccesFirestore.DownloadOneFile(new Action<Boolean>() {
                        @Override
                        public void execute(Boolean param) {
                            Toast.makeText(DetailViewActivity.this, "File Downloaded", Toast.LENGTH_SHORT).show();
                            spinner.setVisibility(View.GONE);
                            btDownload.setEnabled(true);
                            btRename.setEnabled(true);
                            btOpen.setEnabled(true);
                            btDelete.setEnabled(true);
                        }
                    }, userfile);
                }
                else {
                    ActivityCompat.requestPermissions(DetailViewActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });

    }
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("","Permission is granted");
                return true;
            } else {

                Log.v("","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("","Permission is granted");
            return true;
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 123:
                try {
                    localFile.delete();
                    Toast.makeText(DetailViewActivity.this, "Deleted file", Toast.LENGTH_SHORT).show();
                }
                catch (Exception ext){}
                break;
            default:

        }
    }
}
