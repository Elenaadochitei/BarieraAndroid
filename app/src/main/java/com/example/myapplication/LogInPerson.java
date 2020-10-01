package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Base64;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LogInPerson extends AppCompatActivity {
    private TextView userName;
    private TextView password;
    private Button logIn;
    public static final String ID = "id Admin";
    private ConectWithLogInJava conectWithLogInJavaJava;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);

        Toast.makeText(getApplicationContext(), "Bine ai venit!", Toast.LENGTH_SHORT).show();

        logIn = findViewById(R.id.log_in_button);
        sp = getSharedPreferences("logIn", MODE_PRIVATE);

        if (sp.getBoolean("logged", false)) {
            openMainActivity();
        }

        userName = findViewById(R.id.name);
        password = findViewById(R.id.password);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNameAndPassword();
            }
        });
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public void checkNameAndPassword() {

        initializeRetrofit();
        String originalInput = userName.getText().toString();
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        String originalInput1 = password.getText().toString();
        String encodedString1 = Base64.getEncoder().encodeToString(originalInput1.getBytes());

        Call<LoginInfo> call = conectWithLogInJavaJava.checkNameAndPassword(encodedString, encodedString1);

        call.enqueue(new Callback<LoginInfo>() {

            @Override
            public void onResponse(Call<LoginInfo> call, Response<LoginInfo> response) {
                LoginInfo log = response.body();

                boolean isUserPresentInDb = log.userActive;

                if (isUserPresentInDb) {
                    sp.edit().putBoolean("logged", true).apply();
                    openMainActivity();
                    SharedPreferences sharedPreferences = getSharedPreferences(ID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(ID, log.id);
                    System.out.println("EDITOR " + sharedPreferences.getString(ID, null));
                    sharedPreferences.getString(ID, null);
                    editor.apply();

                } else {
                    Toast.makeText(getApplicationContext(), "Logare nereusita ", Toast.LENGTH_SHORT).show();
                    sp.edit().putBoolean("logged", false).apply();
                }
            }

            @Override
            public void onFailure(Call<LoginInfo> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeRetrofit() {
        try {
            String BASE_URL = "http://192.168.100.37:8080/";
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            conectWithLogInJavaJava = retrofit.create(ConectWithLogInJava.class);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}