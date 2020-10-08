package com.example.myapplication;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.time.LocalDateTime;
import java.util.HashSet;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.example.myapplication.LogInPerson.ID;

public class AddNewPerson extends AppCompatActivity {

    private TextView plateRegister;
    private TextView userName;
    private Button saveButton;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "add_person_register_plate";
    private ConectWithJava conectWithJava;
    private ImageView selectedImage;
    private Bitmap currentImage;
    private static final int PERMISSION_REQUEST_CODE = 200;
    TextView defaultText;
    AlertDialog alertDialog1;
    CharSequence[] values = {" 1 ora ", " 8 ore ", " 1 zi ", " 5 zile "};
    CheckBox checkBox;
    LocalDateTime expirationDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_new_person);

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

        defaultText = findViewById(R.id.defaulText);
        checkBox = (CheckBox) findViewById(R.id.checkBoxGuest);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(true);
                    createAlertDialogWithRadioButtonGroup();
                } else {
                    checkBox.setChecked(false);
                    defaultText.setText("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!checkPermission()) {
            System.out.println("Fail");
        } else {
            if (checkPermission()) {
                requestPermissionAndContinue();
            }
        }
        if (resultCode == RESULT_OK) {
            Uri photoUri = data.getData();
            if (photoUri != null) {
                try {
                    currentImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    selectedImage.setImageBitmap(currentImage);
                    uploadFile(photoUri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadFile(Uri photoUri) {
        File file = new File(getRealPathFromURI(photoUri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        NameAndPlateRegister insertNewUser = new NameAndPlateRegister();
        insertNewUser.setPlateRegister(plateRegister.getText().toString());
        insertNewUser.setName(userName.getText().toString());
        SharedPreferences sharedPreferences = getSharedPreferences(ID, MODE_PRIVATE);

        String id = sharedPreferences.getString(ID,null);
        insertNewUser.setUserID(id);
        Call<String> call = getResponse.uploadFile(fileToUpload, insertNewUser);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                ServerResponse serverResponse = new ServerResponse();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("Fail");
            }
        });
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
            insertNameAndPlateRegister();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        HashSet<String> nameAndPlateRegister = new HashSet<>();
        nameAndPlateRegister.add(plateRegister.getText().toString());
        nameAndPlateRegister.add(userName.getText().toString());

        editor.putStringSet(TEXT, nameAndPlateRegister);
        editor.apply();
        Toast.makeText(this, "Date salvate", Toast.LENGTH_SHORT).show();
    }


    private void insertNameAndPlateRegister() {
        NameAndPlateRegister insertNewUser = new NameAndPlateRegister();
        insertNewUser.setPlateRegister(plateRegister.getText().toString());
        insertNewUser.setName(userName.getText().toString());
        insertNewUser.setExpirationDate(expirationDate);
        SharedPreferences sharedPreferences=getSharedPreferences(ID, MODE_PRIVATE);
        String id = sharedPreferences.getString(ID, null);
        insertNewUser.setUserID(id);
        System.out.println(insertNewUser.getUserID());
        Call<NameAndPlateRegister> call = conectWithJava.insertNewUser(insertNewUser);

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


    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissionAndContinue() {
        if (ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, WRITE_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.setCancelable(true);
                alertBuilder.setTitle("STRING 1");
                alertBuilder.setMessage("Read permission is neccessary");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(AddNewPerson.this, new String[]{WRITE_EXTERNAL_STORAGE
                                , READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
                Log.e("", "Permission denied, show dialog");
            } else {
                ActivityCompat.requestPermissions(AddNewPerson.this, new String[]{WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (permissions.length > 0 && grantResults.length > 0) {
                boolean flag = true;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        flag = false;
                    }
                }
            } else {
                finish();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void createAlertDialogWithRadioButtonGroup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Selectati perioada de sedere:");

        builder.setNeutralButton("Anulare", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkBox.setChecked(false);
                dialog.cancel();
            }
        });


        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        defaultText.setText("Perioada de sedere este de 1 ora");
                        expirationDate = LocalDateTime.now().plusHours(1);
                        Toast.makeText(AddNewPerson.this, "1 ora", Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        defaultText.setText("Perioada de sedere este de 8 ore");
                        expirationDate = LocalDateTime.now().plusHours(8);
                        Toast.makeText(AddNewPerson.this, "8 ore", Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        defaultText.setText("Perioada de sedere este de 1 zi");
                        expirationDate = LocalDateTime.now().plusDays(1);
                        Toast.makeText(AddNewPerson.this, "1 zi", Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        defaultText.setText("Perioada de sedere este de 5 zile");
                        expirationDate = LocalDateTime.now().plusDays(5);
                        Toast.makeText(AddNewPerson.this, "5 zile", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();
    }
}

