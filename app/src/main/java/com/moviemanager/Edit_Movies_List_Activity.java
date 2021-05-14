package com.moviemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Edit_Movies_List_Activity extends AppCompatActivity {

    protected DatabaseHelper myDb; //database helper
    protected ArrayList<String> titles = new ArrayList<>(); //array list which contains title name of the movies
    protected ArrayList<String> selectedItems; // array list which contains selected items
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__movies__list_);
        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);

        //get all the movies from the database
        getAllMovieTitles();

        ListView listView = findViewById(R.id.checkable_list4);
        //create an ArrayList object to store selected items
        selectedItems = new ArrayList<>();
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //sort all the titles in alphabetical order
        Collections.sort(titles);
        String[] items = new String[titles.size()];

        //add them to the items array
        for (int i = 0 ; i < titles.size();i++){
            items[i] = titles.get(i);

        }

        //create an arrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout3, R.id.txt_title3, items);
        //set the adapter to display all the movies
        listView.setAdapter(adapter);

        //set the on click listener for the listView
        listView.setOnItemClickListener((parent, view, position, id) -> {
            //get the string that was selected by the user
            String Movie_Title = (String) (listView.getItemAtPosition(position));
            Intent nextActivity = new Intent(this, Edit_Movie_Details_Page.class);

            //put the selected string in to the putExtra() method so that
            nextActivity.putExtra("movie-title", Movie_Title);
            startActivity(nextActivity);

        });


    }


    //method to retrieve all movies stored in DB
    public void getAllMovieTitles() {
        //Using the Cursor to retrieve all the data from the database
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            System.out.println("Nothing Found!");
            return;
        }

        String name;
        while (res.moveToNext()) {
            name = res.getString(0);
            //add all the movie titles to the arrayList
            titles.add(name);
        }




    }

    //override the back button to go to main menu when pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}