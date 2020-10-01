package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyAccount extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private TextView plateRegister;
    private TextView name;
    private TextView newPlateRegister;
    private Button editButton;
    private Button editButton2;
    KeyListener keyListener;
    private ConectWithJava conectWithJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_account);
        plateRegister = findViewById(R.id.plateRegister);
        name = (EditText) findViewById(R.id.name);
        newPlateRegister = findViewById(R.id.newPlateRegister);
        plateRegister.setEnabled(true);
        editButton2 = findViewById(R.id.editButton2);
        editButton = findViewById(R.id.editButton);
        keyListener = plateRegister.getKeyListener();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });
        editButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHistory();

            }
        });
    }

    public void viewHistory() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String BASE_URL = "http://192.168.100.23:8080/";
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            conectWithJava = retrofit.create(ConectWithJava.class);
            viewUsersNamesAndPlates();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
    private void viewUsersNamesAndPlates(){
        initializeRetrofit();
        Intent intent = new Intent(this, GetUsersOfUser.class);
        startActivity(intent);
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String BASE_URL = "http://192.168.100.23:8080/";

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            conectWithJava = retrofit.create(ConectWithJava.class);
            updateNameAndPlateRegister();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void  updateNameAndPlateRegister() {
        HashMap<String, String> updatePlate = new HashMap<>();
        updatePlate.put("name", name.getText().toString());
        updatePlate.put("plateRegister", plateRegister.getText().toString());
        Call<String> call = conectWithJava.getID(updatePlate);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Call<String> call2 = conectWithJava.updateUser(response.body(), updatePlate);
                updatePlate.put("plateRegister", newPlateRegister.getText().toString());
                call2.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call2, Response<String> response) {
                        System.out.println(response.body());
                        if (response.body().equals("Product does not exist...")) {
                            name.setText("Reintrocuceti numele");
                            plateRegister.setText("Reintroduceti numarul masinii");
                            newPlateRegister.setText("Reintroduceti noul numar");
                            editButton.setText("Date incorecte");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call2, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call2, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void initializeRetrofit() {
        try {
            String BASE_URL = "http://192.168.100.23:8080/";
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            conectWithJava = retrofit.create(ConectWithJava.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

}