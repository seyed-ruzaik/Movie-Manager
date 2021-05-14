package com.moviemanager;

public class Movie {


    //instance variables
    private String title;
    private String year;
    private String director;
    private String actors;
    private String rating;
    private String review;



    //default constructor
    public Movie (){


    }

    //args-constructor
   public Movie( String title, String year, String director, String actors , String rating, String review){
        this.title = title;
        this.year = year;
        this.director = director;
        this.actors = actors;
        this.rating = rating;
        this.review  = review;


        
        
   }


   //getters and setters


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }


    //toString Method
    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", director='" + director + '\'' +
                ", actors='" + actors + '\'' +
                ", rating='" + rating + '\'' +
                ", review='" + review + '\'' +
                '}';
    }
}
