package com.makgyber.vbuys.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.makgyber.vbuys.R;

public class MessageActivity extends AppCompatActivity {

    String chatId, topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        if (getIntent().hasExtra("chatId")) {
            chatId = getIntent().getExtras().get("chatId").toString();
        }

        if (getIntent().hasExtra("topic")) {
            topic = getIntent().getExtras().get("topic").toString();
        }

        getSupportActionBar().setTitle(chatId);
    }
}
