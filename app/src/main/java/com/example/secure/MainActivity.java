package com.example.secure;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    // Importing biometric class
    Biometric biometric;
    Button login_btn;
    public BiometricPrompt biometricPrompt;
    public BiometricPrompt.PromptInfo promptInfo;
    private boolean isBackPressedOnce = false;

    // Below line denotes that the annotated element should only be called on the given API level or higher
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Display a one time message when the application is installed
        // SharedPreferences used for accessing and modifying preference data returned by Context.getSharedPreferences.
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        //In other words, if the application has never called firstStart, then call the dialog box
        if (firstStart) {
            showStartDialog();
        };

        //Get login button by ID
        login_btn = findViewById(R.id.login_btn);
        // Use login button to authenticate
        login_btn.setOnClickListener(new View.OnClickListener() {
            // Call biometricPrompt function from Biometric class to display the authentication popup
            @Override
            public void onClick(View v) {

                biometric.biometricPrompt(MainActivity.this);
            }
        });

        //If biometric is not compatible with the phone, display a dialog box
        biometric = new Biometric();
        if(!biometric.checkCompatability(this) == true){
            alertDialog();
        }

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //Method to display the one time message
    private void showStartDialog() {
        //Create dialog box
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

    // Method to display fingerprint authentication error
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Fingerprint Diary");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Your device doesn't support fingerprint feature").setCancelable(false)
                .setPositiveButton("Exist", (dialog, id) -> finish());

        AlertDialog alert = builder.create();
        alert.show();
    }

    // Handle back button to exit the application upon pressing it twice
    @Override
    public void onBackPressed() {
        //check if back button was pressed and exit the app upon the second press
        if(isBackPressedOnce){
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            return;
        }
        // back button pressed
        isBackPressedOnce = true;

        // Back-button-pressed will reset after 2 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressedOnce = false;
            }
        }, 2000);
        Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show();
    }
}