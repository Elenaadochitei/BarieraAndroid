package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class AddNewPerson extends AppCompatActivity {

    private TextView plateRegister;
    private TextView userName;
    private Button saveButton;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "add_person_register_plate";
    private String text;
    private ConectWithJava conectWithJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_new_person);

        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        Button openGallery = (Button) findViewById(R.id.openGallery);

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        plateRegister = findViewById(R.id.plate_register);
        userName = findViewById(R.id.name);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String BASE_URL = "http://192.168.100.37:8080/";
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            conectWithJava = retrofit.create(ConectWithJava.class);
            insertNumeNrMasina();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        HashSet<String> nameAndPlateRegister = new HashSet<>();
        nameAndPlateRegister.add(plateRegister.getText().toString());
        nameAndPlateRegister.add(userName.getText().toString());

        editor.putStringSet(TEXT, numeNrMasina );
        editor.apply();
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "add_person_register_plate");
    }
    public void updateViews() {
        plate_register.setText(text);
    }
    private void insertNumeNrMasina() {

        Nume_Nr_Masina insertNewUser = new Nume_Nr_Masina();
        insertNewUser.setNrMasina(plate_register.getText().toString());
        insertNewUser.setNume(userName.getText().toString());

        Call<Nume_Nr_Masina> call = conectWithJava.insertNewUser(insertNewUser);

        call.enqueue(new Callback<NameAndPlateRegister>() {
            @Override
            public void onResponse(Call<NameAndPlateRegister> call, Response<NameAndPlateRegister> response) {
                plateRegister.setText(response.body().getPlateRegister());
            }

            @Override
            public void onFailure(Call<NameAndPlateRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
