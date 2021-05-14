package com.moviemanager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class View_Ratings_Results_Page extends AppCompatActivity {

    protected Bundle bundle; // Bundle to get the passed value from previous activity to current activity using a string key
    protected ArrayList<String> titles = new ArrayList<>(); // array list which contains titles
    protected ArrayList<String> titlesID = new ArrayList<>(); // array list which contains id of the movie title
    protected ArrayList<String> url_of_images = new ArrayList<>(); // array list which contains the image url of the movie title
    protected ArrayList<String> imdID = new ArrayList<>();// array list which contains IMDBID
    protected ImageView image; // for the selected title an image will be displayed in an ImageView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__ratings__results__page);

        //set image
        image = findViewById(R.id.movie_image);

        //getting the value that was passed from View_Ratings activity by using Bundle
        bundle = getIntent().getExtras();
        String value  = bundle.getString("movie_name");


        //set the url
        getMovieList("https://imdb-api.com/en/API/SearchTitle/k_38qqdupa/" +
                value);


    }



//method to display all movies that are relevant to the option selected by the user
 public void getMovieList(String...urls){


        //use string builder to get the json data

     StringBuilder result  = new StringBuilder();


     //Creating a new Thread
     Thread thread = new Thread(() -> {


         try {
             //converting string to a url
             URL url = new URL(urls[0]);
             //get the html file of the website
             HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
             urlConnection.connect();
             InputStream inputStream = urlConnection.getInputStream();

             //buffer reader to read the lines
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

             String line = "";

             //read line by line and check if its null, if null exit from the loop
             while ((line = reader.readLine()) != null) {
                 result.append(line).append("\n");

             }

             //runOnUiThread to update the ui from background
             runOnUiThread(() -> {


                 try {
                     //create a JSON Object from the JSON Response
                     JSONObject jObj = new JSONObject(result.toString());
                     //create a JSON array
                     JSONArray jArray = jObj.getJSONArray("results");


                     ShowToast("Searching For Your Movie In IMDB!");
                     //if json array is not empty proceed
                     if (jArray.length() > 0) {
                         //show a message if selected movie by the user is available in IMDB
                         ShowToast("List Of Movies Matching Your\nSelected Title Found!");
                         for (int i = 0; i < jArray.length(); i++) {
                             JSONObject json_data = jArray.getJSONObject(i);

                             //get the title
                             String getTitle = json_data.getString("title");
                             //get the id
                             String id = json_data.getString("id");
                             //get description
                             String description = json_data.getString("description");
                             //get the image url
                             String image_url = json_data.getString("image");


                             //add title to titles array list
                             titles.add(getTitle+" "+description);
                             //add the id of the movie title to titles id list
                             titlesID.add(id);
                             //add the url string of the movie to the arraylist
                             url_of_images.add(image_url);


                         }


                         //using a for loop and id to get the ratings for all the movies in the list in order
                         for (int i = 0; i < titlesID.size(); i++) {
                             System.out.println("Before :"+titlesID);
                             ratingsMovie(titles,url_of_images,"https://imdb-api.com/en/API/UserRatings/k_38qqdupa/" +
                                     titlesID.get(i));

                         }

                     }else {
                         //if selected movie was not in the IMDB show an error message.
                         ShowToast("Sorry IMDB Could Not\nFind Your Movie!\nTry Selecting A Different\nMovie");
                     }


                 }catch (Exception ex){
                     ex.printStackTrace();

                 }


             });



         }catch (Exception e){
             e.printStackTrace();


         }


     }); //start the thread
     thread.start();

 }

//display the ratings for the list of movies displayed
 public void ratingsMovie(ArrayList<String> titles,ArrayList<String> urlOfImages,String...urls){

        //using string builder to get the results
     StringBuilder result  = new StringBuilder();

//create a another thread to get ratings for all the movie titles
     Thread thread = new Thread(() -> {

         try {
             //converting string to a url
             URL url = new URL(urls[0]);
             //get the html file of the website
             HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
             urlConnection.connect();

             InputStream inputStream = urlConnection.getInputStream();

             //buffer reader to read the lines
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

             String line = "";
             //read line by line and check if its null, if null exit from the loop
             while ((line = reader.readLine()) != null) {
                 result.append(line).append("\n");

             }
             //runOnUiThread to update the ui from background
             runOnUiThread(() -> {

                 try {


                     //create a JSON Object from the JSON Response
                     JSONObject jObj = new JSONObject(result.toString());
                     System.out.println("JSON Returns "+result.toString());

                     //get the rating for a particular movie
                     String rate = jObj.getString("totalRating");
                     //get the IMBD id
                     String ids  = jObj.getString("imDbId");


                     System.out.println("IMDB IS "+imdID);

                     //get the full title of that movie
                     String name = jObj.getString("fullTitle");


                     /*
                       if rating is not null or empty and title is not null or empty modify the the titles list strings
                       by appending a new strings like rating
                      */
                     if (!rate.equals("null") && !rate.equals("") && !name.equals("null") && !name.equals("")) {

                         int idx = titlesID.indexOf(ids);
                         titles.set(idx, titles.get(idx) + "\nRating {" + rate + "}");


                         /*
                         if rating not found show rating as Not Found!
                          */
                     } else if (name.equals("null") || name.equals("")) {

                         int idx = titlesID.indexOf(ids);
                         titles.set(idx, titles.get(idx) + "\nRating {" + "Not Found!" + "}");

                     } else {

                         /*
                         if rating not found show rating as Not Found!
                          */
                         int idx = titlesID.indexOf(ids);
                         titles.set(idx, titles.get(idx) + "\nRating {" + "Not Found!" + "}");

                     }

                     //ListView to display all movies in an orderly format
                     ListView listView = findViewById(R.id.ratings_list);



                           //set the array adapter to display movies in the listView
                         ArrayAdapter<String> adapter = new ArrayAdapter<>(View_Ratings_Results_Page.this,
                                 R.layout.rowlayout6,
                                 R.id.txt_title7, titles);
                         listView.setAdapter(adapter);




                         //set an onclick listener to display the image of a clicked movie title
                     listView.setOnItemClickListener((parent, view, position, id) -> {
                         //set the string url
                         String img_url = urlOfImages.get(position);

                         ImageView image = findViewById(R.id.movie_image);





                         // create a new thread to load image from a given url
                         new Thread(() -> {
                             try
                             {     //using Bitmap and pass the string url
                                 final Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(img_url).getContent());
                                 image.post(() -> {
                                     if(bitmap !=null)
                                     {
                                         // set the image scaling
                                         image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 550, false));
                                     }
                                 });
                             } catch (Exception e)
                             {
                                 e.printStackTrace();
                             }
                         }).start(); // start the thread

                     });




                 }catch (Exception ex){
                     ex.printStackTrace();

                 }


             });



         }catch (Exception e){
             e.printStackTrace();


         }


     });
     thread.start(); // start the thread




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