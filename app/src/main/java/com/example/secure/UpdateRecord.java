package com.example.secure;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

public class UpdateRecord extends AppCompatActivity {

    Biometric biometric;
    private static Button updateRecordButton;
    EditText updateRecordName, updateRecordEmail, updateRecordPassword, updateRecordWebLink, updateRecordNote;
    private static ScrollView scrollView;
    String name, email, password, url, note, webLogoText;
    Integer id;
    ImageView logo;
    Spinner spinner;
    private static TextView updateTitle;

    public int success = 0;

    //Password regex
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_record);

        // Forcing to let the system know that contents of menu have changed, and menu should be redrawn.
        invalidateOptionsMenu();

        Integer success;

        biometric = new Biometric();
        biometric.biometricPromptAccessData(UpdateRecord.this);

        // Find button/inputs by ID
        scrollView = findViewById(R.id.updateScrollView);
        updateRecordButton = findViewById(R.id.updateRecord);
        updateRecordName = findViewById(R.id.update_input_name);
        updateRecordEmail = findViewById(R.id.update_input_email);
        updateRecordPassword = findViewById(R.id.update_input_password);
        updateRecordWebLink = findViewById(R.id.update_input_webLink);
        updateRecordNote = findViewById(R.id.update_input_Note);
        logo = findViewById(R.id.update_imageLogo);
        updateTitle = findViewById(R.id.updateTitle);

        //disable layouts by default (they will be enabled after biometric authentication succeed)
        scrollView.setVisibility(View.INVISIBLE);
        updateRecordButton.setVisibility(View.INVISIBLE);
        updateTitle.setText("Authentication Required");

        //Spinner for logos
        spinner = (Spinner) findViewById(R.id.spinner_icon);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.logoList, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String logoText = parent.getItemAtPosition(position).toString();

                // get image logo by ID
                ImageView logo = findViewById(R.id.update_imageLogo);

                //Display logo
                displayLogo(logo, logoText);
