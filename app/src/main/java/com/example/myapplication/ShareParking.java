package com.example.myapplication;

import android.app.DatePickerDialog;
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

import java.time.format.DateTimeParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ShareParking extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private TextView spaceNumber;
    private TextView description;
    private Button saveButton;
    private ConectWithJava conectWithJava;
    Button startDate;
    Button expirationDate;
    TextView startTimeAndDate;
    TextView endTimeAndDate;
    private CustomDataTime selectedStartDate;
    private CustomDataTime selectedEndDate;

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
        startTimeAndDate = findViewById(R.id.start);
        endTimeAndDate = findViewById(R.id.expiration);
        selectedStartDate = new CustomDataTime(this, startTimeAndDate);
        selectedEndDate = new CustomDataTime(this, endTimeAndDate);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                spaceNumber.setText("");
                description.setText("");
                startTimeAndDate.setText("");
                endTimeAndDate.setText("");


                String text = selectedStartDate.getDateAsText();
                String text1 = selectedEndDate.getDateAsText();

                startDate.setText(text);
                expirationDate.setText(text1);
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
        }
    }

    private void shareParking() throws DateTimeParseException {
        SharedParkingSpace sharedParkingSpace = new SharedParkingSpace();
        sharedParkingSpace.setDescription(description.getText().toString());
        sharedParkingSpace.setSpaceNumber(Integer.parseInt(spaceNumber.getText().toString()));
        sharedParkingSpace.setExpirationDate(startTimeAndDate.getText().toString());
        sharedParkingSpace.setStartDate(endTimeAndDate.getText().toString());

        Call<SharedParkingSpace> call = conectWithJava.shareParking(sharedParkingSpace);

        call.enqueue(new Callback<SharedParkingSpace>() {
            @Override
            public void onResponse(Call<SharedParkingSpace> call, Response<SharedParkingSpace> response) {
                Toast.makeText(getApplicationContext(), "Date salvate", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SharedParkingSpace> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_LONG).show();
            }
        });
    }
}