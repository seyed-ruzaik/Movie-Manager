package com.moviemanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MovieDetails.db"; //database name
    public static final String TABLE_NAME = "movie_table"; //table name
    public static final String COL_1 = "TITLE"; //title column
    public static final String COL_2 = "YEAR"; // year column
    public static final String COL_3 = "DIRECTOR"; // director column
    public static final String COL_4 = "ACTORS"; // actors column
    public static final String COL_5 = "RATING"; // ratings column
    public static final String COL_6 = "REVIEW"; // review column
    public static final String COL_7 = "FAVOURITES"; // favourite column

    //constructor which takes Context as a parameter
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //on create method which will create the Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (TITLE TEXT,YEAR TEXT," +
                "DIRECTOR TEXT,ACTORS TEXT,RATING TEXT,REVIEW TEXT,FAVOURITES TEXT)");
    }

    //on upgrade method which will drop the table if its exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //method which will insert the data
    public boolean insertData(String title, String year, String director, String actors, String rating,
                              String review, String favourites) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //put all values in contentValues
        contentValues.put(COL_1, title);
        contentValues.put(COL_2, year);
        contentValues.put(COL_3, director);
        contentValues.put(COL_4, actors);
        contentValues.put(COL_5, rating);
        contentValues.put(COL_6, review);
        contentValues.put(COL_7, favourites);

        //insert only if result is not equals to -1
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    //method which will return all data from the DB
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        //will return all data from the table
        return cursor;
    }

    //method which will retrieves all the details for a given title
    public Cursor getMovieDetails(String title){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, "TITLE = ?", new String[]{title},
                null, null, null);
        return cursor;
    }

    //method which will return all the titles which has a match
    public Cursor getSearchItem(String search){

        SQLiteDatabase db = this.getWritableDatabase();
        String query3 = "select TITLE from "+TABLE_NAME+ " where "+COL_1+" like "+"'%"+search+"%' ";
        String query2 = "select TITLE from "+TABLE_NAME+" where "+COL_1 + " or ACTORS like "+"'%"+search+"%' union "+query3;
        String query = "select TITLE from "+TABLE_NAME+ " where "+COL_3+" like "+"'%"+search+"%' union "+query2;
        Cursor cursor = db.rawQuery(query,null);
        return cursor;


    }

    //method which will update all the data in the DB for a given movie title
    public boolean updateData(String title, String titleName, String year, String director, String actors, String rating,
                              String review, String favourites) {
        int num;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //add all to content values
        contentValues.put(COL_1, titleName);
        contentValues.put(COL_2, year);
        contentValues.put(COL_3, director);
        contentValues.put(COL_4, actors);
        contentValues.put(COL_5, rating);
        contentValues.put(COL_6, review);
        contentValues.put(COL_7, favourites);

        num = db.update(TABLE_NAME,contentValues,"TITLE = ?",new String[]{title});
        System.out.println("This is row "+num);


        if (num > 0) {
            return true;
        }else {
            return false;
        }

    }

    //method which will update the favourites column for a given title
    public boolean updateFavourites(String title){
        int num;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //put value to content values
        contentValues.put(COL_7,title);
        num = db.update(TABLE_NAME,contentValues,"TITLE = ?",new String[]{title});



        //update favourites if num > 0

        if (num > 0) {
            return true;
        }else {
            return false;
        }
    }

    //method which will remove favourites from the favourites column
    public boolean removeFavourites(String title,String value){
        int num;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_7,value);
        num = db.update(TABLE_NAME,contentValues,"TITLE = ?",new String[]{title});


        //remove if num > 0


        if (num > 0) {
            return true;
        }else {
            return false;
        }
    }




}