package com.example.phonebook;

import android.net.Uri;
import android.view.Display;

public class Model{

    private String name,contact;
    private int id;
    private String imageUrl;

    public Model(){

    }

    public Model(String name, String contact, String imageUrl) {
        this.name = name;
        this.contact = contact;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl.toString();
    }
}
