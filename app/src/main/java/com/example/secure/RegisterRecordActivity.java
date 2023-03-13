package com.example.secure;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
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


public class RegisterRecordActivity extends AppCompatActivity {

    // References to buttons and other controls on the layout
    Button addRecordButton;
    EditText recordName, recordusername, recordPassword, recordWebLink, recordNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_record);

        // Find button and layouts by ID
        recordName = findViewById(R.id.text_input_name);
        recordusername = findViewById(R.id.text_input_username);
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
                String text = parent.getItemAtPosition(position).toString();
                // get image logo by ID

                ImageView logo = findViewById(R.id.imageLogo);

                if (text.equals("facebook")){
                    logo.setBackgroundResource(R.drawable.facebook);
                } else {
                    logo.setBackgroundResource(R.drawable.circle_photo);
                }
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add records
        addRecordButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebsiteModel websiteModel;
                try {

                    //Feeding the full constructor(websiteModel) with user's input
                    websiteModel = new WebsiteModel(-1, recordName.getText().toString(), recordWebLink.getText().toString(),
                            recordusername.getText().toString(), recordPassword.getText().toString(), recordNote.getText().toString());
                    Toast.makeText(RegisterRecordActivity.this, websiteModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(RegisterRecordActivity.this, "Error creating this record", Toast.LENGTH_SHORT).show();
                    websiteModel = new WebsiteModel(-1, "error", "error", "error", "error", "error");
                }
                //Create database(if not created)
                Database database = new Database(RegisterRecordActivity.this);
                // add records from websiteModel to the database
                boolean success = database.addRecord(websiteModel);

                // Just a check to see if the record has been added, can be removed after testing
                Toast.makeText(RegisterRecordActivity.this,"Record Added= " + success,Toast.LENGTH_SHORT).show();
            }
        }));
    }

    //Context Menu to appear in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    // Functionality of the menu buttons
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                Intent intentMain = new Intent(this,MainActivity.class);
                this.startActivity(intentMain);
                finish();
                System.exit(0);
                return true;
            case R.id.help:
                Intent intentHelp = new Intent(this,HelpActivity.class);
                this.startActivity(intentHelp);

                Toast.makeText(this, "this is help", Toast.LENGTH_SHORT).show();;
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}