package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mikhaellopez.circularimageview.CircularImageView;

public class EditProfileActivity extends AppCompatActivity {

    EditText profileName,profileNumber;
    CircularImageView contactImage;
    Button save;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME="mypref";
    private static final String KEY_NAME="name";
    private static final String KEY_PHONE="email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setIds();
        setTexts();
    }

    public void setTexts(){
        String name = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contacts");
        profileName.setText(name);
        profileNumber.setText(contact);
        saveProfileOnPressed();
    }

    public void setIds() {
        profileName=findViewById(R.id.editContactName);
        profileNumber=findViewById(R.id.editContactNumber);
        contactImage=findViewById(R.id.editContactImage);
        save = findViewById(R.id.save);
    }

    public void saveProfileOnPressed(){
        sharedPreferences = getSharedPreferences("",MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_NAME,profileName.getText().toString());
                editor.putString(KEY_PHONE,profileNumber.getText().toString());
                editor.apply();

                Intent i = new Intent(EditProfileActivity.this,CallDetailsActivity.class);
                startActivity(i);
            }
        });
    }



}