package com.navigasi.budi.kasibi.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseSQLHelper extends SQLiteOpenHelper {

    private static String TAG = DatabaseSQLHelper.class.getName();
    private  String DB_PATH;
    private static String DB_NAME = "peta.db";
    private static String TABLE_NAME = "kota";
    private SQLiteDatabase myDataBase = null;
    private final Context myContext;
    public static final String COL_1 = "ID";
    public static final String COL_2 = "KATA";
    public static final String COL_3 = "KATEGORI";
    public static final String COL_4 = "favorite";

    public DatabaseSQLHelper(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH="/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.v("log_tag", "DBPath: " + DB_PATH);
    }

    public void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){
            Log.v("log_tag", "database does exist");
        }else{
            Log.v("log_tag", "database does not exist");
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase() throws IOException{
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase(){
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return myDataBase != null;
    }


    @Override
    public synchronized void close()
    {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "Upgrading database, this will drop database and recreate.");
    }

    public boolean getUserDetails(String input){
        String selectQuery = "SELECT  * FROM kota where nama = '"+input+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        Log.d("Log", "Fetching user from Sqlite: " + cursor.getCount());

        if (cursor.getCount() > 0)
        {
            cursor.close();
            db.close();
            return true;
        }
        else
        {
            cursor.close();
            db.close();
            return false;
        }
    }
}
