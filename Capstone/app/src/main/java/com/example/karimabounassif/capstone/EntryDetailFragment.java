package com.example.karimabounassif.capstone;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.karimabounassif.capstone.dummy.DummyContent;

/**
 * A fragment representing a single Entry detail screen.
 */
public class EntryDetailFragment extends Fragment {


    public static final String ARG_ITEM_ID = "item_id";
    static private DummyContent.DummyItem mItem;
    static private User user = EntryListActivity.user;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment.
     */
    public EntryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {

            // Load the dummy content specified by the fragment
            // arguments.

            mItem = EntryListActivity.infoDB.get(Integer.parseInt(getArguments().getString(ARG_ITEM_ID))-1);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.entry_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.entry_detail)).setText("$"+mItem.remainingStocks + " total equity." +
                                                                          "\n" + "$" + mItem.stockPrice + "/stock" +"\n\n\n"
                                                                           + "Available Funds: " + user.getFunds());
        }
        return rootView;
    }
}
