package com.example.karimabounassif.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class StartupScreen extends AppCompatActivity {

    TextView mName;
    TextView mDesc;
    TextView mShare;
    TextView mTotalE;


    //This class simply populates text fields with the info entered before, then adds
    //the information to the database. 
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();

        String name = extras.getString("name");
        String desc = extras.getString("desc");
        String share = extras.getString("share");
        String total = extras.getString("total");


        mName = (TextView) findViewById(R.id.name);
        mDesc = (TextView) findViewById(R.id.desc);
        mShare = (TextView) findViewById(R.id.share);
        mTotalE = (TextView) findViewById(R.id.totalE);

        mName.setText(name);
        mDesc.setText(desc);
        mShare.setText(share);
        mTotalE.setText(total);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit = new Intent(getBaseContext(), StartupFill.class);
                edit.putExtra("name", mName.getText());
                edit.putExtra("desc", mDesc.getText());
                edit.putExtra("share", mShare.getText());
                edit.putExtra("total", mTotalE.getText());
                startActivity(edit);
            }
        });
        myDbAdapter helper = new myDbAdapter(this);
        helper.insertCompany(name, share, total, null);
    }
}
