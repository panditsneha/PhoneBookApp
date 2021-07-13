package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    EditText mName,mEmail,mPassword,mPhNumber;
    Button register;
    TextView loginText;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        mName=findViewById(R.id.name);
        mEmail=findViewById(R.id.email);
        mPassword=findViewById(R.id.password);
        mPhNumber=findViewById(R.id.phone_number);
        register=findViewById(R.id.register);
        loginText= findViewById(R.id.login_text);


        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!username1()|!mobileno1()|!email1()|!password1()){
                    return;
                }

                loadingDialog=new Dialog(RegisterActivity.this);
                loadingDialog.setContentView(R.layout.activity_loading);
                loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                loadingDialog.setCancelable(false);
                loadingDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(mPhNumber.getText().toString()).exists()){
                            loadingDialog.dismiss();
                            mPhNumber.setError("Mobile no. Already Exists");
                        }else{
                            loadingDialog.dismiss();
                            User user = new User(mName.getText().toString(),mPassword.getText().toString(),mEmail.getText().toString());
                            table_user.child(mPhNumber.getText().toString()).setValue(user);
                            Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getBaseContext(),LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    private boolean username1(){
        String name1=mName.getText().toString();
        String noWhihteSpaces=("\\A\\w{4,20}\\z");

        if(name1.isEmpty()){
            mName.setError("Field cannot be empty");
            return false;
        }else if(name1.length()>15){
            mName.setError("Username should be less than 15 characters");
            return false;
        }else if(!name1.matches(noWhihteSpaces)){
            mName.setError("Username cannot carry such characters or spaces");
            return false;
        }
        else
            mName.setError(null);
        return true;
    }

    private boolean mobileno1(){
        String name2=mPhNumber.getText().toString();
        int i,r=0;
        if(name2.isEmpty()){
            mPhNumber.setError("Fields cannot be empty");
            return false;
        }else if(name2.length()>10||name2.length()<10){
            mPhNumber.setError("Mobile no. should be of 10 characters");
            return false;
        }
        else if(1==1) {
            for (i = 0; i < name2.length(); i++) {
                Boolean flag = Character.isDigit(name2.charAt(i));
                if (flag) {
                    r=r+1;
                }
            }
            if(r!=10){
                mPhNumber.setError("Mobile no. should be an integer");
                return false;
            }
        }
        else
            mPhNumber.setError(null);
        return true;
    }

    private boolean email1(){
        String name2=mEmail.getText().toString();
        String emailPattern=("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        if(name2.isEmpty()){
            mEmail.setError("Fields cannot be empty");
            return false;
        }else if(name2.length()>45){
            mEmail.setError("Email should be less than 45 characters");
            return false;
        }else if(!name2.matches(emailPattern)){
            mEmail.setError("Email cannot carry such characters or spaces");
            return false;
        }
        else
            mEmail.setError(null);
        return true;
    }

    private boolean password1(){
        String name2=mPassword.getText().toString();
        if(name2.isEmpty()){
            mPassword.setError("Fields cannot be empty");
            return false;
        }else if(name2.length()>18){
            mPassword.setError("Password should be less than 18 characters");
            return false;
        }
        else
            mPassword.setError(null);
        return true;
    }


}