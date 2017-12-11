package com.example.karimabounassif.capstone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import com.example.karimabounassif.capstone.dummy.DummyContent;

//An activity representing a single Entry detail screen.
public class EntryDetailActivity extends AppCompatActivity {

    static User user = EntryListActivity.user;
    static DummyContent.DummyItem company;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                          //Initializing utilities.
        setContentView(R.layout.activity_entry_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        String position = getIntent().getStringExtra(EntryDetailFragment.ARG_ITEM_ID);
        company = EntryListActivity.infoDB.get(Integer.parseInt(position)-1);

                                                                    // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
                                                                    // Create the detail fragment and add it to the activity
                                                                    // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(EntryDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(EntryDetailFragment.ARG_ITEM_ID));
            EntryDetailFragment fragment = new EntryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.entry_detail_container, fragment)
                    .commit();
        }

        ImageView logo = (ImageView) findViewById(R.id.img);
        if(company.imageName != null) {
            logo.setImageResource(getResources().getIdentifier(company.imageName, "drawable", getPackageName()));
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);   //This is the button to purchase a stock.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myDbAdapter helper = new myDbAdapter(EntryDetailActivity.this);             //Running database code.
                helper.purchaseStock(user, company);

                myDbAdapter updatedHelper = new myDbAdapter(EntryDetailActivity.this);  //Resetting local values based on db changes.
                String[] updated = updatedHelper.checkUserPW(user.getUsername(), user.getPassword());

                Intent mainScreen = new Intent(getBaseContext(), EntryListActivity.class);  //Returning to main screen.
                Bundle b = new Bundle();
                b.putStringArray("user", updated);
                mainScreen.putExtras(b);

                startActivity(mainScreen);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, EntryListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
