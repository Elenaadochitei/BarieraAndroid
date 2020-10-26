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
import java.util.Objects;
import java.util.regex.Pattern;

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
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ServerIp.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            conectWithJava = retrofit.create(ConectWithJava.class);
            viewUsersNamesAndPlates();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Conexiune nereusita!", Toast.LENGTH_LONG).show();
        }
    }

    private void viewUsersNamesAndPlates() {
        initializeRetrofit();
        Intent intent = new Intent(this, GetUsersOfUser.class);
        startActivity(intent);
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ServerIp.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            conectWithJava = retrofit.create(ConectWithJava.class);
            updateNameAndPlateRegister();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Conexiune nereusita", Toast.LENGTH_LONG).show();

    }

    private void updateNameAndPlateRegister() {
        HashMap<String, String> updatePlate = new HashMap<>();
        updatePlate.put("name", name.getText().toString());
        updatePlate.put("plateRegister", plateRegister.getText().toString());

        Call<String> call = conectWithJava.getID(updatePlate);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Call<String> call2 = conectWithJava.updateUser(response.body(), updatePlate);
                ValidateNewPlateRegister(updatePlate);

                call2.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call2, Response<String> response) {
                        System.out.println(response.body());
                        if (response.body()==null) {
                            name.setText("");
                            plateRegister.setText("");
                            newPlateRegister.setText("");
                            Toast.makeText(getApplicationContext(), "Datele introduse sunt incorecte!\n               " +
                                    "Reintroduceti!", Toast.LENGTH_LONG).show();
                        }else{
                            name.setText("");
                            plateRegister.setText("");
                            newPlateRegister.setText("");
                            Toast.makeText(getApplicationContext(), "Date salvate", Toast.LENGTH_SHORT).show();  
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call2, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Datele nu au fost modificate!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<String> call2, Throwable t) {
                Toast.makeText(getApplicationContext(), "Persoana nu a fost gasita!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeRetrofit() {
        try {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ServerIp.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            conectWithJava = retrofit.create(ConectWithJava.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Conexiune nereusita!", Toast.LENGTH_LONG).show();
        }
    }

    private void ValidateNewPlateRegister(HashMap<String, String> updatePlate) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        boolean matcher = pattern.matcher(Objects.requireNonNull(newPlateRegister.getText())).matches();
        if (!matcher) {
            Toast.makeText(getApplicationContext(), "Format gresit!", Toast.LENGTH_LONG).show();
            updatePlate.put("plateRegister", plateRegister.getText().toString());
        } else {
            updatePlate.put("plateRegister", newPlateRegister.getText().toString());
            Toast.makeText(getApplicationContext(), "Modificare efectuata!", Toast.LENGTH_LONG).show();
        }
    }
}