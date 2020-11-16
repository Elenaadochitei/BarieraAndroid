package com.example.myapplication;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.retrofit.ConectWithJava;
import com.example.myapplication.retrofit.RetrofitApi;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_SHARED_PREF;
import static com.example.myapplication.constants.SharedPreferencesConstants.LOGGED_USER_TOKEN;

public class MyAccount extends AppCompatActivity {

    private static final String SHARED_PREFS = "sharedPrefs";
    private TextView plateRegister;
    private TextView name;
    private TextView newPlateRegister;
    private Button editButton;
    private Button editButton2;
    private KeyListener keyListener;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_account);
        plateRegister = findViewById(R.id.plateRegister);
        name = (EditText) findViewById(R.id.name);
        newPlateRegister = findViewById(R.id.newPlateRegister);
        plateRegister.setEnabled(true);
        editButton2 = findViewById(R.id.editButton2);
        editButton = findViewById(R.id.editButton);
        keyListener = plateRegister.getKeyListener();

        editButton.setOnClickListener(v -> saveData());
        editButton2.setOnClickListener(v -> viewHistory());
    }

    private void viewHistory() {
        Intent intent = new Intent(this, GetUsersOfUser.class);
        startActivity(intent);
    }

    public void saveData() {
        try {

            updateNameAndPlateRegister();
        } catch (Exception e) {
            toast = Toast.makeText(getApplicationContext(), "Conexiune nereusita", Toast.LENGTH_LONG);
            customErrorToast();
        }
    }

    private void updateNameAndPlateRegister() {
        ConectWithJava conectWithJava = RetrofitApi.getInstance().create(ConectWithJava.class);
        HashMap<String, String> updatePlate = new HashMap<>();
        updatePlate.put("name", name.getText().toString());
        updatePlate.put("plateRegister", plateRegister.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences(LOGGED_USER_SHARED_PREF, MODE_PRIVATE);
        String token = sharedPreferences.getString(LOGGED_USER_TOKEN, null);
        Call<String> call = conectWithJava.getID(token, updatePlate);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Call<String> call2 = conectWithJava.updateUser(token, response.body(), updatePlate);
                ValidateNewPlateRegister(updatePlate);
                if (!ValidateNewPlateRegister(updatePlate)) {
                    newPlateRegister.setText("");
                    return;
                }
                call2.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call2, Response<String> response) {
                        if (response.body() == null) {
                            toast = Toast.makeText(getApplicationContext(), "Datele introduse sunt incorecte!\n               " +
                                    "Reintroduceti!", Toast.LENGTH_LONG);
                            customErrorToast();
                        } else {
                            clearText();
                            Toast.makeText(getApplicationContext(), "Date salvate", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call2, Throwable t) {
                        toast = Toast.makeText(getApplicationContext(), "Datele nu au fost modificate!", Toast.LENGTH_LONG);
                        customErrorToast();
                        clearText();

                    }
                });
            }

            @Override
            public void onFailure(Call<String> call2, Throwable t) {
                toast = Toast.makeText(getApplicationContext(), "Persoana nu a fost gasita!", Toast.LENGTH_LONG);
                customErrorToast();
            }
        });
    }


    private Boolean ValidateNewPlateRegister(HashMap<String, String> updatePlate) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]+$");
        boolean matcher = pattern.matcher(Objects.requireNonNull(newPlateRegister.getText().toString())).matches();
        if (!matcher) {
            updatePlate.put("plateRegister", plateRegister.getText().toString());
            toast = Toast.makeText(getApplicationContext(), "Format gresit!\n                  " +
                    "                                    Reintroduceti noul numar!", Toast.LENGTH_LONG);
            customErrorToast();
        } else {
            updatePlate.put("plateRegister", newPlateRegister.getText().toString());
        }
        return matcher;
    }

    private void clearText() {
        name.setText("");
        plateRegister.setText("");
        newPlateRegister.setText("");
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