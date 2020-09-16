package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CancelPerson extends AppCompatActivity {
    private TextView plate_register;
    private TextView userName;
    private Button saveButton1;
    public static final String SHARED_PREFS1 = "sharedPrefs";
    public static final String TEXT1 = "cancel_person_register_plate";

    private ConectWithJava conectWithJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cancel_person);

        saveButton1 = (Button) findViewById(R.id.save_button);

        userName = (EditText) findViewById(R.id.plateRegister);
        plate_register = (EditText) findViewById(R.id.simpleEditText1);

        saveButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData() {
        //Shared pref - > pare a fi o  memorie a telefonului
        try {
            // plate_register = findViewById(R.id.textView);
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
            deleteNumeNrMasina();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void deleteNumeNrMasina() throws Exception {

        HashMap<String, String> deleteUsers = new HashMap<>();
        deleteUsers.put("nume", userName.getText().toString());
        deleteUsers.put("nrMasina", plate_register.getText().toString());

        Call<String> call = conectWithJava.getID(deleteUsers);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                callDeleteById(response);
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void callDeleteById(Response<String> response) {
        Call<String> stringCall = conectWithJava.deleteUser(response.body());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response.body());
                if(response.body()==null){
                    userName.setText("Reintrocuceti numele");
                    plate_register.setText("Reintroduceti numarul masinii");
                    saveButton1.setText("Date incorecte");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
