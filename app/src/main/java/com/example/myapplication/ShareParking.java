package com.example.myapplication;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.retrofit.ConectWithJava;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_SHARED_PREF;
import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_TOKEN;

public class ShareParking extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private TextView spaceNumber;
    private TextView description;
    private Button saveButton;
    private ConectWithJava conectWithJava;
    Button startDate;
    Button expirationDate;
    private TextView startTimeAndDate;
    private TextView endTimeAndDate;
    private CustomDataTime selectedStartDate;
    private CustomDataTime selectedEndDate;
    private ProgressBar spinner;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_share_parking);

        spaceNumber = findViewById(R.id.numberParking);
        description = findViewById(R.id.description);
        saveButton = findViewById(R.id.editButton);
        startDate = findViewById(R.id.startDate);
        expirationDate = findViewById(R.id.expirationDate);
        startTimeAndDate = findViewById(R.id.startDate);
        endTimeAndDate = findViewById(R.id.expirationDate);
        selectedStartDate = new CustomDataTime(this, startTimeAndDate);
        selectedEndDate = new CustomDataTime(this, endTimeAndDate);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        spinner.setVisibility(View.GONE);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner.setVisibility(View.VISIBLE);
                hideKeyboard(v);

                String text = selectedStartDate.getDateAsText();
                String text1 = selectedEndDate.getDateAsText();

                startDate.setText(text);
                expirationDate.setText(text1);

                saveData();
                spaceNumber.setText("");
                description.setText("");
                startTimeAndDate.setText("");
                endTimeAndDate.setText("");
                startDate.setText("Data si ora inceput");
                expirationDate.setText("Data si ora sfarsit");
            }
        });
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ShareParking.this);
                datePickerDialog.setOnDateSetListener(selectedStartDate);
                datePickerDialog.show();
            }
        });
        expirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ShareParking.this);
                datePickerDialog.setOnDateSetListener(selectedEndDate);
                datePickerDialog.show();
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
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
            shareParking();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Conexiune nereusita", Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            toast = Toast.makeText(getApplicationContext(), "Conexiune nereușită", Toast.LENGTH_LONG);
            customErrorToast();
        }
    }

    private void shareParking() throws DateTimeParseException {

        SharedParkingSpace sharedParkingSpace = new SharedParkingSpace();
        sharedParkingSpace.setDescription(description.getText().toString());
        sharedParkingSpace.setSpaceNumber((spaceNumber.getText().toString()));
        sharedParkingSpace.setExpirationDate(startTimeAndDate.getText().toString());
        sharedParkingSpace.setStartDate(endTimeAndDate.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences(LOGGED_USER_SHARED_PREF, MODE_PRIVATE);
        String token = sharedPreferences.getString(LOGGED_USER_TOKEN, null);
        Call<SharedParkingSpace> call = conectWithJava.shareParking(token, sharedParkingSpace);

        call.enqueue(new Callback<SharedParkingSpace>() {
            @Override
            public void onResponse(Call<SharedParkingSpace> call, Response<SharedParkingSpace> response) {
                Toast.makeText(getApplicationContext(), "Date salvate", Toast.LENGTH_SHORT).show();
                spinner.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<SharedParkingSpace> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Logare Nereusita", Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);
                toast = Toast.makeText(getApplicationContext(), "Logare Nereușită", Toast.LENGTH_LONG);
                customErrorToast();
                spinner.setVisibility(View.GONE);
            }
        });
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
