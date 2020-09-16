package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private ConectWithJava conectWithJava;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        Toast.makeText(getApplicationContext(), "Bine ai venit!", Toast.LENGTH_SHORT).show();

        ImageButton add = (ImageButton) findViewById(R.id.add);
        ImageButton cancel = (ImageButton) findViewById(R.id.cancel);
        ImageButton admin = (ImageButton) findViewById(R.id.admin);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity3();
            }
        });
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity4();
            }
        });
    }
    public void openActivity2() {
        Intent intent = new Intent(this, AddNewPerson.class);
        startActivity(intent);
    }
    public void openActivity3() {
        Intent intent = new Intent(this, CancelPerson.class);
        startActivity(intent);
    }
    public void openActivity4() {
        Intent intent = new Intent(this, MyAccount.class);
        startActivity(intent);
    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {

        Toast.makeText(this, "Press AGAIN to EXIT", Toast.LENGTH_SHORT).show();
//        this.doubleBackToExitPressedOnce = true;
        if (doubleBackToExitPressedOnce) {
            //   super.onBackPressed();
            return;
        }

       // finish();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
   }
}
