package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity {

    EditText messageText;
    Button send_sms;
    TextView name;
    String sContact,sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageText = findViewById(R.id.message_text);
        name = findViewById(R.id.name);
        send_sms = findViewById(R.id.send);

        sName = getIntent().getStringExtra("name");
        this.name.setText(sName);

        send_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(MessageActivity.this,Manifest.permission.SEND_SMS);
                if(permissionCheck == PackageManager.PERMISSION_GRANTED){
                    sendMessage();
                }else{
                    ActivityCompat.requestPermissions(MessageActivity.this,new String[]{Manifest.permission.SEND_SMS},0);
                }
            }
        });

    }

    private void sendMessage() {
        sContact = getIntent().getStringExtra("contacts").trim();
        String sMessage = messageText.getText().toString().trim();

        if(!messageText.getText().toString().equals("")){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sContact,null,sMessage,null,null);
            Toast.makeText(this,"Sent",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Enter the Message",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0){
            if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                sendMessage();
            }else{
                Toast.makeText(this,"No permission",Toast.LENGTH_SHORT).show();
            }
        }
    }
}