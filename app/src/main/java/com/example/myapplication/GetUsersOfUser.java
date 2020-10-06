package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import static com.example.myapplication.LogInPerson.ID;

public class GetUsersOfUser extends AppCompatActivity {

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
        Toast.makeText(this, "Selecteaza pentru a modifica", Toast.LENGTH_SHORT).show();
        ressultat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private static final String TAG = "Clik!";

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
            String BASE_URL = "http:///192.168.0.105:8080/";
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
            Toast.makeText(getApplicationContext(), "Conexiune Nereusita", Toast.LENGTH_LONG).show();
        }
    }
    
    private void viewMyList() {
        SharedPreferences sharedPreferences = getSharedPreferences(ID, MODE_PRIVATE);
        String test= sharedPreferences.getString(ID, null);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, R.id.label, guests);
        Call<List<NameAndPlateRegister>> stringCall = conectWithJava.getNameAndPlateOfUser(test);
        stringCall.enqueue(new Callback<List<NameAndPlateRegister>>() {
            @Override
            public void onResponse(Call<List<NameAndPlateRegister>> call, Response<List<NameAndPlateRegister>> response) {
                assert response.body() != null;
                for (NameAndPlateRegister a : response.body()) {
                    guests.add(a.getName() + " - " + a.getPlateRegister());
                }
                ressultat.setAdapter(adapter);
            }
            @Override
            public void onFailure(Call<List<NameAndPlateRegister>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Nu se poate vizualiza lista", Toast.LENGTH_LONG).show();
            }
        });

    }
}
