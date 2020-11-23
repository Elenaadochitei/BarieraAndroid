package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.retrofit.ConectWithJava;
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

import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_SHARED_PREF;
import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_TOKEN;

public class CancelPerson extends AppCompatActivity {
    private TextView plateRegister;
    private TextView userName;
    private Button saveButton;
    private ConectWithJava conectWithJava;
    private Toast toast;

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
            toast = Toast.makeText(getApplicationContext(), "Date Nesalvate", Toast.LENGTH_LONG);
            customErrorToast();
        }
        Toast.makeText(this, "Date Salvate", Toast.LENGTH_SHORT).show();
    }

    private void deleteNameAndPlateRegister() throws Exception {

        HashMap<String, String> deleteUsers = new HashMap<>();
        deleteUsers.put("name", userName.getText().toString());
        deleteUsers.put("plateRegister", plateRegister.getText().toString());

        SharedPreferences sharedPreferences = getSharedPreferences(LOGGED_USER_SHARED_PREF, MODE_PRIVATE);
        String token = sharedPreferences.getString(LOGGED_USER_TOKEN, null);

        Call<String> call = conectWithJava.getID(token, deleteUsers);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                callDeleteById(token, response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                toast = Toast.makeText(getApplicationContext(), "Nu s-a efectuat ștergerea", Toast.LENGTH_LONG);
                customErrorToast();
            }
        });
    }

    private void callDeleteById(String token, Response<String> response) {
        Call<String> stringCall = conectWithJava.deleteUser(token, response.body());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() == null) {
                    clearText();
                    toast = Toast.makeText(getApplicationContext(), "Date incorecte, reintroduceți!", Toast.LENGTH_LONG);
                    customErrorToast();
                } else
               //     Toast.makeText(getApplicationContext(), "Date salvate", Toast.LENGTH_LONG).show();
                customErrorToast();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                toast = Toast.makeText(getApplicationContext(), "Nu s-a găsit persoana", Toast.LENGTH_LONG);
                customErrorToast();
            }
        });
    }

    private void clearText() {
        userName.setText("");
        plateRegister.setText("");
    }

    @SuppressLint("SetTextI18n")
    private void customErrorToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.relativeLayout1));
//        TextView text = (TextView) layout.findViewById(R.id.text);
//        text.setText("Hello! This is a custom toast!");
        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 140);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
