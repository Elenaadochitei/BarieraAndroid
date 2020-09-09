package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.KeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyAccount extends AppCompatActivity {

    private TextView plateRegister;
    private TextView userName;
    private EditText editMyAccount;
    private Button saveButtonForEdit;
    public static final String SHARED_PREFS1 = "sharedPrefs";
    public static final String TEXT1 = "text";
    private String text1;
    KeyListener keyListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_account);


        EditText simpleEditText = findViewById(R.id.simpleEditText);

        //make edit text non editable
        simpleEditText.setEnabled(false);


        Button editButton = findViewById(R.id.editButtom);

        keyListener =
                simpleEditText.getKeyListener();
        editButton.setOnClickListener(v -> makeEditable(simpleEditText));

    }


    private void makeEditable(EditText editText) {
        boolean enabled = !editText.isEnabled();
        editText.setEnabled(enabled);
        if (enabled) {
            editText.setKeyListener(keyListener);
        } else {
            editText.setKeyListener(null);
        }
    }
}
