package com.example.secure;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.secure.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "Website vault manager";
    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;
    private FloatingActionButton showSheet;
    private BottomSheetDialog bottomSheetDialog;
    private ImageView closeApp;

    private HorizontalScrollView scrollView;

    private RecyclerView recyclerView;
    private RecycleViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    EditText editText;
    String test = "";

    Button noFilter, youtube, facebook, whatsApp, pinterest, twitter, linkedin, github, gmail, instagram, spotify, outlook, netflix, reddit, amazon, amazon_prime;


    List<WebsiteModel> websiteList = new ArrayList<WebsiteModel>();

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change the title of the activity on the toolbar
        getSupportActionBar().setTitle("Home");



        //Bottom Sheet set up
        showSheet = findViewById(R.id.button_open_bottom_sheet);
        //manage bottom sheet dialog box
        showSheet.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(HomeActivity.this,R.style.BottomSheetTheme);

                //Display bottom sheet by getting ID
                View sheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bottom_sheet_layout,
                        (ViewGroup) findViewById(R.id.bottom_sheet));

                // close the sheet when pressing return icon
                sheetView.findViewById(R.id.closeSheet).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                // handle generating password (Work to do)
                sheetView.findViewById(R.id.generatePassword).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(HomeActivity.this, "Generate link selected", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });
                // handle creating record (Work to do)
                sheetView.findViewById(R.id.newRecord).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RegisterRecordActivity.class);
                        startActivity(intent);
                        Toast.makeText(HomeActivity.this, "Create record selected", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.dismiss();
                    }
                });
                // Show the sheet
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });


//        //Code to show all the website records
        Database database = new Database(HomeActivity.this);
        List<WebsiteModel> websiteList = database.getAllWebsites();

        // ArrayAdapter is needed to show the array in list view
        Log.d(TAG, "onCreate: "+websiteList.toString());

        recyclerView = findViewById(R.id.recyclerViewWebsites);

        //Use this setting to improve performance if yu know that changed
        // in content do not change the layout sizer of the recyclerView
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new RecycleViewAdapter(websiteList, HomeActivity.this);
        recyclerView.setAdapter(mAdapter);


        scrollView = findViewById(R.id.filter_hsv);
        noFilter = findViewById(R.id.filter_all);

        facebook = findViewById(R.id.filter_facebook);
        netflix = findViewById(R.id.filter_netflix);
        amazon = findViewById(R.id.filter_amazon);
        github = findViewById(R.id.filter_github);
        reddit = findViewById(R.id.filter_reddit);
        amazon_prime = findViewById(R.id.filter_amazon_prime);
        youtube = findViewById(R.id.filter_youtube);
        whatsApp = findViewById(R.id.filter_whatsApp);
        pinterest = findViewById(R.id.filter_pinterest);
        twitter = findViewById(R.id.filter_twitter);
        linkedin = findViewById(R.id.filter_linkedin);
        gmail = findViewById(R.id.filter_gmail);
        instagram = findViewById(R.id.filter_instagram);
        spotify = findViewById(R.id.filter_spotify);
        outlook = findViewById(R.id.filter_outlook);

        noFilter.setOnClickListener(v -> {
            mAdapter.getFilter().filter("");
            scrollView.setVisibility(View.GONE);
        });

        facebook.setOnClickListener(v -> {
            String facebookStr = (String) facebook.getText();
            mAdapter.getFilter().filter(facebookStr);
        });

        netflix.setOnClickListener(v -> {
            String netflixStr = (String) netflix.getText();
            mAdapter.getFilter().filter(netflixStr);
        });

        amazon.setOnClickListener(v -> {
            String amazonStr = (String) amazon.getText();
            mAdapter.getFilter().filter(amazonStr);
        });

        github.setOnClickListener(v -> {
            String githubStr = (String) github.getText();
            mAdapter.getFilter().filter(githubStr);
        });

        reddit.setOnClickListener(v -> {
            String redditStr = (String) reddit.getText();
            mAdapter.getFilter().filter(redditStr);
        });

        amazon_prime.setOnClickListener(v -> {
            String amazon_primeStr = (String) amazon_prime.getText();
            mAdapter.getFilter().filter(amazon_primeStr);
        });

        youtube.setOnClickListener(v -> {
            String youtubeStr = (String) youtube.getText();
            mAdapter.getFilter().filter(youtubeStr);
        });

        whatsApp.setOnClickListener(v -> {
            String whatsAppStr = (String) whatsApp.getText();
            mAdapter.getFilter().filter(whatsAppStr);
        });

        pinterest.setOnClickListener(v -> {
            String pinterestStr = (String) pinterest.getText();
            mAdapter.getFilter().filter(pinterestStr);
        });

        twitter.setOnClickListener(v -> {
            String twitterStr = (String) twitter.getText();
            mAdapter.getFilter().filter(twitterStr);
        });

        linkedin.setOnClickListener(v -> {
            String linkedinStr = (String) linkedin.getText();
            mAdapter.getFilter().filter(linkedinStr);
        });

        gmail.setOnClickListener(v -> {
            String gmailStr = (String) gmail.getText();
            mAdapter.getFilter().filter(gmailStr);
        });

        instagram.setOnClickListener(v -> {
            String instagramStr = (String) instagram.getText();
            mAdapter.getFilter().filter(instagramStr);
        });

        spotify.setOnClickListener(v -> {
            String spotifyStr = (String) spotify.getText();
            mAdapter.getFilter().filter(spotifyStr);
        });

        outlook.setOnClickListener(v -> {
            String outlookStr = (String) outlook.getText();
            mAdapter.getFilter().filter(outlookStr);
        });
    }

    // Handle back button
    @Override
    public void onBackPressed() {
        Intent intentMain = new Intent(this, MainActivity.class);
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

    // Reloading page to update data
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

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
        @Override
        public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
            return true;
        }

        @Override
        public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
            return true;
        }
    };
        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
                SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
                searchView.setQueryHint("Search");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        mAdapter.getFilter().filter(newText);
                        return false;
                    }
                });

        return true;
    }

    // Functionality of Menu buttons
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.exit:
                Intent intentMain = new Intent(this, MainActivity.class);
                this.startActivity(intentMain);
                finish();
                System.exit(0);
                return true;
            case R.id.filter:
                scrollView.setVisibility(View.VISIBLE);
                return true;
            case R.id.help:
                Intent intent = new Intent(this,HelpActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.privacyPolicy:
                Intent intentPrivacy = new Intent(this,PrivacyPolicy.class);
                this.startActivity(intentPrivacy);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

