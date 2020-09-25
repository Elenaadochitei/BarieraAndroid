package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GetUsersOfUser extends AppCompatActivity {
    private static final String ID = " id";
    private ListView ressultat;
    private TextView label;
    private ConectWithJava conectWithJava;
    ArrayList<String> guests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_get_users_of_user);
        ressultat = findViewById(R.id.ressultat);
        label = findViewById(R.id.label);
        initializeRetrofit();
        Toast.makeText(this, "Clik Item To Update", Toast.LENGTH_SHORT).show();
        ressultat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private static final String TAG ="Clik!" ;
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                Log.i(TAG, "onListItemClick: " + position);
                Intent intent = new Intent(view.getContext(), MyAccount.class);
                view.getContext().startActivity(intent);
            }
    });
    }


    private void initializeRetrofit() {
        try {
            String BASE_URL = "http://192.168.0.101:8080/";
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            conectWithJava = retrofit.create(ConectWithJava.class);
            viewMyList();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    private void viewMyList() {
        SharedPreferences sharedPreferences = getSharedPreferences(ID, MODE_PRIVATE);
        sharedPreferences.getString(ID, null);
        System.out.println(sharedPreferences.getString(ID,null)+"nnnnnnnnnnn");

        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.label, guests);
        Call<List<Nume_Nr_Masina>> stringCall = conectWithJava.getNameAndPlateOfUser();

        stringCall.enqueue(new Callback<List<Nume_Nr_Masina>>() {
            @Override
            public void onResponse(Call<List<Nume_Nr_Masina>> call, Response<List<Nume_Nr_Masina>> response) {
                assert response.body() != null;
                for (Nume_Nr_Masina a : response.body()) {
                    guests.add(a.getNume() + " - " + a.getNrMasina());
                }
                ressultat.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<Nume_Nr_Masina>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
