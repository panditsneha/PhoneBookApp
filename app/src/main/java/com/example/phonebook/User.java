package com.example.phonebook;

import java.security.PrivateKey;

public class User {

    private String Name;
    private String Password;
    private String Eamil;

    public User(){

    }

    public User(String name, String password, String eamil) {
        Name = name;
        Password = password;
        Eamil = eamil;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getEamil() {
        return Eamil;
    }

    public void setEamil(String eamil) {
        Eamil = eamil;
    }
}
