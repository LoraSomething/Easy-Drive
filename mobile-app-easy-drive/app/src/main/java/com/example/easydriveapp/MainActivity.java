package com.example.easydriveapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements UserfileAdapter.ItemClicked {

    ArrayList<Userfile> userfiles;
    RecyclerView tvUserFileList;
    RecyclerView.Adapter myAdapter;
    RecyclerView.LayoutManager layoutManager;
    TextView tvError, tvWelcome, tvUserNav;
    RecyclerViewFiles rvFiles;
    Button btSignIn, btSignOut;
    DrawerLayout drawer ;
    ActionBarDrawerToggle toggle;
    NavigationView nv;

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvError = findViewById(R.id.tvError);
        tvError.setVisibility(View.GONE);
        tvUserFileList = findViewById(R.id.tvUserFileList);
        tvUserFileList.setHasFixedSize(true);
        tvUserFileList.setVisibility(View.GONE);
        btSignIn = findViewById(R.id.btSignIn);
        btSignIn.setVisibility(View.GONE);
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWelcome.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout)findViewById(R.id.activity_main_drawer);
        toggle = new ActionBarDrawerToggle(this, drawer,R.string.Open, R.string.Close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nv = (NavigationView)findViewById(R.id.nv);

        btSignOut = nv.getHeaderView(0).findViewById(R.id.btSignOut);
        tvUserNav = nv.getHeaderView(0).findViewById(R.id.tvUserNav);
        btSignOut.setVisibility(View.GONE);
        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                AuthChanged();
            }
        });

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, FirebaseUIActivity.class);
                startActivity(myIntent);
                AuthChanged();
            }
        });


        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                switch(id)
                {
                    case R.id.nav_item_one:
                        Toast.makeText(MainActivity.this, "Upload",Toast.LENGTH_SHORT).show();
                        uploadFile();
                        break;
                    case R.id.nav_item_two:
                        Toast.makeText(MainActivity.this, "Account",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_item_three:
                        Toast.makeText(MainActivity.this, "Settings",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        return true;
                }
                return true;

            }
        });

        AuthChanged();
        userfiles = new ArrayList<>();
    }

    public void AuthChanged(){
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            tvUserNav.setText("Current user: \n"+FirebaseAuth.getInstance().getCurrentUser().getEmail());
            btSignOut.setVisibility(View.VISIBLE);
            btSignIn.setVisibility(View.GONE);
            tvWelcome.setVisibility(View.GONE);
            getAllFiles(null);
        }
        else
        {
            tvUserNav.setText("Not signed in");
            btSignOut.setVisibility(View.GONE);
            tvWelcome.setVisibility(View.VISIBLE);
            btSignIn.setVisibility(View.VISIBLE);
            tvUserFileList.setVisibility(View.GONE);
        }
    }

    public void getAllFiles(final Action<Boolean> succes) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            DataAccesFirestore.GetAllFiles(new Action<ArrayList<Userfile>>() {
                @Override
                public void execute(ArrayList<Userfile> param) {
                    userfiles = param;
                    populateRecyclerView(userfiles);
                    tvError.setVisibility(View.GONE);
                    tvUserFileList.setVisibility(View.VISIBLE);
                    if (succes!=null){
                        succes.execute(true);
                    }
                }
            });
        }
    }

    public void uploadFile(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("*/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, 1);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    Uri fileUri = data.getData();
                    String filePath = fileUri.getPath();
                    Log.e("--------------",fileUri.getPath());
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        AuthChanged();
        getAllFiles(null);
    }

    private void populateRecyclerView(ArrayList<Userfile> userfiles) {
        myAdapter = new UserfileAdapter(this, userfiles);
        tvUserFileList.setAdapter(myAdapter);
        layoutManager = new GridLayoutManager(this, 2);
        tvUserFileList.setLayoutManager(layoutManager);
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
