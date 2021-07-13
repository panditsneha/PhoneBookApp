package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telecom.Call;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CallDetailsActivity extends AppCompatActivity {

    TextView name,number;
    Button editProfile,call,message;
    CircularImageView contactImage;
    Uri imageUrl;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setIds();
        getStrings();
        callBut();
        editProfileIntent();
        messageBut();
    }

    public void callBut(){
        String contact = getIntent().getStringExtra("contacts");
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(view.getContext(), "Please Grant Permission", Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {

                    String s = "tel:" + contact;
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse(s));
                    view.getContext().startActivity(i);

                }
            }
        });
    }


    public void messageBut(){
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getIntent().getStringExtra("name");
                String contact = getIntent().getStringExtra("contacts");
                Intent i = new Intent(CallDetailsActivity.this,MessageActivity.class);
                i.putExtra("name",name);
                i.putExtra("contacts",contact);
                startActivity(i);
            }
        });
    }

    public void editProfileIntent(){
        String name = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contacts");
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(),EditProfileActivity.class);
                i.putExtra("name",name);
                i.putExtra("contacts",contact);
//                contactImage.buildDrawingCache();
//                Bitmap bitmap = contactImage.getDrawingCache();
//                i.putExtra("BitmapImage", bitmap);
                startActivity(i);


            }
        });

    }

    public void getStrings(){
        String name = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contacts");
        this.name.setText(name);
        this.number.setText(contact);
    }

    public void setIds(){
        name = findViewById(R.id.contactName);
        number = findViewById(R.id.contactNumber);
        call = findViewById(R.id.phone_call);
        contactImage = findViewById(R.id.contactImage);
        editProfile = findViewById(R.id.editinfo);
        message=findViewById(R.id.phone_message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            imageUrl=data.getData();
            contactImage.setImageURI(imageUrl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:break;
            case R.id.calllogs:break;
            case R.id.share:break;
        }
        return super.onOptionsItemSelected(item);
    }


}