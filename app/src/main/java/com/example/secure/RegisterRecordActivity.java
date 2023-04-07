package com.example.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.regex.Pattern;


public class RegisterRecordActivity extends AppCompatActivity {

    //password regex
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

    // References to buttons and other controls on the layout
    Button addRecordButton;
    EditText recordName, recordEmail, recordPassword, recordWebLink, recordNote;
    String recordLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_record);

        // Find button and layouts by ID
        recordName = findViewById(R.id.text_input_name);
        recordEmail = findViewById(R.id.text_input_email);
        recordWebLink = findViewById(R.id.text_input_webLink);
        recordPassword = findViewById(R.id.text_input_password);
        recordNote = findViewById(R.id.text_input_Note);
        addRecordButton = findViewById(R.id.addRecord);

        //Spinner for logos
        Spinner spinner = (Spinner) findViewById(R.id.spinner_icon);
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
                ImageView logo = findViewById(R.id.imageLogo);
                //Display logo
                displayLogo(logo, logoText);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Back button
        toolbar.setNavigationIcon(getDrawable(R.drawable.arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterRecordActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        //Add records
        addRecordButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebsiteModel websiteModel;
                try {
                    recordLogo = spinner.getSelectedItem().toString();
                    if (!validateName() | !validateEmail() | !validatePassword() | !validateWebURL()){
                        return;
                    }
                    //Feeding the full constructor(websiteModel) with user's input
                    websiteModel = new WebsiteModel(-1, recordName.getText().toString(), recordWebLink.getText().toString(),
                            recordEmail.getText().toString(), recordPassword.getText().toString(), recordNote.getText().toString(), recordLogo);
                } catch (Exception e) {
                    Toast.makeText(RegisterRecordActivity.this, "Error creating this record", Toast.LENGTH_SHORT).show();
                    websiteModel = new WebsiteModel(-1, "error", "error", "error", "error", "error", "error");
                }
//                Toast.makeText(RegisterRecordActivity.this, "" +websiteModel.toString(), Toast.LENGTH_SHORT).show();
                //Create database(if not created)
                Database database = new Database(RegisterRecordActivity.this);
                // add records from websiteModel to the database
                boolean success = database.addRecord(websiteModel);
            }
        }));
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

    //Validate Name
    private boolean validateName() {
        String nameInput = recordName.getText().toString().toString();

        if (nameInput.isEmpty()) {
            recordName.setError("Field cannot be empty");
            return false;
        } else {
            recordName.setError(null);
            return true;
        }
    }

    //Validate Email
    private boolean validateEmail() {
        String emailInput = recordEmail.getText().toString().trim();

        if (emailInput.isEmpty()) {
            recordEmail.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {

            recordEmail.setError("Email is not valid");
            return false;
        } else {
            recordName.setError(null);
            return true;
        }
    }

    //Validate Password
    private boolean validatePassword() {
        String passwordInput = recordPassword.getText().toString().trim();

        if (passwordInput.isEmpty()) {
            recordPassword.setError("Field cannot be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            recordPassword.setError("Password too weak");
            return false;
        } else {
            recordPassword.setError(null);
            return true;
        }
    }

    //Validate URL
    private boolean validateWebURL() {
        String webUrlInput = recordWebLink.getText().toString().trim();

        if (webUrlInput.isEmpty()) {
            recordWebLink.setError("Field cannot be empty");
            return false;
        } else if (!Patterns.WEB_URL.matcher(webUrlInput).matches()) {
            recordWebLink.setError("Web link is not valid");
            return false;
        } else {
            recordWebLink.setError(null);
            return true;
        }
    }

    //Context Menu to appear in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    // Functionality of the menu buttons
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                Intent intentMain = new Intent(this, MainActivity.class);
                this.startActivity(intentMain);
                finish();
                System.exit(0);
                return true;
            case R.id.help:
                Intent intentHelp = new Intent(this, HelpActivity.class);
                this.startActivity(intentHelp);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}