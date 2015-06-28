package com.threebrothers.scorestrip;

import android.content.ContentValues;
import android.provider.BaseColumns;

/**
 * Created by mapinguari on 10/05/15.
 */
public class Session_Database_Schema {

    public Session_Database_Schema() {}

    public static class DataBaseTerms implements BaseColumns{
        //Table names
        private static final String USER_TABLE_NAME = "users";
        private static final String INTERVAL_TABLE_NAME = "intervals";
        private static final String WORKOUT_RELATIONS_TABLE_NAME = "workOutRelations";
        private static final String WORKOUTS_TABLE_NAME = "workouts";
        //Column names
        private static final String COLUMN_NAME_DISTANCE = "distance";
        private static final String COLUMN_NAME_TIME = "time";
        private static final String COLUMN_NAME_RESTTIME = "restTime";
        private static final String COLUMN_NAME_NAME = "name";
        private static final String COLUMN_NAME_WEIGHT = "weight";
        private static final String COLUMN_NAME_HEIGHT = "height";
        private static final String COLUMN_NAME_AGE = "age";
        private static final String COLUMN_NAME_WORKOUT_ID = "workout_ID";
        private static final String COLUMN_NAME_INTERVAL_ORDINAL = "IntervalOrdinal";
        private static final String COLUMN_NAME_INTERVAL_ID = "interval_ID";
        private static final String COLUMN_NAME_COMPLETED_TIME = "completed";
        private static final String COLUMN_NAME_USER_ID = "user_ID";

    }

    public static abstract class FeedEntry implements BaseColumns {
            private static final String TABLE_NAME = "sessions";
            private static final String COLUMN_NAME_DATE = "date";
            private static final String COLUMN_NAME_DISTANCE = "distance";
            private static final String COLUMN_NAME_AVG_SPLIT = "avg_split";
            private static final String COLUMN_NAME_SESSION_OBJECT = "sObject";



    }

    //TODO:  NEED WRITING PROPERLY

    public static final String CREATE_INTERVAL_TABLE = "CREATE TABLE Interval (_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT, distance integer NOT NULL,time double NOT NULL,restTime double NOT NULL)";
    /*        "CREATE TABLE " + DataBaseTerms.USER_TABLE_NAME +
            + " (" + DataBaseTerms._ID +
    */
    public static final String CREATE_USER_TABLE = "CREATE TABLE User (_ID integer NOT NULL PRIMARY KEY AUTOINCREMENT,name varchar(20) NOT NULL,weight double NOT NULL,height double NOT NULL,age integer NOT NULL)";
    public static final String CREATE_WORKOUTREL_TABLE = "CREATE TABLE WorkOutRel (_ID integer NOT NULL  PRIMARY KEY AUTOINCREMENT,workout_ID integer NOT NULL,intervalOrdinal integer NOT NULL,interval_ID integer NOT NULL,FOREIGN KEY (Interval_ID) REFERENCES Interval (_ID),FOREIGN KEY (Workout_ID) REFERENCES Workout (_ID))";
    public static final String CREATE_WORKOUT_TABLE = "CREATE TABLE Workout (_ID integer NOT NULL  PRIMARY KEY AUTOINCREMENT,completed datetime NOT NULL, user_ID integer NOT NULL,FOREIGN KEY (User_ID) REFERENCES User (_ID))";

    public static final String DROP_WORKOUT_DATABASE =
            "DROP TABLE IF EXISTS " +
            DataBaseTerms.WORKOUT_RELATIONS_TABLE_NAME + ", " +
            DataBaseTerms.WORKOUTS_TABLE_NAME + ", " +
            DataBaseTerms.USER_TABLE_NAME + ", " +
            DataBaseTerms.INTERVAL_TABLE_NAME;

    public static final ContentValues UserContent(String name, double weight,double height,int age){
        ContentValues cv = new ContentValues(4);
        cv.put(DataBaseTerms.COLUMN_NAME_NAME,name);
        cv.put(DataBaseTerms.COLUMN_NAME_WEIGHT,weight);
        cv.put(DataBaseTerms.COLUMN_NAME_HEIGHT,height);
        cv.put(DataBaseTerms.COLUMN_NAME_AGE,age);
        return cv;
    }

    public static final ContentValues IntervalContent(int distance,double time, double restTime){
        ContentValues cv = new ContentValues(3);
        cv.put(DataBaseTerms.COLUMN_NAME_DISTANCE,distance);
        cv.put(DataBaseTerms.COLUMN_NAME_TIME,time);
        cv.put(DataBaseTerms.COLUMN_NAME_RESTTIME,restTime);
        return cv;
    }

    public static final ContentValues WorkOutRelContent(int workout_ID,int intervalOrdinal, int interval_ID){
        ContentValues cv = new ContentValues(3);
        cv.put(DataBaseTerms.COLUMN_NAME_WORKOUT_ID,workout_ID);
        cv.put(DataBaseTerms.COLUMN_NAME_INTERVAL_ORDINAL,intervalOrdinal);
        cv.put(DataBaseTerms.COLUMN_NAME_INTERVAL_ID,interval_ID);
        return cv;
    }

    public static final ContentValues WorkOutContent(String completed,int user_ID){
        ContentValues cv = new ContentValues(2);
        cv.put(DataBaseTerms.COLUMN_NAME_COMPLETED_TIME,completed);
        cv.put(DataBaseTerms.COLUMN_NAME_USER_ID,user_ID);
        return cv;
    }




    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_DISTANCE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_AVG_SPLIT + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_SESSION_OBJECT + TEXT_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    public static final String SQL_DATE_ORDERED =
            "SELECT rowid , " + FeedEntry.COLUMN_NAME_DATE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_DISTANCE + COMMA_SEP +
                        FeedEntry.COLUMN_NAME_AVG_SPLIT  +
            " FROM " + FeedEntry.TABLE_NAME +
            " ORDER BY " + FeedEntry.COLUMN_NAME_DATE + " DESC";

    public static final String sql_exact_session(long id){
            return "SELECT rowid , " + FeedEntry.COLUMN_NAME_SESSION_OBJECT +
                    " FROM " + FeedEntry.TABLE_NAME +
                    " WHERE rowid = " + Long.toString(id);
    }

}
