package com.moviemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Search_Activity extends AppCompatActivity {
    protected DatabaseHelper myDb; // database helper
    protected EditText editText; // text box to enter items
    protected Button lookup; // lookup button to search
    protected ArrayList<String> titles = new ArrayList<>(); // array list which contains titles
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);

        editText = findViewById(R.id.searchbar_editText);
        lookup = findViewById(R.id.lookup);


        ListView listView = findViewById(R.id.checkable_list5);


        //set the function for lookup button
        lookup.setOnClickListener(v -> {
            //get the string from the text  box
            String getTitle = editText.getText().toString().toLowerCase();
            //pass the parameter as getTitle for search method
            showSearch(getTitle);

            String[] items = new String[titles.size()];

            //add all titles to the items array
            for (int i = 0 ; i < titles.size();i++){
                items[i] = titles.get(i);

            }

            //create an array adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.rowlayout4, R.id.txt_title4, items);
            //set the array adapter to display them in the list view
            listView.setAdapter(adapter);

            //clear list
            titles.clear();
        });


    }


//search method
    public void showSearch(String search) {
        if (!editText.getText().toString().isEmpty()) { //check if the text box is empty or not
            //using cursor to retrieve all titles which has a match
            Cursor res = myDb.getSearchItem(search);
            if (res.getCount() == 0) { //show a message if no match found
                System.out.println("Nothing Found!");
                ShowToast("Nothing Found!");
                return;
            }

            String name;
            while (res.moveToNext()) {
                //if match found add to array list
                name = res.getString(0);
                titles.add(name);


            }


        }else {
            ShowToast("Enter Something!"); // else display an error message
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
        startActivity(new Intent(Search_Activity.this, MainActivity.class));
    }
}