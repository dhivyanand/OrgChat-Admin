package com.example.system.orgchatadmin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

public class NewCircular extends AppCompatActivity {

    Button post;
    EditText title,description;
    GridView attachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circular);

        post = (Button)findViewById(R.id.post);
        title = (EditText)findViewById(R.id.circular_title);
        description = (EditText)findViewById(R.id.circular_description);
        attachment = (GridView)findViewById(R.id.attachment_list);

    }
}
