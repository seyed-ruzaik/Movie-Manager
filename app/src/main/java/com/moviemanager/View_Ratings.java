package com.moviemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class View_Ratings extends AppCompatActivity {

    protected DatabaseHelper myDb; //database helper
    protected ArrayList<String> titles = new ArrayList<>(); // array list which contains title names
    protected Button find; // button to find movie in IMDB
    protected boolean state = false; // boolean to check if user selected a movie name from the list view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__ratings);

        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);

        //retrieve all movie titles in DB
        getAllMovieTitles();

        ListView listView = findViewById(R.id.list_view);
        find = findViewById(R.id.find_movie_ratings_btn);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        //sort the title which are in the list
        Collections.sort(titles);
        String[] items = new String[titles.size()];

        //add them to items array
        for (int i = 0 ; i < titles.size();i++){
            items[i] = titles.get(i);

        }

        //create an an Array Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(View_Ratings.this, R.layout.rowlayout5, R.id.txt_title6, items);
        //set the array adapter to display all movie titles in the list view
        listView.setAdapter(adapter);



        //show an error message when user clicks button without selecting any
        find.setOnClickListener(v -> {
            if (!state){
                ShowToast("Please Select A Movie!");
            }
        });

        //set an onclick listener for all the movie titles in the list view
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = ((TextView) view).getText().toString(); // get the selected movie name
            state = true; // make state as true
            find.setOnClickListener(v -> {

                //go to the next activity which is to view ratings
                Intent nextActivity = new Intent(this,  View_Ratings_Results_Page.class);

                //put the selected title name inside putExtra()
                nextActivity.putExtra("movie_name", selectedItem);
                startActivity(nextActivity);

            });

        });
    }



    //get all the movie titles from the database
    public void getAllMovieTitles() {
        //using cursor to retrieve data from the database
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            System.out.println("Nothing Found!");
            return;
        }

        String name;
        while (res.moveToNext()) {
            name = res.getString(0);
            //add the movie title to an array list
            titles.add(name);
        }




    }


    //show toast message
    @SuppressWarnings("deprecation")
    public void ShowToast(String message){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_layout_v3, findViewById(R.id.toast_message));
        TextView textView = layout.findViewById(R.id.textView_v3);
        textView.setText(message);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0 , 0 );
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();



    }

}