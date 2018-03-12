package com.example.listen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Set the username
        mTextView = findViewById(R.id.text_username);
        mTextView.setText(getIntent().getStringExtra("username"));

        //Set the email
        mTextView = findViewById(R.id.text_email);
        mTextView.setText(getIntent().getStringExtra("email"));
    }
}
