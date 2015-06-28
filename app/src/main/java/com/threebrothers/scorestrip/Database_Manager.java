package com.threebrothers.scorestrip;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mapinguari on 18/06/15.
 */
public class Database_Manager extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ErgoDataBase.db";

    public Database_Manager(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Session_Database_Schema.CREATE_USER_TABLE);
        db.execSQL(Session_Database_Schema.CREATE_INTERVAL_TABLE);
        db.execSQL(Session_Database_Schema.CREATE_WORKOUT_TABLE);
        db.execSQL(Session_Database_Schema.CREATE_WORKOUTREL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //POSSIBLE ISSUE HERE
        super.onUpgrade(db,oldVersion,newVersion);
    }
}
