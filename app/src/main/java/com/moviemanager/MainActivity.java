package com.moviemanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {



    protected static boolean isSplashScreenShown = false; // boolean to check if splash screen is shown initially it has to be false
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //show splash screen
        if (!isSplashScreenShown) {
            startActivity(new Intent(MainActivity.this, Splash_Screen.class));

        }

       //Buttons
        Button register = findViewById(R.id.register_movie_btn);
        Button displayMovies = findViewById(R.id.display_movie_btn);
        Button myFavourites = findViewById(R.id.favourites_btn);
        Button editMovies = findViewById(R.id.edit_movies_btn);
        Button search = findViewById(R.id.search_btn);
        Button ratings = findViewById(R.id.ratings_btn);

        //go to register a movie option
        register.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Register_movie.class)));

        //go to display all movies option
        displayMovies.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Display_Movies.class)));

        //go to favourites option
        myFavourites.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Favourites_Activity.class)));

        //go to the edit page
        editMovies.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Edit_Movies_List_Activity.class)));

        //go to search
       search.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, Search_Activity.class)));

       //go to ratings option
        ratings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, View_Ratings.class)));
    }

    //close app on back pressed
    @Override
    public void onBackPressed() {

        //finish all activity
        finishAffinity();

    }
}

/*
** References
* How to add a icon inside a button that shape oval button -
https://stackoverflow.com/questions/44580113/how-to-add-a-icon-inside-a-button-that-shape-oval-button/44583388
* Animated Gradient Background in Android -
http://www.androidtutorialshub.com/animated-gradient-background-in-android/
* Rounded corner for text view in android - https://stackoverflow.com/questions/18781902/rounded-corner-for-textview-in-android
* Home screen button icons - https://icons8.com/icons/set/search
* Android SQLite Database Tutorial 1 # Introduction + Creating Database and Tables (Part 1 - 6) -
https://www.youtube.com/watch?v=cp2rL3sAFmI&list=PLS1QulWo1RIaRdy16cOzBO5Jr6kEagA07
* How to determine if an input in EditText is an integer? -
https://stackoverflow.com/questions/10120212/how-to-determine-if-an-input-in-edittext-is-an-integer/10121007
* android checkable listView (Youtube) - https://www.youtube.com/watch?v=a-dvLs1g3Ec
* Android ListView CHOICE_MODE_MULTIPLE, how to set checked index? -
https://stackoverflow.com/questions/5146182/android-listview-choice-mode-multiple-how-to-set-checked-index
* Reload activity in Android - https://stackoverflow.com/questions/3053761/reload-activity-in-android
* Load image formURL using a Thread - https://stackoverflow.com/questions/9361966/load-image-formurl-using-a-thread
* passing data from list view to another activity - https://stackoverflow.com/questions/4848260/passing-data-from-list-view-to-another-activity
* How to check which radio button of a radio group is selected? [ANDROID]
https://stackoverflow.com/questions/42502055/how-to-check-which-radio-button-of-a-radio-group-is-selected-android
* Store rating as an integer - android [duplicate] - https://stackoverflow.com/questions/41767961/store-rating-as-an-integer-android
* How to retrieve a specific row using SQLite and Cursors? -
https://stackoverflow.com/questions/29921317/how-to-retrieve-a-specific-row-using-sqlite-and-cursors
* SQLite combine select all from multiple columns - https://stackoverflow.com/questions/15720707/sqlite-combine-select-all-from-multiple-columns
* https://stackoverflow.com/questions/5776851/load-image-from-url - https://stackoverflow.com/questions/5776851/load-image-from-url
* How to exit an Android app programmatically? - https://stackoverflow.com/questions/17719634/how-to-exit-an-android-app-programmatically
* How to Resize a Bitmap in Android? - https://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
 */