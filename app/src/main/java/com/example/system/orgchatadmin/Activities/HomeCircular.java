package com.example.system.orgchatadmin.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.system.orgchatadmin.Adapters.CircularListAdapter;
import com.example.system.orgchatadmin.R;

import java.util.ArrayList;

public class HomeCircular extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_circular);

        getSupportActionBar().setTitle("Circular");

        FloatingActionButton new_circular = (FloatingActionButton) findViewById(R.id.new_circular);
        ListView circular_list = (ListView)findViewById(R.id.circular_list);
        TextView nocirc = (TextView)findViewById(R.id.nocirc);

        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> date = new ArrayList<String>();
        final ArrayList<String> circularID = new ArrayList<String>();

        final CircularListAdapter adap = new CircularListAdapter(HomeCircular.this,list,date);

        circular_list.setAdapter(adap);

        try {

            SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);

            Cursor resultSet = mydatabase.rawQuery("Select TITLE, DATE, CIRCULAR_ID from CIRCULAR",null);

            if(resultSet.moveToFirst()) {

                nocirc.setVisibility(View.INVISIBLE);

                do {

                    list.add(resultSet.getString(0));
                    date.add(resultSet.getString(1));
                    circularID.add(resultSet.getString(2));

                    adap.notifyDataSetChanged();

                } while (resultSet.moveToNext());

            }

            resultSet.close();
            //mydatabase.close();

        }catch(Exception e){
            System.out.println(e.toString());
        }

        circular_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                startActivity(new Intent(getApplicationContext(), CircularActivity.class).putExtra("circular_id",circularID.get(i)));
                finish();

            }
        });

        circular_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {

                AlertDialog dialog = new AlertDialog.Builder(HomeCircular.this)
                        .setTitle("Delete Item")
                        .setMessage("What do you want to delete this item?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                SQLiteDatabase mydatabase = getApplicationContext().openOrCreateDatabase("org_chat_db", MODE_PRIVATE, null);
                                mydatabase.execSQL("delete from CIRCULAR where CIRCULAR_ID = '"+ circularID.get(i) +"' ");
                                list.remove(i);
                                date.remove(i);
                                circularID.remove(i);

                                adap.notifyDataSetChanged();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return false;
            }
        });

        new_circular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),NewCircular.class));
                finish();
            }
        });
    }
}
