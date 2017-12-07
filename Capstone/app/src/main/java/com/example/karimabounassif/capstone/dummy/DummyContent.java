package com.example.karimabounassif.capstone.dummy;

import android.content.Context;

import com.example.karimabounassif.capstone.EntryListActivity;
import com.example.karimabounassif.capstone.myDbAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {


    public DummyContent(Context context) {

    }


    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static void addItem(DummyItem item) {  //TODO: MAKE THIS CONNECT TO DB
        ITEMS.add(item);
    }

    private static DummyItem createDummyItem(String id, String name, String stockPrice, String remainingStocks, String imageName) {
        return new DummyItem(String.valueOf(id), name, stockPrice, remainingStocks, imageName);
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    public static class DummyItem {
        public final String id;
        public final String name;
        public final String stockPrice;
        public final String remainingStocks;
        public final String imageName;


        public DummyItem(String id, String name, String stockPrice, String remainingStocks, String imageName) {
            this.id = id;
            this.name = name;
            this.stockPrice = stockPrice;
            this.remainingStocks = remainingStocks;
            this.imageName = imageName;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
