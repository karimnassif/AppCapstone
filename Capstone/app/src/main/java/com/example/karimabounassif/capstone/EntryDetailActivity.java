package com.example.karimabounassif.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ImageView;
import com.example.karimabounassif.capstone.dummy.DummyContent;
import static java.security.AccessController.getContext;

//An activity representing a single Entry detail screen.

public class EntryDetailActivity extends AppCompatActivity {

    static User user = EntryListActivity.user;
    static DummyContent.DummyItem company;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
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
                                                                    //This is the button to purchase a stock.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDbAdapter helper = new myDbAdapter(EntryDetailActivity.this);
                company = EntryListActivity.current;
                helper.purchaseStock(user, company);
                String[] updated = helper.checkUserPW(user.getUsername(), user.getPassword());
                User update = new User(updated[0],updated[1],updated[2],updated[3],updated[4]);
                user = update;

                Intent mainScreen = new Intent(EntryDetailActivity.this, EntryListActivity.class);
                Bundle b = new Bundle();
                String[] updatedUser = {user.getID(), user.getUsername(), user.getPassword(), user.getFunds(), user.getCompanies()};
                b.putStringArray("user", updatedUser);
                mainScreen.putExtra("user", b);
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
