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
                "('Paul Octave Wiehé Auditorium', 'The Paul Octave Wiehé Auditorium inaugurated on 28th July 1975 by Mr Pierre Abelin, Ministre de la Coopération du Gouvernement de la République Française and named on 25th May 1981 after the former Vice-Chancellor Dr P O Wiehé. It has since been the main place for the graduation ceremony among the many events that take place there.', '-20.233265', '57.497474', 12345678)," +
                "('Library', 'The University of Mauritius Library has been set up to provide the necessary bibliographic support as well as the services and facilities to satisfy the information needs of its various categories of users, especially the registered students and the academic and technical staff of the university.', '-20.234999', '57.497053', 12345678),"+
                "('Faculty of Agriculture', 'An extension of the agricultural college, where all the agriculture-related courses are usually held, including the labs.', '-20.234406', '57.496192 ', 12345678),"+
                "('Finance Building', 'This building is also known as Batiment Roches. Originally founded as the School of Agriculture in 1914, ultimately became the place where all the university payments are issued.', '-20.233713', '57.496771', 12345678),"+
                "('Faculty of Social Studies and Humanities', 'The Faculty of Social Studies and Humanities was created in 1992 in response to changing socio-economic needs of the community. In the past 20 years, the Faculty has increased, diversified and consolidated programmes of studies at both undergraduate and postgraduate levels. ', '-20.235303', '57.497247', 12345678),"+
                "('Gymnasium', 'The UoM Multipurpose Gymnasium came to life on the 4th October 2004 and immediately found its niche at the heart of the University.\n" +
                "It provides a solid platform where potential of individuals can be tapped, the Gymnasium is first of a kind in the country at University level. A handful of sports facilities like the football, badminton, volleyball, table tennis, handball and basketball are practiced, that allow each and every individual to explore his / her abilities.\n', '-20.232802', '57.499236', 12345678),"+
                "('Football Ground', 'Football is the most popular sports activity practiced on campus. In recent years the student participation has been on the increase. So the University decided to build a huge outdoor ground for the students to have their football tournaments.', '-20.232074', '57.498654', 12345678),"+
                "('Cafeteria', 'It is the main place where students can have their food under one roof at an affordable rate, with areas to sit and hangout with the friends.', '-20.234213', '57.497616', 12345678),"+
                "('Ex-common', 'The basic place where game activities like cards, carom, pool tournaments take place, with places to sit around and chill out. Moreover, it consists of labs and a mini canteen.', '-20.234035', '57.496705', 12345678),"+
                "('Engineering Tower', 'Also know as the Professor Sir Edouard Lim Fat Engineering Tower, this building comprises of 8 floors, which consists of a lecture theatre, classes and administration and student records office.', '-20.236001', '57.496815', 12345678),"+
                "('Phase II Building', 'The place which consists of classes normally allocated to the engineering section, science labs and the CITS phase.', '-20.235121', '57.496825', 12345678),"+
                "('Faculty of Law and Management', 'Consists of classes mainly reserved for the students of the faculty of law and management, and the UoM study area.', '-20.234695', '57.497764', 12345678);'";


        db.execSQL(qb1);

    }


}