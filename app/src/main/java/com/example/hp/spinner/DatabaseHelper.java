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

    private static SQLiteDatabase sqliteDb;
    private static DatabaseHelper instance;
    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "list.db";
    public static final String TABLE_NAME = "list_data";
    public static final String COL1 = "Eng";
    public static final String COL2 = "Kan";


    private Context context;
    static Cursor cursor = null;

    DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                   int version) {
        super(context, name, factory, version);
        this.context = context;

    }

    private static void initialize(Context context, String databaseName) {
        if (instance == null) {

            if (!checkDatabase(context, databaseName)) {

                try {
                    copyDataBase(context, databaseName);
                } catch (IOException e) {

                    System.out.println( databaseName
                            + " does not exists ");
                }
            }

            instance = new DatabaseHelper(context, databaseName, null,
                    DATABASE_VERSION);
            sqliteDb = instance.getWritableDatabase();

            System.out.println("instance of  " + databaseName + " created ");
        }
    }

    public static final DatabaseHelper getInstance(Context context,
                                                   String databaseName) {
        initialize(context, databaseName);
        return instance;
    }

    public SQLiteDatabase getDatabase() {
        return sqliteDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private static void copyDataBase(Context aContext, String databaseName)
            throws IOException {

        InputStream myInput = aContext.getAssets().open(databaseName);

        String outFileName = getDatabasePath(aContext, databaseName);

        File f = new File("/data/data/" + aContext.getPackageName() + "/databases/");
        if (!f.exists())
            f.mkdir();

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();

        System.out.println(databaseName + " copied");
    }

    public static boolean checkDatabase(Context aContext, String databaseName) {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = getDatabasePath(aContext, databaseName);

            checkDB = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);

            checkDB.close();
        } catch (SQLiteException e) {

            System.out.println(databaseName + " does not exists");
        }

        return checkDB != null ? true : false;
    }

    private static String getDatabasePath(Context aContext, String databaseName) {
        return "/data/data/" + aContext.getPackageName() + "/databases/"
                + databaseName;
    }

    public static Cursor rawQuery(String query) {
        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }
            sqliteDb = instance.getWritableDatabase();

            cursor = null;
            cursor = sqliteDb.rawQuery(query, null);
        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
        }
        return cursor;
    }

    public static void execute(String query) {
        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }
            sqliteDb = instance.getWritableDatabase();
            sqliteDb.execSQL(query);
        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
        }
    }


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
    /*public void addData(String item1,String item2, SQLiteDatabase db) {

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


   /* public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }*/
    public static Cursor translate(ArrayList<String> result){
        //SQLiteDatabase db = this.getWritableDatabase();


        //  DatabaseHelper myDbHelper = new DatabaseHelper();
        //  myDbHelper = new DatabaseHelper();

        try {
            if (sqliteDb.isOpen()) {
                sqliteDb.close();
            }
            sqliteDb = instance.getWritableDatabase();

            cursor = null;

      //  SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("SELECT myDB.COL2 FROM myDB.TABLE_NAME WHERE myDB.COL1 = ?",new String[] {result.get(0)});
        //Cursor cursor = db.rawQuery("SELECT myDB.COL2 FROM myDB.TABLE_NAME WHERE myDB.COL1 like '"+result.get(0)+"'",null);
      //  Cursor cursor = db.query(TABLE_NAME,new String[] {COL2}, COL1 + "=?",new String[] {result.get(0).toLowerCase()},null,null,null,null);
      //  return cursor;
            cursor = sqliteDb.query(TABLE_NAME,new String[] {COL2}, COL1 + "=?",new String[] {result.get(0).toLowerCase()},null,null,null,null);
        } catch (Exception e) {
            System.out.println("DB ERROR  " + e.getMessage());
            e.printStackTrace();
        }
        return cursor;
    }




}


