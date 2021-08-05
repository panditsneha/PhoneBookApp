package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CallDetailsActivity extends AppCompatActivity {

    TextView name,number;
    Button editProfile,call,message;
    CircularImageView contactImage;
    Uri imageUrl;
    boolean flag=false;
    private DatabaseReference dataRef;
    private FirebaseAuth mAuth;
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

        mAuth = FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference().child("User");

        setIds();
        getStrings();
        callBut();
        editProfileIntent();
        messageBut();

        getUserinfo();
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

    private void getUserinfo() {
        dataRef.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists() && snapshot.getChildrenCount()>0){

                    if(snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(contactImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            case R.id.fav:speedDial();
                        break;
            case R.id.remove:break;
            case R.id.share:break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void speedDial(){

        //Kartik yeh dekh le...yeh intent kr rhi FragmentFav fragment me

        String name = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contacts");

        FragmentFav objects = new FragmentFav();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        Bundle newBundle = new Bundle();
        newBundle.putString("con_name",name);
        newBundle.putString("con_number",contact);

        objects.setArguments(newBundle);
        fragmentTransaction.add(R.id.fragment_grid, objects).commit();

    }

}