package com.example.karimabounassif.capstone.dummy;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.karimabounassif.capstone.EntryDetailFragment;
import com.example.karimabounassif.capstone.EntryListActivity;
import com.example.karimabounassif.capstone.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyContent {


    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final int COUNT = 25;

    static {
        addItem(createDummyItem(10001, "Junc-Tio", 10, 10000, "junc"));
        addItem(createDummyItem(10002, "eBay", 200, 200000, "ebay"));
        addItem(createDummyItem(10003, "AU Capstone", 7, 7700, "au"));
        addItem(createDummyItem(10004, "Foo", 500, 1000500, "foo"));
    }


    private static void addItem(DummyItem item) {

        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static DummyItem createDummyItem(int id, String name, int stockPrice, int remainingStocks, String imageName) {
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
        public final int stockPrice;
        public final int remainingStocks;
        public final String imageName;

        public DummyItem(String id, String name, int stockPrice, int remainingStocks, String imageName) {
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