//                Toast.makeText(getApplicationContext(), logoText + " 1", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //First call below function
        getAndSetIntentData(adapter);

        updateRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Database database = new Database(UpdateRecord.this);
                //Update the strings based on the new values
                name = updateRecordName.getText().toString();
                email = updateRecordEmail.getText().toString();
                password = updateRecordPassword.getText().toString();
                url = updateRecordWebLink.getText().toString();
                note = updateRecordNote.getText().toString();

                webLogoText = spinner.getSelectedItem().toString();

                if (!validateName() | !validateEmail() | !validatePassword() | !validateWebURL()){
                    return;
                }

                // then update data
                database.updateData(id, name, email, password, url, note, webLogoText);
                Toast.makeText(UpdateRecord.this, "Record updated", Toast.LENGTH_SHORT).show();

            }
        });

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change the title of the activity on the toolbar
        getSupportActionBar().setTitle("Update Record");

        //Back button
        toolbar.setNavigationIcon(getDrawable(R.drawable.arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateRecord.this,HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void result(boolean authenticate){
        if (authenticate == true){
            updateTitle.setText("Update Record");
            scrollView.setVisibility(View.VISIBLE);
            updateRecordButton.setVisibility(View.VISIBLE);
            System.out.println("It is bloody working!");
        }else {
            System.out.println("Nope!");
        }
    }

    //Validate Name
    private boolean validateName() {
        String nameInput = updateRecordName.getText().toString().trim();

        if (nameInput.isEmpty()) {
            updateRecordName.setError("Field cannot be empty");
            return false;
        } else {
            updateRecordName.setError(null);
            return true;
        }
    }

    //Validate Email
    private boolean validateEmail() {
        String emailInput = updateRecordEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            updateRecordEmail.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {

            updateRecordEmail.setError("Email is not valid");
            return false;
        } else {
            updateRecordEmail.setError(null);
            return true;
        }
    }

    //Validate Password
    private boolean validatePassword() {
        String passwordInput = updateRecordPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            updateRecordPassword.setError("Field cannot be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            updateRecordPassword.setError("Password too weak");
            return false;
        } else {
            updateRecordPassword.setError(null);
            return true;
        }
    }

    //Validate URL
    private boolean validateWebURL() {
        String webUrlInput = updateRecordWebLink.getText().toString().trim();

        if (webUrlInput.isEmpty()) {
            updateRecordWebLink.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.WEB_URL.matcher(webUrlInput).matches()) {
            updateRecordWebLink.setError("Web link is not valid");
            return false;
        } else {
            updateRecordWebLink.setError(null);
            return true;
        }
    }

    // get and set data from adapter class
    void getAndSetIntentData(ArrayAdapter<CharSequence> adapter) {
        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") & getIntent().hasExtra("email") && getIntent().hasExtra("password")
                && getIntent().hasExtra("url") && getIntent().hasExtra("note") && getIntent().hasExtra("webLogo")) {

            // get data from intent
            id = getIntent().getIntExtra("id", 0);
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            password = getIntent().getStringExtra("password");
            url = getIntent().getStringExtra("url");
            note = getIntent().getStringExtra("note");
            webLogoText = getIntent().getStringExtra("webLogo");

            // setting  intent data
            updateRecordName.setText(name);
            updateRecordEmail.setText(email);
            updateRecordPassword.setText(password);
            updateRecordWebLink.setText(url);
            updateRecordNote.setText(note);

            Integer spinnerPosition = adapter.getPosition(webLogoText);
            spinner.setSelection(spinnerPosition);

            //Display logo
            displayLogo(logo, webLogoText);

        } else{
            Toast.makeText(this, "No Data.", Toast.LENGTH_SHORT).show();
        }
    }

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

    //Display logo next to the spinner
    private void displayLogo(ImageView logo, String logoText) {

        // Set logo on the row
        switch (logoText) {
            case "Facebook":
                logo.setBackgroundResource(R.drawable.facebook);
                break;
            case "WhatsApp":
                logo.setBackgroundResource(R.drawable.whatsapp);
                break;
            case "Twitter":
                logo.setBackgroundResource(R.drawable.twitter);
                break;
            case "Pinterest":
                logo.setBackgroundResource(R.drawable.pinterest);
                break;
            case "Youtube":
                logo.setBackgroundResource(R.drawable.youtube);
                break;
            case "Linkedin":
                logo.setBackgroundResource(R.drawable.linkedin);
                break;
            case "Github":
                logo.setBackgroundResource(R.drawable.github);
                break;
            case "Gmail":
                logo.setBackgroundResource(R.drawable.gmail);
                break;
            case "Instagram":
                logo.setBackgroundResource(R.drawable.instagram);
                break;
            case "Outlook":
                logo.setBackgroundResource(R.drawable.outlook);
                break;
            case "Netflix":
                logo.setBackgroundResource(R.drawable.netflix);
                break;
            case "Viber":
                logo.setBackgroundResource(R.drawable.viber);
                break;
            case "Reddit":
                logo.setBackgroundResource(R.drawable.reddit);
                break;
            case "Amazon":
                logo.setBackgroundResource(R.drawable.amazon);;
                break;
            case "Spotify":
                logo.setBackgroundResource(R.drawable.spotify);
                break;
            case "Amazon Prime":
                logo.setBackgroundResource(R.drawable.amazon_prime);
                break;
            default:
                logo.setBackgroundResource(R.drawable.circle_photo);
                break;
        }
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
                // Back to main page - user needs to login using biometric authentication
                Intent intentMain = new Intent(this, MainActivity.class);
                this.startActivity(intentMain);
                finish();
                System.exit(0);
                return true;
            case R.id.filter:
                // Do not show anything - Filter is only available in the home page
                return true;
            case R.id.help:
                // Open Help page
                Intent intentHelp = new Intent(this,HelpActivity.class);
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

    // Method to display fingerprint authentication error
    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateRecord.this);
        builder.setTitle("Fingerprint Diary");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Your device doesn't support fingerprint feature").setCancelable(false)
                .setPositiveButton("Exist", (dialog, id) -> finish());

        AlertDialog alert = builder.create();
        alert.show();
    }
}