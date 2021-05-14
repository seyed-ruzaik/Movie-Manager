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

public class Favourites_Activity extends AppCompatActivity {

    protected ArrayList<String> fav = new ArrayList<>(); // array list which contains favourites
    protected DatabaseHelper myDb; // database helper
    protected Button save; // save button
    protected ArrayList<String> selectedItems; // list which contains selected items
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);

        save = findViewById(R.id.btn_save_favourites);
        //get all favourite titles saved in the DB
        getFavourites();

        ListView listView = findViewById(R.id.checkable_list2);
        selectedItems = new ArrayList<>();

        //set the list view choice mode to multiple selections
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //sort the favourites movie titles in the list
        Collections.sort(fav);
        String[] items = new String[fav.size()];

        //add the favourite titles to the items array
        for (int i = 0 ; i < fav.size();i++){
            items[i] = fav.get(i);

        }

        //create an arrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout2, R.id.txt_title2, items);
        //set the adapter to the list view
        listView.setAdapter(adapter);

        //set on click listener for the list view
        listView.setOnItemClickListener((parent, view, position, id) -> {
            // selected item
            String selectedItem = ((TextView) view).getText().toString();
            if(selectedItems.contains(selectedItem)) {
                selectedItems.remove(selectedItem); //remove deselected item from the list of selected items
            }
            else {
                selectedItems.add(selectedItem); //add selected item to the list of selected items
            }
        });

        //check all favourite movie titles
       for (int i = 0 ; i < fav.size(); i++){
           listView.setItemChecked(i,true);
       }
    }

    //set the function for the button
    public void SaveAll(View view) {
        //ListView
        ListView listView = findViewById(R.id.checkable_list2);

        //boolean to check if the favourite title is removed
        boolean isRemoved = false;
        if (fav.size() > 0) {
            for (int i = 0; i < fav.size(); i++) {

                //check if an item is unchecked from the list view
              if (!listView.isItemChecked(i)){
                  String name;
                  //get the title which was selected by the user
                  name = listView.getItemAtPosition(i).toString();
                  //remove the favourite movie from DB by replacing the value with null
                  isRemoved = myDb.removeFavourites(name,null);
                  System.out.println("unchecked : "+name);
              }
            }
        }
        if(isRemoved) {

            //if removed from list display a message to the user saying it has been removed
            // and restart the activity
                    ShowToast("Removed From Favourites!");
            finish();
            startActivity(getIntent());

        }else if (fav.size() == 0){
            //if no favourites present and user clicks the button show an error message
            ShowToast("Please Select A Movie\nAnd Add It To Favourites!");
        }
        else {
            //if no changes made and user clicks the button display a message
            ShowToast("Please DeSelect Any To Make Changes!");
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

    //get all favourites movie titles
    public void getFavourites(){
        //using cursor to retrieve data from DB
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            System.out.println("Nothing Found!");
            return;
        }

        String name;
        while (res.moveToNext()) {
            name = res.getString(6);
            if (name != null)
                //add the title to list only if its not null
                //null means in other words not favourite
                fav.add(name);


        }

    }

    //override the back button to go to main menu when pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Favourites_Activity.this, MainActivity.class));
    }
}