package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.constants.SharedPreferencesConstants;
import com.example.myapplication.model.LoginRequest;
import com.example.myapplication.retrofit.ConectWithLogInJava;
import com.example.myapplication.retrofit.RetrofitApi;

import org.apache.http.HttpStatus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_SHARED_PREF;

public class LogInPerson extends AppCompatActivity {
    private TextView userName;
    private TextView password;
    private Button logIn;
    SharedPreferences sp;
    private Toast toast;

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


        logIn.setOnClickListener(v -> checkNameAndPassword());

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
        ConectWithLogInJava connectWithLogInJava = RetrofitApi.getInstance().create(ConectWithLogInJava.class);
        LoginRequest loginRequest = LoginRequest.builder().username(userName.getText().toString())
                .password(password.getText().toString()).build();
        Call<LoginResponse> call = connectWithLogInJava.checkNameAndPassword(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == HttpStatus.SC_OK) {
                    LoginResponse responseBody = response.body();
                    String loggedUserId = responseBody.getId();
                    if (loggedUserId != null && !loggedUserId.isEmpty()) {
                        sp.edit().putBoolean("logged", true).apply();
                        SharedPreferences sharedPreferences = getSharedPreferences(LOGGED_USER_SHARED_PREF, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SharedPreferencesConstants.LOGGED_USER_ID, loggedUserId);
                        editor.putString(SharedPreferencesConstants.LOGGED_USER_TOKEN, "Bearer " + responseBody.getAccessToken());
                        editor.apply();
                        openMainActivity();
                    } else {
                        toast = Toast.makeText(getApplicationContext(), "Logare Nereusita ", Toast.LENGTH_SHORT);
                        customErrorToast();
                        sp.edit().putBoolean("logged", false).apply();
                    }
                } else if (HttpStatus.SC_UNAUTHORIZED == response.code()) {
                    toast = Toast.makeText(getApplicationContext(), "Logare Nereusita", Toast.LENGTH_LONG);
                    customErrorToast();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
               toast = Toast.makeText(getApplicationContext(), "Eroare la conectarea cu serverul.", Toast.LENGTH_LONG);
               customErrorToast();
            }
        });
    }

    private void customErrorToast() {
        toast.setGravity(Gravity.TOP, 0, 140);
        View view = toast.getView();
        view.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        TextView text = view.findViewById(android.R.id.message);
        text.setTextColor(Color.WHITE);
        Typeface typeface = Typeface.create("normal", Typeface.BOLD);
        text.setTypeface(typeface);
        toast.show();
    }
}