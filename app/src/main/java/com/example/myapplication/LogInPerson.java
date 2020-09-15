package com.example.myapplication;

import android.content.Intent;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LogInPerson extends AppCompatActivity {
    private TextView nume;
    private TextView parola;
    private Button logIn;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "add_person_register_plate";

    private ConectWithLogInJava conectWithLogInJavaJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_log_in);

        Toast.makeText(getApplicationContext(), "Bine ai venit!", Toast.LENGTH_SHORT).show();

        logIn = findViewById(R.id.log_in_button);  //initializare buton
        logIn.setOnClickListener(v -> openMainActivity());

        nume = findViewById(R.id.nume);
        parola = findViewById(R.id.parola);

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

        Call<Boolean> call = conectWithLogInJavaJava.checkNameAndPassword(nume.getText().toString(), parola.getText().toString());

        call.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                boolean isUserPresentInDb = response.body().booleanValue();
                if (isUserPresentInDb) {
                    openMainActivity();
                } else {
                    Toast.makeText(getApplicationContext(), "INVALID USER", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initializeRetrofit() {
        try {
            String BASE_URL = "http://192.168.1.186:8080/";
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
