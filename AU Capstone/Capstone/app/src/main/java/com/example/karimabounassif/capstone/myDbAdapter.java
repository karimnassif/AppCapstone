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

    private static final String DATABASE_NAME = "Junc_Tio";     // Database Name
    private static final String TABLE_COMPANIES = "Companies";  // Table Name
    private static final String TABLE_USERS = "Users";
    private static final int DATABASE_Version = 11;             // Database Version
    private static final String UID="_id";                      //Columns for companies database
    private static final String NAME = "Name";
    private static final String STOCK_PRICE = "Price";
    private static final String TOTAL_EQUITY = "Equity";
    private static final String IMAGE_NAME = "ImageName";
    private static final String USER_ID = "_uid";              //Columns for user table
    private static final String USERNAME = "username";         //This will be their email as well
    private static final String PASSWORD = "Password";
    private static final String FUNDS = "Funds";
    private static final String COMPANIES = "Companies";       //Since SQLite does not have a list type, this will have to be
                                                               //comma seperated id's that I manually strip.

    private static final String CREATE_TABLE_COMPANIES = "CREATE TABLE "+ TABLE_COMPANIES +
                    "(" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAME+" VARCHAR(255),"
                    + STOCK_PRICE + " VARCHAR(225)," + TOTAL_EQUITY + " VARCHAR(225)," +
                    IMAGE_NAME + " VARCHAR(225)" + ")";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS +
                    "(" + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME
                    + " VARCHAR(255), " +PASSWORD + " VARCHAR(255), " + FUNDS + " " +
                    "VARCHAR(255), " + COMPANIES + " VARCHAR(255)" + ")";

    public void onCreate(SQLiteDatabase db) {                                   //Creation of db and initial population of tables
            db.execSQL(CREATE_TABLE_COMPANIES);
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL("INSERT INTO Companies(" + UID +", " + NAME +", " +     //Inserting initial companies
                    STOCK_PRICE + ", " + TOTAL_EQUITY + ", " + IMAGE_NAME +
                    ") VALUES('1', 'Junc_Tio', '10', '10000', 'junc')");
            db.execSQL("INSERT INTO Companies(" + UID +", " + NAME +", " +
                    STOCK_PRICE + ", " + TOTAL_EQUITY + ", " + IMAGE_NAME +
                    ") VALUES('2', 'eBay', '15', '15000', 'ebay')");
            db.execSQL("INSERT INTO Companies(" + UID +", " + NAME +", " +
                    STOCK_PRICE + ", " + TOTAL_EQUITY + ", " + IMAGE_NAME +
                    ") VALUES('3', 'AU_Capstone', '20', '20000', 'au')");
            db.execSQL("INSERT INTO Users(" + USER_ID + ", " + USERNAME +
                    ", " + PASSWORD + ", " + FUNDS + ", " + COMPANIES +
                    ") VALUES('1', '@', 'asdf', '15000', '')");
            db.execSQL("INSERT INTO Users(" + USER_ID + ", " + USERNAME +
                    ", " + PASSWORD + ", " + FUNDS + ", " + COMPANIES +
                    ") VALUES('2', 'test@au.com', 'asdf', '20000', '')");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { //When a new db version is detected, replaces tables.
            db.execSQL("DROP TABLE "+ TABLE_COMPANIES);
            db.execSQL("DROP TABLE " + TABLE_USERS);
            onCreate(db);
        }


    public ArrayList<String[]> fetchCompanies(){                        //Fetches all companies in db

        SQLiteDatabase dbb = this.getReadableDatabase();
        ArrayList<String[]> data = new ArrayList<>();

        //Setting up SQL query
        Cursor c = dbb.rawQuery("SELECT * FROM Companies ", null);
        c.moveToFirst();
        do{                                                             //Iterating through columns, saving data
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
        if(c.moveToFirst()){                                            //Checks if there was a match in the db
            if(!c.getString(2).equals(password)){
                user[0] = "Wrong password.";
            }
            else{
            for(int i =0; i<=c.getColumnCount()-1;i++) {                //Copies all user info
                user[i] = c.getString(i);
                }
            }
        }
        else {
            user[0] = "No such username.";
        }
        return user;
    }

    public void purchaseStock(User check, DummyContent.DummyItem company){               //The method for a user buying a stock.

        SQLiteDatabase dbb = this.getWritableDatabase();                                //Retrieve database

        String[] update = checkUserPW(check.getUsername(), check.getPassword());        //Check db to update user fields.
        User user = new User(update[0], update[1], update[2], update[3], update[4]);

        double price = Double.parseDouble(company.stockPrice);                          //Preparing variables and columns to be changed
        double newFunds = Double.parseDouble(user.getFunds()) - price;
        double totalEquity = Double.parseDouble(company.remainingStocks) - price;
        String companies = checkUserPW(user.getUsername(), user.getPassword())[4];
        String currentCompanies = companies + company.name + " ";
        String[] username = {user.getUsername()};
        String[] companyName = {company.name};

        ContentValues changeUser = new ContentValues();                                 //Update Users table
        changeUser.put(FUNDS, Double.toString(newFunds));
        changeUser.put(COMPANIES, currentCompanies);
        dbb.update(TABLE_USERS, changeUser, USERNAME + "= ?", username);

        ContentValues changeCompany = new ContentValues();                              //Update Companies table
        changeCompany.put(TOTAL_EQUITY, Double.toString(totalEquity));
        dbb.update(TABLE_COMPANIES, changeCompany, NAME + "= ?", companyName);
    }

    public void tradeStock(User user, String trader, DummyContent.DummyItem stock)
    {
        SQLiteDatabase dbb = this.getWritableDatabase();
        Cursor c = dbb.rawQuery("SELECT * FROM Users WHERE username = '" + trader + "'", null);
        String[] tradeUser = new String[5];
        if(c.moveToFirst()){                                            //Checks if there was a match in the db{
                for(int i =0; i<=c.getColumnCount()-1;i++) {                //Copies all user info
                    tradeUser[i] = c.getString(i);
                }
            }

        double newFunds = Double.parseDouble(user.getFunds()) + Double.parseDouble(stock.stockPrice);
        double traderFunds = Double.parseDouble(tradeUser[3]) - Double.parseDouble(stock.stockPrice);
        String companies = tradeUser[4] + stock.name + " ";

        String[] username = {user.getUsername()};
        String[] traderUsername = {tradeUser[1]};

        ContentValues cv = new ContentValues();
        cv.put(FUNDS, Double.toString(newFunds));
        dbb.update(TABLE_USERS, cv, USERNAME + "= ?", username);

        ContentValues tradercv = new ContentValues();
        tradercv.put(FUNDS, Double.toString(traderFunds));
        tradercv.put(COMPANIES, companies);
        dbb.update(TABLE_USERS, tradercv, USERNAME + "= ?", traderUsername);
    }

    public DummyContent.DummyItem fetchCompany(String name){
        SQLiteDatabase dbb = getReadableDatabase();

        Cursor c = dbb.rawQuery("SELECT * FROM Companies WHERE Name = '" + name + "'", null);
        String[] company = new String[5];

        if(c.moveToFirst()){                                            //Checks if there was a match in the db{
            for(int i =0; i<=c.getColumnCount()-1;i++) {
                company[i] = c.getString(i);
            }
        }
        DummyContent.DummyItem comp = new DummyContent.DummyItem(company[0],company[1],company[2],company[3],company[4]);
        return comp;
    }

    public long insertCompany(String name, String stock, String total, String imgName){ //Method to insert new company to db.

        SQLiteDatabase dbb = this.getWritableDatabase();                                //Retrieve DB

        ContentValues contentvalues = new ContentValues();                              //Prepare new values
        contentvalues.put(NAME, name);
        contentvalues.put(STOCK_PRICE, stock);
        contentvalues.put(TOTAL_EQUITY, total);
        contentvalues.put(IMAGE_NAME, imgName);

        long id = dbb.insert(TABLE_COMPANIES, null, contentvalues);                     //Updated db.
        return id;
    }

    public long insertUser(String user, String password, int funds){                    //Method to insert user

        SQLiteDatabase dbb = this.getWritableDatabase();                                //Retrieve DB

        ContentValues contentvalues = new ContentValues();                              //Prepare new values
        contentvalues.put(USERNAME, user);
        contentvalues.put(PASSWORD, password);
        contentvalues.put(FUNDS, funds);

        long id = dbb.insert(TABLE_USERS, null, contentvalues);                         //Updated db.
        return id;
    }

    }


