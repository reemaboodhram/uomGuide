package com.boodhram.guideme.Database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by vgobin on 20-Apr-16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "uom.db";
    private SQLiteDatabase db;
    private static DatabaseHelper mInstance = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }


    public DatabaseHelper open() throws SQLException {
        db = this.getWritableDatabase();
        return this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTables(db);
        db.execSQL("PRAGMA foreign_keys = \"1\"; commit;");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("PRAGMA foreign_keys = \"1\"; commit;");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys = \"1\"; commit;");
    }

    private void createTables(SQLiteDatabase db) {
        createTablePlaces(db);

    }

    private void createTablePlaces(SQLiteDatabase db){
        StringBuilder qb = new StringBuilder();
        qb.append("CREATE TABLE IF NOT EXISTS buildings (");
        qb.append(" id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL, ");
        qb.append(" placeName TEXT DEFAULT NULL, ");
        qb.append(" placeDesc TEXT DEFAULT NULL, ");
        qb.append(" placeLong NUMERIC NOT NULL, ");
        qb.append(" placeLat NUMERIC NOT NULL, ");
        qb.append(" phone NUMERIC NOT NULL, ");
        qb.append(" fav NUMERIC DEFAULT 0 ); ");
        db.execSQL(qb.toString());
    }



}