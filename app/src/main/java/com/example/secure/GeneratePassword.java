package com.example.secure;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class GeneratePassword extends AppCompatActivity {

    Button generatePassword;
    CheckBox upperCase, lowerCase, number, symbols;
    Integer passwordRange = 4;
    TextView stringPasswordSize;
    SeekBar seekBar;

    String passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_password);

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // finding elements by ID
        upperCase = findViewById(R.id.upperCase);
        lowerCase = findViewById(R.id.lowerCase);
        number = findViewById(R.id.number);
        symbols = findViewById(R.id.specialCharacter);
        generatePassword = findViewById(R.id.generatePassword);
        stringPasswordSize = (TextView) findViewById(R.id.progress);
        seekBar = findViewById(R.id.seekBar);

        //Back button
        toolbar.setNavigationIcon(getDrawable(R.drawable.arrow_back));

        // Forcing to let the system know that contents of menu have changed, and menu should be redrawn.
        invalidateOptionsMenu();

        // Handle back button in the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeneratePassword.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        // Change the title of the activity on the toolbar
        getSupportActionBar().setTitle("Generate Password");

        // Set the minimum seekbar to 4 and maximum to 50
        if (seekBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                seekBar.setMin(4);
            }
            seekBar.setMax(50);
        }

        // Seekbar to display and get the range of password length
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // This function handles any changes when the user modify the range.
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stringPasswordSize.setText("" + progress);
                passwordRange = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Generate password
        generatePassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //  Ensure one of the below conditions are checked
                if(!upperCase.isChecked() && !lowerCase.isChecked() && !number.isChecked() && !symbols.isChecked()){
                    Toast.makeText(GeneratePassword.this, "At least one of the above checkboxes must be ticked.", Toast.LENGTH_SHORT).show();
                }
                else {
                    //generate password and pass it to a string
                    passwordString = generateRandomPassword(passwordRange, upperCase.isChecked(), lowerCase.isChecked(), number.isChecked(),symbols.isChecked());
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Generated password")
                            .setCancelable(false)
                            .setMessage(passwordString)
                            //Copy password to clipboard
                            .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("label", passwordString);
                                    clipboard.setPrimaryClip(clip);
                                    Toast.makeText(GeneratePassword.this, "Password copied.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Dismiss", null)
                            .show();
                }
            }
        });
    }

    // Handle back button
    @Override
    public void onBackPressed() {
        Intent intentMain = new Intent(this, HomeActivity.class);
        this.startActivity(intentMain);
    }

    // Destroy the application when the user exit
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intentMain = new Intent(this, MainActivity.class);
        this.startActivity(intentMain);
        finish();
        System.exit(0);
    }

    // Reloading page to call onDestroy() method
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    //Context Menu to appear in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);

        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
        return true;
    }

    // Functionality of Menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                // Back to main page - user needs to login using biometric authentication.
                Intent intentMain = new Intent(this, MainActivity.class);
                this.startActivity(intentMain);
                finish();
                System.exit(0);
                return true;
            case R.id.filter:
                // Do not show anything - Filter is only available in the home page
                return true;
            case R.id.help:
                Intent intentHelp= new Intent(this,HelpActivity.class);
                this.startActivity(intentHelp);
                return true;
            case R.id.backup:
                Toast.makeText(this, "Please proceed with back up in the home page.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.importData:
                Toast.makeText(this, "Please proceed with the import in the home page.", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.privacyPolicy:
                // Open Privacy Policy
                Intent intentPrivacy = new Intent(this,PrivacyPolicy.class);
                this.startActivity(intentPrivacy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // function to generate random password and return a string
    private static String generateRandomPassword(int max_length, boolean upperCase, boolean lowerCase, boolean numbers, boolean specialCharacters)
    {
        // range of characters to consider when randomising the password
        String upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseChars = "abcdefghijklmnopqrstuvwxyz";
        String numberChars = "0123456789";
        String specialChars = "!@#$%^&*()_-+=<>?/{}~|";
        String allowedChars = "";

        Random rn = new Random();
        StringBuilder sb = new StringBuilder(max_length);

        //this will fulfill the requirements of at least one character of a type.
        if(upperCase) {
            allowedChars += upperCaseChars;
            sb.append(upperCaseChars.charAt(rn.nextInt(upperCaseChars.length()-1)));
        }

        if(lowerCase) {
            allowedChars += lowerCaseChars;
            sb.append(lowerCaseChars.charAt(rn.nextInt(lowerCaseChars.length()-1)));
        }

        if(numbers) {
            allowedChars += numberChars;
            sb.append(numberChars.charAt(rn.nextInt(numberChars.length()-1)));
        }

        if(specialCharacters) {
            allowedChars += specialChars;
            sb.append(specialChars.charAt(rn.nextInt(specialChars.length()-1)));
        }

        //fill the allowed length from different chars now.
        for(int i=sb.length();i < max_length;++i){
            sb.append(allowedChars.charAt(rn.nextInt(allowedChars.length())));
        }
        Log.d(TAG,"generateRandomPassword: "+sb.toString());

        return sb.toString();
    }
}