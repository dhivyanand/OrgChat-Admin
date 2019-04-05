package com.example.system.orgchatadmin.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.system.orgchatadmin.R;

public class NewFeedback extends AppCompatActivity {

    ListView list;
    Button add, done, cancel;
    EditText title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feedback);

        add = (Button)findViewById(R.id.new_question);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(getApplicationContext(),NewQuestion.class),9);

            }
        });

    }
}
