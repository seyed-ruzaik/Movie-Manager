package com.moviemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Register_movie extends AppCompatActivity {

   protected DatabaseHelper myDb; //database helper
   protected EditText title,year,director,actors,rating,review; //text boxes
   protected Button save; //save button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        title = findViewById(R.id.editText_title);
        year = findViewById(R.id.ediText_year);
        director = findViewById(R.id.ediText_director);
        actors = findViewById(R.id.ediText_cast);
        rating = findViewById(R.id.ediText_rating);
        review = findViewById(R.id.ediText_review);
        save = findViewById(R.id.btn_save);

        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);


        //call the insert data method
        insertData();




    }


    //method which will insert the data to the database
    public void insertData() {
        save.setOnClickListener(
                v -> {
                                //if all fields are not empty proceed
                    if (    !title.getText().toString().isEmpty() &&
                            !year.getText().toString().isEmpty() &&
                            !director.getText().toString().isEmpty() &&
                            !actors.getText().toString().isEmpty() &&
                            !rating.getText().toString().isEmpty() &&
                            !review.getText().toString().isEmpty()){


                        //create 2 variables for year and rating to check if they are both integers
                        String checkYear = year.getText().toString();
                        String checkRating = rating.getText().toString();


                    try {
                        //convert the checkYear and checkRating strings to int
                        int yearNum = Integer.parseInt(checkYear);
                        int ratingNum = Integer.parseInt(checkRating);

                        //if the year is greater than 1895 and rating is greater than 0 and less than or equal to 10 then proceed
                        if ((yearNum > 1895) && (ratingNum > 0 && ratingNum <= 10)) {
                            //insert the data
                            boolean isInserted = myDb.insertData(title.getText().toString().toLowerCase(),
                                    year.getText().toString().toLowerCase(),
                                    director.getText().toString().toLowerCase(),
                                    actors.getText().toString().toLowerCase(),
                                    rating.getText().toString().toLowerCase(),
                                    review.getText().toString().toLowerCase(),
                                    null);
                            //clear all the fields
                            title.setText("");
                            year.setText("");
                            director.setText("");
                            actors.setText("");
                            rating.setText("");
                            review.setText("");
                            //if data is inserted to the DB show a message
                            if (isInserted) {
                                ShowToast("Data Is Saved");
                            } else {
                                //if data is not inserted to the DB show an error message
                               ShowToast("Data Is Not Saved");
                            }
                        } else { //if the year is less than 1895 data won't be inserted and will display an error message
                            if (yearNum <= 1895) {
                                ShowToast("Enter A Year Greater Than\n" +
                                        "1895");
                            } else {
                                //if the rating value is not in range data won't be inserted and will display an error message
                               ShowToast("Rating Should Be In Range\n" +
                                       "Between 1-10");
                            }
                            //clear all the fields
                            title.setText("");
                            year.setText("");
                            director.setText("");
                            actors.setText("");
                            rating.setText("");
                            review.setText("");
                        }

                    } catch (NumberFormatException exception) {
                        //display an error message when user does'nt enter an Integer values
                       ShowToast("Please Enter Integer Values\nFor " +
                               "Title and Rating");
                       //clear all the fields
                        title.setText("");
                        year.setText("");
                        director.setText("");
                        actors.setText("");
                        rating.setText("");
                        review.setText("");
                    }
                  }else {
                        //if user enters nothing and clicks the submit button this will display an error message
                       ShowToast("Please Enter In All Of\n" +
                               "TextBoxes!");
                        title.setText("");
                        year.setText("");
                        director.setText("");
                        actors.setText("");
                        rating.setText("");
                        review.setText("");
                    }
                }

        );
    }

    //show a toast message
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
        startActivity(new Intent(Register_movie.this, MainActivity.class));
    }


}