package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MyAccount extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TEXT = "test";
    private TextView plateRegister;
    private TextView userName;
    private TextView newPlateRegister;
    private EditText editMyAccount;
    private Button editButton;
    public static final String SHARED_PREFS1 = "sharedPrefs";
    public static final String TEXT1 = "text";
    private String text1;
    KeyListener keyListener;
    private ConectWithJava conectWithJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_account);
        plateRegister = findViewById(R.id.plateRegister);
        userName = (EditText) findViewById(R.id.userName);
        newPlateRegister= findViewById(R.id.newPlateRegister);
        //make edit text non editable
        plateRegister.setEnabled(true);

        editButton = findViewById(R.id.editButton);
        keyListener = plateRegister.getKeyListener();
        // editButton.setOnClickListener(v -> makeEditable(simpleEditText));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }


    private void makeEditable(EditText editText) {
        boolean enabled = !editText.isEnabled();
        editText.setEnabled(enabled);
        if (enabled) {
            editText.setKeyListener(keyListener);
        } else {
            editText.setKeyListener(null);
        }
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String BASE_URL = "http://192.168.0.106:8080/";
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            conectWithJava = retrofit.create(ConectWithJava.class);
            updateNumeNrMasina();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void updateNumeNrMasina() {
        HashMap<String, String> updatePlate = new HashMap<>();
        updatePlate.put("nume", userName.getText().toString());
        updatePlate.put("nrMasina", plateRegister.getText().toString());

        Call<String> call = conectWithJava.getID(updatePlate);
      //  String test = String.valueOf(call);


        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Call<String> call2 = conectWithJava.updateUser(response.body(), updatePlate);
                updatePlate.remove("nrMasina",plateRegister.getText().toString());
                updatePlate.put("nrMasina", newPlateRegister.getText().toString());
                call2.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call2, Response<String> response) {
                        String a=newPlateRegister.getText().toString();
                        newPlateRegister.setText(a);
                      //  System.out.println(response.body());
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


}
