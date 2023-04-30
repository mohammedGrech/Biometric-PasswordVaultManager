package com.example.secure;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Back button
        toolbar.setNavigationIcon(getDrawable(R.drawable.arrow_back));

        // Forcing to let the system know that contents of menu have changed, and menu should be redrawn.
        invalidateOptionsMenu();

        // Handle back button in the toolbar
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HelpActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });

        // Change the title of the activity on the toolbar
        getSupportActionBar().setTitle("Help");
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
                Toast.makeText(this, "This is help page.", Toast.LENGTH_SHORT).show();
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
}