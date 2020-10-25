package com.example.myapplication;

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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CancelPerson extends AppCompatActivity {
    private TextView plateRegister;
    private TextView userName;
    private Button saveButton;
    private ConectWithJava conectWithJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_cancel_person);

        saveButton = (Button) findViewById(R.id.save_button);

        userName = (EditText) findViewById(R.id.name);
        plateRegister = (EditText) findViewById(R.id.plate_register);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData() {
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
            deleteNameAndPlateRegister();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Date Nesalvate", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "Date Salvate", Toast.LENGTH_SHORT).show();
    }

    private void deleteNameAndPlateRegister() throws Exception {

        HashMap<String, String> deleteUsers = new HashMap<>();
        deleteUsers.put("name", userName.getText().toString());
        deleteUsers.put("plateRegister", plateRegister.getText().toString());

        Call<String> call = conectWithJava.getID(deleteUsers);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                callDeleteById(response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Nu s-a efectuat stergerea", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void callDeleteById(Response<String> response) {
        Call<String> stringCall = conectWithJava.deleteUser(response.body());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println(response.body());
                if (response.body().equals("Product does not exist...")) {
                    userName.setText(" ");
                    plateRegister.setText(" ");
                    Toast.makeText(getApplicationContext(), "Date incorecte, reintroduceti!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Nu s-a gasit persoana", Toast.LENGTH_LONG).show();
            }
        });
    }
}
