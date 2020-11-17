package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    boolean doubleBackToExitPressedOnce = false;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        nv = (NavigationView) findViewById(R.id.nv);

        t = new ActionBarDrawerToggle(this, dl, R.string.fab_transformation_sheet_behavior, R.string.hide_bottom_view_on_scroll_behavior);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sp = getSharedPreferences("logIn", MODE_PRIVATE);

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.log:
                        Toast.makeText(MainActivity.this, "V-ați deconectat de la aplicație!", Toast.LENGTH_SHORT).show();
                        sp.edit().putBoolean("logged", false).apply();
                        openLogInActivity();
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        Toast.makeText(getApplicationContext(), "Bine ai venit!", Toast.LENGTH_SHORT).show();

        ImageButton add = (ImageButton) findViewById(R.id.add);
        ImageButton cancel = (ImageButton) findViewById(R.id.cancel);
        ImageButton admin = (ImageButton) findViewById(R.id.admin);
        ImageButton parking = (ImageButton) findViewById(R.id.parking);
        Button parkingSpaces = (Button) findViewById(R.id.parkingSpaces);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityAddNewPerson();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityCancelPerson();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityMyAccount();
            }
        });
        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityShareParking();
            }
        });
        parkingSpaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openListFreeShareParking();
            }
        });
    }

    public void openLogInActivity() {
        Intent intent = new Intent(this, LogInPerson.class);
        startActivity(intent);
    }

    public void openActivityAddNewPerson() {
        Intent intent = new Intent(this, AddNewPerson.class);
        startActivity(intent);
    }

    public void openActivityCancelPerson() {
        Intent intent = new Intent(this, CancelPerson.class);
        startActivity(intent);
    }

    public void openActivityMyAccount() {
        Intent intent = new Intent(this, MyAccount.class);
        startActivity(intent);
    }

    public void openActivityShareParking() {
        Intent intent = new Intent(this, ShareParking.class);
        startActivity(intent);
    }

    public void openListFreeShareParking() {
        Intent intent = new Intent(this, ListFreeShareParking.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Apasă din nou pentru a ieși", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (t.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }
}