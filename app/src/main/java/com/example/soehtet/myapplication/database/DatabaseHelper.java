package com.example.soehtet.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.soehtet.myapplication.model.Result;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "dictionary.sqlite";
    public static final String DBLOCATION = "/data/data/com.example.soehtet.myapplication/databases/";
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        super(context, DBNAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void openDatabase() {
        String dbPath = mContext.getDatabasePath(DBNAME).getPath();
        if(mDatabase != null && mDatabase.isOpen()) {
            return;
        }

         mDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDatabase() {
        if(mDatabase!=null) {
            mDatabase.close();
        }
    }

    public ArrayList<Result> getListProduct(String name) {
        Result product = null;
        ArrayList<Result> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Dictionary3 Where col_1 = '"+name+"' COLLATE NOCASE" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Result(cursor.getString(1), cursor.getString(2), cursor.getString(2));
            productList.add(product);
            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }

    public ArrayList<Result> getSuggestion(String name) {
        Result product = null;
        ArrayList<Result> productList = new ArrayList<>();
        openDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Dictionary3 Where col_1 LIKE '"+name+"%' COLLATE NOCASE" , null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            product = new Result(cursor.getString(0), cursor.getString(1), cursor.getString(2));
            productList.add(product);

            if (productList.size() > 10){
                break;
            }

            cursor.moveToNext();
        }
        cursor.close();
        closeDatabase();
        return productList;
    }
}
