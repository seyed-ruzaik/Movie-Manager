package com.moviemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Edit_Movie_Details_Page extends AppCompatActivity {
    protected Bundle bundle; //Bundle to get the passed data using a string key
    protected EditText titleName,year,director,actors,review; //edit boxes
    protected RatingBar stars; //ratings bar to display stars
    protected RadioGroup group; // radio buttons
    protected RadioButton radioButton,radioButton2 ; // radio buttons
    protected DatabaseHelper myDb; // database helper
    protected Button save; //save button
    protected ArrayList<Movie> movieArrayList = new ArrayList<>(); // array list which will store objects
    protected ArrayList<String> fav = new ArrayList<>(); // array list which contains all favourite movies
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__movie__deatils__page);



        group = findViewById(R.id.radio_fv_status2);
        stars = findViewById(R.id.rating_stars);
        save = findViewById(R.id.button_update_edit_details);

        radioButton = findViewById(R.id.yes);
        radioButton2 = findViewById(R.id.no);

        //get the value passed from previous activity using the string key
        bundle = getIntent().getExtras();
        String value  = bundle.getString("movie-title");

        //set context for the DatabaseHelper
        myDb = new DatabaseHelper(this);


        //method which add all details to the object array list
        getMovieDetails(value);
        getFavourites();
        System.out.println("This is "+movieArrayList);


        titleName = findViewById(R.id.edit_text_title2);
        year = findViewById(R.id.edit_text_year2);
        director = findViewById(R.id.edit_text_director2);
        actors = findViewById(R.id.edit_text_actors2);
        review = findViewById(R.id.edit_text_review_2);

        //get the rating from the movie object array list
        int ratings = Integer.parseInt(movieArrayList.get(0).getRating());


        //get all details from the array list eg. (title, year, director) etc...
        titleName.setText(movieArrayList.get(0).getTitle());
        year.setText(movieArrayList.get(0).getYear());
        director.setText(movieArrayList.get(0).getDirector());
        actors.setText(movieArrayList.get(0).getActors());
        review.setText(movieArrayList.get(0).getReview());
        //set the ratings of the movie using RatingBar
        stars.setRating(ratings);

        //set the favourites status of the movie
        radioChecked(movieArrayList.get(0).getTitle());


        //update all the details
        update(value);


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






    //retrieve and display all the details for a particular  movie
    public void getMovieDetails(String value) {
        //using cursor to retrieve all details of that movie
        Cursor res = myDb.getMovieDetails(value);
        if(res.getCount() == 0) {
            System.out.println("Nothing Found!");
            return;
        }


        while (res.moveToNext()) {
            //Instantiate the Movie class
            Movie movie = new Movie(res.getString(0),res.getString(1),
                    res.getString(2),
                    res.getString(3),res.getString(4),res.getString(5));

            //add all objects to objects ArrayList
            movieArrayList.add(movie);


        }


    }

    //method which retrieves the favourite movies
    public void getFavourites(){
        //using cursor to retrieve all the data from favourites column
        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
            System.out.println("Nothing Found!");
            return;
        }

        String name;
        while (res.moveToNext()) {
            name = res.getString(6);
            //add title only if its not indicated as null in the favourites column
            if (name != null)
                //add title to the array list
                fav.add(name);


        }

    }
    //method to indicate the favourite status of a particular title
    private void radioChecked(String title) {
        //if its a favourite it will indicate as "Yes"
        if (fav.contains(title)){
            radioButton.setChecked(true);
        }else {
            //else if its not a favourite it will indicate as "No"
            radioButton2.setChecked(true);

        }
    }
    
    //method which will update the edited details
    public void update(String title){

        //set the function for update button
        save.setOnClickListener(v -> {

            //check if the year field is empty or not
            if (!year.getText().toString().isEmpty()) {
                //favourite status
                boolean isRemoved = false; // for favourite
                boolean isUpdated = false; // if not favourite
                int checkYear = Integer.parseInt(year.getText().toString());

                //check the year range and is all fields are empty or not
                if (checkYear > 1895 && !titleName.getText().toString().isEmpty() &&
                        !year.getText().toString().isEmpty() &&
                        !director.getText().toString().isEmpty() &&
                        !actors.getText().toString().isEmpty() &&
                        !review.getText().toString().isEmpty()) {

                    //if title is selected as favourite by the user
                    if (radioButton.isChecked()) {
                        //set the title in the favourite column
                        isUpdated = myDb.updateFavourites(title);
                        //set the rating
                        double rating_stars = stars.getRating();
                        int integer_star = (int) rating_stars;


                        //update all details in the DB
                        myDb.updateData(title, titleName.getText().toString().toLowerCase(), year.getText().toString().toLowerCase(),
                                director.getText().toString().toLowerCase(),
                                actors.getText().toString().toLowerCase(), String.valueOf(integer_star).toLowerCase(),
                                review.getText().toString().toLowerCase(), titleName.getText().toString().toLowerCase());


                    }
                    //if title is selected as not favourite by the user
                    else if (radioButton2.isChecked()) {
                        //set the ratings
                        double rating_stars = stars.getRating();
                        int integer_star = (int) rating_stars;

                        //set as null in the favourite column in the DB
                        isRemoved = myDb.removeFavourites(title, null);
                        //update all the details
                        myDb.updateData(title, titleName.getText().toString().toLowerCase(), year.getText().toString().toLowerCase(),
                                director.getText().toString().toLowerCase(),
                                actors.getText().toString().toLowerCase(), String.valueOf(integer_star).toLowerCase(),
                                review.getText().toString().toLowerCase(), null);

                    }

                    if (isRemoved) { //display a message when the movie is updated
                        ShowToast("Movie Updated!");
                        startActivity(new Intent(Edit_Movie_Details_Page.this, Edit_Movies_List_Activity.class));

                    } else if (isUpdated) { //display a message when the movie is updated

                        ShowToast("Movie Updated!");
                        startActivity(new Intent(Edit_Movie_Details_Page.this, Edit_Movies_List_Activity.class));
                    }

                } else { //display a message when it cannot be updated
                    ShowToast("Error Cannot Update!");
                }
            }else {
                //display an error message
                ShowToast("Please Enter A Valid Year!");
            }
        });

        //clear the movie arrayList
        movieArrayList.clear();
    }





}