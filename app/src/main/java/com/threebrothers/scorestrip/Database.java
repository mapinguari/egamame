package com.threebrothers.scorestrip;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class Database extends ActionBarActivity {

    private SQLiteDatabase database;
    private String SESSION_OBJECT = "com.threebrothers.scorestrip.Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        ListView databaseView = (ListView) findViewById(R.id.database_list_view);
        TextView databaseHeader = (TextView) LayoutInflater.from(this).inflate(R.layout.database_header, null);
        databaseView.addHeaderView(databaseHeader);
        Session_DB_Helper db_helper = new Session_DB_Helper(this);
        SQLiteDatabase db = db_helper.getReadableDatabase();
        this.database = db;
        Cursor cursor = db.rawQuery(Session_Database_Schema.SQL_DATE_ORDERED,null);
        CursorAdapter sessionAdp = new Session_adapter(this,cursor);
        databaseView.setAdapter(sessionAdp);

    }

    private class Session_adapter extends CursorAdapter implements AdapterView.OnItemClickListener{
        public Session_adapter(Context context, Cursor cursor){
            super(context,cursor,0);
        }
        @Override
        public View newView(Context context,Cursor cursor, ViewGroup parent){
            return LayoutInflater.from(context).inflate(R.layout.database_item,parent,false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView date = (TextView) view.findViewById(R.id.db_item_date);
            TextView distance = (TextView) view.findViewById(R.id.db_item_distance);
            TextView avg_spl = (TextView) view.findViewById(R.id.db_item_avg_split);
            String dateV = cursor.getString(1);
            String distanceV = cursor.getString(2);
            String avgV = cursor.getString(3);


        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor carry = database.rawQuery(Session_Database_Schema.sql_exact_session(id),null);
            String sess_obj = carry.getString(1);
            Intent show_session = new Intent(parent.getContext(), Session_show.class);
            show_session.putExtra(SESSION_OBJECT, sess_obj);
            startActivity(show_session);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_database, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
