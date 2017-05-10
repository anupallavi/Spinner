package com.example.hp.spinner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Hp on 5/10/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
   // private static String DB_PATH = "/data/data/com.example.hp.spinner/databases/";

    private static String DB_NAME = "myDBName";

    private SQLiteDatabase myDataBase;

    private final Context myContext;


    private static final String DATABASE_NAME = "list.db";
    private static final String TABLE_NAME = "list_data";
    private static final int DATABASE_VERSION = 1;
    public static final String COL1 = "Eng";
    public static final String COL2 = "Kan";

    public static final String FILE_DIR = "diaryContent";
    private static String DB_PATH = Environment.getExternalStorageDirectory()+ File.separator + FILE_DIR + File.separator + DB_NAME;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }


        try {

            openDataBase();

        } catch (SQLException sqle) {

            throw sqle;

        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.







  /*  public DatabaseHelper(Context context) {super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (English TEXT, " +
                " Kannada TEXT)";
        Log.e("DATABASE OPERATIONS","going to do");
        db.execSQL(createTable);
        Log.e("DATABASE OPERATIONS","database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF TABLE EXISTS " + TABLE_NAME);
        onCreate(db);
    }*/

    //public boolean addData(String item1,String item2) {
    public void addData(String item1,String item2, SQLiteDatabase db) {

        //SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, item1);
        contentValues.put(COL2, item2);
        //long result = db.insert(TABLE_NAME,null,contentValues);
        db.insert(TABLE_NAME,null,contentValues);
        Log.e("DATABASE OPERATION", "New row inserted");
        //if date as inserted incorrectly it will return -1
      /*  if (result == -1) {
            return false;
        } else {
            return true;
        }*/
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
    public Cursor translate(ArrayList<String> result){
        //SQLiteDatabase db = this.getWritableDatabase();


        //  DatabaseHelper myDbHelper = new DatabaseHelper();
        //  myDbHelper = new DatabaseHelper();



        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT myDB.COL2 FROM myDB.TABLE_NAME WHERE myDB.COL1 = ?",new String[] {result.get(0)});
        //Cursor cursor = db.rawQuery("SELECT myDB.COL2 FROM myDB.TABLE_NAME WHERE myDB.COL1 like '"+result.get(0)+"'",null);
        Cursor cursor = db.query(TABLE_NAME,new String[] {COL2}, COL1 + "=?",new String[] {result.get(0).toLowerCase()},null,null,null,null);
        return cursor;



    }

}


