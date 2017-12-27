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
        populateTableBuildings(db);

    }

    private void createTablePlaces(SQLiteDatabase db){
        StringBuilder qb = new StringBuilder();
        qb.append("CREATE TABLE IF NOT EXISTS buildings (");
        qb.append(" id INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL, ");
        qb.append(" placeName TEXT DEFAULT NULL, ");
        qb.append(" placeDesc TEXT DEFAULT NULL, ");
        qb.append(" placeLong NUMERIC NOT NULL, ");
        qb.append(" placeLat NUMERIC NOT NULL, ");
        qb.append(" phone NUMERIC DEFAULT NULL, ");
        qb.append(" fav NUMERIC DEFAULT 0 ); ");
        db.execSQL(qb.toString());


    }

    private void populateTableBuildings(SQLiteDatabase db) {
            String qb1 = "INSERT INTO buildings (placeName, placeDesc, placeLat,placeLong,phone) VALUES\n" +
                    "('POWA', 'paul octave wiehe auditorium', '-20.233378', '57.497468', 12345678)," +
                    "('CAFE', 'university of mauritius cafetaria', '-20.234205', '57.497626', 12345678),"+
                    "('NAC', 'NAC', '-20.234691', '57.497531', 12345678),"+
                    "('ENGINEERING TOWER', 'ENGINEERING TOWER', '-20.236041', '57.497093', 12345678),"+
                    "('EX COMMON', 'EX COMMON', '-20.233973', '57.496622', 12345678),"+
                    "('FOA', 'FOA', '-20.234504', '57.496226', 12345678),"+
                    "('FSSSH', 'FSSSH', '-20.235273', '57.497194', 12345678),"+
                    "('LIBRARY', 'LIBRARY', '-20.234913', '57.496991', 12345678);";

            db.execSQL(qb1);

        }


}