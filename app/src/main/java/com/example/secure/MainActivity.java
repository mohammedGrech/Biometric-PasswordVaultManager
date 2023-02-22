package com.example.secure;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Biometric biometric;
    Button login_btn;
    public BiometricPrompt biometricPrompt;
    public BiometricPrompt.PromptInfo promptInfo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display a one time message when the application is installed
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
            showStartDialog();
        };

        login_btn = findViewById(R.id.login_btn);
        // Use login button to authenticate
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometric.biometricPrompt(MainActivity.this);
            }
        });

        biometric = new Biometric();
        if(biometric.checkCompatability(this) == true){

        }else {
            alertDialog();
        }
    }

    private void showStartDialog() {
        new AlertDialog.Builder(this)
                .setTitle("One Time Dialog")
                .setMessage("If you wish to use fingerprint authentication on Secure app, please enable biometric on \nSetting > Security > Fingerprint")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();

        // Once above message is displayed, disable the message by changing value of the preferences editor to false
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Fingerprint Diary");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Your device doesn't support fingerprint feature").setCancelable(false)
                .setPositiveButton("Exist", (dialog, id) -> finish());

        AlertDialog alert = builder.create();
        alert.show();
    }
}




