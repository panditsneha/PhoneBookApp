package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLDisplay;

import static android.content.ContentValues.TAG;

public class EditProfileActivity extends AppCompatActivity {

    EditText profileName,profileNumber;
    CircularImageView contactImage;
    Button save,changePic;
    SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ArrayList<Model> model = new ArrayList<Model>();
    ProgressBar progressBar;

    private DatabaseReference dataRef;
    private FirebaseAuth mAuth;
    private StorageReference storageProfilePicRef;
    private StorageTask uploadTask;
    private Uri imageUri;
    private String myUri="";


    private static final String SHARED_PREF_NAME="mypref";
    private static final String KEY_NAME="name";
    private static final String KEY_PHONE="email";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.arrow_back);
        actionBar.setDisplayHomeAsUpEnabled(true);

        setIds();
        setTexts();

        saveProfileOnPressed();

        mAuth = FirebaseAuth.getInstance();
        dataRef = FirebaseDatabase.getInstance().getReference().child("User");
        storageProfilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");

        changePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1).start(EditProfileActivity.this);
            }
        });

        getUserinfo();
    }

    public void setTexts(){
        String name = getIntent().getStringExtra("name");
        String contact = getIntent().getStringExtra("contacts");
        profileName.setText(name);
        profileNumber.setText(contact);
    }

    public void setIds() {
        profileName=findViewById(R.id.editContactName);
        profileNumber=findViewById(R.id.editContactNumber);
        contactImage=findViewById(R.id.editContactImage);
        save = findViewById(R.id.save);
        changePic=findViewById(R.id.changeButton);
        progressBar=findViewById(R.id.progress_bar);
    }

    public void saveProfileOnPressed(){
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uploadTask!=null && uploadTask.isInProgress()){
                    Toast.makeText(EditProfileActivity.this,"Upload in progress",Toast.LENGTH_SHORT).show();
                }else{
                    uploadProfileImage();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            contactImage.setImageURI(imageUri);
        }else{
            Toast.makeText(this,"Error, Try Again",Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadProfileImage(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Set your profile");
        progressDialog.setMessage("Please wait, while we are setting your data");
        progressDialog.show();

        if(imageUri!=null){
            final  StorageReference fileRef = storageProfilePicRef.child(mAuth.getCurrentUser().getUid()+".jpg");

            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = (Uri) task.getResult();
                        myUri = downloadUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image",myUri);

                        dataRef.child(mAuth.getCurrentUser().getUid()).updateChildren(userMap);

                        progressDialog.dismiss();
                    }
                }
            });
        }else{
            progressDialog.dismiss();
            Toast.makeText(this,"Image not selected",Toast.LENGTH_SHORT).show();
        }
    }

}