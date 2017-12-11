package com.example.karimabounassif.capstone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.karimabounassif.capstone.dummy.DummyContent;

import java.util.HashMap;

public class UserPage extends AppCompatActivity {

    private TextView mTextMessage;
    private User check = EntryListActivity.user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        final myDbAdapter helper = new myDbAdapter(this);                               //Retrieving user info from db
        String[] update = helper.checkUserPW(check.getUsername(), check.getPassword());
        final User user = new User(update[0], update[1], update[2], update[3], update[4]);

        getSupportActionBar().setTitle(user.getUsername());
        mTextMessage = (TextView) findViewById(R.id.message);

        HashMap<String, Integer> comps = new HashMap<>();                               //Using a hashmap to iterate through
        String companyMessage = new String();                                           //string of companies and show by frequency.

        String[] split = user.getCompanies().split(" ");

        for (String s : split) {
            if (comps.get(s) == null) {
                comps.put(s, 1);
            } else {
                comps.put(s, comps.get(s) + 1);
            }
        }
        for (String key : comps.keySet()) {                                             //Organizing bought stock by amount.
            companyMessage += "    " + String.valueOf(key) + ": " + String.valueOf(comps.get(key)) + "\n";
        }
        mTextMessage.setText("Username: " + user.getUsername() + "\n\n\n\nFunds: " + user.getFunds() +
                "\n\n\n\nCompanies: \n" + companyMessage);                              //Setting up textviews with user info.

        final EditText mUser = (EditText) findViewById(R.id.username);
        final EditText stock = (EditText) findViewById(R.id.stock);
        Button mTradeButton = (Button) findViewById(R.id.trade);
        mTradeButton.setOnClickListener(new View.OnClickListener() {                    //The button for trading stock.
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem comp = helper.fetchCompany(stock.getText().toString());
                helper.tradeStock(user, mUser.getText().toString(), comp);
            }

        });
    }
}



