package com.example.karimabounassif.capstone;

//Class to organize user information pulled from database.

import java.util.HashMap;

public class User {

    private String id;
    private String username;
    private String password;
    private String funds;
    private String companies;

    public User(String id, String username, String password, String funds, String companies){
        this.id = id;
        this.username = username;
        this.password = password;
        this.funds = funds;
        this.companies = companies;
    }
    String getID() {
        return this.id;
    }
    public void set(String id, String username, String password, String funds, String companies){
        this.id = id;
        this.username = username;
        this.password = password;
        this.funds = funds;
        this.companies = companies;
    }

    String getUsername(){
        return this.username;
    }
    String getPassword(){
        return this.password;
    }
    String getFunds(){
        return this.funds;
    }
    String getCompanies(){
        return this.companies;
    }
}
