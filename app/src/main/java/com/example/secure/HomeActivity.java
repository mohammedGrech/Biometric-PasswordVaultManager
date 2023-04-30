package com.example.secure;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.secure.databinding.ActivityHomeBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    // Calling classes and interfaces
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
    public Button noFilter, youtube, facebook, whatsApp, pinterest, twitter, linkedin, github, gmail, instagram, spotify, outlook, netflix, reddit, amazon, amazon_prime, others;
    Database database;
    Backup backup;
    HomeActivity activity;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Manage external storage by checking SDK version
        if (Build.VERSION.SDK_INT >= 30){
            if (!Environment.isExternalStorageManager()){
                Intent getPermission = new Intent();
                getPermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(getPermission);
            }
        }

        // Instantiate new backup
        backup = new Backup(this);

        // customised toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Change the title of the activity on the toolbar
        getSupportActionBar().setTitle("Home");

        //Bottom Sheet set up
        showSheet = findViewById(R.id.button_open_bottom_sheet);

        // Manage bottom sheet dialog box
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
                // handle generating password
                sheetView.findViewById(R.id.generatePassword).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), GeneratePassword.class);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                // handle creating record
                sheetView.findViewById(R.id.newRecord).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), RegisterRecordActivity.class);
                        startActivity(intent);
                        bottomSheetDialog.dismiss();
                    }
                });
                // Show the sheet
                bottomSheetDialog.setContentView(sheetView);
                bottomSheetDialog.show();
            }
        });

       // Show all the website records
        database = new Database(HomeActivity.this);
        List<WebsiteModel> websiteList = database.getAllWebsites();

        recyclerView = findViewById(R.id.recyclerViewWebsites);
        recyclerView.setHasFixedSize(true);

        //use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter
        mAdapter = new RecycleViewAdapter(websiteList, HomeActivity.this);
        recyclerView.setAdapter(mAdapter);

        //Following elements are related to website filtering
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
        others = findViewById(R.id.filter_others);

        // This function remove all filters
        noFilter.setOnClickListener(v -> {
            mAdapter.getFilter().filter("");
            scrollView.setVisibility(View.GONE);
            resetBackground();
        });

        //This function filter by any credentials that has no logo
        others.setOnClickListener(v -> {
            mAdapter.getSearch().filter("logo");
            resetBackground();
            changeBackground(others);
        });

        //The below functions filter by the name of websites
        facebook.setOnClickListener(v -> {
            String facebookStr = (String) facebook.getText();
            mAdapter.getFilter().filter(facebookStr);
            resetBackground();
            changeBackground(facebook);
        });

        // The following functions filter by their respective logo
        //Filter by Netflix
        netflix.setOnClickListener(v -> {
            String netflixStr = (String) netflix.getText();
            mAdapter.getFilter().filter(netflixStr);
            resetBackground();
            changeBackground(netflix);
        });

        //Filter by amazon
        amazon.setOnClickListener(v -> {
            String amazonStr = (String) amazon.getText();
            mAdapter.getFilter().filter(amazonStr);
            resetBackground();
            changeBackground(amazon);
        });

        //Filter by github
        github.setOnClickListener(v -> {
            String githubStr = (String) github.getText();
            mAdapter.getFilter().filter(githubStr);
            resetBackground();
            changeBackground(github);
        });

        //Filter by reddit
        reddit.setOnClickListener(v -> {
            String redditStr = (String) reddit.getText();
            mAdapter.getFilter().filter(redditStr);
            resetBackground();
            changeBackground(reddit);
        });

        //Filter by amazon_prime
        amazon_prime.setOnClickListener(v -> {
            String amazon_primeStr = (String) amazon_prime.getText();
            mAdapter.getFilter().filter(amazon_primeStr);
            resetBackground();
            changeBackground(amazon_prime);
        });

        //Filter by youtube
        youtube.setOnClickListener(v -> {
            String youtubeStr = (String) youtube.getText();
            mAdapter.getFilter().filter(youtubeStr);
            resetBackground();
            changeBackground(youtube);
        });

        //Filter by whatsApp
        whatsApp.setOnClickListener(v -> {
            String whatsAppStr = (String) whatsApp.getText();
            mAdapter.getFilter().filter(whatsAppStr);
            resetBackground();
            changeBackground(whatsApp);
        });

        //Filter by pinterest
        pinterest.setOnClickListener(v -> {
            String pinterestStr = (String) pinterest.getText();
            mAdapter.getFilter().filter(pinterestStr);
            resetBackground();
            changeBackground(pinterest);
        });

        //Filter by twitter
        twitter.setOnClickListener(v -> {
            String twitterStr = (String) twitter.getText();
            mAdapter.getFilter().filter(twitterStr);
            resetBackground();
            changeBackground(twitter);
        });

        //Filter by linkedin
        linkedin.setOnClickListener(v -> {
            String linkedinStr = (String) linkedin.getText();
            mAdapter.getFilter().filter(linkedinStr);
            resetBackground();
            changeBackground(linkedin);
        });

        //Filter by gmail
        gmail.setOnClickListener(v -> {
            String gmailStr = (String) gmail.getText();
            mAdapter.getFilter().filter(gmailStr);
            resetBackground();
            changeBackground(gmail);
        });

        //Filter by instagram
        instagram.setOnClickListener(v -> {
            String instagramStr = (String) instagram.getText();
            mAdapter.getFilter().filter(instagramStr);
            resetBackground();
            changeBackground(instagram);
        });

        //Filter by spotify
        spotify.setOnClickListener(v -> {
            String spotifyStr = (String) spotify.getText();
            mAdapter.getFilter().filter(spotifyStr);
            resetBackground();
            changeBackground(spotify);
        });

        //Filter by outlook
        outlook.setOnClickListener(v -> {
            String outlookStr = (String) outlook.getText();
            mAdapter.getFilter().filter(outlookStr);
            resetBackground();
            changeBackground(outlook);
        });
    }

    // change background when filter button is selected
    public void changeBackground(Button button){
        button.setBackground(getDrawable(R.drawable.filter_button_selected));
        button.setTextColor(Color.WHITE);
    }

    // reset background colour of the filters
    @SuppressLint("UseCompatLoadingForDrawables")
    public void resetBackground(){
        // Set background colour of all filter buttons to their initial colours
        facebook.setBackground(getDrawable(R.drawable.filter_buttons));
        netflix.setBackground(getDrawable(R.drawable.filter_buttons));
        amazon.setBackground(getDrawable(R.drawable.filter_buttons));
        github.setBackground(getDrawable(R.drawable.filter_buttons));
        reddit.setBackground(getDrawable(R.drawable.filter_buttons));
        amazon_prime.setBackground(getDrawable(R.drawable.filter_buttons));
        youtube.setBackground(getDrawable(R.drawable.filter_buttons));
        whatsApp.setBackground(getDrawable(R.drawable.filter_buttons));
        pinterest.setBackground(getDrawable(R.drawable.filter_buttons));
        twitter.setBackground(getDrawable(R.drawable.filter_buttons));
        linkedin.setBackground(getDrawable(R.drawable.filter_buttons));
        gmail.setBackground(getDrawable(R.drawable.filter_buttons));
        instagram.setBackground(getDrawable(R.drawable.filter_buttons));
        spotify.setBackground(getDrawable(R.drawable.filter_buttons));
        outlook.setBackground(getDrawable(R.drawable.filter_buttons));
        others.setBackground(getDrawable(R.drawable.filter_buttons));

        // Set text colour of all filter buttons to black
        facebook.setTextColor(Color.BLACK);
        netflix.setTextColor(Color.BLACK);
        amazon.setTextColor(Color.BLACK);
        github.setTextColor(Color.BLACK);
        reddit.setTextColor(Color.BLACK);
        amazon_prime.setTextColor(Color.BLACK);
        youtube.setTextColor(Color.BLACK);
        whatsApp.setTextColor(Color.BLACK);
        pinterest.setTextColor(Color.BLACK);
        twitter.setTextColor(Color.BLACK);
        linkedin.setTextColor(Color.BLACK);
        gmail.setTextColor(Color.BLACK);
        instagram.setTextColor(Color.BLACK);
        spotify.setTextColor(Color.BLACK);
        outlook.setTextColor(Color.BLACK);
        others.setTextColor(Color.BLACK);
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

        //Function to expand the search bar
        @Override
        public boolean onMenuItemActionExpand(@NonNull MenuItem item) {

            return true;
        }
        //Function to collapse the search bar
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
                        String searchStr = newText;
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
                // Back to main page - user needs to login using biometric authentication
                Intent intentMain = new Intent(this, MainActivity.class);
                this.startActivity(intentMain);
                finish();
                System.exit(0);
                return true;
            case R.id.filter:
                //Show filter
                scrollView.setVisibility(View.VISIBLE);
                return true;
            case R.id.help:
                //Open help page
                Intent intent = new Intent(this,HelpActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.backup:
                //Start the backup of the database
                String outFileName = Environment.getExternalStorageDirectory() + File.separator + getResources().getString(R.string.app_name) + File.separator;
                backup.performBackup(database, outFileName);
                return true;
            case R.id.importData:
                // Importing of the database
                backup.performRestore(database);
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


