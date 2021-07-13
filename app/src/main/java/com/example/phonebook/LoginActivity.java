package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText mPassword,mPhNumber;
    Button login;
    TextView registerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mPhNumber=findViewById(R.id.phno);
        mPassword=findViewById(R.id.password);
        login=findViewById(R.id.login);
        registerText= findViewById(R.id.register_text);

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("User");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!mobileno1() | !password1()){
                    return;
                }else{
                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(mPhNumber.getText().toString()).exists()) {
                                mPhNumber.setError(null);
                                mPassword.setError(null);

                                User user = dataSnapshot.child(mPhNumber.getText().toString()).getValue(User.class);
                                if (user.getPassword().equals(mPassword.getText().toString())) {
                                    Toast.makeText(LoginActivity.this, "Log In Successfully!", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getBaseContext(),FirstActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    mPassword.setError("Wrong Password");
                                    mPassword.requestFocus();
                                }
                            } else {
                                mPhNumber.setError("No such user exists");
                                mPhNumber.requestFocus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Not able to connect", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean mobileno1(){
        String name2=mPhNumber.getText().toString();
        int i,r=0;
        if(name2.isEmpty()){
            mPhNumber.setError("Fields cannot be empty");
            return false;
        }
        else
            mPhNumber.setError(null);
        return true;
    }

    private boolean password1(){
        String name2=mPassword.getText().toString();
        if(name2.isEmpty()){
            mPassword.setError("Fields cannot be empty");
            return false;
        }
        else
            mPassword.setError(null);
        return true;
    }

    public void isUser(){
        String userEnteredPhoneNo = mPhNumber.getText().toString();
        String userEnteredPassword = mPassword.getText().toString();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

        Query checkUser = reference.orderByChild("mPhNumber").equalTo(userEnteredPassword);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String password = snapshot.child(userEnteredPhoneNo).child("password").getValue(String.class);
                    if (password.equals(userEnteredPassword)){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}