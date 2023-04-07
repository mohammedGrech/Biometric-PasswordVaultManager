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

public class PrivacyPolicy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Back button
        toolbar.setNavigationIcon(getDrawable(R.drawable.arrow_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PrivacyPolicy.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        // Change the title of the activity on the toolbar
        getSupportActionBar().setTitle("Privacy Policy");

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


    //Context Menu to appear in the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return true;
    }

    // Functionality of Menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                Intent intent = new Intent(this,MainActivity.class);
                this.startActivity(intent);
                finish();
                System.exit(0);
                return true;
            case R.id.help:
                Intent intent2 = new Intent(this,HelpActivity.class);
                this.startActivity(intent2);

                Toast.makeText(this, "this is help", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}