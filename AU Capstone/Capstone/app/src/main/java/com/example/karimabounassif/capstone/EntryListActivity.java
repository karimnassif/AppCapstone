package com.example.karimabounassif.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.karimabounassif.capstone.dummy.DummyContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * An activity representing a list of Entries.  On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link EntryDetailActivity} representing
 * item details.
 */


public class EntryListActivity extends AppCompatActivity {

    static List<DummyContent.DummyItem> infoDB;
    static DummyContent.DummyItem current;
    static User user;
    String[] temp;
    String[] info;

    public List<DummyContent.DummyItem> items(ArrayList<String[]> initial){         //This is a linkedlist of companies
        List<DummyContent.DummyItem> returnList = new LinkedList<>();               //used to populate the main screen.
        for(int i=0; i<=initial.size()-1; i++){
            temp = initial.get(i);
            DummyContent.DummyItem dummy = new DummyContent.DummyItem(temp[0],temp[1],temp[2],temp[3],temp[4]);
            returnList.add(dummy);
        }
        return returnList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle b = getIntent().getExtras();                                         //Retrieving current user info
        if(b != null){
            info = b.getStringArray("user");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_list);

        //Updating user info from the database.
        myDbAdapter helper = new myDbAdapter(this);
        String[] update = helper.checkUserPW(info[1],info[2]);
        user = new User(update[0],update[1],update[2],update[3],update[4]);
        infoDB = items(helper.fetchCompanies());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);   //Button to go to User Page
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = view.getContext();
                Intent userPage = new Intent(c, UserPage.class);
                c.startActivity(userPage);
            }
        });

        View recyclerView = findViewById(R.id.entry_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView, infoDB);

    }

    //The following three methods set up and populate the scrolling field that contains
    //the companies for sale.
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<DummyContent.DummyItem> infoDB) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(infoDB));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.entry_list_content, parent, false);
            return new ViewHolder(view);
        }

        //This method is for opening expanded info on companies when one is clicked.
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            String imgName = mValues.get(position).imageName;
            holder.mItem = mValues.get(position);
            current = holder.mItem;
            if(imgName != null)
            {
            //This creates the logo image based on the string name of the file saved.
            holder.mImageView.setImageResource(getResources().getIdentifier(imgName, "drawable", getPackageName()));
            }
            holder.mContentView.setText(mValues.get(position).name);
            holder.mIdView.setText("$"+mValues.get(position).stockPrice + " per stock."
                                    +"\n$"+mValues.get(position).remainingStocks + " total equity.");

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {                 //When a company is clicked, this sends their id and their position
                        Context context = v.getContext();     //in the company list to EntryDetail Activity to open more details.
                        Intent intent = new Intent(context, EntryDetailActivity.class);
                        intent.putExtra(EntryDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }


    //A constructor for a single element in the scrolling list.
        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;
            public final ImageView mImageView;
            public final TextView mIdView;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mContentView = (TextView) view.findViewById(R.id.content);
                mImageView = (ImageView) view.findViewById(R.id.img);
                mIdView = (TextView) view.findViewById(R.id.id);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
