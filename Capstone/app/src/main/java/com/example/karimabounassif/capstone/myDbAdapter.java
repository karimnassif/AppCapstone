package com.example.karimabounassif.capstone;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.*;
import android.util.Log;

import com.example.karimabounassif.capstone.dummy.DummyContent;

import java.util.ArrayList;



public class myDbAdapter extends SQLiteOpenHelper{

    public myDbAdapter(Context context){
        super(context, DATABASE_NAME, null, DATABASE_Version);
    }

    private static final String DATABASE_NAME = "Junc_Tio";    // Database Name
    private static final String TABLE_COMPANIES = "Companies"; // Table Name
    private static final String TABLE_USERS = "Users";
    private static final int DATABASE_Version = 7;             // Database Version
    private static final String UID="_id";                     //Columns for companies database
    private static final String NAME = "Name";
    private static final String STOCK_PRICE = "Price";
    private static final String TOTAL_EQUITY = "Equity";
    private static final String IMAGE_NAME = "ImageName";
    private static final String USER_ID = "_uid";              //Columns for user table
    private static final String USERNAME = "username";         //This will be their email as well
    private static final String PASSWORD = "Password";
    private static final String FUNDS = "Funds";
    private static final String COMPANIES = "Companies";   //Since SQLite does not have a list type, this will have to be
                                                               //comma seperated id's that I manually strip.

    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE "+ TABLE_COMPANIES +
                    "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME+" VARCHAR(255)," + STOCK_PRICE + " VARCHAR(225),"
                    + TOTAL_EQUITY + " VARCHAR(225)," + IMAGE_NAME + " VARCHAR(225)" + ")";
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
                    "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " VARCHAR(255), " +PASSWORD + " VARCHAR(255), "
                    + FUNDS + " VARCHAR(255), " + COMPANIES + " VARCHAR(255)" + ")";



    public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_COMPANIES);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL("INSERT INTO Companies(" + UID +", " + NAME +", " + STOCK_PRICE + ", " +
                    TOTAL_EQUITY + ", " + IMAGE_NAME +") VALUES('1', 'Junc_Tio', '10', '10000', 'junc')");
            db.execSQL("INSERT INTO Companies(" + UID +", " + NAME +", " + STOCK_PRICE + ", " +
                TOTAL_EQUITY + ", " + IMAGE_NAME +") VALUES('2', 'eBay', '15', '15000', 'ebay')");
            db.execSQL("INSERT INTO Companies(" + UID +", " + NAME +", " + STOCK_PRICE + ", " +
                TOTAL_EQUITY + ", " + IMAGE_NAME +") VALUES('3', 'AU Capstone', '20', '20000', 'au')");
            db.execSQL("INSERT INTO Users(" + USER_ID + ", " + USERNAME + ", " + PASSWORD + ", " +
                        FUNDS + ", " + COMPANIES +") VALUES('1', '@', 'asdf', '15000', '')");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE "+ TABLE_COMPANIES);
            db.execSQL("DROP TABLE " + TABLE_USERS);
            onCreate(db);
        }


    public ArrayList<String[]> fetchCompanies(){                  //Fetches all companies in db

        SQLiteDatabase dbb = this.getReadableDatabase();
        ArrayList<String[]> data = new ArrayList<>();

        //Setting up SQL query
        Cursor c = dbb.rawQuery("SELECT * FROM Companies ", null);
        c.moveToFirst();
        do{                                                       //Iterating through columns, saving data
            String[] info = new String[5];
            info[0] = c.getString(0);                                   //Each company has data in format:
            info[1] = c.getString(1);                                   //[0] = id, [1] = name
            info[2] = c.getString(2);                                   //[2] = stock price, [3] = total equity
            info[3] = c.getString(3);                                   //[4] = imageName
            info[4] = c.getString(4);
            data.add(info);
        } while(c.moveToNext());
        return data;
    }

    String[] checkUserPW(String username, String password){             //Checks if the username/pw combo exists, and also
        SQLiteDatabase dbb = this.getReadableDatabase();                //retrieves the user's information.
        String[] user = new String[5];
        Cursor c = dbb.rawQuery("SELECT * FROM Users WHERE username = '" + username + "'", null);
        if(c.moveToFirst()){
            if(!c.getString(2).equals(password)){
                user[0] = "Wrong password.";
            }
            else{
            for(int i =0; i<=c.getColumnCount()-1;i++) {
                user[i] = c.getString(i);
                }
            }
        }
        else {
            user[0] = "No such username.";
        }
        return user;
    }

    public void purchaseStock(User user, DummyContent.DummyItem company){
        //Retrieve database
        SQLiteDatabase dbb = this.getWritableDatabase();

        //Preparing variables and columns to be changed
        double price = Double.parseDouble(company.stockPrice);
        double newFunds = Double.parseDouble(user.getFunds()) - price;
        double totalEquity = Double.parseDouble(company.remainingStocks) - price;
        String currentCompanies = user.getCompanies();
        String[] username = {user.getUsername()};
        String[] companyName = {company.name};

        //Update Users table
        ContentValues changeUser = new ContentValues();
        changeUser.put(FUNDS, Double.toString(newFunds));
        changeUser.put(COMPANIES, currentCompanies + company.name + " ");
        dbb.update(TABLE_USERS, changeUser, USERNAME + "= ?", username);

        //Update Companies table
        ContentValues changeCompany = new ContentValues();
        changeCompany.put(TOTAL_EQUITY, Double.toString(totalEquity - price));
        dbb.update(TABLE_COMPANIES, changeCompany, NAME + "= ?", companyName);
    }

    public long insertCompany(String name, String stock, String total, String imgName){
        SQLiteDatabase dbb = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(NAME, name);
        contentvalues.put(STOCK_PRICE, stock);
        contentvalues.put(TOTAL_EQUITY, total);
        contentvalues.put(IMAGE_NAME, imgName);
        long id = dbb.insert(TABLE_COMPANIES, null, contentvalues);
        return id;
    }

    public long insertUser(String user, String password, int funds){
        SQLiteDatabase dbb = this.getWritableDatabase();
        ContentValues contentvalues = new ContentValues();
        contentvalues.put(USERNAME, user);
        contentvalues.put(PASSWORD, password);
        contentvalues.put(FUNDS, funds);
        long id = dbb.insert(TABLE_USERS, null, contentvalues);
        return id;
    }

    }


