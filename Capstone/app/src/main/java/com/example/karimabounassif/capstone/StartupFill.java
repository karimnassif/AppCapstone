package com.example.karimabounassif.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karimabounassif.capstone.dummy.DummyContent;

public class StartupFill extends AppCompatActivity {


    EditText mName;
    EditText mDesc;
    EditText mShare;
    EditText mTotalE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup_fill);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle edit = getIntent().getExtras();
        if(edit!=null){
            String name = edit.getString("name");
            String desc = edit.getString("desc");
            String share = edit.getString("share");
            String total = edit.getString("total");

            mName.setText(name, TextView.BufferType.EDITABLE);
            mDesc.setText(desc, TextView.BufferType.EDITABLE);
            mShare.setText(share, TextView.BufferType.EDITABLE);
            mTotalE.setText(total, TextView.BufferType.EDITABLE);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    mName = (EditText) findViewById(R.id.name);
                    mDesc = (EditText) findViewById(R.id.desc);
                    mShare = (EditText) findViewById(R.id.share);
                    mTotalE = (EditText) findViewById(R.id.totalE);

                    Intent done = new Intent(StartupFill.this, StartupScreen.class);
                    done.putExtra("name", mName.getText().toString());
                    done.putExtra("desc", mDesc.getText().toString());
                    done.putExtra("share", mShare.getText().toString());
                    done.putExtra("total", mTotalE.getText().toString());
                    startActivity(done);
                }

        });
    }



}
