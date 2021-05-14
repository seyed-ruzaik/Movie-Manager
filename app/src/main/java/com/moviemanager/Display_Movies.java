package com.moviemanager;

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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;

public class Display_Movies extends AppCompatActivity {

    protected ArrayList<String> selectedItems; //array list which contains selected items
    protected ArrayList<String> titles = new ArrayList<>(); // array list which contains all title names
    protected ArrayList<String> favourites = new ArrayList<>(); // array list which has all favourites
    protected DatabaseHelper myDb; // database helper
    protected Button myFav_btn; // add to favourites button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__movies);

        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);

        //get all movies from the database
        getAllMovieTitles();

        ListView listView = findViewById(R.id.checkable_list);
        //create an ArrayList object to store selected items
        selectedItems = new ArrayList<>();
        myFav_btn = findViewById(R.id.addToFavourites_btn);

        //set the selection mode in list view to multiple selection
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


        //sort all the titles in alphabetical order
        Collections.sort(titles);
        String[] items = new String[titles.size()];

        //add the items in the ArrayList to the string array
        for (int i = 0 ; i < titles.size();i++){
            items[i] = titles.get(i);

        }




        //create an array adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout, R.id.txt_title, items);
        //set the adapter
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // selected item
            String selectedItem = ((TextView) view).getText().toString();
            if(selectedItems.contains(selectedItem))
                selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
            else
                selectedItems.add(selectedItem); //add selected item to the list of selected items

        });



    }

    //get all movies from the DB
    public void getAllMovieTitles() {
        //using the Cursor to retrieve all the data from DB
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            System.out.println("Nothing Found!");
            return;
        }

        String name;
        while (res.moveToNext()) {
            name = res.getString(0);
            //add the retrieved movie title to the arrayList
            titles.add(name);
        }




    }



    //set the function for the button addToFavourites
    public void addToFavourites(View view) {
        //display a message when user clicks the button and did not select any
        if (selectedItems.isEmpty()){
            ShowToast("Please select a movie!");
        }
        getAllFavourites();
        ArrayList<String> allItems = new ArrayList<>(); // list which will store favourites
        boolean isUpdate = false;
        for(String item: selectedItems){

            //if favourites arrayList does not contains item is
            // used to check for duplicates
                if (!favourites.contains(item)){
                    favourites.add(item);
                    //update favourite column in DB
                    isUpdate = myDb.updateFavourites(item);
                    allItems.add(item);

                }else {

                    //if movie already in favourites display an error message
                    ShowToast(item+ " Already In\nFavourites!");

                }




        }

//     show a message when title is added to favourites
        if(isUpdate) {
            if (allItems.size() > 0) {
                for (int i = 0 ; i < allItems.size();i++) {
                    ShowToast(allItems.get(i)+" Added To Favourites!");
                }
            }
        }



    }

    //get all the favourite movies
  public void getAllFavourites(){

        //Using cursor to retrieve all the data
      Cursor res = myDb.getAllData();
      if(res.getCount() == 0) {
          System.out.println("Nothing Found!");
          return;
      }

      String name;
      while (res.moveToNext()) {
          name = res.getString(6);
          //check if the title is already in the favourite arrayList
          // if not add it to favourites ArrayList
          if (!favourites.contains(name)) {
              favourites.add(name);
          }

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

    //override the back button to go to main menu when pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Display_Movies.this, MainActivity.class));
    }
}