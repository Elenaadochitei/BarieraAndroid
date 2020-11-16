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

import com.example.myapplication.retrofit.ConectWithJava;
import com.example.myapplication.retrofit.RetrofitApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_ID;
import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_SHARED_PREF;
import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_TOKEN;


public class GetUsersOfUser extends AppCompatActivity {

    private ListView ressultat;
    private TextView label;
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
        populateView();
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

    private void populateView() {
        ConectWithJava conectWithJava = RetrofitApi.getInstance().create(ConectWithJava.class);
        SharedPreferences sharedPreferences = getSharedPreferences(LOGGED_USER_SHARED_PREF, MODE_PRIVATE);
        String test = sharedPreferences.getString(LOGGED_USER_ID, null);
        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.label, guests);
        String token = sharedPreferences.getString(LOGGED_USER_TOKEN, null);
        Call<List<NameAndPlateRegister>> stringCall = conectWithJava.getNameAndPlateOfUser(token, test);
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
