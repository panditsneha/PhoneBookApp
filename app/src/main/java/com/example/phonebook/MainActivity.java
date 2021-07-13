package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Model> model = new ArrayList<Model>();
    RecycleViewAdapter adaptor;
    Cursor cursor;
    TextView contacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView3);
        contacts = findViewById(R.id.contact);

        checkPermission();

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.READ_CONTACTS}, 100);
        } else {
            getContactList();
        }
    }

    private void getContactList() {

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        if (uri != null) {
            cursor = getContentResolver().query(uri, null, null, null, sort);
        }

        if (cursor.getCount() > 0) {
            int i = 0;
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                Uri uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                String selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?";

                Cursor phoneCursor = getContentResolver().query(uriPhone, null, selection, new String[]{id}, null);

                if (phoneCursor.moveToNext()) {
                    String number = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                    Model m = new Model();
                    m.setName(name);
                    m.setContact(number);
                    m.setId(i++);

                    model.add(m);
                    phoneCursor.close();
                }
            }
            cursor.close();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adaptor = new RecycleViewAdapter(this, model);
        recyclerView.setAdapter(adaptor);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        } else {
            Toast.makeText(MainActivity.this, "Permission Denied.", Toast.LENGTH_SHORT).show();
        }
    }

}





